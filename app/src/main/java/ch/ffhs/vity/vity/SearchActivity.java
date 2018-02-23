package ch.ffhs.vity.vity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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

}
