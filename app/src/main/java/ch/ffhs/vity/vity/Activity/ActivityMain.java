package ch.ffhs.vity.vity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.ffhs.vity.vity.R;

public class ActivityMain extends Activity {

    private ProgressBar loadProgressbar;
    private Handler progressHandler = new Handler();
    private int statusProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);

        // Set 20db font
        Typeface tf = Typeface.createFromAsset(getAssets(), "vity20db.otf");
        TextView tw = (TextView) findViewById(R.id.vity_bootscreen);
        tw.setTypeface(tf);

        // ProgressBar
        loadProgressbar = (ProgressBar) findViewById(R.id.progressBar);
        final Intent intent = new Intent(this, ActivityMap.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (statusProgressbar < 100) {
                    statusProgressbar++;
                    android.os.SystemClock.sleep(20);
                    progressHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadProgressbar.setProgress(statusProgressbar);
                        }
                    });
                }
                android.os.SystemClock.sleep(500);
                startActivity(intent);
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}