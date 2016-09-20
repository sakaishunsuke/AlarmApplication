package com.example.sakashun.alarmapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Saka Shun on 2016/09/05.
 */
public class AlarmSetting extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

        Button alarm_make_finish_button = (Button) findViewById(R.id.alarm_make_finish_button);
        //アラーム画面へのボタン
        alarm_make_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}