package ch.ffhs.vity.vity.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.ffhs.vity.vity.helper.ActivityRegistry;
import ch.ffhs.vity.vity.R;


public class ActivityMap extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Toast.makeText(getApplicationContext(),"onCreate", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_map);
        ActivityRegistry.register(this);

        // Check if username is set already
        SharedPreferences username = PreferenceManager.getDefaultSharedPreferences(this);
        if(username.getString("username", "0").equals("0")){
            try {
                editUsername(username);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
            case R.id.opt_exit:
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            if (!success) {
                Toast.makeText(getApplicationContext(), "Konnte Map Style nicht laden!!", Toast.LENGTH_LONG).show();
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(getApplicationContext(), "Konnte Map Style nicht finden!!", Toast.LENGTH_LONG).show();
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

        LatLng bern = new LatLng(46.948393, 7.436325);
        mMap.addMarker(new MarkerOptions().position(bern).title("Welle 7"));
        float zoomLevel = 16.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bern, zoomLevel));

    }

    // Überprüft die geforderten Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 100: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Toast.makeText(getApplicationContext(), "Permission granted GPS", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied using GPS", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // Check if GPS is enabled
    private void isGPSenbaled() throws InterruptedException {
        final LocationManager lManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( lManager != null && !lManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // Set Title to Alert
            alertDialogBuilder.setTitle("GPS is disabled!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("To use the App with your current Location, you have to turn on gps!")
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                            dialog.cancel();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
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
        alertDialogBuilder.setTitle("Username");

        // Build Input Field
        final EditText name = new EditText(this);
        name.setHint("Username");
        alertDialogBuilder.setView(name);

        // set dialog message
        alertDialogBuilder
                .setMessage("Please enter your Name!")
                .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        SharedPreferences.Editor editor = username.edit();
                        editor.putString("username", name.getText().toString());
                        editor.apply();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                        ActivityRegistry.finishAll();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    // Button Functions
    public void goToNewActivity(View button){
        startActivity(new Intent(this, ActivityNew.class));
    }

    public void goToSearchActivity(View button){
        startActivity(new Intent(this, ActivitySearch.class));
    }

    public void goNavigate(View button){
        Toast.makeText(getApplicationContext(), "Navigate..", Toast.LENGTH_LONG).show();
    }

    // Zeige Android LifeCycle
    /*
    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_LONG).show();
    }
    */

}
