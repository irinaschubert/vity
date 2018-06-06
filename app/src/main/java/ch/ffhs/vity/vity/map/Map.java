package ch.ffhs.vity.vity.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.activity.ActivityNew;
import ch.ffhs.vity.vity.activity.ActivitySearch;
import ch.ffhs.vity.vity.activity.ActivitySettings;
import ch.ffhs.vity.vity.menu.BaseActivity;

public class Map extends BaseActivity implements OnMapReadyCallback {
    private static Location currentLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleMap mMap;
    private static final int REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_FINE_LOCATION_FOR_CURRENTLOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        isGPSenbaled();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }else{
            mMap.setMyLocationEnabled(true);
            setCurrentLocation();
        }

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION_FOR_CURRENTLOCATION);
        }else{
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location newLocation) {
                    if (newLocation != null) {
                        currentLocation = newLocation;
                    }
                }
            });
        }
    }

    public static Location getCurrentLocation(){
        return currentLocation;
    }

    // Check permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_FINE_LOCATION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
                    }else {
                        mMap.setMyLocationEnabled(true);
                        setCurrentLocation();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.permission_denied_fine_location, Toast.LENGTH_LONG).show();
                }
                return;
            }
            case REQUEST_FINE_LOCATION_FOR_CURRENTLOCATION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    setCurrentLocation();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.permission_denied_fine_location_3, Toast.LENGTH_LONG).show();
                }
                return;
            }
            default:
                // The app will not have this permission
                finish();
        }
    }

    // Check if GPS is enabled
    private void isGPSenbaled() {
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

    public void goToNewActivity(View button){
        startActivity(new Intent(this, ActivityNew.class));
    }

    public void goToSearchActivity(View button){
        startActivity(new Intent(this, ActivitySearch.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setCurrentLocation();
    }
}
