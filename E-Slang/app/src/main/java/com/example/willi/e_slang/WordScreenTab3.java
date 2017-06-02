package com.example.willi.e_slang;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by willi on 31/05/2017.
 */

public class WordScreenTab3 extends Fragment {
    DbManager dbm;
    String word;
    String id;
    String flag;
    String videoUrlString;
    Cursor cursor;

    TextView videoUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_screen_tab_3, container, false);

        dbm = DbManager.getInstance();
        dbm.mCtx = getContext();
        dbm.open();

        videoUrl = (TextView) view.findViewById(R.id.video_url);

        Bundle b = getActivity().getIntent().getExtras();
        flag = b.getString("flag");
        //id = b.getString("id");
        word = b.getString("word");

        cursor = dbm.getOneWord(flag, word);
        if (cursor.moveToFirst()) {
            do {
                videoUrlString = DbManager.elaborateDesc(cursor.getString(cursor.getColumnIndex("video_url")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (TextUtils.isEmpty(videoUrlString))
            videoUrl.setText("No videos found!");
        else
            videoUrl.setText(videoUrlString);

        return view;
    }
}
