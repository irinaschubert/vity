package ch.ffhs.vity.vity.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ch.ffhs.vity.vity.mock.Activities_Mock;
import ch.ffhs.vity.vity.R;


public class ActivityDetail extends Activity {

    private TextView title;
    private TextView category;
    private TextView link;
    private TextView date;
    private TextView owner;
    private TextView descritpion;

    private Activities_Mock mockData;
    private VityItem activity;
    private int id;

    private Image image;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_detail);
        ActivityRegistry.register(this);

        title = (TextView)findViewById(R.id.detail_title);
        category = (TextView)findViewById(R.id.detail_cateogry);
        link = (TextView)findViewById(R.id.detail_link);
        date = (TextView)findViewById(R.id.detail_date);
        owner = (TextView)findViewById(R.id.detail_owner);
        descritpion = (TextView)findViewById(R.id.detail_description);

        id = getIntent().getIntExtra("id", 0);

        loadMessage(id);
    }

    private void loadMessage(int id){
        mockData = new Activities_Mock();
        activity = mockData.getActivity(id);

        title.setText(activity.getTitle());
        category.setText(activity.getCategory());
        link.setText(activity.getLink());
        // date.setText(activity.getDate());
        owner.setText(activity.getOwner());
        descritpion.setText(activity.getDescription());
        descritpion.setText(activity.getDescription());
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
    public void onClickShowOnMap(View button) {
        Toast.makeText(getApplicationContext(), "showOnMap", Toast.LENGTH_LONG).show();
    }

    public void onClickCancel(View button) {
        finish();
    }

    public void onClickEdit(View button){
        Intent intentActivityEdit = new Intent(this, ActivityEdit.class);
        intentActivityEdit.putExtra("id", id);
        startActivity(intentActivityEdit);
    }
}
