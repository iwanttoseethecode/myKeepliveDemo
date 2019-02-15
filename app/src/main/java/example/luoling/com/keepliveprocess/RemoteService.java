package example.luoling.com.keepliveprocess;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2019/2/14.
 */

public class RemoteService extends Service {

    public static final String TAG = "luoling";
    private MyBinder binder;
    private MyServiceConnection connection;

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null){
            binder = new MyBinder();
        }
        connection = new MyServiceConnection();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class MyBinder extends RemoteConnection.Stub{

        @Override
        public String getProcessName() throws RemoteException {
            return "RemoteService";
        }
    }

    class MyServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG,"与LocalService连接成功");
            try {
                RemoteConnection remoteConnection = RemoteConnection.Stub.asInterface(service);
                remoteConnection.getProcessName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "LocalService服务被干掉了~~~~断开连接！");
            Toast.makeText(RemoteService.this, "断开连接", Toast.LENGTH_SHORT).show();

            RemoteService.this.startService(new Intent(RemoteService.this,LocalService.class));
            RemoteService.this.bindService(new Intent(RemoteService.this,LocalService.class),connection, Context.BIND_IMPORTANT);
        }
    }

}
