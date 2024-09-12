package com.example.pomodoroodev_turkeratac;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int POMODORO_DURATION = 25*60*1000;
    private static final int SHORT_BREAK_DURATION = 5*60*1000;
    private long mTimeLeftInMillis = POMODORO_DURATION;
    private SeekBar seekBar;
    private boolean timerRunning = false;
    private CountDownTimer countDownTimer;
    private Button startButton;
    private TextView timeText;
    private boolean onBreak = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startButton);
        timeText = findViewById(R.id.timerTextView);
        seekBar=findViewById(R.id.seekBar);
        seekBar.setMax(25);
        seekBar.setProgress(25);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    resetTimer();
                    startTimer();
                }
            }
        });

        updateTimer();
    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis=millisUntilFinished;
                updateTimer();
            }
            @Override
            public void onFinish() {
                if(!onBreak)
                {
                    mTimeLeftInMillis=SHORT_BREAK_DURATION;
                    startButton.setText("5 DK MOLA");
                    startButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.yesil));
                    onBreak=true;
                }
                else{
                    mTimeLeftInMillis=POMODORO_DURATION;
                    startButton.setText("Başlat");
                    startButton.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.yesil));
                    onBreak=false;
                }
                timerRunning=false;
            }
        }.start();
        timerRunning=true;
        startButton.setText("İptal Et");
        startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.kirmizi));
        seekBar.setMax(25);
        seekBar.setProgress(25);
    }
    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        if (!onBreak) {
            startButton.setText("Başlat");
            startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.yesil));
        } else {
            startButton.setText("5 DK MOLA");
            startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.yesil));
        }
    }
    private void resetTimer() {
        if (!onBreak) {
            mTimeLeftInMillis = POMODORO_DURATION;
        } else {
            mTimeLeftInMillis = SHORT_BREAK_DURATION;
        }
        updateTimer();
    }
    private void updateTimer() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        seekBar.setProgress(minutes+1);
        if(minutes==0 &&seconds==0)
        {
            seekBar.setProgress(0);
        }
        timeText.setText(String.format("%02d:%02d", minutes, seconds));
    }
}