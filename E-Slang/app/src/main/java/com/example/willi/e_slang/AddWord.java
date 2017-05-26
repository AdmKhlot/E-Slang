package com.example.willi.e_slang;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class AddWord extends AppCompatActivity {
    DbManager dbm;

    Button addEditButton;
    TextView headerText;
    EditText inputWord;
    EditText inputShortTerm;
    EditText inputLongTerm;
    EditText inputCharacter;
    EditText inputExample;
    EditText inputAudioUrl;
    EditText inputVideoUrl;
    EditText inputCountry;
    EditText inputTag;

    ArrayList<String> shortTerm = new ArrayList<String>(); // creates string
    ArrayList<String> longTerm = new ArrayList<String>();
    ArrayList<String> character = new ArrayList<String>();
    ArrayList<String> example = new ArrayList<String>();
    ArrayList<String> audioUrl = new ArrayList<String>();
    ArrayList<String> videoUrl = new ArrayList<String>();
    ArrayList<String> tag = new ArrayList<String>();
    boolean type;
    String flag;
    String word;
    String id;
    String word2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();
    }


    protected void onStart() {
        super.onStart();

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();

        inputWord =(EditText) findViewById(R.id.wordName);
        inputShortTerm =(EditText) findViewById(R.id.shortTerm);
        inputLongTerm =(EditText) findViewById(R.id.longTerm);
        inputCharacter =(EditText) findViewById(R.id.character);
        inputExample =(EditText) findViewById(R.id.example);
        inputAudioUrl =(EditText) findViewById(R.id.audioUrl);
        inputVideoUrl =(EditText) findViewById(R.id.videoUrl);
        inputCountry =(EditText) findViewById(R.id.country);
        inputTag =(EditText) findViewById(R.id.tag);

        addEditButton = (Button) findViewById(R.id.addWordButton);
        headerText = (TextView) findViewById(R.id.header);

        Intent intent = getIntent();
        word2 = intent.getStringExtra("word");
        word = intent.getStringExtra("type");
        id = intent.getStringExtra("id");
        flag = intent.getStringExtra("1"); //retrieves 1 = Korea
        if (word.equals("add"))
            type = true; // ADD WORD
        else
            type = false; //  EDIT WORD

        if (!type) {
            Cursor cursor = dbm.getOneWord(flag, id, word2); // TODO GET INDEX
            if (cursor.moveToFirst()){
                do{
                    inputWord.setText(cursor.getString(cursor.getColumnIndex("word")));
                    inputShortTerm.setText(cursor.getString(cursor.getColumnIndex("short")));
                    inputLongTerm.setText(cursor.getString(cursor.getColumnIndex("long")));
                    inputCharacter.setText(cursor.getString(cursor.getColumnIndex("characteristic")));
                    inputExample.setText(cursor.getString(cursor.getColumnIndex("example")));
                    inputAudioUrl.setText(cursor.getString(cursor.getColumnIndex("audio_url")));
                    inputVideoUrl.setText(cursor.getString(cursor.getColumnIndex("video_url")));
                    inputCountry.setText(cursor.getString(cursor.getColumnIndex("country")));
                    inputTag.setText(cursor.getString(cursor.getColumnIndex("tag")));
                }while(cursor.moveToNext()); //TODO ADD OVERRIDE METHODS TO DB
            }
            cursor.close();
            addEditButton.setText("Edit Word");
            headerText.setText("Edit Word");


        }

    }

    //go to Main screen
    public void back(View v) {
        if (type) {
            Intent myIntent = new Intent(AddWord.this, MainActivity.class);
            AddWord.this.startActivity(myIntent);
        }
        else {
            Intent myIntent = new Intent(AddWord.this, Dictionary.class);
            myIntent.putExtra("1", flag); // string you want to pass, variable to receive
            AddWord.this.startActivity(myIntent);
        }
    }

    public void applyText (View v) {

        String word = inputWord.getText().toString();
        shortTerm.add(inputShortTerm.getText().toString()); //adds inputshortterm to string
        longTerm.add(inputLongTerm.getText().toString());
        example.add(inputExample.getText().toString());
        character.add(inputCharacter.getText().toString());
        audioUrl.add(inputAudioUrl.getText().toString());
        videoUrl.add(inputVideoUrl.getText().toString());
        String country = inputCountry.getText().toString();
        tag.add(inputTag.getText().toString());

        if (type)
            dbm.insert(word, shortTerm, longTerm, character, example, audioUrl, videoUrl, country, tag); // TODO
        else
            Log.d(AddWord.class.getName(), "EDIT ACTIVATED");

        Log.d(AddWord.class.getName(), "word = " + word);
        Log.d(AddWord.class.getName(), "short def: " + Arrays.toString(shortTerm.toArray()));
        Log.d(AddWord.class.getName(), "long def: " + Arrays.toString(longTerm.toArray()));
        Log.d(AddWord.class.getName(), "ex: " + Arrays.toString(example.toArray()));
        Log.d(AddWord.class.getName(), "char " + Arrays.toString(character.toArray()));
        Log.d(AddWord.class.getName(), "video url: " + Arrays.toString(videoUrl.toArray()));
        Log.d(AddWord.class.getName(), "audio url: " + Arrays.toString(audioUrl.toArray()));
        Log.d(AddWord.class.getName(), "country = " + country);
        Log.d(AddWord.class.getName(), "tag: " + Arrays.toString(tag.toArray()));

    }
}