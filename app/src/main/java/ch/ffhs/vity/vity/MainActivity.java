package ch.ffhs.vity.vity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    private ProgressBar loadProgressbar;
    private Handler progressHandler = new Handler();
    private int statusProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadProgressbar = (ProgressBar) findViewById(R.id.progressBar);
        final Intent intent = new Intent(this, HomeActivity.class);

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
                startActivity(intent);
            }
        }).start();
    }


}