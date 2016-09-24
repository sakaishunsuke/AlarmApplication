package com.example.sakashun.alarmapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextClock;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.io.IOException;

/**\data
 * Created by Saka Shun on 2016/09/05.
 */
public class AlarmSetting extends Activity{

    int alarm_list_number=0;//アラームの番号
    TextClock alarm_time;//アラームの時間
    MediaPlayer mp = new MediaPlayer();//アラーム音の格納
    EditText alarm_content;//アラームの内容(タイトル)
    Uri music_uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);//アラーム曲のUri
    Button alarm_music_list_button;//アラーム曲設定ボタン
    SeekBar volumeSeekbar;//音量シークバー
    private final static int MUSIC_FILE_CODE = 12345;// 曲選択intentの識別用のコード
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);

        alarm_content = (EditText)findViewById(R.id.alarm_content);//アラームの内容(タイトル)
        volumeSeekbar = (SeekBar)findViewById(R.id.volumeSeekbar);//音量シークバー

        //アラームのデフォルトの曲をセット&リピート設定
        mp=MediaPlayer.create(this,music_uri);
        mp.setLooping(true);

        // AudioManagerを取得する
        final AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //曲再生時の音量を取得
        final int now_volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);



        //アラームの番号取得
        try{
            InputStream in = openFileInput("alarm_list_data.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String s;
            //System.out.println("中身は"+reader.readLine()+"←");
            while((s = reader.readLine())!= null) {
                //現在のアラームの番号を受け取る
                System.out.println("中身は"+s+"←");
                if(s!=" "){
                    //alarm_list_number= (int) Long.parseLong(s,0);
                    alarm_list_number = Integer.parseInt(s);
                }else{
                    System.out.println("結果NULLでした");
                }
            }
            reader.close();
            alarm_content.setText("アラーム"+alarm_list_number);
        }catch(IOException e){
            //もし番号の取得に失敗つまりは、最初だった場合は新しく作る
            alarm_list_number = 1;
            e.printStackTrace();
            OutputStream out;
            try {
                out = openFileOutput("alarm_list_data.txt",MODE_PRIVATE);
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
                //追記する
                writer.append("1\n");
                writer.close();
            } catch (IOException ee) {
                // TODO 自動生成された catch ブロック
                ee.printStackTrace();
                Toast.makeText(AlarmSetting.this,"アラーム番号の初回設定に失敗", Toast.LENGTH_SHORT).show();
            }
            alarm_content.setText("アラーム"+alarm_list_number);
        }

        //登録ボタン
        Button alarm_make_finish_button = (Button) findViewById(R.id.alarm_make_finish_button);
        alarm_make_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputStream out;
                try {
                    out = openFileOutput("alarm_data"+ alarm_list_number+".txt",MODE_PRIVATE);
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));

                    //追記する
                    writer.append("name "+alarm_content.getText()+"\n");
                    writer.append("time "+alarm_time.getText()+"\n");
                    writer.append("music "+music_uri.toString()+"\n");
                    writer.append("volume "+volumeSeekbar.getProgress()+"\n");
                    writer.append("name "+alarm_content.getText()+"\n");
                    writer.close();
                } catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                    Toast.makeText(AlarmSetting.this,"登録に失敗しました", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //無事保存に成功したら
                alarm_list_number++;
                try {
                    out = openFileOutput("alarm_list_data.txt",MODE_PRIVATE);
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
                    //追記する
                    writer.append(alarm_list_number+"\n");
                    writer.close();
                } catch (IOException ee) {
                    // TODO 自動生成された catch ブロック
                    ee.printStackTrace();
                    Toast.makeText(AlarmSetting.this,"アラームリスト番号の更新に失敗", Toast.LENGTH_SHORT).show();
                }

                System.out.println("内容確認↓");
                try{
                    InputStream in = openFileInput("alarm_data"+ (alarm_list_number-1)+".txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                    String s;
                    while((s = reader.readLine())!= null) {
                        //内容をどんどん表示
                        System.out.println(s);
                    }
                    reader.close();
                }catch(IOException e){
                    // TODO 自動生成された catch ブロッ
                    e.printStackTrace();
                }
                finish();
            }
        });

        //アラーム時間のボタン
        alarm_time = (TextClock) findViewById(R.id.alarm_time);
        alarm_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
                //timePicker.getShowsDialog();
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });

        //アラーム曲設定ボタン
        alarm_music_list_button = (Button) findViewById(R.id.alarm_music_list_button);
        alarm_music_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("audio/*");
                //startActivityForResult(intent, MUSIC_FILE_CODE);

                //Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT,false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent,MUSIC_FILE_CODE);
            }
        });

        //音量シークバー設定
        volumeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(
                    SeekBar volumeSeekbar,
                    int progress,
                    boolean fromUser
            ) {
                // シークバーの入力値に変化が生じた段階で音量を変更
                mp.setVolume((float) progress/100f,(float) progress/100f);
                //System.out.println("シークバーの値は"+progress);
                if (!mp.isPlaying()) {
                    // MediaPlayerの再生
                    mp.seekTo(0); // プレイ中のBGMをスタート位置に戻す
                    mp.start();
                }
            }
            public void onStartTrackingTouch(SeekBar volumeSeekbar) {
                // トグル（シークバーのつまみ）がタッチされたときの動作
                am.setStreamVolume(AudioManager.STREAM_MUSIC,100,0);//音量をmaxにする
                if(mp!=null)
                    mp.seekTo(0); // プレイ中のBGMをスタート位置に戻す
                    mp.start();
            }
            public void onStopTrackingTouch(SeekBar volumeSeekbar) {
                // トグル（シークバーのつまみ）がリリースされたときの動作
                am.setStreamVolume(AudioManager.STREAM_MUSIC,now_volume,0);//音量を元に戻す
                if(mp!=null) {
                    mp.stop();
                    try {
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // リソースの解放
                    //mp.release();
                }
            }
        });

    }

    //intentでの結果を受け取る
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MUSIC_FILE_CODE && resultCode == RESULT_OK) {
            // アラームの曲intentの結果の処理
            String filePath = "null";
            filePath = data.getDataString();
            System.out.println("ファイルパスだよ！　"+filePath);
            if(data.getDataString()!=null && data.getDataString().matches(".*content://.*")){
                //filePath = data.getDataString().replace("file:///", "");

                //サウンドピッカー以外の時は設定できない
                Toast.makeText(this, "その曲は設定できませんでした", Toast.LENGTH_SHORT).show();

                music_uri = Uri.parse(filePath);
                try {
                    mp=MediaPlayer.create(this,music_uri);
                    mp.prepare();
                    mp.start();
                }catch(IllegalArgumentException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
                /*
                String decodedfilePath = null;
                try {
                    decodedfilePath = URLDecoder.decode(filePath, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    // いい感じに例外処理
                }
                File file = new File(decodedfilePath);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                alarm_music_list_button.setText( decodedfilePath );
                //File file = new File(filePath);
                //Uri uri = Uri.fromFile(file);
               mp = MediaPlayer.create(this, data.getData());
                // alarm_music_list_button.setText( uri.toString() );
                //mp.setDataSource(data.getData());
               mp.start();
               */
            }else{
                music_uri = (Uri) data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                //alarm_music_list_button.setText( music_uri.toString() );
                mp = MediaPlayer.create(this, music_uri);
                //mp.start();
            }
        }
    }

    //時計ダイアログの処理
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
            alarm_time.setFormat24Hour(String.valueOf(hourOfDay)+":"+String.format("%02d", minute));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAndRelaese();
    }

    private void stopAndRelaese() {
        //もし曲がセットされていたら
        if (mp != null) {
            // 再生終了
            mp.stop();
            // リソースの解放
            mp.release();
        }
    }
}
