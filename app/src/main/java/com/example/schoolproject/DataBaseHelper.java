package com.example.schoolproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
    User
        text id (Primary Key, Unique Constraint)
        text pw
    Post
        integer postId (Primary Key, Unique Constraint, Auto-Increment)
        text boardType
        text userId (Foreign Key)
        text title
        text content
        text date
        text time
        integer heartCount
        integer chatCount
*/
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "test.db";
    private static final int DB_VER = 1;
    public DataBaseHelper(Context context){
        super(context, DB_NAME, null,DB_VER);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create db schema
        String createUserTableQuery = "CREATE TABLE User (" +
                "id TEXT PRIMARY KEY UNIQUE," +
                "pw TEXT)";

        String createPostTableQuery = "CREATE TABLE Post (" +
                "postId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "boardType TEXT," +
                "userId TEXT," +
                "title TEXT," +
                "content TEXT," +
                "date TEXT," +
                "time TEXT," +
                "heartCount INTEGER," +
                "chatCount INTEGER," +
                "FOREIGN KEY (userId) REFERENCES User(id))";
        db.execSQL(createUserTableQuery);
        db.execSQL(createPostTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // database upgrade logic
        // e.g. drop table and recreate
    }

    public long insertData(String table, ContentValues values){
        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        result = db.insert(table, null, values);
        db.close();
        return result;
    }
    public int updateData(String table, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.update(table, values, whereClause, whereArgs);
        db.close();
        return rowsAffected;
    }
    public int deleteData(String table, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(table, whereClause, whereArgs);
        db.close();
        return rowsAffected;
    }


}
