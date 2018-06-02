package ch.ffhs.vity.vity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;

import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.map.Map;
import ch.ffhs.vity.vity.mock.DatabaseInitializer;

public class ActivityMain extends Activity {

    private AppDatabase mDb;
    private String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        final Intent mapView = new Intent(this, Map.class);
        SharedPreferences username = PreferenceManager.getDefaultSharedPreferences(this);

        // load mock data
        mDb = AppDatabase.getDatabase(this.getApplication());
        populateDb();

        // Check if username is set already
        if (username.getString(USERNAME, "").equals("")) {
            editUsername(username);
        }
        else{
            startActivity(mapView);
        }
    }

    private void populateDb() {
        DatabaseInitializer.populateAsync(mDb);
    }

    // Edit Username
    private void editUsername(final SharedPreferences username) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(USERNAME);
        final EditText name = new EditText(this);
        name.setHint(USERNAME);
        alertDialogBuilder.setView(name);

        alertDialogBuilder
                .setMessage(R.string.enter_name)
                .setPositiveButton(R.string.btn_save,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        SharedPreferences.Editor editor = username.edit();
                        editor.putString("username", name.getText().toString());
                        editor.apply();
                        dialog.cancel();
                        Intent mapView = new Intent(getApplicationContext(), ActivityMain.class);
                        startActivity(mapView);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}