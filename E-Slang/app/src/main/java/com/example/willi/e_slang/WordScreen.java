package com.example.willi.e_slang;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class WordScreen extends AppCompatActivity {

    private ViewPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    DbManager dbm;
    String word;
    String id;
    String flag;
    String imageFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_screen);

        mSectionsPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        word = intent.getStringExtra("word");
        //id = intent.getStringExtra("id");
        imageFlag = setFirstCharToLower(flag);

        getSupportActionBar().setTitle(word);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();
    }

    private void setupViewPager (ViewPager viewPager) {
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new WordScreenTab1(), "Basic Info");
        adapter.addFragment(new WordScreenTab2(), "More Info & Video");
        viewPager.setAdapter(adapter);
    }

    public void goToEditWord(View v) {
        Intent myIntent = new Intent(this, EditWord.class);
        myIntent.putExtra("type", "edit"); // string you want to pass, variable to receive
        myIntent.putExtra("word", word); // string you want to pass, variable to receive
        //myIntent.putExtra("id", id); // string you want to pass, variable to receive
        myIntent.putExtra("1", flag); // string you want to pass, variable to receive
        startActivity(myIntent);
    }

    public void goToVideoScreen (View v) {
        Intent myIntent = new Intent(this, VideoScreen.class);
        myIntent.putExtra("word", word); // string you want to pass, variable to receive
        myIntent.putExtra("flag", flag); // string you want to pass, variable to receive
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