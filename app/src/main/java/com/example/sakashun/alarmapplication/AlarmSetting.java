package com.example.sakashun.alarmapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TimePicker;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;

/**
 * Created by Saka Shun on 2016/09/05.
 */
public class AlarmSetting extends Activity{

    TextClock alarm_time;//アラームの時間
    Button alarm_music_list_button;//アラーム曲設定ボタン
    private final static int MUSIC_FILE_CODE = 12345;// 識別用のコード

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

        Button alarm_make_finish_button = (Button) findViewById(R.id.alarm_make_finish_button);
        //登録ボタン
        alarm_make_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        alarm_time = (TextClock) findViewById(R.id.alarm_time);
        //アラーム時間のボタン
        alarm_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                //timePicker.getShowsDialog();
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });

        alarm_music_list_button = (Button) findViewById(R.id.alarm_music_list_button);
        //アラーム曲設定ボタン
        alarm_music_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("audio/*");
                //startActivityForResult(intent, MUSIC_FILE_CODE);

                //Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT,false);
                startActivityForResult(intent,MUSIC_FILE_CODE);

            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MUSIC_FILE_CODE && resultCode == RESULT_OK) {
            String filePath = data.getDataString().replace("file://", "");
            String decodedfilePath = null;
            try {
                decodedfilePath = URLDecoder.decode(filePath, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                // いい感じに例外処理
            }
            alarm_music_list_button.setText(decodedfilePath);
        }
    }

    @SuppressLint("ValidFragment")
    public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);

            return timePickerDialog;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //時刻が選択されたときの処理
            alarm_time.setFormat24Hour(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
        }

    }
}
