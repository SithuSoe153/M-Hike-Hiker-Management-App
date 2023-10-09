package com.uog.soemhike.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mHike.db";
    private static final String TABLE_HIKE = "tblHike";
    private static final String TABLE_OBSERVATION = "tblObservation";
//
//    public static final String HIKE_ID = "id";
//    public static final String NAME = "name";
//    public static final String LOCATION = "location";
//    public static final String DATE = "date";
//    public static final String PARKING = "parking";
//    public static final String LENGTH = "length";
//    public static final String DIFFICULTY = "difficulty";
//    public static final String DESCRIPTION = "description";


    private SQLiteDatabase database;
    private Hike hike;


    private static final String CREATE_HIKE_TABLE =String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s INTEGER," +
                    " %s TEXT," +
                    " %s TEXT)"
            , TABLE_HIKE, Hike.ID, Hike.NAME, Hike.LOCATION, Hike.DATE, Hike.PARKING, Hike.LENGTH, Hike.DIFFICULTY, Hike.DESCRIPTION);


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        database =getWritableDatabase();
        if(database !=null) database.execSQL( "PRAGMA encoding ='UTF-8'" );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HIKE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long saveHike(Hike hike){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(Hike.NAME, hike.getName());
        rowValues.put(Hike.LOCATION, hike.getLocation());
        rowValues.put(Hike.DATE, hike.getDate());
        rowValues.put(Hike.PARKING, hike.getParking());
        rowValues.put(Hike.LENGTH, hike.getLength());
        rowValues.put(Hike.DIFFICULTY, hike.getDifficulty());
        rowValues.put(Hike.DESCRIPTION, hike.getDescription());

        result =database.insertOrThrow(TABLE_HIKE, null, rowValues);

        Log.i("test", "saveHike");

        return result;
    }

    public long updateHike(Hike hike){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(Hike.NAME, hike.getName());
        rowValues.put(Hike.LOCATION, hike.getLocation());
        rowValues.put(Hike.DATE, hike.getDate());
        rowValues.put(Hike.PARKING, hike.getParking());
        rowValues.put(Hike.LENGTH, hike.getLength());
        rowValues.put(Hike.DIFFICULTY, hike.getDifficulty());
        rowValues.put(Hike.DESCRIPTION, hike.getDescription());

        Log.i("test", "updateHike");

        String where = "id=?";
        String values[] = {hike.getId() + ""};
        result =database.update(TABLE_HIKE,rowValues,where,values);
        return result;
    }

    public long delete(int id){
        long result = 0;
        String where = "id = ?";
        String valuse[] = {String.valueOf(id)};
        result  = database.delete(TABLE_HIKE,where,valuse);
        return  result;

    }

    public List search(String keyword ) throws Exception{
        Cursor cursor = null;
        String query ="SELECT * FROM " + TABLE_HIKE
                +" WHERE " + Hike.NAME +" LIKE '" + keyword +"%'";

        return searchHike( query, cursor );

    }

    public List searchHike(String name, String location, String date, Double length) throws Exception{

        Cursor cursor = null;
        String query ="SELECT * FROM " + TABLE_HIKE
                +" WHERE " + Hike.NAME +" LIKE '" + location +"%'";


        if (date !=null && !date.trim().isEmpty())
            query+= " AND " + Hike.DATE + "='" + date + "'";

        if (length != null)
            query += " AND " + Hike.LENGTH + "=" + length;
        return searchHike(query,cursor);

    }

    public List<Hike> searchHike(String query, Cursor cursor) throws Exception{

        List<Hike> results =new ArrayList<>();
        cursor = database.rawQuery( query, null );
        cursor.moveToFirst( );
        while( !cursor.isAfterLast() ){

            hike = new Hike(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getDouble(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );
            results.add(hike);
            cursor.moveToNext();
        }
        cursor.close();
        return results;
    }



    public void deleteDatabase(Context context) {
        context.deleteDatabase("mHikessssss.db");
        System.out.println("Database " + "mHikessssss.db" + " has been deleted.");
    }

}
