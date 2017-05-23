package com.assignment.nv.novopayassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by USPL on 5/20/2017.
 */

public class MyDataBase {

   private MyHelper myhelp;
   private SQLiteDatabase db;

    private final static String COUNTRY ="country";
    private final static String CITY ="city";
    private final static String SUNRISE ="sunrise";
    private final static String SUNSET ="sunset";
    private final static String TEMPERATURE ="temp";
    private final static String HUMIDITY ="humidity";
    private final static String LATLNG ="latlng";
    private final static String DATABASE_NAME = "Weather";
    private final static String  TABLENAME = "WeatherData";
    private final static String  TAG = MyDataBase.class.getName();

      MyDataBase(Context c){
        myhelp = new MyHelper(c,DATABASE_NAME,null,1);

    }
    public MyDataBase() {

    }
      void open()
    {
        db = myhelp.getWritableDatabase();
    }
      void insertData(String country, String city,String sunrise,String sunset,String temp,String humidity,String latln){
        ContentValues cv = new ContentValues();
        cv.put(COUNTRY,country);
        cv.put(CITY,city);
        cv.put(SUNRISE,sunrise);
        cv.put(SUNSET,sunset);
        cv.put(TEMPERATURE,temp);
        cv.put(HUMIDITY,humidity);
        cv.put(LATLNG,latln);
        db.insert(TABLENAME,null,cv);
        Log.d(TAG,"Inser Succesfully");
    }


    public Cursor getData()
    {
        Cursor c = null;
        c = db.query(TABLENAME,null,null, null,null,null,null);
        return c;
    }

    public Cursor getDataById(String id){
        Cursor c = null;
        c = db.query(TABLENAME,null,"_id LIKE ?",new String[]{id},null,null,null);

        return c;
    }

    public class MyHelper extends SQLiteOpenHelper{
        public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table WeatherData (_id integer primary key, country text, city text,sunrise text,sunset text,temp text,humidity text,latlng text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
