package com.example.satoken.myscheduler;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        mDateEdit = (EditText) findViewById(R.id.dateEdit);
        mTitleEdit = (EditText) findViewById(R.id.titleEdit);
        mDetailEdit = (EditText) findViewById(R.id.detailEdit);
        mDelete = (Button) findViewById(R.id.delete);

        long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        // idを取得した場合は更新、取得できなかった時はscheduleIdが-1となりその場合は新規登録

        if (scheduleId != -1) {
            // クエリの更新
            Realm realm = Realm.getInstance(this); // Realm:データベース
            RealmResults<Schedule> results = realm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();
            Schedule schedule = results.first();
            // フォーマット変換を行い、日付をStringに変換
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(schedule.getDate());
            // 各ビューに表示
            mDateEdit.setText(date);
            mTitleEdit.setText(schedule.getTitle());
            mDetailEdit.setText(schedule.getDetail());
            mDelete.setVisibility(View.VISIBLE); // 削除ボタンを表示
        } else {
            // 新規登録の時
            mDelete.setVisibility(View.INVISIBLE); // 削除ボタンを非表示
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
        // idを取得した場合は更新保存、取得できなかった時はscheduleIdが-1となりその場合は新規保存
        if (scheduleId != -1) {
            // 更新保存の場合
            Realm realm = Realm.getInstance(this);
            RealmResults<Schedule> results = realm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();

            realm.beginTransaction();
            Schedule schedule = results.first(); //更新対象の最初のモデルを取得
            // 各ビューの更新
            schedule.setDate(date);
            schedule.setTitle(mTitleEdit.getText().toString());
            schedule.setDetail(mDetailEdit.getText().toString());
            realm.commitTransaction(); // 更新情報を保存

            // スナックバーの設置：スナックバーの場合、トーストと異なり、アクションを設定できる
            Snackbar.make(findViewById(android.R.id.content),
                    "アップデートしました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setActionTextColor(Color.YELLOW).show();
        } else {
            // 新規保存の場合

            Realm realm = Realm.getInstance(this); //Realmインスタンスを取得

            realm.beginTransaction();
            // Scheduleのidフィールドの最大値を取得
            Number maxId = realm.where(Schedule.class).max("id");
            long nextId = 1; // 新規クエリのIDを1としておく
            if (maxId != null) // もし既にクエリ(id)がある場合
                // 新規に登録するモデルのID (例：idが3あるとき新規クエリのidが4になる)
                nextId = maxId.longValue() + 1;
                Schedule schedule = realm.createObject(Schedule.class);
                schedule.setId(nextId);
                schedule.setDate(date);
                schedule.setTitle(mTitleEdit.getText().toString());
                schedule.setDetail(mDetailEdit.getText().toString());
                realm.commitTransaction();

                Toast.makeText(this, "追加しました",
                        Toast.LENGTH_SHORT).show(); // ウィンドウ画面に一定時間表示
                finish();
        }
    }

    public void onDeleteTapped(View view) {
        long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        // idを取得したクエリを削除。例外として取得できなかった時はscheduleIdが-1とする
        if (scheduleId != -1) {
            // クエリの削除
            Realm realm = Realm.getInstance(this);
            RealmResults<Schedule> results = realm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();
            realm.beginTransaction();
            results.remove(0); // 検索結果からモデルを削除(最初のレコードを削除：最初が0)
            realm.commitTransaction();
            Toast.makeText(this, "削除しました",
                    Toast.LENGTH_SHORT).show(); // ウィンドウ画面に一定時間表示
        }
        finish();
    }
}
