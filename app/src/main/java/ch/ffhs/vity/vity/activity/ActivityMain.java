package ch.ffhs.vity.vity.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.mock.DatabaseInitializer;

public class ActivityMain extends Activity {

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(this, ActivityMap.class);

        // Set 20db font
        Typeface tf = Typeface.createFromAsset(getAssets(), "vity20db.otf");
        TextView tw = (TextView) findViewById(R.id.vity_bootscreen);
        tw.setTypeface(tf);

        // load mock data
        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
        populateDb();
        startActivity(intent);
    }

    private void populateDb() {
        DatabaseInitializer.populateSync(mDb);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}