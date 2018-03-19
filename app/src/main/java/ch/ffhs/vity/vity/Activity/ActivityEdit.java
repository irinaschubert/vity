package ch.ffhs.vity.vity.Activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ch.ffhs.vity.vity.Helper.ActivityItem;
import ch.ffhs.vity.vity.Helper.ActivityRegistry;
import ch.ffhs.vity.vity.Mock.Activities_Mock;
import ch.ffhs.vity.vity.R;

/**
 * Created by irina on 18.03.2018.
 */

public class ActivityEdit extends Activity {

    private String title;
    private String category;
    private String link;
    private String date;
    private String owner;
    private String descritpion;

    private Activities_Mock mockData;
    private ActivityItem activity;

    ArrayAdapter<String> categoryAdapter;

    private int id;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_edit);
        ActivityRegistry.register(this);

        id = getIntent().getIntExtra("id", 0);

        loadActivity(id);
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

    private void loadActivity(int id){
        this.id = id;
        mockData = new Activities_Mock();
        activity = mockData.getActivity(id);

        EditText title = findViewById(R.id.new_name);
        title.setText(activity.getTitle(), TextView.BufferType.EDITABLE);

        EditText description = findViewById(R.id.new_description);
        description.setText(activity.getDescription(), TextView.BufferType.EDITABLE);

    }

    // onClickFunctions
    public void onClickAddPicture(View button) {
        Toast.makeText(getApplicationContext(), "addPicture", Toast.LENGTH_LONG).show();
    }

    public void onClickAddLocation(View button) {
        Toast.makeText(getApplicationContext(), "addLocation", Toast.LENGTH_LONG).show();
    }

    public void onClickUpdateActivity(View button) {
        Toast.makeText(getApplicationContext(), "updateActivity", Toast.LENGTH_LONG).show();
    }

    public void onClickCancel(View button) {
        finish();
    }
}
