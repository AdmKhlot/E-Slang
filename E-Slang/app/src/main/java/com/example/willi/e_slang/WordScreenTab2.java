package com.example.willi.e_slang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by willi on 31/05/2017.
 */

public class WordScreenTab2 extends Fragment {
    DbManager dbm;
    String word;
    String id;
    String flag;
    String exampleString;
    String videoUrlString;
    Cursor cursor;

    TextView longDefTv;
    TextView exampleTv;
    TextView videoTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_screen_tab_2, container, false);

        dbm = DbManager.getInstance();
        dbm.mCtx = getContext();
        dbm.open();

        longDefTv = (TextView) view.findViewById(R.id.long_def);
        exampleTv = (TextView) view.findViewById(R.id.eg);
        videoTv = (TextView) view.findViewById(R.id.video_url);

        Bundle b = getActivity().getIntent().getExtras();
        flag = b.getString("flag");
        //id = b.getString("id");
        word = b.getString("word");

        cursor = dbm.getOneWord(flag, word);
        if (cursor.moveToFirst()){
            do{
                longDefTv.setText(DbManager.elaborateDesc(cursor.getString(cursor.getColumnIndex("long"))));
                exampleString = DbManager.elaborateDesc(cursor.getString(cursor.getColumnIndex("example")));
                videoUrlString = DbManager.elaborateDesc(cursor.getString(cursor.getColumnIndex("video_url")));
            }while(cursor.moveToNext());
        }

        cursor.close();


        if (TextUtils.isEmpty(videoUrlString))
            videoTv.setText("No videos found!");
        else
            videoTv.setText(videoUrlString);

        if (TextUtils.isEmpty(exampleString))
            exampleTv.setText("No examples found!");
        else
            exampleTv.setText(exampleString);

        return view;
    }
}
