package ch.ffhs.vity.vity.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.mock.DatabaseInitializer;


public class ActivitySearch extends Activity {

    private Context context;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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

    // onClickFunctions
    public void onClickSearchActivity(View button) {
        // Toast.makeText(getApplicationContext(), "searchActivity", Toast.LENGTH_LONG).show();
        loadResults();

        // Hide Keyboard
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null){
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void onClickCancel(View button) {
        finish();
    }

    // Load list
    private void loadResults(){
        mDb = AppDatabase.getInMemoryDatabase(getApplicationContext());
        populateDb();
        fetchData();
    }

    private void populateDb() {
        DatabaseInitializer.populateSync(mDb);
    }

    private void fetchData() {
        ListView listView = (ListView) findViewById(R.id.list_activities);
        ArrayList liste = new ArrayList<VityItem>();
        liste.addAll(mDb.itemModel().findAllItems());
        ActivityListAdapter listAdapter = new ActivityListAdapter(liste, getApplicationContext());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent activity = new Intent(view.getContext(), ActivityDetail.class);
                activity.putExtra("id", position);
                startActivity(activity);
            }
        });
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}