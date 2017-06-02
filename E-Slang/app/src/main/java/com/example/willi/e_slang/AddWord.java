package com.example.willi.e_slang;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class AddWord extends AppCompatActivity {
    DbManager dbm;

    EditText inputWord;
    EditText inputShortDef;
    EditText inputLongDef;
    EditText inputCharacter;
    EditText inputExample;
    EditText inputVideoUrl;
    EditText inputTag;

    TextView tagList;

    // creates string for adding // creates string for editing
    ArrayList<String> shortTerm = new ArrayList<String>();
    ArrayList<String> editShortTerm = new ArrayList<String>();
    ArrayList<String> longTerm = new ArrayList<String>();
    ArrayList<String> editLongTerm = new ArrayList<String>();
    ArrayList<String> character = new ArrayList<String>();
    ArrayList<String> editCharacter = new ArrayList<String>();
    ArrayList<String> example = new ArrayList<String>();
    ArrayList<String> editExample = new ArrayList<String>();
    ArrayList<String> videoUrl = new ArrayList<String>();
    ArrayList<String> editVideoUrl = new ArrayList<String>();
    ArrayList<String> tag = new ArrayList<String>();
    ArrayList<String> editTag = new ArrayList<String>();

    String shortTermErrorCheck;
    String longTermErrorCheck;
    String checkVideoUrl;

    String imageFlag;
    String flag;
    String id;
    String word;

    Spinner countrySpinner;
    Spinner shortTermSpinner;
    String[] countryList;
    String[] numberOfShortTermEntries;

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

        //for url checking - makes sure it does not run on main process
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();

        inputWord = (EditText) findViewById(R.id.wordName);
        inputShortDef = (EditText) findViewById(R.id.shortTerm);
        inputLongDef = (EditText) findViewById(R.id.longTerm);
        inputCharacter = (EditText) findViewById(R.id.character);
        inputExample = (EditText) findViewById(R.id.example);
        inputVideoUrl = (EditText) findViewById(R.id.videoUrl);
        inputTag = (EditText) findViewById(R.id.tag);
        tagList = (TextView) findViewById(R.id.tagListView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        getSupportActionBar().setTitle("Add Word");


        Intent intent = getIntent();
        word = intent.getStringExtra("word");
        //id = intent.getStringExtra("id");
        flag = intent.getStringExtra("1"); //retrieves flag
        imageFlag = setFirstCharToLower(flag);

         Cursor cursor = dbm.getOneWord(flag, word);
            if (cursor.moveToFirst()) {
                do {
                    inputWord.setText(cursor.getString(cursor.getColumnIndex("word")));
                    inputShortDef.setText(cursor.getString(cursor.getColumnIndex("short")));
                    inputLongDef.setText(cursor.getString(cursor.getColumnIndex("long")));
                    inputCharacter.setText(cursor.getString(cursor.getColumnIndex("characteristic")));
                    inputExample.setText(cursor.getString(cursor.getColumnIndex("example")));
                    inputVideoUrl.setText(cursor.getString(cursor.getColumnIndex("video_url")));
                    tagList.setText(cursor.getString(cursor.getColumnIndex("tag")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        countrySpinner();
    }

    public void addWord () { // upon clicking Add Word
        // error checking for the first 3 inputs
        if (checkEmptyEntry()) {
            emptyEntryPopUp(); // error check fails -> popup
        }

        // pass (all inputs filled), error checking if word exists
        else {
            word = inputWord.getText().toString();

            Log.d(AddWord.class.getName(), "CHECKING WORD!!!");
            if (checkWord(flag, word)) { //check if word is in db
                wordInDbPopUp(); // error check fails -> popup
                Log.d(AddWord.class.getName(), "WORD EXIST IN DB!!!");
            }

            // pass (word not in db), error checking if URL exists
            else {
                Log.d(AddWord.class.getName(), "WORD NOT IN DB!!!");
                if (checkUrl(inputVideoUrl.getText().toString())) {
                    //check if url is valid (return true = invalid, false = valid)
                    invalidVideoUrlPopUp();
                }
                //pass (url valid), add/edits word
                    else{
                        //adds whatever is on the screen to string
                        shortTerm.add(inputShortDef.getText().toString());
                        longTerm.add(inputLongDef.getText().toString());
                        character.add(inputCharacter.getText().toString());
                        example.add(inputExample.getText().toString());
                        videoUrl.add(inputVideoUrl.getText().toString());
                        tag.add(tagList.getText().toString());

                        dbm.insert(word, shortTerm, longTerm, character, example, videoUrl, flag, tag); //adds word to DB
                        addPopUp();

                        Log.d(AddWord.class.getName(), "ADD word = " + word);
                        Log.d(AddWord.class.getName(), "ADD short def: " + Arrays.toString(shortTerm.toArray()));
                        Log.d(AddWord.class.getName(), "ADD long def: " + Arrays.toString(longTerm.toArray()));
                        Log.d(AddWord.class.getName(), "ADD char: " + Arrays.toString(example.toArray()));
                        Log.d(AddWord.class.getName(), "ADD ex: " + Arrays.toString(character.toArray()));
                        Log.d(AddWord.class.getName(), "ADD video url: " + Arrays.toString(videoUrl.toArray()));
                        Log.d(AddWord.class.getName(), "ADD country = " + flag);
                        Log.d(AddWord.class.getName(), "ADD tag: " + Arrays.toString(tag.toArray()));

                }
            }
        }
    }

    //for spinner to display countries
    public String countrySpinner() {
        countrySpinner = (Spinner) findViewById(R.id.country_spinner);
        countryList = new String[]{"Korea", "England", "France"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                flag = countryList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }
        });
        return flag;
    }

    //for checking if inputted URL is valid
    public boolean checkUrl(String url) {
        if (TextUtils.isEmpty(url))
            return false;

        if (!urlChecker(url)) //videoUrl fails check
            return true;
        else
            return false; // videoURL passes check
    }

    //for checking if URL is valid (can be accessed)
    public boolean urlChecker (String url) {
        HttpURLConnection connection = null;

        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            int code = connection.getResponseCode();
            Log.d(AddWord.class.getName(), "CODE = " + code);
            return true;
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(AddWord.class.getName(), "MALFORMED URL!!!");
            return false;
        }

        catch (IOException e) {
            Log.d(AddWord.class.getName(), "IO EXCEPTION!!!");
            e.printStackTrace();
            return false;
        }

        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    //for checking if word exists in db
    public boolean checkWord(String flag, String word) {
        ArrayList<String> wordsInCountry;
        wordsInCountry = dbm.getAllWords(flag);
        Log.d(AddWord.class.getName(), "i am checking word...");

        if (wordsInCountry.toString().contentEquals(word)) // word is in wordsinCountry
            return true;
        else
            return false;
    }

    //for adding of tags
    public void addTag(View v) {
        tagList = (TextView)findViewById(R.id.tagListView);

        if (inputTag.getText().toString().isEmpty()) {
            Log.d(AddWord.class.getName(), "TAG IS EMPTY!!");
            inputTag.setHint("ERROR: Empty tag!");
        }

        else {
            Log.d(AddWord.class.getName(), "TAG ADDED");
            inputTag.setHint("");
            tagList.setText(tagList.getText().toString() + "#" + inputTag.getText().toString() + " ");
            inputTag.setText("");
        }
    }

    //for checking if any input field is empty
    public boolean checkEmptyEntry() {
        Boolean emptyWord = false; // assumes all are not empty (filled)

        if (TextUtils.isEmpty(inputWord.getText().toString())) {
            inputWord.setHint("ERROR! Input required!");
            emptyWord = true;
        }

        if (TextUtils.isEmpty(inputShortDef.getText().toString())) {
            inputShortDef.setHint("ERROR! Input required!");
            emptyWord = true;
        }

        if (TextUtils.isEmpty(inputLongDef.getText().toString())) {
            inputLongDef.setHint("ERROR! Input required!");
            emptyWord = true;
        }

        if (TextUtils.isEmpty(inputCharacter.getText().toString())) {
            inputCharacter.setHint("ERROR! Input required!");
            emptyWord = true;
        }

        if (TextUtils.isEmpty(inputExample.getText().toString())) {
            inputExample.setHint("ERROR! Input required!");
            emptyWord = true;
        }

        /*if (TextUtils.isEmpty(inputVideoUrl.getText().toString())) {
            inputVideoUrl.setHint("ERROR! Input required!");
            emptyWord = true;
        }*/
        return emptyWord;
    }

    //popup when user edits or adds word
    private void addPopUp() {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);

            messageBox.setTitle("Word Added!");
            messageBox.setMessage("You have successfully added " + word + " to " + flag + "!");
            messageBox.setNeutralButton("Main Menu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent myIntent = new Intent(AddWord.this, MainActivity.class); // add word -> main activity
                    AddWord.this.startActivity(myIntent);
                }
            });
        messageBox.setCancelable(false);
        messageBox.show();
    }

    //popup when word exists in db
    public void wordInDbPopUp() {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);

        messageBox.setTitle("Word exists!");
        messageBox.setMessage("The word you entered already exists! Please enter a new word.");
        messageBox.setNeutralButton("Continue editing", null);
        messageBox.setCancelable(false);
        messageBox.show();
    }

    //popup when video URL is invalid
    public void invalidVideoUrlPopUp() {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);

        messageBox.setTitle("Invalid Entry!");
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("Continue editing", null);
        messageBox.setMessage("Your video URL is invalid!\n\nPlease ensure it starts with http:// ");
        messageBox.show();
    }

    //popup when there is an empty entry
    public void emptyEntryPopUp() {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle("Invalid Entry!");
        messageBox.setMessage("Please enter the required inputs!");
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("Continue editing", null);
        messageBox.show();
    }

    // for back button and add word
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if (id == R.id.action_save) {
                addWord();
            }
            else
                onBackPressed();
        return true;
    }

    //creates add button in actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_word_button, menu);
        return true;
    }

    //sets first char to lowercase for displaying of flags (XML only allows lowercase)
    public String setFirstCharToLower (String str) {
        str = str.substring(0,1).toLowerCase() + str.substring(1).toLowerCase();
        return str;
    }
}
