package com.igames2go.t4f.utils;

/**
 * Created by Raj Amal on 5/30/13.
 */




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "T4F";

    // Login table name
    private static final String TABLE_NAME = "Pla";

    // Login Table Columns names
    private static final String KEY_ID = "pla_id";
    private static final String KEY_NO_LOGIN_ID = "pla_nolog_id";
    private static final String KEY_LOGIN_ID = "pla_log_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NO_LOGIN_ID + " INTEGER,"
                + KEY_LOGIN_ID + " INTEGER" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void storeLoginPlayerId(int loginPlayerId) {
    	
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        if(getRowCount() > 0){
        	values.put(KEY_LOGIN_ID, loginPlayerId);
        	db.update(TABLE_NAME, values, null, null);
        }else{
        	values.put(KEY_LOGIN_ID, loginPlayerId);
         	values.put(KEY_NO_LOGIN_ID, 0);
             // Inserting Row
             db.insert(TABLE_NAME, null, values);
        }
        db.close(); // Closing database connection
        
    	
    }
    public void storeNoLoginPlayerId(int noLoginPlayerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_NO_LOGIN_ID, noLoginPlayerId);
        db.update(TABLE_NAME, values, null, null);
        db.close(); // Closing database connection
    }
    
    public int getLoginPlayerId(){
    	
    	String selectQuery = "SELECT pla_log_id FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        int nonFBPlayerId = 0;
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
        	nonFBPlayerId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        // return user
        return nonFBPlayerId;
    }
    
    /**
     * Storing user details in database
     * */
    public void addUser(String fname, String lname, String email, String uname, String uid, String created_at) {
        
    }


    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("fname", cursor.getString(1));
            user.put("lname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("uname", cursor.getString(4));
            user.put("uid", cursor.getString(5));
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        } 
        // return row count
        return rowCount;
    }

    public void insertDefaultRow(){
    	 SQLiteDatabase db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         
         if(getRowCount() > 0){
        	 return;
         }else{
         	values.put(KEY_LOGIN_ID, -1);
         	values.put(KEY_NO_LOGIN_ID, -1);
             // Inserting Row
             db.insert(TABLE_NAME, null, values);
         }
         db.close(); // Closing database connection
    }
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
