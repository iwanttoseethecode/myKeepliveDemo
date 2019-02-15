package example.luoling.com.keepliveprocess;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2019/2/15.
 */

public class KeepLiveJobService extends JobService {

    private int kJobId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("info","jobService create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("info","jobservice start");
        scheduleJob(getJobInfo());
        return START_NOT_STICKY;
    }

    public JobInfo getJobInfo(){
        JobInfo.Builder builder = new JobInfo.Builder(kJobId++,new ComponentName(this,KeepLiveJobService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .setPeriodic(10)//间隔时间--周期
        .setPersisted(true);
        return builder.build();
    }

    public void scheduleJob(JobInfo jobInfo){
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(jobInfo);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("info","job start");
        boolean isLocalServiceWork = isServiceWork(this,"example.luoling.com.keepliveprocess.LocalService");
        boolean isRemoteServiceWork = isServiceWork(this,"example.luoling.com.keepliveprocess.RemoteService");
        if (!isLocalServiceWork || !isRemoteServiceWork){
            this.startService(new Intent(this,LocalService.class));
            this.startService(new Intent(this,RemoteService.class));
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("info","job stop");
        scheduleJob(getJobInfo());
        return false;
    }

    public boolean isServiceWork(Context mContext,String serviceName){
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0){
            return false;
        }
        for (int i = 0; i < myList.size(); i++){
            String name = myList.get(i).service.getClassName().toString();
            if (name.equals(serviceName)){
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
