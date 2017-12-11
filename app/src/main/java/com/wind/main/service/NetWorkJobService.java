package com.wind.main.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import com.wind.main.util.LogUtil;

/**
 * Created by zhuyuqiang on 17-11-22.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class NetWorkJobService extends JobService {

    /*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ssss(){
        ComponentName componentName = new ComponentName(this, NetWorkJobService.class.getName());
        JobScheduler mJob = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder=new JobInfo.Builder(10, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        builder.setRequiresCharging(true);
        builder.setRequiresDeviceIdle(false);
        builder.setMinimumLatency(5000);
        builder.setOverrideDeadline(60000);
        mJob.schedule(builder.build());
        LogUtil.e("zyq","schedule ......");
    }*/

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtil.e("zyq","NetWorkJobService : onStartJob =" + System.currentTimeMillis());
        jobFinished(params,true);
        return true ;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
