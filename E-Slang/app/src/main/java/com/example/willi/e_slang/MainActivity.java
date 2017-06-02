package com.example.willi.e_slang;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    DbManager dbm;
    String countryName;
    int countryCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();
    }
    // go to Dictionary screen
    public void goToDictionaryKorea(View v) {
        countryCode = 1;
        countryName = getCountry(countryCode);
        goToDictionary(countryName);
    }

    public void goToDictionaryEngland(View v) {
        countryCode = 2;
        countryName = getCountry(countryCode);
        goToDictionary(countryName);
    }

    public void goToDictionaryFrance(View v) {
        countryCode = 3;
        countryName = getCountry(countryCode);
        goToDictionary(countryName);
    }

    private void goToDictionary (String country) {
        Intent myIntent = new Intent(this, Dictionary.class);
        myIntent.putExtra("1", country);

        Bundle bundle2 = new Bundle();
        bundle2.putString("1", country);

        DictionaryTab1 send2 = new DictionaryTab1();
        send2.setArguments(bundle2);

        startActivity(myIntent);

    }

    private String getCountry (int countryCode) {
        switch(countryCode) {
            case 1:
                return countryName = "Korea";
            case 2:
                return countryName = "England";
            case 3:
                return countryName = "France";
            default:
                return "Error";
        }
    }

    // go to AddWord screen
    public void goToAddWord(View v) {
        Intent myIntent = new Intent(this, AddWord.class);
        myIntent.putExtra("type", "add"); // string you want to pass, variable to receive
        myIntent.putExtra("1", "a");
        startActivity(myIntent);
    }
}

