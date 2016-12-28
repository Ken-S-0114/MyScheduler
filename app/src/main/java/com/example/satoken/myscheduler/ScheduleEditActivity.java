package com.example.satoken.myscheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleEditActivity extends AppCompatActivity {

    EditText mDateEdit;
    EditText mTitleEdit;
    EditText mDetailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        mDateEdit = (EditText) findViewById(R.id.dateEdit);
        mTitleEdit = (EditText) findViewById(R.id.titleEdit);
        mDetailEdit = (EditText) findViewById(R.id.detailEdit);

        long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        // 取得できなかった時scheduleIdが１になりその場合は新規登録、それ以外は更新
        if (scheduleId != -1) {
            Realm realm = Realm.getInstance(this); // Realm:データベース
            RealmResults<Schedule> results = realm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();
            // 各ビューに反映
            Schedule schedule = results.first();
            // フォーマット変換を行い、日付をStringに変換
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(schedule.getDate());
            mDateEdit.setText(date);
            mTitleEdit.setText(schedule.getTitle());
            mDetailEdit.setText(schedule.getDetail());
        }
    }

    public void onSaveTapped(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        try {
            date = sdf.parse(mDateEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        if (scheduleId != -1) {
            Realm realm = Realm.getInstance(this);
            RealmResults<Schedule> results = realm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();

            realm.beginTransaction();
            Schedule schedule = results.first();

        //Realmインスタンスを取得
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction(); // データベースの処理を行うときまずはじめに実行する
        Number maxId = realm.where(Schedule.class).max("id");
        long nextId = 1;
        if (maxId != null) nextId = maxId.longValue() + 1;
        Schedule schedule = realm.createObject(Schedule.class);
        schedule.setId(nextId);
        schedule.setDate(date);
        schedule.setTitle(mTitleEdit.getText().toString());
        schedule.setDetail(mDetailEdit.getText().toString());
        realm.commitTransaction();

        Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show(); // ウィンドウ画面に一定時間表示
        finish();
    }
}
