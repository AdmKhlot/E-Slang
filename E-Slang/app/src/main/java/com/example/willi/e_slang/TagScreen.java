package com.example.willi.e_slang;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by willi on 01/06/2017.
 */

public class TagScreen extends AppCompatActivity {
    DbManager dbm;

    String flag;
    String tag;
    String id;
    String imageFlag;

    Context ctx;
    TextView name_of_country;

    int flagIdInt;

    LinearLayout displayTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_screen);

        ctx = this;
        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();
    }

    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        tag = intent.getStringExtra("tag");
        //id = intent.getStringExtra("id");

        imageFlag = setFirstCharToLower(flag);
        ImageView img = (ImageView) findViewById(R.id.country_image);
        flagIdInt = getResources().getIdentifier(imageFlag, "drawable", getPackageName());
        img.setImageResource(flagIdInt);

        name_of_country = (TextView) findViewById(R.id.name_of_country);
        name_of_country.setText(flag);

        getSupportActionBar().setTitle("#" + tag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayTags = (LinearLayout) findViewById(R.id.ScrollLayout);

        ArrayList<String> tagList = new ArrayList<>();
        Cursor cursor = dbm.getWordsFromTag(flag, tag);
        if (cursor.moveToFirst()){
            do{
                if(cursor.getString(8).contains(tag)) {
                    tagList.add(cursor.getString(1));
                }
            }while(cursor.moveToNext());
        }
        displayTags.removeAllViews();
        cursor.close();

        for (int i=0; i<tagList.size();i++) {
            TextView htext =new TextView(ctx);
            htext.setText(tagList.get(i));
            //htext.setMaxLines(Integer.parseInt(tmp2.get(i).first));
            htext.setTextSize(18);

            htext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView b = (TextView)v;
                    goToWordScreen(b.getText().toString(), Integer.toString(b.getMaxLines()));
                }
            });
            displayTags.addView(htext);
        }
    }

    private void goToWordScreen (String word, String id) {
        Intent myIntent = new Intent(this, WordScreen.class);

        myIntent.putExtra("word", word); // string you want to pass, variable to receive
        //myIntent.putExtra("id", "null"); // string you want to pass, variable to receive
        myIntent.putExtra("flag", flag); // string you want to pass, variable to receive

        Bundle bundle = new Bundle();
        bundle.putString("word", word);
        //.putString("id", "null");
        bundle.putString("flag", flag);

        WordScreenTab1 send3 = new WordScreenTab1();
        send3.setArguments(bundle);

        startActivity(myIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public String setFirstCharToLower (String str) {
        str = str.substring(0,1).toLowerCase() + str.substring(1).toLowerCase();
        return str;
    }


}
