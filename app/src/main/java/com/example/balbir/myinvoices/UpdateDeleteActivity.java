package com.example.balbir.myinvoices;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class UpdateDeleteActivity extends AppCompatActivity {

    private UserModel userModel;
    final int REQUEST_CODE_GALLERY = 999;
  //  private TextView invoice;
    ArrayAdapter<CharSequence> adapter;

    TextView upload;
    public  static final int RequestPermissionCode  = 1 ;
    ImageView mImageView;
    private boolean zoomOut =  false;
    private EditText title, shop,  comment;
    Spinner invoice;
    private TextView loc,date;
    static final int DATE_DIALOG_ID = 999;
    private int year;
    Button maps;
    private int month;
    private int day;
    private Button btnupdate, btndelete;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        Intent intent = getIntent();
        userModel = (UserModel) intent.getSerializableExtra("user");

        //   mImageView = findViewById(R.id.im);
        databaseHelper = new DatabaseHelper(this);

        mImageView = findViewById(R.id.imageView);

        upload = (TextView) findViewById(R.id.txtt);

        title = (EditText) findViewById(R.id.ettitle);
        date = (TextView) findViewById(R.id.etdate);

        invoice = (Spinner) findViewById(R.id.etinvoicetype);

        shop = (EditText) findViewById(R.id.etshop);
        loc = (TextView) findViewById(R.id.etloc);
        comment = (EditText) findViewById(R.id.etcomment);
        maps = (Button)findViewById(R.id.map);
        btndelete = (Button) findViewById(R.id.btndelete);
        btnupdate = (Button) findViewById(R.id.btnupdate);
        byte[] outImage=userModel.image;
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        mImageView.setImageBitmap(theImage);
        title.setText(userModel.getTitle());
        date.setText(userModel.getdate());

        shop.setText(userModel.getshop());
        loc.setText(userModel.getloc());
        adapter = ArrayAdapter.createFromResource(this, R.array.tutor_array, android.R.layout.simple_list_item_checked);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        invoice.setAdapter(adapter);

        invoice.setSelection(adapter.getPosition(userModel.getinvoice()));


        //     invoice.setText(userModel.getinvoice());
        comment.setText(userModel.getcomment());
        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        EnableRuntimePermission();

       maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(loc.getText().toString()));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }, 1000);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(intent, 7);

            }
        });
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 7);

            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.updateUser(userModel.getId(), title.getText().toString(), date.getText().toString(), invoice.getSelectedItem().toString(), shop.getText().toString(), loc.getText().toString(), comment.getText().toString(), imageViewToByte(mImageView));
                Toast.makeText(UpdateDeleteActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateDeleteActivity.this, GetAllUsersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteUSer(userModel.getId());
                Toast.makeText(UpdateDeleteActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateDeleteActivity.this, GetAllUsersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
     public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
                    date.setText(new StringBuilder().append(month + 1).append("-")
                            .append(day).append("-").append(year).append(" "));

                }
            };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            mImageView.setImageBitmap(bitmap);
        }
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateDeleteActivity.this,
                Manifest.permission.CAMERA))
        {

         //   Toast.makeText(UpdateDeleteActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(UpdateDeleteActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                } else {


                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        databaseHelper.updateUser(userModel.getId(), title.getText().toString(), date.getText().toString(), invoice.getSelectedItem().toString(), shop.getText().toString(), loc.getText().toString(), comment.getText().toString(), imageViewToByte(mImageView));
        Toast.makeText(UpdateDeleteActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UpdateDeleteActivity.this, GetAllUsersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}