package com.antelope.android.intelliagrished.note.util;

import java.util.UUID;

public class NoteBean {

    private UUID mId;
    private String title;
    private String content;
    private String date_time;
    private String item_time;
    private String item_date;

    public NoteBean(){
        this(UUID.randomUUID());
        //id = UUID.randomUUID();
    }

    public NoteBean(UUID id){
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getItem_time() {
        return item_time;
    }

    public void setItem_time(String item_time) {
        this.item_time = item_time;
    }

    public String getItem_date() {
        return item_date;
    }

    public void setItem_date(String item_date) {
        this.item_date = item_date;
    }


    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPhotoFileName(){
        return "IMG_" + getId().toString() +".jpg";
    }
}
