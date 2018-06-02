package ch.ffhs.vity.vity.activity;

import android.Manifest;
import android.app.Activity;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.LocationTypeConverter;
import ch.ffhs.vity.vity.database.VityItem;
import ch.ffhs.vity.vity.database.VityItemDao;

import static android.location.Location.distanceBetween;
import static android.widget.Toast.makeText;


public class ActivitySearch extends Activity {
    private AppDatabase mDb;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int REQUEST_FINE_LOCATION = 1;
    private Location currentLocation;
    private ListView listView;
    private int radius;
    private ArrayList<VityItem> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = findViewById(R.id.list_activities);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setCurrentLocation();
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
        Spinner categorySpinner = findViewById(R.id.new_category);
        String category = categorySpinner.getSelectedItem().toString();

        Spinner radiusSpinner = findViewById(R.id.search_radius);
        radius = Integer.parseInt(radiusSpinner.getSelectedItem().toString());

        searchItems(category);
        //ArrayList<VityItem> listAllWithChosenCategory = new ArrayList<>(mDb.itemModel().findItemByCategory(category));
    }

    private String getDistance(VityItem item){
        Location itemLocation = LocationTypeConverter.toLocation(item.getLocation());
        Location currentLocation = getCurrentLocation();
        float[] results = new float[1];
        distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), itemLocation.getLatitude(), itemLocation.getLongitude(), results);
        DecimalFormat df = new DecimalFormat("#0");
        return df.format(results[0]);
    }

    private void setCurrentLocation(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location newLocation) {

                if (newLocation != null) {
                    currentLocation = newLocation;
                }
            }
        });
    }

    public Location getCurrentLocation(){
        return currentLocation;
    }

    // Check permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocation();
                }else {
                    makeText(getApplicationContext(), R.string.permission_denied_fine_location, Toast.LENGTH_LONG).show();
                }
            default:
                // The app will not have this permission
                finish();
        }
    }

    private void searchItems(String category) {
        SearchItemsAsync task = new SearchItemsAsync(category, mDb);
        task.execute();
    }

    private class SearchItemsAsync extends AsyncTask<String, Void, ArrayList<VityItem>> {
        private String category;
        private AppDatabase mDb;
        private ArrayList resultList;

        SearchItemsAsync(String category, AppDatabase mDb) {
            resultList = new ArrayList<VityItem>();
            this.category = category;
            this.mDb = mDb;
        }
        @Override
        protected ArrayList<VityItem> doInBackground(String... params) {
            ArrayList<VityItem> listAllWithChosenCategory = new ArrayList<>(mDb.itemModel().findItemByCategory(category));
            return listAllWithChosenCategory;
        }

        @Override
        protected void onPostExecute(ArrayList<VityItem> listAllWithChosenCategory){
            for (VityItem item : listAllWithChosenCategory) {
                if (Float.parseFloat(getDistance(item)) <= radius) {
                    resultList.add(item);
                }
            }

            if(resultList.isEmpty()){
                makeText(getApplicationContext(), R.string.no_results, Toast.LENGTH_LONG).show();
            }

            final VityItemListAdapter listAdapter = new VityItemListAdapter(resultList, getApplicationContext());
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        setCurrentLocation();
    }

}