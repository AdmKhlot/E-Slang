package com.example.willi.e_slang;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;

/**
 * Created by willi on 01/06/2017.
 */

public class DictionaryTab1 extends Fragment {

    DbManager dbm;
    String flag;

    TextView name_of_country;
    EditText searchText;

    List<String> tmp = null;

    int flagIdInt;
    Context ctx;
    String imageFlag;

    LinearLayout linearLayout;  ///added

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dictionary_tab_1, container, false);

        ctx = getContext();
        dbm = DbManager.getInstance();
        dbm.mCtx = getContext();
        dbm.open();

        Bundle b = getActivity().getIntent().getExtras();
        flag = b.getString("1");
        Log.d(DictionaryTab1.class.getName(), "flag = " + flag);

        imageFlag = setFirstCharToLower(flag);
        ImageView img = (ImageView) view.findViewById(R.id.country_image);
        flagIdInt = getResources().getIdentifier(imageFlag, "drawable", getActivity().getPackageName());
        img.setImageResource(flagIdInt);

        name_of_country =(TextView) view.findViewById(R.id.name_of_country);
        name_of_country.setText(flag);

        searchText =(EditText) view.findViewById(R.id.searchText);

///////////////////////////////////////////////////////////////////////////added from here //////////////
        linearLayout = (LinearLayout) view.findViewById(R.id.ScrollLayout);
        linearLayout.removeAllViews();
       // htext=new TextView(ctx);

        ArrayList<Pair<String, String> > wordList = new ArrayList<Pair<String, String> >();
        ArrayList<String> word=new ArrayList<String>();
        ArrayList<Integer> index=new ArrayList<Integer>();
        Cursor cursor = dbm.getAllWords2(flag);
        if (cursor.moveToFirst()){
            do{
                    wordList.add(new Pair(cursor.getString(0), cursor.getString(1)));
                   // word.add(cursor.getString(0));

            }while(cursor.moveToNext());
        }
        cursor.close();

        for(int i=0;i<wordList.size();i++){
            TextView text=new TextView(ctx);
            text.setText(wordList.get(i).second);
            text.setMaxLines(Integer.parseInt(wordList.get(i).first));
            text.setTextSize(18);

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView b = (TextView)v;
                    goToWordScreen(b.getText().toString(), Integer.toString(b.getMaxLines()));
                }
            });
            linearLayout.addView(text);
      }


////////////////////////////////////////////////////////////////////////to here////////////

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
                        if(cursor.getString(1).contains(searchText.getText())) {
                            tmp2.add(new Pair(cursor.getString(0), cursor.getString(1)));
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
               // LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ScrollLayout);  ////deleted
                linearLayout.removeAllViews();
                for (int i=0; i<tmp2.size();i++) {

                    TextView htext =new TextView(ctx);
                    htext.setText(tmp2.get(i).second);
                    htext.setMaxLines(Integer.parseInt(tmp2.get(i).first));
                    htext.setTextSize(18);

                    htext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView b = (TextView)v;
                            goToWordScreen(b.getText().toString(), Integer.toString(b.getMaxLines()));
                        }
                    });
                    linearLayout.addView(htext);
                }
                cursor.close();
            }
        });
        return view;
    }

    private String setFirstCharToLower (String str) {
        str = str.substring(0,1).toLowerCase() + str.substring(1).toLowerCase();
        return str;
    }

    private void goToWordScreen (String word, String id) {
        Intent myIntent = new Intent(getActivity(), WordScreen.class);

        myIntent.putExtra("word", word); // string you want to pass, variable to receive
        myIntent.putExtra("id", id); // string you want to pass, variable to receive
        myIntent.putExtra("flag", flag); // string you want to pass, variable to receive

        Bundle bundle = new Bundle();
        bundle.putString("word", word);
        bundle.putString("id", id);
        bundle.putString("flag", flag);

        WordScreenTab1 send1 = new WordScreenTab1();
        send1.setArguments(bundle);

        startActivity(myIntent);
    }
    private void sortWords(){

    }
}
