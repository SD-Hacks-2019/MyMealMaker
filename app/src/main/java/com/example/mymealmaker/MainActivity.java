package com.example.mymealmaker;

import android.content.res.AssetManager;
import android.os.Bundle;

import com.amazonaws.util.IOUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.auth.BasicAWSCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Properties fAWSKey = new Properties();
        try {
            // try to read the AWS properties file
            InputStream awsKeyReadStream = getBaseContext().getAssets().open("AWS.properties");
            fAWSKey.load(awsKeyReadStream);
            awsKeyReadStream.close();
        }
        catch (IOException exception) {
            Toast.makeText(this, "An Error has Occurred", Toast.LENGTH_SHORT).show();
        }

        // get the key and secret
        String keyID = fAWSKey.getProperty("keyID");
        String secret = fAWSKey.getProperty("secret");
        System.out.println("test");

        try {
            InputStream testImage = getAssets().open("IMG_20191026_102738.jpg");
            byte[] imgArr = IOUtils.toByteArray(testImage);
            Ingredient testIngredient = new Ingredient(keyID, secret, imgArr);
            Thread myThread = new Thread(testIngredient);
            myThread.start();
            myThread.join();
            System.out.println(testIngredient.getLabel());
        } catch (Exception e) {}
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
}
