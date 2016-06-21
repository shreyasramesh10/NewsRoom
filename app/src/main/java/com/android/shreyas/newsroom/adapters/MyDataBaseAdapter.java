package com.android.shreyas.newsroom.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SHREYAS on 4/10/2016.
 */
public class MyDataBaseAdapter {
    MyDbHelper helper;

    public MyDataBaseAdapter(Context context){
        helper = new MyDbHelper(context);
    }

    public long insertData(String url, String title, String description, String image_url){
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDbHelper.URL,url);
        contentValues.put(MyDbHelper.TITLE, title);
        contentValues.put(MyDbHelper.DESCRIPTION, description);
        contentValues.put(MyDbHelper.IMAGEURL, image_url);
        long id = sqLiteDatabase.insert(MyDbHelper.TABLE_NAME,null,contentValues);
        return id;
    }


    public Cursor getAllDataCursor(){
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String[] projection = {MyDbHelper.UID, MyDbHelper.URL, MyDbHelper.TITLE,MyDbHelper.DESCRIPTION,MyDbHelper.IMAGEURL};
        Cursor cursor = sqLiteDatabase.query(MyDbHelper.TABLE_NAME,projection,null,null,null,null,null);
        return cursor;
    }

    public int deleteRow(String url){
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String[] whereArgs={url};
        int count = sqLiteDatabase.delete(MyDbHelper.TABLE_NAME, MyDbHelper.URL + "=?", whereArgs);
        return count;
    }

    public Cursor getInformation(SQLiteDatabase db){
        String[] projection = {MyDbHelper.UID, MyDbHelper.URL, MyDbHelper.TITLE,MyDbHelper.DESCRIPTION,MyDbHelper.IMAGEURL};
        Cursor cursor = db.query(MyDbHelper.TABLE_NAME,projection,null,null,null,null,null);
        return cursor;
    }

    static class MyDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "storednews.db";
        private static final String TABLE_NAME = "STOREDNEWSTABLE";
        private static final String UID ="_id";
        private static final String TITLE = "Tilte";
        private static final String DESCRIPTION = "Description";
        private static final String URL = "Url";
        private static final String IMAGEURL = "Image_Url";

        private static final int DATABASE_VERSION = 1;

        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +URL+" VARCHAR(255),"+TITLE+" VARCHAR(255),"+DESCRIPTION+" VARCHAR(255),"+IMAGEURL+" VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;
        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            //Toast.makeText(context,"Adapter Constructor called",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Toast.makeText(context,"On create called",Toast.LENGTH_SHORT).show();
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Toast.makeText(context,"on upgrade called",Toast.LENGTH_SHORT).show();
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }
}
