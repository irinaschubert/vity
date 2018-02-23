package ch.ffhs.vity.vity;

import android.app.SearchableInfo;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Toast.makeText(getApplicationContext(),"onCreate", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_home);
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


    // Zeige Android LifeCycle
    /*
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_LONG).show();
    }
    */

}
