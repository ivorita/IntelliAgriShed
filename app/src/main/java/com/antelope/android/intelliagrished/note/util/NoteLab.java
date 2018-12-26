package com.antelope.android.intelliagrished.note.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.antelope.android.intelliagrished.note.database.MyDatabaseHelper;
import com.antelope.android.intelliagrished.note.database.NoteCursorWrapper;
import com.antelope.android.intelliagrished.note.database.NoteDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class NoteLab {

    //s前缀表示静态变量
    private static NoteLab sNoteLab;
    //private List<Student> mStudents;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static NoteLab get(Context context) {
        if (sNoteLab == null) {
            sNoteLab = new NoteLab(context);
        }
        return sNoteLab;
    }

    public NoteLab(Context context) {
        //创建student数据库
        mContext = context.getApplicationContext();
        mDatabase = new MyDatabaseHelper(mContext)
                .getWritableDatabase();

        //mStudents = new ArrayList<>();
    }

    //返回数组列表
    public List<NoteBean> getNotes() {
        List<NoteBean> noteBeanList = new ArrayList<>();
        NoteCursorWrapper cursorWrapper = queryNote(null, null);
        try {
            cursorWrapper.moveToFirst();
            //isAfterLast()返回游标是否指向第最后一行的位置，当下一行没有数据时，返回true。
            while (!cursorWrapper.isAfterLast()) {
                noteBeanList.add(cursorWrapper.getNote());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return noteBeanList;
        //return mStudents;
    }

    //返回带指定id的Student对象
    public NoteBean getNote(UUID id) {
        NoteCursorWrapper cursorWrapper = queryNote(
                NoteDbSchema.NoteTable.Cols.UUID + " =? ", new String[]{id.toString()});
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            //取出已存在的首条记录
            cursorWrapper.moveToFirst();
            return cursorWrapper.getNote();
        } finally {
            cursorWrapper.close();
        }
        /*for (Student student : mStudents){
            if (student.getId().equals(id)){
                return student;
            }
        }
        return null;*/
    }

    //添加数据
    public void addNote(NoteBean mNoteBean) {
        ContentValues values = getContentValues(mNoteBean);
        mDatabase.insert(NoteDbSchema.NoteTable.NAME, null, values);
        //mStudents.add(student);
    }

    //删除数据
    public void removeNote(NoteBean noteBean) {
        String uuidString = noteBean.getId().toString();
        mDatabase.delete(NoteDbSchema.NoteTable.NAME, NoteDbSchema.NoteTable.Cols.UUID + "=?", new String[]{uuidString});
        //mStudents.remove(student);
    }

    public File getPhotoFile(NoteBean noteBean) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, noteBean.getPhotoFileName());//返回指向某个具体位置的File对象
    }

    //更新数据
    public void updateNote(NoteBean noteBean) {
        String uuidString = noteBean.getId().toString();
        ContentValues values = getContentValues(noteBean);

        mDatabase.update(NoteDbSchema.NoteTable.NAME, values, NoteDbSchema.NoteTable.Cols.UUID + "=?", new String[]{uuidString});
    }

    //Cursor是一个表数据处理工具，其功能是封装数据表中的原始字段值。
    private NoteCursorWrapper queryNote(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteDbSchema.NoteTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new NoteCursorWrapper(cursor);
    }

    //负责处理数据库写入和更新操作的辅助类是ContentValues，一个键值存储类，只能处理SQLite数据。
    private static ContentValues getContentValues(NoteBean noteBean) {
        ContentValues values = new ContentValues();
        values.put(NoteDbSchema.NoteTable.Cols.UUID, noteBean.getId().toString());
        values.put(NoteDbSchema.NoteTable.Cols.TITLE, noteBean.getTitle());
        values.put(NoteDbSchema.NoteTable.Cols.CONTENT, noteBean.getContent());
        values.put(NoteDbSchema.NoteTable.Cols.ITEM_DATE, noteBean.getItem_date());
        values.put(NoteDbSchema.NoteTable.Cols.ITEM_TIME, noteBean.getItem_time());
        values.put(NoteDbSchema.NoteTable.Cols.DATE_TIME, noteBean.getDate_time());

        return values;
    }

}
