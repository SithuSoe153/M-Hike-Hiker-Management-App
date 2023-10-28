package com.uog.soemhike.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private Observation observation;


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


    private static final String CREATE_OBSERVATION_TABLE =String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s integer,"+
                    " FOREIGN KEY (%s)"+
                    " REFERENCES %s(%s)" +
                    " ON DELETE CASCADE" +
                    ")"
            , TABLE_OBSERVATION, Observation.O_ID, Observation.O_TITLE,Observation.O_YEAR, Observation.O_HIKEID, Observation.O_HIKEID, TABLE_HIKE, Hike.ID);


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 3);
        database =getWritableDatabase();
        if(database !=null) database.execSQL( "PRAGMA encoding ='UTF-8'" );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_HIKE_TABLE);
        sqLiteDatabase.execSQL(CREATE_OBSERVATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String dropTable1 = "drop table if exists "+TABLE_HIKE;
        sqLiteDatabase.execSQL(dropTable1);

        String dropTable2 = "drop table if exists "+TABLE_OBSERVATION;
        sqLiteDatabase.execSQL(dropTable2);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
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

    public long addObservation(Observation observation){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(Observation.O_TITLE, observation.getTitle());
        rowValues.put(Observation.O_YEAR, observation.getYear());
        rowValues.put(Observation.O_HIKEID, observation.getUser_id());

        result =database.insertOrThrow(TABLE_OBSERVATION, null, rowValues);

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

    public long update_Observation(Observation observation){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(Observation.O_TITLE, observation.getTitle());
        rowValues.put(Observation.O_YEAR, observation.getYear());
        rowValues.put(Observation.O_HIKEID, observation.getUser_id());
//        rowValues.put(Hike.DATE, hike.getDate());

        Log.i("test", "updateObservation");

        String where = "id=?";
        String values[] = {observation.getId()+ ""};
        result =database.update(TABLE_OBSERVATION,rowValues,where,values);
        return result;
    }


    public long delete(int id){
        long result = 0;
        String where = "id = ?";
        String valuse[] = {String.valueOf(id)};
        result  = database.delete(TABLE_HIKE,where,valuse);
        return  result;

    }

//    public long delete_Observation(int id){
//        long result = 0;
//        String where = "id = ?";
//        String valuse[] = {String.valueOf(id)};
//        result  = database.delete(TABLE_OBSERVATION,where,valuse);
//        return  result;
//
//    }

    public void delete_Observation(int id){

        database.delete(TABLE_OBSERVATION, Observation.O_ID + "=?", new String[]{String.valueOf(id)});
        Log.i("test111", String.valueOf(id));
    }

    public List search(String keyword ) throws Exception{
        Cursor cursor = null;
        String query ="SELECT * FROM " + TABLE_HIKE
                +" WHERE " + Hike.NAME +" LIKE '" + keyword +"%'";

        return searchHike( query, cursor );

    }

    public List<Hike> searchHike(String keyword ) throws Exception{
        Cursor cursor = null;
        String query ="SELECT * FROM " + TABLE_HIKE
                +" WHERE " + Hike.NAME +" LIKE '" + keyword +"%'";// "SELECT * FROM tblHike WHERE name LIKE %%"

        return searchHike( query, cursor );
    }

    public List<Observation> searchObservation(String keyword ) throws Exception{
        Cursor cursor = null;
        String query ="SELECT * FROM " + TABLE_OBSERVATION
                +" WHERE " + Observation.O_HIKEID +" LIKE '" + keyword +"%'";// "SELECT * FROM tblHike WHERE name LIKE %%"

        return searchObservation( query, cursor );
    }

    public List searchHike(String name, String location, String date) throws Exception{

//        date = "2023/10/17";
        Log.i("dataadv",name);
        Log.i("dataadv",location);
        Log.i("dataadv",date);
//        Log.i("dataadv", String.valueOf(length));
        Cursor cursor = null;
        String query ="SELECT * FROM " + TABLE_HIKE
                +" WHERE "
                + Hike.NAME +" = '" + name + "'"
                + " AND " + Hike.LOCATION + "='"
                + location +"'";


        if (date !=null && !date.trim().isEmpty())
            query+= " AND " + Hike.DATE + "='" +
                    date + "'";

//        if (length != null)
//            query += " AND " + Hike.LENGTH + "=" + length;

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


    public List<Observation> searchObservation(String query, Cursor cursor) throws Exception{

        List<Observation> results =new ArrayList<>();
        cursor = database.rawQuery( query, null );
        cursor.moveToFirst( );
        while( !cursor.isAfterLast() ){

            observation = new Observation(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3)

            );
            results.add(observation);
            cursor.moveToNext();
        }
        cursor.close();
        return results;
    }

    public void deleteAllData(){
        database.delete(TABLE_HIKE,null,null);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase("mHikessssss.db");
        System.out.println("Database " + "mHikessssss.db" + " has been deleted.");
    }

}
