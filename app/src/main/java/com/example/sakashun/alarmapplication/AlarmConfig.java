package com.example.sakashun.alarmapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Saka Shun on 2016/09/05.
 */
public class AlarmConfig extends Activity{

    private static final  R = ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_config);

        Button alarm_make_button = (Button) findViewById(R.id.alarm_make_button);
        //アラーム画面へのボタン
        alarm_make_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplication()
                        ,com.example.sakashun.alarmapplication.AlarmSetting.class);
                startActivity(intent);
            }
        });
    }
}
