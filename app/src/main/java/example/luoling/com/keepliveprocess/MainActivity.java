package example.luoling.com.keepliveprocess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this,LocalService.class));
        startService(new Intent(this,RemoteService.class));
//        startService(new Intent(this,KeepLiveJobService.class));
    }

}


























