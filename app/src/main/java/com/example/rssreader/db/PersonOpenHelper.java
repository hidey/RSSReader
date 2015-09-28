package com.example.rssreader.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rssreader.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hideyuki.Kikuma on 15/09/28.
 */
public class PersonOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "person";
    private static final String TABLE_NAME = "person";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_PERSON_TABLE = "create table " + TABLE_NAME + "(" +
            " _id integer PRIMARY KEY AUTOINCREMENT," +
            " name text not null," +
            " age integer not null," +
            " comment text" +
            ");";

    private static final String DROP_PERSON_TABLE = "drop table person;";
    private static final String TAG = PersonOpenHelper.class.getSimpleName();

    public PersonOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate");
        db.execSQL(CREATE_PERSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 今日はこれは使いません
        // 興味がある人は自分で調べてみましょう
    }

    public List<Person> selectAll() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Person> list = new ArrayList<>();
        try {
            db = getReadableDatabase();
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{"_id", "name", "age", "comment"},
                    null, null, null, null, "_id"
            );
            boolean hasNext = cursor.moveToFirst();
            while (hasNext) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                Person person = new Person(name, age, comment);
                list.add(person);
                hasNext = cursor.moveToNext();
            }


        } finally {
            if (db != null) db.close();
            if (cursor != null) cursor.close();
        }

        return list;
    }

    public Person selectById(long id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{"_id", "name", "age", "comment"},
                    "_id=?", new String[]{String.valueOf(id)}, null, null, "_id"
            );
            boolean hasNext = cursor.moveToFirst();
            if (!hasNext){
                return null;
            }
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                return new Person(name, age, comment);

        } finally {
            if (db != null) db.close();
            if (cursor != null) cursor.close();
        }
    }
}
