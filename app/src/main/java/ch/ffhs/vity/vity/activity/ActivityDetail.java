package ch.ffhs.vity.vity.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static ch.ffhs.vity.vity.database.LocationTypeConverter.toLocation;

import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.VityItem;
import ch.ffhs.vity.vity.map.Map;


public class ActivityDetail extends Activity {
    private ImageView image;
    private TextView title;
    private TextView category;
    private TextView link;
    private TextView date;
    private TextView owner;
    private TextView description;
    private long id;
    private VityItem item;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_detail);
        image = findViewById(R.id.detail_image);
        title = findViewById(R.id.detail_title);
        category = findViewById(R.id.detail_cateogry);
        link = findViewById(R.id.detail_link);
        date = findViewById(R.id.detail_date);
        owner = findViewById(R.id.detail_owner);
        description = findViewById(R.id.detail_description);
        id = getIntent().getLongExtra("itemId", 0);
        loadActivity(id);
    }

    private void loadActivity(long id){
        AppDatabase mDb = AppDatabase.getDatabase(this.getApplication());
        item = mDb.itemModel().loadItemById(id);
        title.setText(item.getTitle());
        if(item.getCategory() != null){
            category.setText(item.getCategory());
        }
        if(item.getImageUri() != null){
            image.setImageURI(Uri.parse(item.getImageUri()));
        }
        if(item.getLink() != null){
            link.setText(item.getLink());
        }
        if(item.getDate() != null){
            date.setText(item.getDate());
        }
        if(item.getOwner() != null){
            owner.setText(item.getOwner());
        }
        if(item.getDescription() != null){
            description.setText(item.getDescription());
        }
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
    public void onClickShowOnMap(View button) {
        Location vityItemLocation = toLocation(item.getLocation());
        Intent mapView = new Intent(this, Map.class);
        Bundle b = new Bundle();
        b.putDouble("lat", vityItemLocation.getLatitude());
        b.putDouble("long", vityItemLocation.getLongitude());
        b.putString("title", item.getTitle());
        mapView.putExtras(b);
        startActivity(mapView);
    }

    public void onClickEdit(View button){
        Intent intentActivityEdit = new Intent(this, ActivityEdit.class);
        intentActivityEdit.putExtra("itemId", id);
        startActivity(intentActivityEdit);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        loadActivity(id);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadActivity(id);
    }

}
