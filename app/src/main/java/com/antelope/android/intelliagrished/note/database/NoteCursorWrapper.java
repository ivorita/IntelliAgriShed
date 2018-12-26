package com.antelope.android.intelliagrished.note.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.antelope.android.intelliagrished.note.util.NoteBean;

import java.util.UUID;

/**
 * CursorWrapper封装Cursor的对象，然后添加有用的扩展方法。
 */
public class NoteCursorWrapper extends CursorWrapper {

    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    //考虑到代码的复用
    public NoteBean getNote(){
        String id = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.TITLE));
        String content = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.CONTENT));
        String item_time = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.ITEM_TIME));
        String item_date = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.ITEM_DATE));
        String date_time = getString(getColumnIndex(NoteDbSchema.NoteTable.Cols.DATE_TIME));

        NoteBean note = new NoteBean(UUID.fromString(id));
        note.setTitle(title);
        note.setContent(content);
        note.setItem_time(item_time);
        note.setItem_date(item_date);
        note.setDate_time(date_time);

        return note;
    }
}
