package com.example.willi.e_slang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by USER on 2017-05-24.
 */
public final class DbManager{
    private final static DbManager ourInstance = new DbManager();
    public static DbManager getInstance() {
        return ourInstance;
    }
    public static final String INDEX="_index";
    public static final String WORD="word";
    public static final String SHORT="short";
    public static final String LONG="long";
    public static final String CHARACTERISTIC="characteristic";
    public static final String EX="example";
    public static final String AUURL="audio_url";
    public static final String VIURL="video_url";
    public static final String COUNTRY="country";
    public static final String TAG="tag";
    public static final String _TABLENAME="WORD";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table WORD (_index integer primary key autoincrement, "
            +"word text not null, short text not null, long text not null, characteristic text, example text, audio_url text, video_url text, country text not null,tag text not null);";
    //word, short term, long term, country, tag cannot be null values

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;
    public Context mCtx = null;




    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           /* Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");*/
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    private DbManager(Context ctx) {
        this.mCtx = ctx;
    }

    private DbManager() {}

    public DbManager open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    //insert new word
    public long insert(String word, ArrayList<String> shortTerm, ArrayList<String> longTerm, ArrayList<String> character, ArrayList<String> example, ArrayList<String> audioUrl,ArrayList<String> videoUrl, String country, ArrayList<String> tag) {
        ArrayList<String> wordList = new ArrayList<String>();
        wordList = getAllWords(country);

        String shortT=handleDescription(shortTerm);
        String longT=handleDescription(longTerm);
        String charact=handleDescription(character);
        String ex=handleDescription(example);
        String auUrl=handleDescription(audioUrl);
        String viUrl=handleDescription(videoUrl);
        String tags=handleDescription(tag);

        int temp = 0;
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).equals(word)) temp++;
        }
        if (temp == 0) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(WORD, word);
            initialValues.put(SHORT, shortT);
            initialValues.put(LONG, longT);
            initialValues.put(CHARACTERISTIC, charact);
            initialValues.put(EX, ex);
            initialValues.put(AUURL, auUrl);
            initialValues.put(VIURL, viUrl);
            initialValues.put(COUNTRY, country);
            initialValues.put(TAG, tags);
            return mDb.insert(_TABLENAME, null, initialValues);
        }
        else return 0;
    }

    public boolean delete(int index) {
        //Log.i("Delete called", "value__" + index);
        return mDb.delete(_TABLENAME, INDEX + "=" + index, null) > 0;
    }
    public void drop(){
        mDb.execSQL("delete from " + _TABLENAME);
    }

    public void deleteWord(String word){
        mDb.execSQL("delete from WORD where word="+"'" +word+"'"+";");
    }


    public Cursor fetchAll() {
        return mDb.query(_TABLENAME, new String[] { INDEX, WORD, SHORT, LONG, CHARACTERISTIC,EX,AUURL,VIURL,COUNTRY,TAG }, null, null, null, null, null);
    }

    public Cursor fetch(long index) throws SQLException {

        Cursor mCursor = mDb.query(true, _TABLENAME, new String[] { WORD, SHORT, LONG, CHARACTERISTIC,EX,AUURL,VIURL,COUNTRY,TAG}, INDEX
                + "=" + index, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public ArrayList<String> getAllCountry(){
        int temp=0;
        ArrayList<String> countryList=new ArrayList<String>();
        Cursor cursor=mDb.query(_TABLENAME, new String[] { INDEX, WORD, SHORT, LONG, CHARACTERISTIC,EX,AUURL,VIURL,COUNTRY,TAG  }, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            for(int i=0;i<countryList.size();i++){
                if(countryList.get(i).equals(cursor.getString(1))) temp++;
            }
            if(temp==0) countryList.add(cursor.getString(8));
            cursor.moveToNext();
        }
        return countryList;
    }

    public ArrayList<String> getAllWords(String country){
        if (country == null)
            return null;
        ArrayList<String> wordList=new ArrayList<String>();
        Cursor mCursor = mDb.query(_TABLENAME, new String[] { INDEX, WORD, SHORT, LONG, CHARACTERISTIC,EX,AUURL,VIURL,COUNTRY,TAG }, null, null, null, null, null);

        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {
            if(country.equals(mCursor.getString(8))){
                wordList.add(mCursor.getString(1));
            }
            mCursor.moveToNext();
        }
        mCursor.close();
        return wordList;
    }

    public Cursor getAllWords2(String country){
        if (country == null)
            return null;
        ArrayList<String> wordList=new ArrayList<String>();
        String query=new String();
        query = "SELECT * FROM " + _TABLENAME + " where country='" + country + "';";
        Cursor mCursor = mDb.rawQuery(query, null);
        //Cursor mCursor = mDb.query(_TABLENAME, new String[] { INDEX, WORD, SHORT, LONG, CHARACTERISTIC,EX,AUURL,VIURL,COUNTRY,TAG }, null, null, null, null, null);

        mCursor.moveToFirst();

        return mCursor;
    }

    public Cursor getOneWord(String country, String id, String word){
        if (country == null)
            return null;
        ArrayList<String> wordList=new ArrayList<String>();
        String query=new String();
        query = "SELECT * FROM " + _TABLENAME + " where country='" + country + "' AND word='" + word + "' AND _index='" + id + "';";
        Cursor mCursor = mDb.rawQuery(query, null);
        //Cursor mCursor = mDb.query(_TABLENAME, new String[] { INDEX, WORD, SHORT, LONG, CHARACTERISTIC,EX,AUURL,VIURL,COUNTRY,TAG }, null, null, null, null, null);

        mCursor.moveToFirst();

        return mCursor;
    }

    public void updateShortTerm(String word, ArrayList<String> shortTerm){
        String shortDesc=new String();
        shortDesc=handleDescription(shortTerm);
        String query=new String();
        query="UPDATE WORD SET short="+"'"+shortDesc+"'"+"WHERE word="+"'"+word+"'";
        mDb.execSQL(query);
    }

    public void updateLongTerm(String word, ArrayList<String> longTerm){
        String longDesc=new String();
        longDesc=handleDescription(longTerm);
        String query=new String();
        query="UPDATE WORD SET long="+"'"+longDesc+"'"+"WHERE word="+"'"+word+"'";
        mDb.execSQL(query);
    }

    //change array list to string. string separates descriptions by ';'
    private String handleDescription(ArrayList<String> description){
        String handledString=new String();

        for(int i=0;i<description.size();i++){
            handledString=handledString+description.get(i);
            handledString=handledString+";";
        }

        return handledString;
    }

}

