package com.appdeveloper.rh.giphyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Roman on 11/6/2017.
 */

public class DBFavGif {
    static final String KEY_ROWID = "_id";
    static final String KEY_TITLE = "title";
    static final String KEY_PHOTO_URL = "photoUrl";

    private static final String DATABASE_NAME = "MyDBFav";
    private static final String DATABASE_TABLE = "favItems";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TITLE + " text, "
                    + KEY_PHOTO_URL + " text);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBFavGif(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS favItems");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBFavGif open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    public void resetDB() {
        db.execSQL("DROP TABLE IF EXISTS favItems");
        try {
            db.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---insert a contact into the database---
    public void insertItem(String mTitle, String mPhotoUrl) {
        ContentValues insertValues = new ContentValues();
        insertValues.put(KEY_TITLE, mTitle);
        insertValues.put(KEY_PHOTO_URL, mPhotoUrl);
        db.insertOrThrow(DATABASE_TABLE, null, insertValues);
    }

    //---deletes a particular contact---
    public boolean deleteItem(String mPhotoUrl) {
        return db.delete(DATABASE_TABLE, KEY_PHOTO_URL + "='" + mPhotoUrl + "'", null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllItems() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_PHOTO_URL}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getItem(String mPhotoUrl) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
                                KEY_TITLE, KEY_PHOTO_URL}, KEY_PHOTO_URL + "='" + mPhotoUrl + "'", null,
                        null, null, null, null);
        return mCursor;
    }

    //---updates a contact---
    public boolean updateItem(long rowId, String mTitle, String mPhoto) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, mTitle);
        args.put(KEY_PHOTO_URL, mPhoto);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
