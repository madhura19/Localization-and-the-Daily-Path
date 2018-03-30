package com.example.madhura.location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.version;
import static com.example.madhura.location.DatabaseHelper.MyHelper.Address;
import static com.example.madhura.location.DatabaseHelper.MyHelper.CustomName;
import static com.example.madhura.location.DatabaseHelper.MyHelper.Latitude;
import static com.example.madhura.location.DatabaseHelper.MyHelper.Longitude;
import static com.example.madhura.location.DatabaseHelper.MyHelper.TABLE_NAME;
import static com.example.madhura.location.DatabaseHelper.MyHelper.Time;

/**
 * Created by Madhura on 16-11-2017.
 */


public class DatabaseHelper{

    MyHelper helper;

    //Constructor
    public DatabaseHelper(Context context) {
        helper = new MyHelper(context);
    }
    //method to insert data
    public long insertData(String dateTime, double lat, double lon, String  addr, String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //put the time, latitude and longitude
        values.put(Time, dateTime);
        values.put(Latitude, lat);
        values.put(Longitude, lon);
        values.put(Address, addr);
        values.put(CustomName, name);

        //inserting rows
        long id = db.insert(TABLE_NAME, null, values);
        return id;
    }

    //getting data from the database for adding markers
    public Cursor getLocData() {
        Toast.makeText(helper.context, "getData called", Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        return res;
    }

    //getting data from the database for displaying List
    public ArrayList<String> getData(){
        ArrayList<String> values = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        res.moveToFirst();;

        int icount = res.getInt(0);
        if (res != null && icount > 0){
            String query = "SELECT * FROM " + TABLE_NAME;
            res = db.rawQuery(query, null);
            res.moveToFirst();
            do{

                if (res.getDouble(res.getColumnIndex(Latitude)) != 0){
                    String dateTime = "Time: " + res.getString(res.getColumnIndex(Time));
                    String lat = "\nLatitude: " + res.getDouble(res.getColumnIndex(Latitude));
                    String lon = "\nLongitude: " + res.getDouble(res.getColumnIndex(Longitude));
                    String addr = "\nAddress: " + res.getString(res.getColumnIndex(Address));
                    String cname = "\nName: " + res.getString(res.getColumnIndex(CustomName));
                    values.add(0, dateTime + lat + lon + addr + cname);
                }
            }while (res.moveToNext());
        }
        else {
            System.out.println("Empty db");
        }
        return values;
    }

    /*
    public ArrayList<Double> getLoc(){

        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Double> items = new ArrayList<Double>();
        Cursor locationCursor = db.rawQuery("SELECT LATITUDE, LONGITUDE FROM " + TABLE_NAME, null);

        locationCursor.moveToFirst();

        do {
            double latitude = (locationCursor.getDouble(locationCursor
                    .getColumnIndex("latitude")) * 1E6);
            double longitude = (locationCursor.getDouble(locationCursor
                    .getColumnIndex("longitude")) * 1E6);

            items.add((int) latitude, longitude);
        } while (locationCursor.moveToNext());
        return items;
    }

    public void setLat(double lat){
        helper._lat = lat;
    }
    */


    static class MyHelper extends SQLiteOpenHelper{

        //initializations
        //Database name
        static String DATABASE_NAME = "LocationDatabase";
        //Table name
        public static final String TABLE_NAME = "locationInfo2";
        //Column names
        public static final String ID = "ID";
        public static final String Time = "DateTime";
        public static final String Latitude = "Latitude";
        public static final String Longitude = "Longitude";
        public static final String Address = "Address";
        public static final String CustomName = "CustomName";
        public Context context;

        double _lat;
        double _lng;


        public MyHelper(Context context) {
            super(context, TABLE_NAME, null, 11);
            this.context = context;
            Toast.makeText(context, "Constructor Called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +"(ID INTEGER PRIMARY KEY, DateTime TIME, Latitude DOUBLE, Longitude DOUBLE, Address NVARCHAR(200), CustomName NVARCHAR(100));";
                //String CREATE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
                db.execSQL(CREATE_TABLE);
                Toast.makeText(context, "onCreate Called", Toast.LENGTH_SHORT).show();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try{
                db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME + ";");
                Toast.makeText(context, "onUpgrade Called", Toast.LENGTH_SHORT).show();
                onCreate(db);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}


/*
public class DatabaseHelper extends SQLiteOpenHelper{

    //Database name
    public static final String DATABASE_NAME = "LocationDatabase";
    //Table name
    public static final String TABLE_NAME = "locationInfo";
    //Column names
    public static final String ID = "ID";
    public static final String Time = "DateTime";
    public static final String Latitude = "Latitude";
    public static final String Longitude = "Longitude";

    public Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 11);
        this.context = context;
        Toast.makeText(context, "Constructor Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +"(ID INTEGER PRIMARY KEY, DateTime TIME, Latitude DOUBLE, Longitude DOUBLE);";
        db.execSQL(CREATE_TABLE);
        Toast.makeText(context, "onCreate Called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME + ";");
        Toast.makeText(context, "onUpgrade Called", Toast.LENGTH_SHORT).show();
        onCreate(db);
    }

    public long insertData(String dateTime, double lat, double lon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //put the time, latitude and longitude
        values.put(Time, dateTime);
        values.put(Latitude, lat);
        values.put(Longitude, lon);

        //inserting rows
        long id = db.insert(TABLE_NAME, null, values);
        return id;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" ORDER BY "+ID+" DESC",null);
        return res;
    }


}
*/