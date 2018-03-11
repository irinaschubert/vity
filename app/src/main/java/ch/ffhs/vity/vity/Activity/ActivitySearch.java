package ch.ffhs.vity.vity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ch.ffhs.vity.vity.Helper.ActivityItem;
import ch.ffhs.vity.vity.Helper.ActivityListAdapter;
import ch.ffhs.vity.vity.Helper.ActivityRegistry;
import ch.ffhs.vity.vity.Mock.Activities_Mock;
import ch.ffhs.vity.vity.R;

public class ActivitySearch extends Activity {

    private ListView listView;
    private List<ActivityItem> list;
    private ActivityListAdapter listAdapter;

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
            /*case R.id.opt_map:
                startActivity(new Intent(this, ActivityMap.class));
                return true;
            case R.id.opt_new:
                startActivity(new Intent(this, ActivityNew.class));
                return true;
            case R.id.opt_search:
                // Already there
                return true;*/
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
    }

    public void onClickCancel(View button) {
        Intent home = new Intent(this, ActivityMap.class);
        finish();
        startActivity(home);
    }

    private void loadResults(){
        listView = (ListView) findViewById(R.id.list_activities);

        Activities_Mock mockDatea = new Activities_Mock();
        list = mockDatea.getActivities();

        listAdapter = new ActivityListAdapter(list, getApplicationContext());
        listView.setAdapter(listAdapter);
    }
}