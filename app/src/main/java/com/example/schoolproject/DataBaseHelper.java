package com.example.schoolproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
    private Object object;
    private List<Object> objectList;
    private static final String DB_NAME = "test.db";
    private static final int DB_VER = 2;
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
                "author TEXT," +  // added ver 2
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
        if (oldVersion < 2){
            String addAuthorColumnQuery = "ALTER TABLE Post ADD COLUMN author TEXT";
            db.execSQL(addAuthorColumnQuery);
        }
    }

    public long insertData(String table, ContentValues values){
        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        result = db.insert(table, null, values);
        db.close();
        return result;
    }
    @SuppressLint("Range")
    public List<Object> getPreviewData(String BoardName){
        objectList =  new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String [] columns = {"postId", "boardType", "userId", "title", "content", "author", "date", "time", "heartCount", "chatCount"};
        String selection = "boardType = ?";
        String[] selectionArgs = {BoardName};
        String orderBy = "postId DESC";
        Cursor cursor = db.query("Post", columns, selection, selectionArgs, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()){
            do {
                DataPost data1 = new DataPost();
                data1.setPostId(cursor.getInt(cursor.getColumnIndex("postId")));
                data1.setBoardType(cursor.getString(cursor.getColumnIndex("boardType")));
                data1.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                data1.setContent(cursor.getString(cursor.getColumnIndex("content")));
                data1.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                data1.setTime(cursor.getString(cursor.getColumnIndex("time")));
                data1.setHeart_count(cursor.getString(cursor.getColumnIndex("heartCount")));
                data1.setChat_count(cursor.getString(cursor.getColumnIndex("chatCount")));
                objectList.add(data1);

            } while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        return objectList;
    }
    @SuppressLint("Range")
    public DataPost getPostData(int postId){
        DataPost data = new DataPost();
        SQLiteDatabase db = this.getReadableDatabase();
        String [] columns = {"postId", "boardType", "userId", "title", "content", "author", "date", "time", "heartCount", "chatCount"};
        String selection = "postId = ?";
        String[] selectionArgs = {String.valueOf(postId)};
        Cursor cursor = db.query("Post", columns, selection, selectionArgs, null, null,null);

        if (cursor != null && cursor.moveToFirst()){
            data.setBoardType(cursor.getString(cursor.getColumnIndex("boardType")));
            data.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            data.setDate(cursor.getString(cursor.getColumnIndex("date")));
            data.setTime(cursor.getString(cursor.getColumnIndex("time")));
            data.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            data.setContent(cursor.getString(cursor.getColumnIndex("content")));
            data.setHeart_count(cursor.getString(cursor.getColumnIndex("heartCount")));
            data.setChat_count(cursor.getString(cursor.getColumnIndex("chatCount")));
        }
        if (cursor != null){
            cursor.close();
        }
        return data;
    }
    @SuppressLint("Range")
    public Object getHomeBoardData(String table, String BoardName, String postLimit){
        objectList =  new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String [] columns = {"postId", "boardType", "userId", "title", "content", "author", "date", "time", "heartCount", "chatCount"};
        String selection = "boardType = ?";
        String[] selectionArgs = {BoardName};
        String orderBy = "postId DESC";
        String limit = postLimit;  // Limit rows from parameter
        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, orderBy, limit);

        List<String> post_titles, post_contents, post_ids;
        post_titles = new ArrayList<>();
        post_contents = new ArrayList<>();
        post_ids = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()){
            do {
                post_titles.add(cursor.getString(cursor.getColumnIndex("title")));
                post_contents.add(cursor.getString(cursor.getColumnIndex("content")));
                post_ids.add(String.valueOf(cursor.getInt(cursor.getColumnIndex("postId"))));
            } while (cursor.moveToNext());
        }
        if (cursor != null){
            cursor.close();
        }
        DataHomeBoard data = new DataHomeBoard(BoardName, post_titles, post_contents, post_ids);
        return data;
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
