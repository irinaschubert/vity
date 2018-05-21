package ch.ffhs.vity.vity.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.activity.ActivityNew;
import ch.ffhs.vity.vity.activity.ActivitySearch;
import ch.ffhs.vity.vity.activity.ActivitySettings;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    private static Location currentLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private LatLng itemPosition;
    private static final int REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Check if username is set already
        SharedPreferences username = PreferenceManager.getDefaultSharedPreferences(this);
        if(username.getString("username", "").equals("")){
            try {
                editUsername(username);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            if (!success) {
                Toast.makeText(getApplicationContext(), R.string.warning_not_load_map_style, Toast.LENGTH_LONG).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getApplicationContext(), R.string.warning_not_find_map_style, Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return;
        }

        // Check if GPS is enabled
        try {
            isGPSenbaled();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mMap.setMyLocationEnabled(true);

        setCurrentLocation();

        Intent i = getIntent();
        if(i != null){
            Bundle b = i.getExtras();
            if  (b != null)
            {
                LatLng itemPosition = new LatLng(b.getDouble("lat"), b.getDouble("long"));
                String itemTitle = b.getString("title");
                mMap.addMarker(new MarkerOptions().position(itemPosition).title(itemTitle));
                float zoomLevel = 16.0f;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itemPosition, zoomLevel));
            }
        }
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

    public static Location getCurrentLocation(){
        return currentLocation;
    }

    // Check permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 100: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                } else {
                    Toast.makeText(getApplicationContext(), R.string.permission_denied_gps, Toast.LENGTH_LONG).show();
                }
            }
            case REQUEST_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocation();
                }else {
                    Toast.makeText(getApplicationContext(), R.string.permission_denied_fine_location, Toast.LENGTH_LONG).show();
                }
            default:
                // The app will not have this permission
                finish();
        }
    }

    // Check if GPS is enabled
    private void isGPSenbaled() throws InterruptedException {
        final LocationManager lManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( lManager != null && !lManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // Set Title to Alert
            alertDialogBuilder.setTitle(R.string.gps_disabled);

            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.enable_gps)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    // Edit Username
    private void editUsername(final SharedPreferences username) throws InterruptedException {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // Set Title to Alert
        alertDialogBuilder.setTitle(R.string.username);

        // Build Input Field
        final EditText name = new EditText(this);
        name.setHint(R.string.username);
        alertDialogBuilder.setView(name);

        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.enter_name)
                .setPositiveButton(R.string.btn_save,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        SharedPreferences.Editor editor = username.edit();
                        editor.putString("username", name.getText().toString());
                        editor.apply();
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    // onClick Functions
    public void goToNewActivity(View button){
        startActivity(new Intent(this, ActivityNew.class));
    }

    public void goToSearchActivity(View button){
        startActivity(new Intent(this, ActivitySearch.class));
    }

    public void goNavigate(View button){
        if(itemPosition != null){
            float[] results = new float[1];
            currentLocation.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), itemPosition.latitude, itemPosition.longitude, results);
            LatLng myPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myPosition).title(getString(R.string.my_position)));
            float zoomLevel = 16.0f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, zoomLevel));
        }else{
            Toast.makeText(getApplicationContext(), R.string.warning_no_item_selected, Toast.LENGTH_LONG).show();
        }

    }
}
