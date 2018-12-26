package com.antelope.android.intelliagrished.note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "noteBase.db";

    private static final String CREATE_NOTE = "create table " + NoteDbSchema.NoteTable.NAME + "("
            + "id integer primary key autoincrement,"
            + NoteDbSchema.NoteTable.Cols.UUID + ","
            + NoteDbSchema.NoteTable.Cols.TITLE  + ","
            + NoteDbSchema.NoteTable.Cols.CONTENT  + ")";

    private Context mContext;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteDbSchema.NoteTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                NoteDbSchema.NoteTable.Cols.UUID + "," +
                NoteDbSchema.NoteTable.Cols.TITLE + "," +
                NoteDbSchema.NoteTable.Cols.CONTENT  + "," +
                NoteDbSchema.NoteTable.Cols.ITEM_DATE  + "," +
                NoteDbSchema.NoteTable.Cols.ITEM_TIME  + "," +
                NoteDbSchema.NoteTable.Cols.DATE_TIME  +
                ")"
        );
        //db.execSQL(CREATE_NOTE);
        //Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
