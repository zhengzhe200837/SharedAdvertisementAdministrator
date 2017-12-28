package com.wind.main;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wind.main.util.LogUtil;
import com.wind.main.R;

import java.util.List;


/**
 * Created by xiaoxiao on 2017/12/17.
 */

public class MapActivity extends AppCompatActivity implements OnGetGeoCoderResultListener, View.OnClickListener,BaiduMap.OnMapClickListener
,LocationListener{
    private MapView mMapView;
    private GeoCoder geoCoder;
    private BaiduMap mBaiduMap;
    private EditText mAddAddress;
    private LocationManager mLocation;
    private String providerLocation;
    private Button mSearch;
    public LocationClient mLocationClient =null;
    private MyLocationListener myLocationListener =new MyLocationListener();
    private Context context;
    private TextView mCancel;
    private Double latitude;
    private Double longitude;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme(R.style.add_ad_Theme);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.map_layout);
        initData();
    }
    public void initData(){
        mMapView =findViewById(R.id.mv_map);
        mAddAddress =(EditText)findViewById(R.id.et_add_address);
        mBaiduMap =mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setMyLocationEnabled(true);
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mMapView.removeViewAt(1);
        geoCoder =GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(MapActivity.this);
        mBaiduMap.setOnMapClickListener(this);
        mSearch =(Button)findViewById(R.id.btn_search);
        mSearch.setOnClickListener(this);
        mLocation =(LocationManager) getSystemService(LOCATION_SERVICE);
        mCancel =(TextView)findViewById(R.id.tv_cancel);
        mCancel.setOnClickListener(this);
        context =this;
//        Criteria criteria =new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//        criteria.setCostAllowed(true);
//        criteria.setSpeedRequired(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        criteria.setAltitudeRequired(false);
//        providerLocation =mLocation.getBestProvider(criteria,false);
        List<String> list = mLocation.getProviders(true);
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            providerLocation = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            providerLocation = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(this, "当前不能提供位置信息", Toast.LENGTH_LONG).show();

        }

        mLocationClient =new LocationClient(getApplicationContext());
        LocationClientOption clientOption =new LocationClientOption();
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType("bd09ll");
        clientOption.setIsNeedAddress(true);
        clientOption.setOpenGps(true);
       // clientOption.setScanSpan(10);
        mLocationClient.setLocOption(clientOption);
        mLocationClient.registerLocationListener(myLocationListener);

    }

    public void navigationTo(Location location){
        LatLng latLng =new LatLng(location.getLatitude(),location.getLongitude());
        updateMarkUi(latLng);
    }

    public void updateMarkUi(LatLng latLng){
        if(mLocationClient !=null) {
            mLocationClient.unRegisterLocationListener(myLocationListener);
            mLocationClient.stop();
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .position(latLng));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));

    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mLocationClient.start();
        try {
            Location location = mLocation.getLastKnownLocation(providerLocation);
            LogUtil.d("location; "+location);
            if(location !=null){
                LogUtil.d("location; "+location.getLongitude());
                navigationTo(location);
            }
            LogUtil.d("location: request " +providerLocation);
            mLocation.requestLocationUpdates(providerLocation,6000,50,this);
        }catch (SecurityException se){
            LogUtil.d(se.getMessage());
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        mLocation.removeUpdates(this);
    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if(geoCodeResult ==null || geoCodeResult.error !=GeoCodeResult.ERRORNO.NO_ERROR){
            LogUtil.d("geoCodeResult: "+geoCodeResult.error.toString());
            Toast.makeText(context,geoCodeResult.error.toString(),Toast.LENGTH_SHORT).show();
        }else {
            updateMarkUi(geoCodeResult.getLocation());
            latitude =geoCodeResult.getLocation().latitude;
            longitude =geoCodeResult.getLocation().longitude;
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if(reverseGeoCodeResult ==null || reverseGeoCodeResult.error !=ReverseGeoCodeResult.ERRORNO.NO_ERROR) {
            LogUtil.d("geoCodeResult: "+reverseGeoCodeResult.error.toString());
            Toast.makeText(context,reverseGeoCodeResult.error.toString(),Toast.LENGTH_SHORT).show();
        }else {
            LogUtil.d("geoCodeResult: 1" + reverseGeoCodeResult.getAddress());
            mAddAddress.setText(reverseGeoCodeResult.getAddress());
            latitude =reverseGeoCodeResult.getLocation().latitude;
            longitude =reverseGeoCodeResult.getLocation().longitude;
            updateMarkUi(reverseGeoCodeResult.getLocation());
       }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
            if (mAddAddress.getText() != null) {
                String address = mAddAddress.getText().toString();
                String[] addresses = address.split("市");
                String city = addresses[0];
                String addr = address.substring(city.length() + 1, address.length());
                LogUtil.d("ciry; " + city + "addr: " + addr);
                geoCoder.geocode(new GeoCodeOption().city(city).address(addr));
            } else {
                Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.tv_cancel:
                Intent intent =getIntent();
                LogUtil.d("latitude: "+latitude +" longitude" +longitude);
                intent.putExtra("address",mAddAddress.getText().toString());
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("dddd","sdf");
                LogUtil.d("addadress: "+mAddAddress.getText());
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        ReverseGeoCodeOption reverseGeoCodeOption =new ReverseGeoCodeOption();
        reverseGeoCodeOption.location(latLng);
        LogUtil.d("latlng: "+latLng.longitude +"latlng: "+latLng.latitude);
        geoCoder.reverseGeoCode(reverseGeoCodeOption);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        LogUtil.d("location; 1"+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LogUtil.d("location; 1"+status +provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        LogUtil.d("onProviderEnabled; " +provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        LogUtil.d("onProviderDisabled; " +provider);
    }

    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            LogUtil.d("bdlocation: "+bdLocation.getLongitude() +"bdlocation: "+bdLocation.getLatitude() +"address: "+bdLocation.getAddrStr());
            latitude =bdLocation.getLatitude();
            longitude =bdLocation.getLongitude();
            LatLng latLng =new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            mAddAddress.setText(bdLocation.getAddrStr());
            updateMarkUi(latLng);
        }
    }
}
