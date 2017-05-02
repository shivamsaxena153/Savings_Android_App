package com.example.shivam.newsavings;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shivam on 01-05-2017.
 */

public class SavingDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SavingsDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm a";
    public static final String SAVING_TABLE_NAME = "savings";
    public static final String SAVING_COLUMN_ID = "_id";
    public static final String SAVING_COLUMN_DATE = "eventtime";
    public static final String SAVING_COLUMN_AMOUNT = "amount";
    public static final String SAVING_COLUMN_DBTCR = "dbtcr";


    public SavingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+SAVING_TABLE_NAME+"("+
                SAVING_COLUMN_ID+" INTEGER PRIMARY KEY, "+
                SAVING_COLUMN_DATE+" TEXT, "+
                SAVING_COLUMN_AMOUNT+" INTEGER, "+
                SAVING_COLUMN_DBTCR+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+SAVING_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertEntry(int amount, String dbtcr){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SAVING_COLUMN_DATE, getDate());
        cv.put(SAVING_COLUMN_AMOUNT, amount);
        cv.put(SAVING_COLUMN_DBTCR, dbtcr);
        db.insert(SAVING_TABLE_NAME, null,cv);
        return true;
    }

    public boolean updateEntry(Integer id, int amount, String dbtcr){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SAVING_COLUMN_DATE, getDate());
        cv.put(SAVING_COLUMN_AMOUNT, amount);
        cv.put(SAVING_COLUMN_DBTCR, dbtcr);
        db.update(SAVING_TABLE_NAME,cv,SAVING_COLUMN_ID+" = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteEntry(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SAVING_TABLE_NAME,SAVING_COLUMN_ID+" = ? ", new String[]{Integer.toString(id)});
    }

    public int deleteAllEntry(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SAVING_TABLE_NAME,"1",null);
    }

    public Cursor getEntry(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM "+SAVING_TABLE_NAME+" WHERE "+SAVING_COLUMN_ID+" = ? ", new String[]{Integer.toString(id)});
        return cur;
    }

    public Cursor getAllEntry(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM "+SAVING_TABLE_NAME, null);
        return cur;
    }

    public int getTotalBalance(){
        int a,b;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM("+SAVING_COLUMN_AMOUNT+") FROM "+SAVING_TABLE_NAME+" WHERE "+SAVING_COLUMN_DBTCR+" = ? ", new String[]{"cr"});
        if(cur.moveToFirst()){
            a = cur.getInt(0);
        }
        else {
            a = 0;
        }
        cur.close();
        cur = db.rawQuery("SELECT SUM("+SAVING_COLUMN_AMOUNT+") FROM "+SAVING_TABLE_NAME+" WHERE "+SAVING_COLUMN_DBTCR+" = ? ", new String[]{"dbt"});
        if(cur.moveToFirst()){
            b = cur.getInt(0);
        }
        else {
            b = 0;
        }
        cur.close();
        return a - b;
    }
    public static String getDate(){
        Date d= new Date();
        SimpleDateFormat sdf =new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return sdf.format(d);
    }
}
