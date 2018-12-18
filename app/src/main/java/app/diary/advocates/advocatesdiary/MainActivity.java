package app.diary.advocates.advocatesdiary;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        final SwipeRefreshLayout swipeRefreshLayout=findViewById(R.id.swipeRefresh);
        if (SharedPrefLoginInformation.getInstance(this).isLoggedIN()){
            SharedPrefLoginInformation.getInstance(this).logout();
        }

        new CountDownTimer(1000,100){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (checkConncetion()==true){
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                } else {
                   NotificationDialogue.getNotifications("Warnings","NO INTERNET CONNECTION \n SWIPE TO RELOAD",MainActivity.this);
                    swipeRefreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            swipeRefreshLayout.setRefreshing(true);
                            (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(false);
                                    if (checkConncetion()==true){
                                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                        startActivity(intent);
                                    }else {
                                        NotificationDialogue.getNotifications("Warnings","NO INTERNET CONNECTION \n SWIPE TO RELOAD",MainActivity.this);
                                    }
                                }
                            },300);
                        }
                    });
                }
            }
        }.start();

    }
    private boolean checkConncetion(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }else {
            return false;
        }
    }
}
