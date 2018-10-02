package com.example.balbir.myinvoices;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LocationListener {
    ImageView im;
    Button b;

    LocationManager locationManager;
   // ImageView mImageView;
    TextView upload;
    private boolean zoomOut =  false;
    ImageView mImageView;
    Bitmap bmapp;
    ArrayAdapter<CharSequence> adapter;
    final int REQUEST_CODE_GALLERY = 999;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button btnStore, btnGetall;
    private EditText title, shop,  comment;
    private  TextView date,loc ;
    public  static final int RequestPermissionCode  = 1 ;
   // TextView upload;
    Spinner invoice;
   // final Calendar c = Calendar.getInstance();
    static final int DATE_DIALOG_ID = 999;
    private int year;
    private int month;
    private int day;
    Integer mYear, mMonth, mDay;
    private DatabaseHelper databaseHelper;
    // String s1, s2, s3, s4, s5, s6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        databaseHelper = new DatabaseHelper(this);
        mImageView = findViewById(R.id.imageView);

        upload=(TextView) findViewById(R.id.txtt);

        btnStore = (Button) findViewById(R.id.btnAdd);
        btnGetall = (Button) findViewById(R.id.btnList);

        title = (EditText) findViewById(R.id.ettitle);
        date = (TextView) findViewById(R.id.etdate);
        invoice = (Spinner) findViewById(R.id.etinvoicetype);
        shop = (EditText) findViewById(R.id.etshop);
        loc = (TextView) findViewById(R.id.etloc);
        comment = (EditText) findViewById(R.id.etcomment);

        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        adapter = ArrayAdapter.createFromResource(this, R.array.tutor_array, android.R.layout.simple_list_item_checked);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        invoice.setAdapter(adapter);


      date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EnableRuntimePermission();
           Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 7);

            }
        });

        //select image by on imageview click
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }, 1000);
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper.addUser(title.getText().toString(), date.getText().toString(), invoice.getSelectedItem().toString(), shop.getText().toString(), loc.getText().toString(), comment.getText().toString(), imageViewToByte(mImageView));

                Toast.makeText(MainActivity.this, "Stored Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, GetAllUsersActivity.class);
                startActivity(intent);
            }
        });

        btnGetall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  databaseHelper.addUser(title.getText().toString(), date.getText().toString(), invoice.getSelectedItem().toString(), shop.getText().toString(), loc.getText().toString(), comment.getText().toString(), imageViewToByte(mImageView));

                Toast.makeText(MainActivity.this, "Cancelled action", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, GetAllUsersActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);
        }
        return null;
    }

    public DatePickerDialog.OnDateSetListener datePickerListener =
            new    DatePickerDialog.   OnDateSetListener(){

                // when dialog box is closed, below method will be called.
                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    year = selectedYear;
                    month = selectedMonth;
                    day = selectedDay;

                    // set selected date into edittext
                    date.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append(" "));

                }
            };


    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            loc.setText(addresses.get(0).getAddressLine(0));
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    } public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            mImageView.setImageBitmap(bitmap);
        }
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA))
        {

           // Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

              //      Toast.makeText(MainActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

              //      Toast.makeText(MainActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
       if( (title.getText().toString().equals(""))&& (date.getText().toString().equals(""))&& (invoice.getSelectedItem().toString().equals(""))&& (shop.getText().toString().equals("")&&(loc.getText().toString().equals(""))&& (comment.getText().toString().equals("")) &&((imageViewToByte(mImageView).equals("")))))
        {

            Toast.makeText(MainActivity.this, "empty data!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, GetAllUsersActivity.class);
            startActivity(intent);
        }
        else {

           databaseHelper.addUser(title.getText().toString(), date.getText().toString(), invoice.getSelectedItem().toString(), shop.getText().toString(), loc.getText().toString(), comment.getText().toString(), imageViewToByte(mImageView));

           Toast.makeText(MainActivity.this, "Stored Successfully!", Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(MainActivity.this, GetAllUsersActivity.class);
           startActivity(intent);
       } }
}

