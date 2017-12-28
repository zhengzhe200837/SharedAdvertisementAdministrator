package com.wind.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wind.main.network.Network;
import com.wind.main.network.api.UploadBillBoardInfoApi;
import com.wind.main.network.model.BillboardInfo;
import com.wind.main.util.LogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by xiaoxiao on 2017/12/25.
 */

public class MyAdActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView adListShow;
    private List<Map<String,String>> adNames;
    private UploadBillBoardInfoApi mUploadBillBoardInfoApi;
    private SimpleAdapter mSimpleAdapter;
    private Context context;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme(R.style.add_ad_Theme);
        setContentView(R.layout.ad_list_show);
        adNames =new ArrayList<>();
        adListShow =(ListView)findViewById(R.id.lv_ad_show);
        context =this;
        mSimpleAdapter =new SimpleAdapter(this,adNames,R.layout.ad_name_detai,new String[]{"name"},new int[]{R.id.tv_show_name});
      //  adListShow.setAdapter(new SimpleAdapter(this, ))
        adListShow.setAdapter(mSimpleAdapter);

        adListShow.setOnItemClickListener(this);


    }
    public void onResume(){
        super.onResume();
        getAdName("billBoardInfo","query");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void getAdName(String tableName,String action){
        mUploadBillBoardInfoApi =Network.getUploadBillBoardInfoApi();
        BillboardInfo billboardInfo =new BillboardInfo();
        billboardInfo.setTableName(tableName);
        billboardInfo.setBusinessPhone("4444444444");
        billboardInfo.setMethod("getBillboardByPhone");
        billboardInfo.setTodo(action);
        mUploadBillBoardInfoApi.getBillBoardInfo(billboardInfo)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BillboardInfo>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<BillboardInfo> billboardInfos) throws Exception {
                        adNames.clear();
                        for (BillboardInfo billboardInfo:billboardInfos){
                            Map<String,String> map =new HashMap<String, String>();
                            map.put("name",billboardInfo.getEquipmentName());
                            LogUtil.d("name: "+billboardInfo.getEquipmentName());
                            adNames.add(map);
                            mSimpleAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
