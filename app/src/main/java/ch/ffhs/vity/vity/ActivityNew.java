package ch.ffhs.vity.vity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
            case R.id.opt_map:
                startActivity(new Intent(this, ActivityMap.class));
                return true;
            case R.id.opt_new:
                startActivity(new Intent(this, ActivityNew.class));
                return true;
            case R.id.opt_search:
                startActivity(new Intent(this, ActivitySearch.class));
                return true;
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

    /**public void onClickAddPicture(View button) {

     }**/

    /**public void onClickAddLocation(View button) {

     }**/

    /**public void onClickSaveNewActivity(View button) {

     }**/
}
