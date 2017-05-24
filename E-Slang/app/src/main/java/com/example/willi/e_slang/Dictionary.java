package com.example.willi.e_slang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary extends AppCompatActivity {
    DbManager dbm;
    String flag;
    EditText searchText;
    TextView countryName;
    ScrollView mScrollView;
    List<String> tmp = null;
    private Handler mHandler = new Handler();
    Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        ctx = this;
        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();

        Intent intent = getIntent();
        flag = intent.getStringExtra("1"); //retrieves flag selected by user

        Log.d(Dictionary.class.getName(), flag);

        countryName =(TextView) findViewById(R.id.countryName);
        countryName.setText(flag);

        mScrollView =(ScrollView) findViewById(R.id.scroll1);

        searchText =(EditText) findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() { //reiteratively searches database everytime new character is entered
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                tmp = null;
                ArrayList<String> test = dbm.getAllWords(flag);
               tmp = new ArrayList<String>();
                for (int i=0; i<test.size();i++){
                    if(test.get(i).contains(searchText.getText())) {
                        Log.d(Dictionary.class.getName(), test.get(i));
                        tmp.add(test.get(i));
                    }
                }
                LinearLayout AAAA = new LinearLayout(ctx);
                for (int i=0; i<tmp.size();i++) {
                    //TextView tv1 = new TextView(ctx);
                    //tv1.setText(tmp.get(i));
                    //AAAA.addView(tv1);
                    TextView htext =new TextView(ctx);
                    htext.setText("Test");


                    htext.setId(i);
                    htext.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    AAAA.addView(htext);

                    //setContentView(R.layout.activity_dictionary);
                }
                mScrollView.addView(AAAA);
                //setContentView(R.layout.activity_dictionary);

            }
        });
    }

    //go to Main screen
    public void back(View v) {
        Intent myIntent = new Intent(Dictionary.this, MainActivity.class);
        myIntent.putExtra("key", 1); //Optional parameters
        Dictionary.this.startActivity(myIntent);
    }

    //ScrollView mScrollView =(ScrollView) findViewById(R.id.scroll1);



}
