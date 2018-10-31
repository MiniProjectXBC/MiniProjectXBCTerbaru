package xbc.miniproject.com.xbcapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import xbc.miniproject.com.xbcapplication.utility.Constanta;
import xbc.miniproject.com.xbcapplication.utility.SessionManager;

public class SplashScreenActivity extends Activity {
    private Context context = this;
    private TextView textVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textVersion = (TextView) findViewById(R.id.textVersion);

        getTextVersion();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (SessionManager.isLogin(context)) {
                    Intent intent = new Intent(context, HomeActivity. class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LoginActivity. class);
                    startActivity(intent);
                }
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, Constanta.SPLASH_DELAY_TIME);
    }

    private void getTextVersion(){
        String versionGradle = BuildConfig.VERSION_NAME;
        String versionDisplayText = "Version : "+versionGradle;
        textVersion.setText(versionDisplayText);
    }
}