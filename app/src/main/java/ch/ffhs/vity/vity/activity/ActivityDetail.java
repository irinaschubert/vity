package ch.ffhs.vity.vity.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.database.VityItem;
import ch.ffhs.vity.vity.map.Map;

import static ch.ffhs.vity.vity.database.LocationTypeConverter.toLocation;


public class ActivityDetail extends BaseActivity {
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

    private void loadActivity(long id) {
        AppDatabase mDb = AppDatabase.getDatabase(this.getApplication());
        LoadItemAsync task = new LoadItemAsync(id, mDb);
        task.setListener(new LoadItemAsync.LoadItemAsyncListener() {
            @Override
            public void onLoadItemFinished(VityItem vityitem) {
                item = vityitem;

                title.setText(item.getTitle());
                if (item.getCategory() != null) {
                    category.setText(item.getCategory());
                }
                if (item.getImageUri() != null) {
                    image.setImageURI(Uri.parse(item.getImageUri()));
                }
                if (item.getLink() != null) {
                    link.setText(item.getLink());
                }
                if (item.getDate() != null) {
                    date.setText(item.getDate());
                }
                if (item.getOwner() != null) {
                    owner.setText(item.getOwner());
                }
                if (item.getDescription() != null) {
                    description.setText(item.getDescription());
                }
            }
        });
        task.execute();
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

    public void onClickEdit(View button) {
        Intent intentActivityEdit = new Intent(this, ActivityEdit.class);
        intentActivityEdit.putExtra("itemId", id);
        startActivity(intentActivityEdit);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadActivity(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActivity(id);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    static class LoadItemAsync extends AsyncTask<Long, Void, VityItem> {
        private long id;
        private AppDatabase mDb;
        private LoadItemAsyncListener listener;
        private VityItem vityitem;

        LoadItemAsync(long id, AppDatabase mDb) {
            this.id = id;
            this.mDb = mDb;
            this.vityitem = new VityItem();
        }

        @Override
        protected VityItem doInBackground(Long... params) {
            this.vityitem = mDb.itemModel().loadItemById(id);
            return vityitem;
        }

        @Override
        protected void onPostExecute(VityItem vityitem) {
            this.vityitem = vityitem;
            if (listener != null) {
                listener.onLoadItemFinished(vityitem);
            }
        }

        private void setListener(LoadItemAsyncListener listener) {
            this.listener = listener;
        }

        public interface LoadItemAsyncListener {
            void onLoadItemFinished(VityItem vityitem);
        }
    }
}
