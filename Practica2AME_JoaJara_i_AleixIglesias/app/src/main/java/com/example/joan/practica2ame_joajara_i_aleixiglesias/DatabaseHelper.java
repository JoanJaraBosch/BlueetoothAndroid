package com.example.joan.practica2ame_joajara_i_aleixiglesias;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pedometer.db";
    public static final String TABLE_NAME = "pedometer_table";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String DAYS = "days";
    public static final String FOOT_SIZE = "foot";
    public static final String STEPS_DONE = "steps";
    public static final String METERS = "meters";
    public static final String DATA = "data";
    public static final String OBJECTIVE = "goal";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" TEXT, "+USER+" TEXT, "+PASSWORD+" TEXT, "+DAYS+" INTEGER, "+FOOT_SIZE+" INTEGER, "+STEPS_DONE+" INTGER, "+METERS+" INTEGER, "+DATA+" TEXT, "+OBJECTIVE+" INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String user, String password, String day, String fSize, String sDone, String meter, String data, String goal){

        boolean var= true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(NAME,name);
        content.put(USER,user);
        content.put(PASSWORD,password);
        content.put(DAYS,day);
        content.put(FOOT_SIZE,fSize);
        content.put(STEPS_DONE,sDone);
        content.put(METERS,meter);
        content.put(DATA,data);
        content.put(OBJECTIVE,goal);
        long res = db.insert(TABLE_NAME, null, content);
        if (res==-1) var = false;

        return var;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, String name, String user, String password, String day, String foot, String steps, String meter, String data, String goal){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(ID,id);
        content.put(NAME,name);
        content.put(USER,user);
        content.put(PASSWORD,password);
        content.put(DAYS,day);
        content.put(FOOT_SIZE,foot);
        content.put(STEPS_DONE,steps);
        content.put(METERS,meter);
        content.put(DATA,data);
        content.put(OBJECTIVE,goal);
        db.update(TABLE_NAME, content, "ID = ?", new String[] {id});

        return true;
    }
}
