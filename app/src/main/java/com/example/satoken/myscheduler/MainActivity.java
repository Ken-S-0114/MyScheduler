package com.example.satoken.myscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Realm realm = Realm.getInstance(this);
        RealmResults<Schedule> schedules =
                realm.where(Schedule.class).findAll();
        ScheduleAdapter adapter =
                new ScheduleAdapter(this, schedules, true);
        mListView.setAdapter(adapter);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,
                        ScheduleEditActivity.class));
            }
        });

        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    // リストビューの項目がタップされた時の処理
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        /* リスト内の指定された位置に関連するデータ
                        (position:タップされた項目のリスト上の位置)を取得 */
                        Schedule schedule =
                                (Schedule) parent.getItemAtPosition(position);
                        /* インテントにschedule_idとしてidを格納することで
                          idをScheduleEditActivityに渡している */
                        startActivity(new Intent(MainActivity.this,
                                ScheduleEditActivity.class)
                        .putExtra("schedule_id", schedule.getId()));

                    }
                }
        );
    }
}
