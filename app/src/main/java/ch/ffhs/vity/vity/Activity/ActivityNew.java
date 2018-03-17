package ch.ffhs.vity.vity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ch.ffhs.vity.vity.Helper.ActivityRegistry;
import ch.ffhs.vity.vity.R;

/**
 * Created by irina on 10.02.2018.
 */

public class ActivityNew extends Activity {
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_new);
        ActivityRegistry.register(this);
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Menu Events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.opt_settings:
                startActivity(new Intent(this, ActivitySettings.class));
                return true;
            case R.id.opt_exit:
                ActivityRegistry.finishAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /** Schlüssel für den Namen der neuen Aktivität. */
    public static final String NAME_ACTIVITY = "name";

    /** Schlüssel für die Kategorie der neuen Aktivität. */
    public static final String CATEGORY = "cat";

    // onClickFunctions
    public void onClickAddPicture(View button) {
        Toast.makeText(getApplicationContext(), "addPicture", Toast.LENGTH_LONG).show();
    }

    public void onClickAddLocation(View button) {
        Toast.makeText(getApplicationContext(), "addLocation", Toast.LENGTH_LONG).show();
     }

    public void onClickSaveNewActivity(View button) {
        Toast.makeText(getApplicationContext(), "saveNewActivity", Toast.LENGTH_LONG).show();
     }

    public void onClickCancel(View button) {
        finish();
    }
}
