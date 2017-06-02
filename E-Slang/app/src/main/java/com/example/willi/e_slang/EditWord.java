package com.example.willi.e_slang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class EditWord extends AddWord {
    Context context;

    DbManager dbm;

    EditText inputWord;
    EditText inputShortDef;
    EditText inputLongDef;
    EditText inputCharacter;
    EditText inputExample;
    EditText inputVideoUrl;
    EditText inputTag;

    TextView previewShortDef;
    TextView previewLongDef;
    TextView previewCharacter;
    TextView previewExample;
    TextView previewTags;
    TextView previewVideoUrl;

    TextView tagList;

    String shortDef = new String();
    String longDef = new String();
    String character = new String();
    String example = new String();
    String videoUrl = new String();
    String tag = new String();

    String imageFlag;
    String flag;
    String word;
    String id;

    Spinner shortDefSpinner;
    Spinner longDefSpinner;
    Spinner characteristicSpinner;
    Spinner exampleSpinner;
    Spinner videoUrlSpinner;
    Spinner tagSpinner;


    public EditWord(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);


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
        getSupportActionBar().setTitle("Edit Word");

        Intent intent = getIntent();
        word = intent.getStringExtra("word");
        //id = intent.getStringExtra("id");
        flag = intent.getStringExtra("1"); //retrieves flag
        imageFlag = setFirstCharToLower(flag);

        shortDefSpinner = (Spinner) findViewById(R.id.short_def_spinner);
        longDefSpinner = (Spinner) findViewById(R.id.long_def_spinner);
        characteristicSpinner = (Spinner) findViewById(R.id.characteristic_spinner);
        exampleSpinner = (Spinner) findViewById(R.id.example_spinner);
        videoUrlSpinner = (Spinner) findViewById(R.id.video_url_spinner);

        countrySpinner();
        editShortTermSpinner(1);
        editLongTermSpinner(2);
        editCharacterSpinner(3);
        editExampleSpinner(4);
        editVideoUrlSpinner(5);
        //editTagSpinner(6);

        shortDefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getPreviewString(1, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }
        });
        longDefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getPreviewString(2, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }
        });
        characteristicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getPreviewString(3, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }
        });
        exampleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getPreviewString(4, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }
        });
        videoUrlSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getPreviewString(5, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing
            }
        });
    }

    public void editWord () {
        // error checking for the first 3 inputs
        if (checkEmptyEntry())
            emptyEntryPopUp(); // error check fails -> popup

        // pass (all inputs filled), error checking if word exists
        else {
            if (checkUrl(inputVideoUrl.getText().toString())) //check if url is valid (return true = invalid, false = valid)
                    invalidVideoUrlPopUp();
            else {
                //word = inputWord.getText().toString();
                if (checkEditWord(word)) {  //check if word is in db
                    wordInDbPopUp();
                } else {
                    //pass (url valid), add/edits word
                    flag = countrySpinner();
                    int shortDefSpinnerPosition;
                    int longDefSpinnerPosition;
                    int characterSpinnerPosition;
                    int exampleSpinnerPosition;
                    int videoUrlSpinnerPosition;
                    int tagSpinnerPosition;

                    //adds whatever is on the screen to string
                    word = inputWord.getText().toString();
                    shortDef = inputShortDef.getText().toString();
                    longDef = inputLongDef.getText().toString();
                    character = inputCharacter.getText().toString();
                    example = inputExample.getText().toString();
                    videoUrl = inputVideoUrl.getText().toString();
                    tag = tagList.getText().toString();

                    shortDefSpinnerPosition = shortDefSpinner.getSelectedItemPosition();
                    longDefSpinnerPosition = longDefSpinner.getSelectedItemPosition();
                    characterSpinnerPosition = characteristicSpinner.getSelectedItemPosition();
                    exampleSpinnerPosition = exampleSpinner.getSelectedItemPosition();
                    //videoUrlSpinnerPosition = videoUrlSpinner.getSelectedItemPosition();
                    //tagSpinnerPosition = tagSpinner.getSelectedItemPosition();

                    dbm.updateExactString(word, 1, shortDefSpinnerPosition, shortDef);
                    dbm.updateExactString(word, 2, longDefSpinnerPosition, longDef);
                    dbm.updateExactString(word, 3, characterSpinnerPosition, character);
                    dbm.updateExactString(word, 4, exampleSpinnerPosition, example);
                    //dbm.updateExactString(word, 5, videoUrlSpinnerPosition, videoUrl);
                    //dbm.updateExactString(word, 6, tagSpinnerPosition, tag);
                    editPopUp();

/*                    Log.d(AddWord.class.getName(), "EDITED word = " + word);
                    Log.d(AddWord.class.getName(), "EDITED short def: " + Arrays.toString(editShortTerm.toArray()));
                    Log.d(AddWord.class.getName(), "EDITED long def: " + Arrays.toString(editLongTerm.toArray()));
                    Log.d(AddWord.class.getName(), "EDITED char: " + Arrays.toString(editCharacter.toArray()));
                    Log.d(AddWord.class.getName(), "EDITED ex: " + Arrays.toString(editExample.toArray()));
                    Log.d(AddWord.class.getName(), "EDITED video url: " + Arrays.toString(editVideoUrl.toArray()));
                    Log.d(AddWord.class.getName(), "EDITED country = " + flag);
                    Log.d(AddWord.class.getName(), "EDITED tag: " + Arrays.toString(editTag.toArray()));*/
                }
            }
        }
    }

    private Boolean checkEditWord(String word) {
        Log.d(EditWord.class.getName(), "i am checking word");

        ArrayList<String> wordsInCountry = dbm.getAllWords(flag);
        ArrayList<String> wordCheck = new ArrayList<>();
        wordCheck.add(word);

        if (word.contentEquals(inputWord.getText().toString())) {
            Log.d(EditWord.class.getName(), "i am here0");
            return false;}

        word = inputWord.getText().toString(); // word newly typed in

        for (int i=0; i<wordsInCountry.size(); i++) {
            if (wordsInCountry.get(i).toString().contentEquals(word)) {
                Log.d(EditWord.class.getName(), "i am here1");
                return true;
            }}
        if (word.contentEquals(wordCheck.get(0))) {
            Log.d(EditWord.class.getName(), "i am here3");
            return false;
        } else
            Log.d(EditWord.class.getName(), "i am here4");
        return false; // adds word in (does not exist in db)
    }

    private void editShortTermSpinner(int typedef) { // 1 = shortdef
        ArrayList<String> numberOfShortTermSpinner;

        numberOfShortTermSpinner = numberOfSpinner(typedef);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numberOfShortTermSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shortDefSpinner.setAdapter(adapter);
    }

    private void editLongTermSpinner(int typedef) { // 2 = longdef
        ArrayList<String> numberOfLongTermSpinner;

        numberOfLongTermSpinner = numberOfSpinner(typedef);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numberOfLongTermSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        longDefSpinner.setAdapter(adapter);
    }

    private void editCharacterSpinner(int typedef) { // 3 = characteristic
        ArrayList<String> numberOfCharacteristicSpinner;

        numberOfCharacteristicSpinner = numberOfSpinner(typedef);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numberOfCharacteristicSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        characteristicSpinner.setAdapter(adapter);
    }

    private void editExampleSpinner(int typedef) { // 4 = examples
        ArrayList<String> numberOfExampleSpinner;

        numberOfExampleSpinner = numberOfSpinner(typedef);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numberOfExampleSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exampleSpinner.setAdapter(adapter);
    }

    private void editVideoUrlSpinner(int typedef) { // 5 = video url
        ArrayList<String> numberOfVideoUrlSpinner;

        numberOfVideoUrlSpinner = numberOfSpinner(typedef);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numberOfVideoUrlSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        videoUrlSpinner.setAdapter(adapter);
    }

    private void getPreviewString (int typedef, int position) {
        previewShortDef = (TextView) findViewById(R.id.preview_short_def);
        previewLongDef = (TextView) findViewById(R.id.preview_long_def);
        previewCharacter = (TextView) findViewById(R.id.preview_characteristic);
        previewExample = (TextView) findViewById(R.id.preview_example);
        previewVideoUrl = (TextView) findViewById(R.id.preview_video_url);

        switch(typedef) {
            case 1:
                inputShortDef.setText(dbm.getExactString(word, typedef, position));
                previewShortDef.setText(dbm.getExactString(word, typedef, position));
                break;
            case 2:
                inputLongDef.setText(dbm.getExactString(word, typedef, position));
                previewLongDef.setText(dbm.getExactString(word, typedef, position));
                break;
            case 3:
                inputCharacter.setText(dbm.getExactString(word, typedef, position));
                previewCharacter.setText(dbm.getExactString(word, typedef, position));
                break;
            case 4:
                inputExample.setText(dbm.getExactString(word, typedef, position));
                previewExample.setText(dbm.getExactString(word, typedef, position));
                break;
            case 5:
                inputVideoUrl.setText(dbm.getExactString(word, typedef, position));
                previewVideoUrl.setText(dbm.getExactString(word, typedef, position));
                break;
            case 6:
                previewTags.setText(dbm.getExactString(word, typedef, position));
                break;
        }
    }

    private ArrayList<String> numberOfSpinner(int typedef) {
        ArrayList<String> numberOfSpinners;
        String[] adder;

        int howManyDesc;
        adder = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        howManyDesc = dbm.getHowManyDesc(word, typedef);
        numberOfSpinners = new ArrayList<>(howManyDesc);

        for (int i=0;i<howManyDesc;i++)
            numberOfSpinners.add(i, adder[i]);
        return numberOfSpinners;
    }

    //counterSpinner - for spinner to display countries
    //checkUrl - for checking if inputted URL is valid
    //urlChecker - for checking if URL is valid (can be accessed)
    //checkWord - for checking if word exists in db
    //addTag - for adding of tags
    //invalidEntryInputPopup - popup when one of first three inputs are empty
    //editPopUp - popup when user edits or adds word
    //wordInDpPopUp - popup when word exists in db
    //inValidVideoUrl - popup when video URL is invalid

    private void editPopUp() {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);

        messageBox.setTitle("Word Edited!");
        messageBox.setMessage("You have successfully edited " + word + "!");
        messageBox.setNeutralButton("Dictionary", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(EditWord.this, Dictionary.class);
                myIntent.putExtra("1", flag);
                EditWord.this.startActivity(myIntent);
            }
        });
        messageBox.setCancelable(false);
        messageBox.show();
    }

    // for back button and edit word
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            editWord();
        }
        else
            onBackPressed();
        return true;
    }

    //creates add button in actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_word_button, menu);
        return true;
    }

    //sets first char to lowercase for displaying of flags (XML only allows lowercase)
    public String setFirstCharToLower (String str) {
        str = str.substring(0,1).toLowerCase() + str.substring(1).toLowerCase();
        return str;
    }
}
