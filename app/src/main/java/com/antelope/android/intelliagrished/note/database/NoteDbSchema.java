package com.antelope.android.intelliagrished.note.database;

public class NoteDbSchema {

    public static final class NoteTable {
        public static final String NAME = "notes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String CONTENT = "content";
            public static final String ITEM_DATE = "item_date";
            public static final String ITEM_TIME = "item_time";
            public static final String DATE_TIME = "date_time";
        }
    }

}
