package com.example.willi.e_slang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class WordScreen extends AppCompatActivity {
    DbManager dbm;
    String word;
    String id;
    String flag;
    String wordNameString;
    String shortDefString;
    String longDefString;
    String characteristicString;
    Cursor c;
    TextView display_word;
    TextView shortDefinition;
    TextView longDefinition;
    TextView characteristic;

    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_screen);

    }

    protected void onStart() {
        super.onStart();


        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        word = intent.getStringExtra("word");
        id = intent.getStringExtra("id");


        Log.d(Dictionary.class.getName(), "word selected = " + word);

        display_word =(TextView) findViewById(R.id.word_name);
        shortDefinition = (TextView) findViewById(R.id.short_def);
        longDefinition = (TextView) findViewById(R.id.long_def);
        characteristic = (TextView) findViewById(R.id.charac);


        c = dbm.fetch(10); // word ID number -> how to get from dictionary screen?

        while (i < 10){
            switch (i) {
                case 0:
                    wordNameString = c.getString(0); // word ID number (int?)
                    display_word.setText(wordNameString);
                    break;
                case 1:
                    shortDefString = elaborateDesc(c.getString(i));
                    shortDefinition.setText(shortDefString);
                    break;
                case 2:
                    longDefString = elaborateDesc(c.getString(i);
                    longDefinition.setText(longDefString);
                    break;
                case 3:
                    characteristicString = elaborateDesc(c.getString(i);
                    characteristic.setText(characteristicString);
            }
            i++;
        }

        dbm.close();
    }


    //go back to dictionary
    public void wordScreen(View v) {
        Intent myIntent = new Intent(this, Dictionary.class);
        myIntent.putExtra("1", flag); // TODO
        startActivity(myIntent);
    }

    public void clickEditsWord(View v) {
        Intent myIntent = new Intent(this, AddWord.class);
        myIntent.putExtra("type", "edit"); // string you want to pass, variable to receive
        myIntent.putExtra("word", word); // string you want to pass, variable to receive
        myIntent.putExtra("id", id); // string you want to pass, variable to receive
        myIntent.putExtra("1", flag); // string you want to pass, variable to receive
        startActivity(myIntent);
    }
}
