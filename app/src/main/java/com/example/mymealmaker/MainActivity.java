package com.example.mymealmaker;

import android.content.Intent;
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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    public static final String INGREDIENTS = "com.example.mymealmaker.INGREDIENTS";

    public static String awsID = "";
    public static String awsSecret = "";

    public static String edamamID = "";
    public static String edamamSecret = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Meal Maker");
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
        awsID = awsKey.getProperty("keyID");
        awsSecret = awsKey.getProperty("secret");

        // get the Edamam key and secret
        edamamID = edamamKey.getProperty("appID");
        edamamSecret = edamamKey.getProperty("appKey");
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

    public void doSearchRecipes(View view) {
        Intent intent = new Intent(this, RecipeListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INGREDIENTS, (Serializable) Arrays.asList(new String[] {"chicken", "pasta"}));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
