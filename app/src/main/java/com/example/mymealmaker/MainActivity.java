package com.example.mymealmaker;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import  android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.auth.BasicAWSCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CAMERA = 1;
    static final int REQUEST_EXTERNAL_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView ingredientList = (ListView) findViewById(R.id.list_view);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // onClick for camera button ->
                captureImage();
            }
        });

        Properties awsKey = new Properties();
        try {
            // try to read the properties file
            InputStream awsKeyReadStream = getBaseContext().getAssets().open("AWS.properties");
            awsKey.load(awsKeyReadStream);
            awsKeyReadStream.close();
        }
        catch (IOException exception) {
            Toast.makeText(this, "An Error has Occurred", Toast.LENGTH_SHORT).show();
        }

        // get the key and secret
        String keyID = awsKey.getProperty("keyID");
        String secret = awsKey.getProperty("secret");

        // log into AWS Rekognition
        AmazonRekognitionClient myClient = new AmazonRekognitionClient(new BasicAWSCredentials(keyID, secret));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void captureImage() {
        final String optCamera = "Take a Photo";
        final String optGallery = "Choose from Gallery";
        final String optCancel = "Cancel";
        final String alertTitle = "Choose a method to add ingredient";

        final CharSequence[] options = {optCamera, optGallery, optCancel};
        final AlertDialog.Builder optionAlertBuilder = new AlertDialog.Builder(MainActivity.this);

        optionAlertBuilder.setTitle(alertTitle);
        optionAlertBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(optCamera)) {
                    try {
                        Intent cameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        if(cameraIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, REQUEST_CAMERA);
                        }
                    } catch (ActivityNotFoundException ex) {
                        String errorMessage = "Camera was not accessible";
                    }
                } else if (options[which].equals(optGallery)) {

                    Intent galleryIntent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if(galleryIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(galleryIntent, REQUEST_EXTERNAL_IMAGE);
                    }
                } else if (options[which].equals(optCancel)) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog dialog = optionAlertBuilder.create();
        dialog.show();
    }

    public void onActivityResult(int requestcode, int resultcode, Intent intent) {
        super.onActivityResult(requestcode, resultcode, intent);
        Bitmap sourceImage = null;

        if (resultcode == RESULT_OK) {
            if (requestcode == REQUEST_CAMERA) {
                sourceImage = (Bitmap) intent.getExtras().get("data");
            } else if (requestcode == REQUEST_EXTERNAL_IMAGE) {
                Uri selectedImageUri = intent.getData();
                sourceImage = (BitmapFactory.decodeFile(selectedImageUri.toString()));
            }

        }
    }
}
