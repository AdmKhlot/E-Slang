package com.example.willi.e_slang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    DbManager dbm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();
    }
    // go to Dictionary screen
    public void clickDictionaryKorea(View v) {
        Intent myIntent = new Intent(this, Dictionary.class);
        myIntent.putExtra("1", "Korea"); // string you want to pass, variable to receive
        startActivity(myIntent);
    }

    public void clickDictionaryEngland(View v) {
        Intent myIntent = new Intent(this, Dictionary.class);
        myIntent.putExtra("1", "England"); // string you want to pass, variable to receive
        startActivity(myIntent);
    }

    public void clickDictionaryFrance(View v) {
        Intent myIntent = new Intent(this, Dictionary.class);
        myIntent.putExtra("1", "France"); // string you want to pass, variable to receive
        startActivity(myIntent);
    }

    // go to AddWord screen
    public void clickAddWord(View v) {
        Intent myIntent = new Intent(this, AddWord.class);
        myIntent.putExtra("type", "add"); // string you want to pass, variable to receive
        startActivity(myIntent);
    }

}

