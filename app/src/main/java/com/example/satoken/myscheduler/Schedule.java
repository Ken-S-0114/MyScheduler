package com.example.satoken.myscheduler;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

// モデルの作成:このモデルを使ってRealmでデータを保存する
public class Schedule extends RealmObject {
    @PrimaryKey
    private long id; // データを識別するための項目(主キー)
    private Date date;
    private String title;
    private String detail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
