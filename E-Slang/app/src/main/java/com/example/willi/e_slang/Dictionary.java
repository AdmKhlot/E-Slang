package com.example.willi.e_slang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Dictionary extends AppCompatActivity  {
    DbManager dbm;
    String flag;
    EditText searchText;
    TextView name_of_country;
    TextView search_result;
    TextView print;
    TextView scrollText;
    List<String> tmp = null;
    ScrollView mScrollView;
    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        ctx = this;
        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.d(Dictionary.class.getName(), "HEYYYYYYYYYYYY");


        ctx = this;
        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();

        Intent intent = getIntent();
        flag = intent.getStringExtra("1"); //retrieves 1 = Korea

        Log.d(Dictionary.class.getName(), "country selected = " + flag);
        name_of_country =(TextView) findViewById(R.id.name_of_country);
        name_of_country.setText(flag);

        searchText =(EditText) findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() { //reiteratively searches database everytime new character is entered
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                if (flag == null)
                    return;
                tmp = null;

                ArrayList<Pair<String, String> > tmp2 = new ArrayList<Pair<String, String> >();
                Cursor cursor = dbm.getAllWords2(flag);
                if (cursor.moveToFirst()){
                    do{
                        //Log.d(Dictionary.class.getName(), "OMGGGGGGG " + cursor.getString(1));
                        if(cursor.getString(1).contains(searchText.getText())) {
                            //Log.d(Dictionary.class.getName(), test.get(i));
                            tmp2.add(new Pair(cursor.getString(0), cursor.getString(1)));
                        }
                        // do what ever you want here
                    }while(cursor.moveToNext());
                }
                cursor.close();
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ScrollLayout);
                linearLayout.removeAllViews();
                for (int i=0; i<tmp2.size();i++) {

                    TextView htext =new TextView(ctx);
                    htext.setText(tmp2.get(i).second);
                    htext.setMaxLines(Integer.parseInt(tmp2.get(i).first));
                    htext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView b = (TextView)v;
                            addWord(b.getText().toString(), Integer.toString(b.getMaxLines()));
                        }
                    });
                    linearLayout.addView(htext);
                }
                /**
                ArrayList<String> test = dbm.getAllWords(flag);
                tmp = new ArrayList<String>();
                for (int i=0; i<test.size();i++){
                    if(test.get(i).contains(searchText.getText())) {
                        Log.d(Dictionary.class.getName(), test.get(i));
                        tmp.add(test.get(i));
                    }
                }
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ScrollLayout);
                linearLayout.removeAllViews();
                for (int i=0; i<tmp.size();i++) {

                    TextView htext =new TextView(ctx);
                    htext.setText(tmp.get(i));
                    htext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView b = (TextView)v;
                            wordScreen(b.getText().toString(), "1");
                        }
                    });
                    linearLayout.addView(htext);
                } **/
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //do something
    }
        //go to Main screen
    public void back(View v) {
        Intent myIntent = new Intent(this, MainActivity.class);
        //myIntent.putExtra("key", 1); // string you want to pass, variable to receive
        startActivity(myIntent);
    }

    //go to word screen
    public void addWord(String word, String id) {
        Intent myIntent = new Intent(this, WordScreen.class);
        myIntent.putExtra("word", word); // string you want to pass, variable to receive
        myIntent.putExtra("id", id); // string you want to pass, variable to receive
        myIntent.putExtra("flag", flag); // string you want to pass, variable to receive

        startActivity(myIntent);
    }

    //ScrollView mScrollView =(ScrollView) findViewById(R.id.scroll1);

}