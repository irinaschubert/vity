package ch.ffhs.vity.vity.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ch.ffhs.vity.vity.R;
import ch.ffhs.vity.vity.database.AppDatabase;
import ch.ffhs.vity.vity.database.VityItem;

import static ch.ffhs.vity.vity.database.LocationTypeConverter.locationToString;

public class ActivityEdit extends BaseActivity {
    private static final int REQUEST_CODE_STORAGE = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    private static final int REQUEST_IMAGE_PICK = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;
    private static final int REQUEST_CODE_FINE_LOCATION = 5;
    private static final int REQUEST_CODE_STORAGE_FOR_TAKEN_PICTURES = 6;
    private ImageView image;
    private EditText title;
    private EditText description;
    private EditText link;
    private TextView location;
    private Spinner categorySpinner;
    private AppDatabase mDb;
    private VityItem item;
    private Location currentLocation;
    private FusedLocationProviderClient locationClient;
    private String mCurrentPhotoPath;
    private long id;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_edit);
        mCurrentPhotoPath = "";
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        id = getIntent().getLongExtra("itemId", 0);
        loadActivity(id);
    }

    private void loadActivity(long id) {
        mDb = AppDatabase.getDatabase(this.getApplication());
        title = findViewById(R.id.new_name);
        description = findViewById(R.id.new_description);
        link = findViewById(R.id.new_link);
        location = findViewById(R.id.new_detail_location);
        image = findViewById(R.id.new_detail_image);
        categorySpinner = findViewById(R.id.new_category);

        LoadItemAsync task = new ActivityEdit.LoadItemAsync(id, mDb);
        task.setListener(new ActivityEdit.LoadItemAsync.LoadItemAsyncListener() {
            @Override
            public void onLoadItemFinished(VityItem vityitem) {
                item = vityitem;

                title.setText(item.getTitle(), TextView.BufferType.EDITABLE);
                description.setText(item.getDescription(), TextView.BufferType.EDITABLE);
                link.setText(item.getLink(), TextView.BufferType.EDITABLE);
                location.setText(item.getLocation(), TextView.BufferType.NORMAL);
                if (item.getImageUri() != null) {
                    image.setImageURI(Uri.parse(item.getImageUri()));
                }
                String category = item.getCategory();
                categorySpinner.setSelection(getSpinnerIndex(categorySpinner, category));
            }
        });
        task.execute();
    }

    private int getSpinnerIndex(Spinner spinner, String string) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(string)) {
                index = i;
            }
        }
        return index;
    }

    public void onClickAddPicture(View button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.img_choice, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //checks permission for external storage access on runtime and asks if not yet set
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                        } else {
                            getPicture();
                        }
                        break;
                    case 1:
                        //checks permission for camera usage on runtime and asks if not yet set
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                        } else {
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
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPicture();
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                break;
            case REQUEST_CODE_STORAGE_FOR_TAKEN_PICTURES:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                break;
            case REQUEST_CODE_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    printCurrentLocation();
                }
                break;
            default:
                // The app will not have this permission
                finish();
        }
    }

    private void getPicture() {
        Intent getPictureIntent = new Intent();
        getPictureIntent.setType("image/*");
        getPictureIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(getPictureIntent, getResources().getText(R.string.select_picture_using)), REQUEST_IMAGE_PICK);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ch.ffhs.vity.vity.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_FOR_TAKEN_PICTURES);
                } else {
                    startActivityForResult(Intent.createChooser(takePictureIntent, getResources().getText(R.string.take_picture_using)), REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_PICK:
                if (resultCode == RESULT_OK) {
                    Uri photoUri = data.getData();
                    try {
                        mCurrentPhotoPath = photoUri.toString();
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.bad_image_request), Toast.LENGTH_LONG).show();
                    }

                    try {
                        image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri));

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.bad_image_request), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Uri photoUri = savePictureToGallery();
                    try {
                        image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), getResources().getText(R.string.bad_image_request), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                finish();
        }
    }

    private Uri savePictureToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = FileProvider.getUriForFile(this, "ch.ffhs.vity.vity.fileprovider", f);
        mediaScanIntent.setData(contentUri);
        mediaScanIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, getResources().getText(R.string.image_saved) + contentUri.toString(), Toast.LENGTH_LONG).show();
        return contentUri;
    }

    private File createImageFile() throws IOException {
        // Create an image file name with timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onClickAddLocation(View button) {
        printCurrentLocation();
    }

    private void printCurrentLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE_LOCATION);
        } else {
            locationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location newLocation) {

                    if (newLocation != null) {
                        currentLocation = newLocation;
                        location.setText(locationToString(currentLocation));
                    }
                }
            });
        }
    }

    public void onClickUpdate(View button) {
        item.setTitle(title.getText().toString());
        item.setDescription(description.getText().toString());
        item.setLink(link.getText().toString());
        item.setLocation(location.getText().toString());
        item.setCategory(categorySpinner.getSelectedItem().toString());
        String username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
        long currentDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        String currentDateString = sdf.format(currentDate);
        item.setOwner(username);
        item.setDate(currentDateString);
        if (!"".equals(mCurrentPhotoPath)) {
            item.setImageUri(mCurrentPhotoPath);
        }
        //new activity should at least have a title
        if (title.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.warning_missing_title), Toast.LENGTH_SHORT).show();
        } else {
            mDb.itemModel().updateItem(item);
            finish();
        }
    }

    public void onClickDelete(View button) {
        mDb.itemModel().deleteItem(item);
        Intent activity = new Intent(this, ActivitySearch.class);
        startActivity(activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActivity(id);
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
