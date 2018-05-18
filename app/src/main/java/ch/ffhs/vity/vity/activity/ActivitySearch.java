package ch.ffhs.vity.vity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.VityItem;


public class ActivitySearch extends Activity {
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // onClickFunctions
    public void onClickSearchActivity(View button) {
        loadResults();
    }

    private void loadResults(){
        mDb = AppDatabase.getDatabase(this.getApplication());
        fetchData();
    }

    private void fetchData() {
        ListView listView = findViewById(R.id.list_activities);
        Spinner categorySpinner = findViewById(R.id.new_category);
        String category = categorySpinner.getSelectedItem().toString();
        ArrayList liste = new ArrayList<VityItem>();
        liste.addAll(mDb.itemModel().findItemByCategory(category));
        if(liste.isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.no_results, Toast.LENGTH_LONG).show();
        }
        final VityItemListAdapter listAdapter = new VityItemListAdapter(liste, getApplicationContext());
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
    }

}