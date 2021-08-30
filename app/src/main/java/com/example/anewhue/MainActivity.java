package com.example.anewhue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private SharedMemory mSharedMemory;
    private ToggleButton mToggleButton;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar alpha, red, green, blue;
        alpha = findViewById(R.id.seek_alpha);
        red = findViewById(R.id.seek_red);
        green = findViewById(R.id.seek_green);
        blue = findViewById(R.id.seek_blue);

        mToggleButton = findViewById(R.id.startButton);

        mSharedMemory = new SharedMemory(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.filterbtn);
        //Making the navigation bar work
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.testbtn:
                        startActivity(new Intent(getApplicationContext(),Test.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.filterbtn:
                        return true;
                    case R.id.settingsbtn:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSharedMemory.setAlpha(alpha.getProgress());
                mSharedMemory.setRed(red.getProgress());
                mSharedMemory.setGreen(green.getProgress());
                mSharedMemory.setBlue(blue.getProgress());

                if (ANewHueFilterService.STATE == ANewHueFilterService.STATE_ACTIVE) {
                    Intent intent =new Intent(MainActivity.this, ANewHueFilterService.class);
                    startService(intent);
                }

                mToggleButton.setChecked(ANewHueFilterService.STATE == ANewHueFilterService.STATE_ACTIVE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        alpha.setOnSeekBarChangeListener(changeListener);
        red.setOnSeekBarChangeListener(changeListener);
        green.setOnSeekBarChangeListener(changeListener);
        blue.setOnSeekBarChangeListener(changeListener);

        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, ANewHueFilterService.class);
                if (ANewHueFilterService.STATE == ANewHueFilterService.STATE_ACTIVE) {
                    stopService(i);
                } else {
                    startService(i);
                }
                refresh();
            }
        });
    }

    private void refresh() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();

        mCountDownTimer = new CountDownTimer(100, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mToggleButton.setChecked(ANewHueFilterService.STATE == ANewHueFilterService.STATE_ACTIVE);
            }
        };
        mCountDownTimer.start();
    }
}