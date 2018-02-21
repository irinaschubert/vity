package ch.ffhs.vity.vity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int BOOTSCREEN_TIMEOUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home_intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(home_intent);
                finish();
            }
        }, BOOTSCREEN_TIMEOUT);
    }

}