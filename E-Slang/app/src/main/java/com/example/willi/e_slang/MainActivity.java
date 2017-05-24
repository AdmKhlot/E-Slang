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
    public void clickDictionary(View v) {
        Intent myIntent = new Intent(MainActivity.this, Dictionary.class);
        myIntent.putExtra("1", "" +
                ""); //sends key 1 (korea) to next activity (dictionary)
        MainActivity.this.startActivity(myIntent);
    }

    // go to AddWord screen
    public void clickAddWord(View v) {
        Intent myIntent = new Intent(MainActivity.this, AddWord.class);
        myIntent.putExtra("1", "Korea");
        MainActivity.this.startActivity(myIntent);
    }

}

