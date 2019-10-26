package com.example.mymealmaker;

import android.os.Bundle;

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

        Properties awsKey = new Properties();
        Properties edamamKey = new Properties();
        try {
            // try to read the AWS properties file
            InputStream awsKeyReadStream = getBaseContext().getAssets().open("AWS.properties");
            awsKey.load(awsKeyReadStream);
            awsKeyReadStream.close();

            // try to read the Edamam properties file
            InputStream edamamKeyReadStream = getBaseContext().getAssets().open("Edamam.properties");
            edamamKey.load(edamamKeyReadStream);
            edamamKeyReadStream.close();
        }
        catch (IOException exception) {
            Toast.makeText(this, "An Error has Occurred", Toast.LENGTH_SHORT).show();
        }

        // get the AWS key and secret
        String keyID = awsKey.getProperty("keyID");
        String secret = awsKey.getProperty("secret");

        // log into AWS Rekognition
        AmazonRekognitionClient myAWSClient = new AmazonRekognitionClient(new BasicAWSCredentials(keyID, secret));

        // get the Edamam key and secret
        String appID = edamamKey.getProperty("appID");
        String appKey = edamamKey.getProperty("appKey");

        EdamamCallback myCallback = new EdamamCallback(this);
        EdamamClient myEdamamClient = new EdamamClient(appID, appKey);
        myEdamamClient.requestRecipe(Arrays.asList(new String[]{"chicken", "pasta"}), myCallback);
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
