package ch.ffhs.vity.vity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.map.Map;
import ch.ffhs.vity.vity.mock.DatabaseInitializer;

public class ActivityMain extends Activity {

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        final Intent mapView = new Intent(this, Map.class);

        // load mock data
        mDb = AppDatabase.getDatabase(this.getApplication());
        populateDb();
        startActivity(mapView);
    }

    private void populateDb() {
        DatabaseInitializer.populateAsync(mDb);
    }
}