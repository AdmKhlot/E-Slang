package com.example.willi.e_slang;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by willi on 31/05/2017.
 */

public class WordScreenTab1 extends Fragment  {

    DbManager dbm;
    String word;
    String id;
    String flag;
    Cursor cursor;

    String charctString;
    String tagsString;

    TextView shortDefTV;
    TextView charctTV;
    TextView countryTV;
    TextView tagsTV;

    ImageView img;
    String flagId;
    int flagIdInt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_screen_tab_1, container, false);

        dbm = DbManager.getInstance();
        dbm.mCtx = getContext();
        dbm.open();

        shortDefTV = (TextView) view.findViewById(R.id.short_def);
        charctTV = (TextView) view.findViewById(R.id.characteristics);
        countryTV = (TextView) view.findViewById(R.id.Country);
        tagsTV = (TextView) view.findViewById(R.id.tag);

        Bundle b = getActivity().getIntent().getExtras();
        flag = b.getString("flag");
        //id = b.getString("id");
        word = b.getString("word");

        flagId = setFirstCharToLower(flag);
        img = (ImageView) view.findViewById(R.id.country_image);
        flagIdInt = getResources().getIdentifier(flagId, "drawable", getActivity().getPackageName());
        img.setImageResource(flagIdInt);

        cursor = dbm.getOneWord(flag, word);
        if (cursor.moveToFirst()){
            do{
                shortDefTV.setText(DbManager.elaborateDesc(cursor.getString(cursor.getColumnIndex("short"))));
                charctString = DbManager.elaborateDesc(cursor.getString(cursor.getColumnIndex("characteristic")));
                countryTV.setText(cursor.getString(cursor.getColumnIndex("country")));
                tagsString = DbManager.getTags(cursor.getString(cursor.getColumnIndex("tag")));
            }while(cursor.moveToNext());
        }

        if (TextUtils.isEmpty(charctString))
            charctTV.setText("No data found!");
        else
            charctTV.setText(charctString);

        if (TextUtils.isEmpty(tagsString))
            tagsTV.setText("No data found!");
        else
            tagsTV.setText(tagsString);

        cursor.close();
        return view;
    }

    public String setFirstCharToLower (String str) {
        str = str.substring(0,1).toLowerCase() + str.substring(1).toLowerCase();
        return str;
    }
}
