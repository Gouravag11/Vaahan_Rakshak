package com.rakshak.vaahan.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "user.db";
    private static final String CarList = "carList";   // Table Name
    private static final String car_list = "CREATE TABLE carList(Brand VARCHAR(255),Model VARCHAR(255) ,RegNo VARCHAR(255) PRIMARY KEY);";
    private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+CarList;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(car_list);db.execSQL(car_list);
        } catch (Exception e) {
            Log.d("Error",e.toString());
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }catch (Exception e) {
            Log.d("Error",e.toString());
        }
    }

    public void insertCarList(List<String> carData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Brand", carData.get(0));
        contentValues.put("Model", carData.get(1));
        contentValues.put("RegNo", carData.get(2));
        long result = db.insert(CarList, null, contentValues);
        if (result == -1) {
            Log.d("Error","Error");
        }
        db.close();
    }

    @SuppressLint({"Range","Recycle"})
    public List<List<String>> getCarList()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"Brand","Model","RegNo"};
        Cursor cursor =db.query(CarList,columns,null,null,null,null,null);
        List carList = new ArrayList();
//        StringBuilder buffer= new StringBuilder();
        while (cursor.moveToNext())
        {
            String Brand  =cursor.getString(cursor.getColumnIndex("Brand"));
            String Model =cursor.getString(cursor.getColumnIndex("Model"));
            String RegNo =cursor.getString(cursor.getColumnIndex("RegNo"));
            List<String> raw_1 = new ArrayList<>();
            raw_1.add(Brand);raw_1.add(Model);raw_1.add(RegNo);
            carList.add(raw_1);
        }
        db.close();
        return carList;
    }

}