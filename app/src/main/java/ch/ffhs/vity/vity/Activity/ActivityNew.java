package ch.ffhs.vity.vity.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.annotation.RetentionPolicy;

import ch.ffhs.vity.vity.Helper.ActivityRegistry;
import ch.ffhs.vity.vity.R;

public class ActivityNew extends Activity {
    public static final int PICK_IMAGE = 1;
    private ImageView newImage;
    int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_new);
        ActivityRegistry.register(this);

        // get a reference to the image view that holds the image that the user will see.
        newImage = (ImageView) findViewById(R.id.new_detail_image);
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
    public void onClickAddPicture(View button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(R.array.img_choice, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0:
                                Intent getImageIntent = new Intent();
                                getImageIntent.setType("image/*");
                                getImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                                if (getImageIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(Intent.createChooser(getImageIntent, "Select Picture"), PICK_IMAGE);
                                }

                            case 1:
                                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
                                }else{
                                    dispatchTakePictureIntent();
                                }

                            default:
                                finish();
                        }

                    }
                });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();

            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission
                Toast.makeText(getApplicationContext(), "didn't work", Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            newImage.setImageBitmap(imageBitmap);
        }
    }


    public void onClickAddLocation(View button) {
        Toast.makeText(getApplicationContext(), "addLocation", Toast.LENGTH_LONG).show();
     }

    public void onClickSaveNewActivity(View button) {
        Toast.makeText(getApplicationContext(), "saveNewActivity", Toast.LENGTH_LONG).show();
     }

    public void onClickCancel(View button) {
        finish();
    }

}
