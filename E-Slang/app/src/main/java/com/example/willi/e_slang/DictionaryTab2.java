package com.example.willi.e_slang;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by willi on 01/06/2017.
 */

public class DictionaryTab2 extends Fragment {

    DbManager dbm;
    String flag;

    TextView name_of_country;
    EditText searchText;

    List<String> tmp = null;
    int flagIdInt;
    Context ctx;
    String imageFlag;

    Cursor cursor;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dictionary_tab_2, container, false);

        ctx = getContext();
        dbm = DbManager.getInstance();
        dbm.mCtx = getContext();
        dbm.open();

        Bundle b = getActivity().getIntent().getExtras();
        flag = b.getString("1");

        imageFlag = setFirstCharToLower(flag);
        ImageView img = (ImageView) view.findViewById(R.id.country_image);
        flagIdInt = getResources().getIdentifier(imageFlag, "drawable", getActivity().getPackageName());
        img.setImageResource(flagIdInt);

        name_of_country =(TextView) view.findViewById(R.id.name_of_country);
        name_of_country.setText(flag);

        searchText =(EditText) view.findViewById(R.id.searchText);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ScrollLayout);

        cursor = dbm.getAllWords2(flag);

        if (searchText.getText().toString().isEmpty()) // edit text is empty
        {
            ArrayList<String> tmp1 = new ArrayList<String>();
            if (cursor.moveToFirst()) {
                do {
                    tmp1.add(cursor.getString(8));
                } while (cursor.moveToNext());
            }
            tmp1 = deleteDuplicates(tmp1);
            displayTags(tmp1, linearLayout);
            cursor.close();
        }

        searchText.addTextChangedListener(new TextWatcher() { //reiteratively searches database everytime new character is entered
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                ArrayList<String> tmp2 = new ArrayList<String>();
                cursor = dbm.getAllWords2(flag);
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(9).contains(searchText.getText()))
                            tmp2.add(cursor.getString(9));
                        } while (cursor.moveToNext());
                    }
                cursor.close();
            }
        });
        return view;
    }

    private ArrayList<String> deleteDuplicates (ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            for (int j = 1; j < array.size(); j++) {
                if (array.get(i).toString().contentEquals(array.get(j).toString()))
                    array.remove(j);
            }
        }
        return array;
    }

    private void displayTags(ArrayList<String> tmp2, LinearLayout linearLayout) {

        linearLayout.removeAllViews();

        for (int i=0; i<tmp2.size();i++) {
            TextView htext =new TextView(ctx);

            htext.setText(tmp2.get(i));
            //htext.setMaxLines(Integer.parseInt(tmp2.get(i).first));
            htext.setTextSize(18);

            htext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView b = (TextView)v;
                    //goToWordScreen(b.getText().toString(), Integer.toString(b.getMaxLines()));
                    goToTagScreen(b.getText().toString());
                }
            });
            linearLayout.addView(htext);

        }
    }

    public String setFirstCharToLower (String str) {
        str = str.substring(0,1).toLowerCase() + str.substring(1).toLowerCase();
        return str;
    }

    public void goToTagScreen (String tag) {
        Intent myIntent = new Intent(getActivity(), TagScreen.class);

        myIntent.putExtra("tag", tag); // string you want to pass, variable to receive
        //myIntent.putExtra("id", id); // string you want to pass, variable to receive
        myIntent.putExtra("flag", flag); // string you want to pass, variable to receive

        /*Bundle bundle = new Bundle();
        bundle.putString("tag", tag);
        bundle.putString("id", id);
        bundle.putString("flag", flag);

        WordScreenTab1 send1 = new WordScreenTab1();
        send1.setArguments(bundle);*/

        startActivity(myIntent);
    }
}
