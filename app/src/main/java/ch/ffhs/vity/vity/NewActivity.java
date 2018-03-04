package ch.ffhs.vity.vity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class NewActivity extends Activity {
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_new);
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Menu onClickEvents
    public void goToNewActivity(MenuItem item){
        Intent intent = new Intent(this, NewActivity.class);
        startActivity(intent);
    }
    public void goToSettingsActivity(MenuItem item){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void goToHomeActivity(MenuItem item){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void goToSearchActivity(MenuItem item){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /** Schlüssel für den Namen der neuen Aktivität. */
    public static final String NAME_ACTIVITY = "name";

    /** Schlüssel für die Beschreibung der neuen Aktivität. */
    public static final String DESCRIPTION_ACTIVITY = "desc";

    /** Schlüssel für die Kategorie der neuen Aktivität. */
    public static final String CATEGORY = "cat";

    /**public void onClickAddPicture(View button) {
        Intent intent = new Intent(this, PictureActivity.class);
        startActivity(intent);
     }**/

    /**public void onClickAddLocation(View button) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
     }**/

    /**public void onClickSaveNewActivity(View button) {
        Intent intent = new Intent(this, SaveActivity.class);
        startActivity(intent);
     }**/

    public void onClickCancelNewActivity(View button) {
      Intent intent = new Intent(this, HomeActivity.class);
      startActivity(intent);
     }
}
