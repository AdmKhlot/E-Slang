package com.example.willi.e_slang;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class Dictionary extends AppCompatActivity {

    private ViewPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    DbManager dbm;
    String word;
    String id;
    String flag;
    String imageFlag;
    String flag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

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
        id = intent.getStringExtra("id");

        getSupportActionBar().setTitle("Dictionary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();
    }

    //for the back button   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void setupViewPager (ViewPager viewPager) {
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new DictionaryTab1(), "Search by Word");
        adapter.addFragment(new DictionaryTab2(), "Search by Tag");
        viewPager.setAdapter(adapter);
    }

}
