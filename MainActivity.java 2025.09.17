package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. UI의 버튼을 코드와 연결
        Button timePickerButton = findViewById(R.id.timePickerButton);

        // 2. 버튼 클릭 이벤트 설정
        timePickerButton.setOnClickListener(v -> {
            // 현재 시간을 기본으로 보여주는 TimePickerDialog 생성
            Calendar now = Calendar.getInstance();
            TimePickerDialog timePicker = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        // 사용자가 시간을 선택하면 이 부분이 호출됩니다.
                        Log.d("MainActivity", "선택된 시간: " + hourOfDay + "시 " + minute + "분");

                        // 권한을 확인하고, 있으면 알람을 설정하는 메소드 호출
                        checkPermissionAndSetAlarm(hourOfDay, minute);
                    },
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false // true로 바꾸면 12시간제 (AM/PM)
            );

            timePicker.show();
        });
    }

    // 3. 알람 설정 전 권한 확인
    private void checkPermissionAndSetAlarm(int hourOfDay, int minute) {
        // 안드로이드 12 (S) 버전 이상인지 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // 정확한 알람을 설정할 수 있는 권한이 있는지 확인
            if (!alarmManager.canScheduleExactAlarms()) {
                // 권한이 없으면 사용자에게 권한 설정 화면으로 이동하도록 요청
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            } else {
                // 권한이 있으면 알람 설정
                setAlarm(hourOfDay, minute);
            }
        } else {
            // 버전이 낮으면 바로 알람 설정
            setAlarm(hourOfDay, minute);
        }
    }

    // 4. 사용자가 선택한 시간으로 실제 알람을 설정하는 메소드
    private void setAlarm(int hourOfDay, int minute) {
        // Calendar 객체를 사용해 알람 시간 설정
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 만약 선택한 시간이 현재 시간보다 이전이라면, 다음 날로 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        // AlarmManager 설정
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        // 알람 등록
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "알람이 " + hourOfDay + "시 " + minute + "분에 설정되었습니다.", Toast.LENGTH_SHORT).show();
        Log.d("MainActivity", "알람이 " + calendar.getTime() + "에 설정되었습니다.");
    }
}
