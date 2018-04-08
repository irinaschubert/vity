package ch.ffhs.vity.vity.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ch.ffhs.vity.vity.helper.ActivityItem;
import ch.ffhs.vity.vity.helper.ActivityRegistry;
import ch.ffhs.vity.vity.mock.Activities_Mock;
import ch.ffhs.vity.vity.R;

/**
 * Created by irina on 18.03.2018.
 */

public class ActivityEdit extends Activity {
    private static final int REQUEST_CODE_STORAGE = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    private static final int REQUEST_IMAGE_PICK = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;

    private ImageView newImage;

    private String title;
    private String category;
    private String link;
    private String date;
    private String owner;
    private String descritpion;

    private Activities_Mock mockData;
    private ActivityItem activity;

    ArrayAdapter<String> categoryAdapter;

    private int id;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_edit);
        ActivityRegistry.register(this);

        newImage = (ImageView) findViewById(R.id.new_detail_image);
        id = getIntent().getIntExtra("id", 0);

        loadActivity(id);
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

    private void loadActivity(int id){
        this.id = id;
        mockData = new Activities_Mock();
        activity = mockData.getActivity(id);

        EditText title = findViewById(R.id.new_name);
        title.setText(activity.getTitle(), TextView.BufferType.EDITABLE);

        EditText description = findViewById(R.id.new_description);
        description.setText(activity.getDescription(), TextView.BufferType.EDITABLE);

        EditText link = findViewById(R.id.new_link);
        link.setText(activity.getLink(), TextView.BufferType.EDITABLE);

        // sets category value to category spinner
        String category = activity.getCategory();
        Spinner categorySpinner = findViewById(R.id.new_category);
        categorySpinner.setSelection(getIndex(categorySpinner, category));
    }

    private int getIndex(Spinner spinner, String string){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(string)){
                index = i;
            }
        }
        return index;
    }

    public void onClickAddPicture(View button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.img_choice, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        //checks permission on runtime and asks if not set
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                        }else {
                            getPicture();
                        }
                        break;
                    case 1:
                        //checks permission on runtime and asks if not set
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                        }else{
                            takePicture();
                        }
                        break;
                    default:
                        finish();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPicture();
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                break;
            default:
                // The app will not have this permission
                finish();
        }
    }

    private void getPicture(){
        Intent getPictureIntent = new Intent();
        getPictureIntent.setType("image/*");
        getPictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getPictureIntent, "Select your picture"), REQUEST_IMAGE_PICK);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_IMAGE_PICK:
                if(resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();

 /*                   String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap img = BitmapFactory.decodeFile(filePath);

                    newImage.setImageBitmap(img);*/
                    try {
                        newImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Bad Image Request. Please Try Again!", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap img = (Bitmap) extras.get("data");
                    newImage.setImageBitmap(img);
                }
                break;
            default:
                finish();
        }
    }

    // onClickFunctions
    public void onClickAddLocation(View button) {
        Toast.makeText(getApplicationContext(), "addLocation", Toast.LENGTH_LONG).show();
    }

    public void onClickUpdateActivity(View button) {
        Toast.makeText(getApplicationContext(), "updateActivity", Toast.LENGTH_LONG).show();
    }

    public void onClickCancel(View button) {
        finish();
    }
}
