package com.example.myapplication;

// public class AlarmReceiver {
// }
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    // 알람 시간이 되면 시스템이 이 메소드를 호출합니다.
    @Override
    public void onReceive(Context context, Intent intent) {
        // Logcat에서 "Alarm Receiver" 태그로 메시지를 필터링해서 볼 수 있습니다.
        Log.d("AlarmReceiver", "알람이 울렸습니다!");

        // 기능이 동작하는지 간단히 확인하기 위해 화면에 짧은 메시지를 띄웁니다.
        Toast.makeText(context, "알람!", Toast.LENGTH_SHORT).show();

        // [최종 목표] 나중에 이 곳에 날씨 정보와 할 일을 가져와
        // 알림(Notification)을 만드는 코드가 들어갈 예정입니다.
    }
}
