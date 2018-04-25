package ch.ffhs.vity.vity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.R;


public class ActivitySearch extends Activity {
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
        loadResults();
    }

    public void onClickCancel(View button) {
        finish();
    }

    // Load mock data
    private void loadResults(){
        mDb = AppDatabase.getDatabase(this.getApplication());
        fetchData();
    }

    private void fetchData() {
        ListView listView = findViewById(R.id.list_activities);
        ArrayList liste = new ArrayList<VityItem>();
        liste.addAll(mDb.itemModel().getAllItems());
        final ActivityListAdapter listAdapter = new ActivityListAdapter(liste, getApplicationContext());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VityItem selectedItem = (VityItem)listAdapter.getItem(position);
                long itemId = selectedItem.getId();
                Intent activity = new Intent(view.getContext(), ActivityDetail.class);
                activity.putExtra("itemId", itemId);
                startActivity(activity);
            }
        });
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadResults();
    }


}