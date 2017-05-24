package com.example.willi.e_slang;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import java.util.ArrayList;


public class AddWord extends AppCompatActivity {
    DbManager dbm;

    EditText inputWordName;
    EditText inputShortDef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();
    }

    //go to Main screen
    public void back(View v) {
        Intent myIntent = new Intent(AddWord.this, MainActivity.class);
        AddWord.this.startActivity(myIntent);
    }

    public void applyText (View v) {

        inputWordName =(EditText) findViewById(R.id.wordName);
        inputShortDef =(EditText) findViewById(R.id.shortDef);

        String word = inputWordName.getText().toString();
        String shortDef = inputShortDef.getText().toString();

        Log.d(AddWord.class.getName(), word);
        Log.d(AddWord.class.getName(), shortDef);

        ArrayList<String> test = new ArrayList<String>();
        test.add(shortDef);
        dbm.insert(word, test, test, test, test, test, "Korea", test);

        Log.d(AddWord.class.getName(), "IT WORK " + dbm.getAllWords("Korea").get(0));

    }
}