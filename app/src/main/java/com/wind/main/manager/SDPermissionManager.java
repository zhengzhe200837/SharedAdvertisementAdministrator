package com.wind.main.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.wind.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuyuqiang on 17-11-22.
 */

public class SDPermissionManager {

    private String packageName = null;
    private List<String> mDangerousPermissions;
    private PackageInfo info;
    private Context mContext;
    private RequestPermission mRequestPermission;

    public interface RequestPermission {
        void onRequestPermission(String[] permission);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public SDPermissionManager(Context context) {
        this.packageName = context.getPackageName();
        this.mContext = context;
        mDangerousPermissions = new ArrayList<>();
        try {
            info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String permissions[] = info.requestedPermissions;
            for (String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                    mDangerousPermissions.add(permission);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setRequestPermission(RequestPermission r) {
        this.mRequestPermission = r;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void startCheckPermission() {
        ((MainActivity)mContext).setPermissionResult(mResult);
        if (mDangerousPermissions != null && mDangerousPermissions.size() > 0) {
            String permissions[] = new String[mDangerousPermissions.size()];
            for(int i = 0; i<mDangerousPermissions.size();i++){
                permissions[i] = mDangerousPermissions.get(i);
            }
            mRequestPermission.onRequestPermission(permissions);
        }
    }

    private MainActivity.SDPermissionResult mResult = new MainActivity.SDPermissionResult() {
        @Override
        public void onPermissionResult(String[] permissions, int[] grantResults) {
            boolean allGranted = true;
            for(int i : grantResults){
                if(i != PackageManager.PERMISSION_GRANTED){
                    allGranted = false;
                }
            }
            if(!allGranted){
                //open settings
            }
        }
    };



    private void openSystemSettings(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
        mContext.startActivity(intent);
    }


}
