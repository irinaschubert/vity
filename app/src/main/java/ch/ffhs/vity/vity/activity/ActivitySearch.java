package ch.ffhs.vity.vity.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.database.LocationTypeConverter;
import ch.ffhs.vity.vity.database.VityItem;

import static android.location.Location.distanceBetween;
import static android.widget.Toast.makeText;


public class ActivitySearch extends BaseActivity {
    private static final int REQUEST_FINE_LOCATION = 1;
    private AppDatabase mDb;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location currentLocation;
    private ListView listView;
    private int radius;
    private ArrayList<VityItem> resultList;
    private SearchItemsAsync task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = findViewById(R.id.list_activities);
        resultList = new ArrayList<>();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setCurrentLocation();
        currentLocation = getCurrentLocation();
    }


    // onClickFunctions
    public void onClickSearchActivity(View button) {
        loadResults();
    }

    private void loadResults() {
        mDb = AppDatabase.getDatabase(this.getApplication());
        Spinner categorySpinner = findViewById(R.id.new_category);
        String category = categorySpinner.getSelectedItem().toString();

        Spinner radiusSpinner = findViewById(R.id.search_radius);
        radius = Integer.parseInt(radiusSpinner.getSelectedItem().toString());

        searchItems(category);
    }

    private String getDistance(VityItem item) {
        Location itemLocation = LocationTypeConverter.toLocation(item.getLocation());
        Location currentLocation = getCurrentLocation();
        float[] results = new float[1];
        distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), itemLocation.getLatitude(), itemLocation.getLongitude(), results);
        DecimalFormat df = new DecimalFormat("#0");
        return df.format(results[0]);
    }

    private void setCurrentLocation() {
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

    public Location getCurrentLocation() {
        return currentLocation;
    }

    // Check permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocation();
                } else {
                    makeText(getApplicationContext(), R.string.permission_denied_fine_location, Toast.LENGTH_LONG).show();
                }
            default:
                // The app will not have this permission
                finish();
        }
    }

    private void searchItems(String category) {
        task = new SearchItemsAsync(category, mDb, this);
        task.setListener(new SearchItemsAsync.SearchItemsAsyncListener() {
            @Override
            public void onSearchItemsFinished(ArrayList<VityItem> list) {
                resultList = list;
                final VityItemListAdapter listAdapter = new VityItemListAdapter(resultList, getApplicationContext());

                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        VityItem selectedItem = (VityItem) listAdapter.getItem(position);
                        long itemId = selectedItem.getId();
                        Intent i = new Intent(view.getContext(), ActivityDetail.class);
                        i.putExtra("itemId", itemId);
                        startActivity(i);
                    }
                });
            }
        });
        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setCurrentLocation();
    }

    static class SearchItemsAsync extends AsyncTask<String, Void, ArrayList<VityItem>> {
        private String category;
        private AppDatabase mDb;
        private WeakReference<ActivitySearch> activityReference;
        private SearchItemsAsyncListener listener;

        SearchItemsAsync(String category, AppDatabase mDb, ActivitySearch context) {

            this.category = category;
            this.mDb = mDb;
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected ArrayList<VityItem> doInBackground(String... params) {
            return new ArrayList<>(mDb.itemModel().findItemByCategory(category));
        }

        @Override
        protected void onPostExecute(ArrayList<VityItem> listAllWithChosenCategory) {
            ActivitySearch activity = activityReference.get();

            for (VityItem item : listAllWithChosenCategory) {
                if (Float.parseFloat(activity.getDistance(item)) <= activity.radius) {
                    activity.resultList.add(item);
                }
            }

            if (activity.resultList.isEmpty()) {
                makeText(activity.getApplicationContext(), R.string.no_results, Toast.LENGTH_LONG).show();
            }

            if (listener != null) {
                listener.onSearchItemsFinished(activity.resultList);
            }


        }

        private void setListener(SearchItemsAsyncListener listener) {
            this.listener = listener;
        }

        public interface SearchItemsAsyncListener {
            void onSearchItemsFinished(ArrayList<VityItem> list);
        }
    }

}