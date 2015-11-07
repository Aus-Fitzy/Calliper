package com.example.dean.calliper.app;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dean.calliper.app.CalibrationFragment.OnCalibrationChangedListener;
import com.mbientlab.metawear.MetaWearBleService;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.module.Led;

public class MeasureActivity extends AppCompatActivity implements ServiceConnection, OnCalibrationChangedListener, MeasureFragment.FragmentListener {
    public static final String EXTRA_BT_DEVICE= "com.example.dean.calliper.app.MeasureActivity.EXTRA_BT_DEVICE";

    private static final String TAG = "MeasureActivity";
    private final MetaWearBoard.ConnectionStateHandler connectionHandler = new MetaWearBoard.ConnectionStateHandler() {
        @Override
        public void connected() {
//            ((DialogFragment) getSupportFragmentManager().findFragmentByTag(RECONNECT_DIALOG_TAG)).dismiss();
//            ((ModuleFragmentBase) currentFragment).reconnected();
            Log.i(TAG, "CSH: Connected");
        }

        @Override
        public void disconnected() {
//            attemptReconnect();
            Log.i(TAG, "CSH: Disconnected");
        }

        @Override
        public void failure(int status, Throwable error) {
//            Fragment reconnectFragment= getSupportFragmentManager().findFragmentByTag(RECONNECT_DIALOG_TAG);
//            if (reconnectFragment != null) {
//                mwBoard.connect();
//            } else {
//                attemptReconnect();
//            }
            Log.i(TAG, "CSH: Error Connecting");
        }
    };
    private MetaWearBoard mwBoard;
    private BluetoothDevice btDevice;
    private int calMax = 1024;
    private int calMin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messure);

        btDevice= getIntent().getParcelableExtra(EXTRA_BT_DEVICE);
        getApplicationContext().bindService(new Intent(this, MetaWearBleService.class), this, BIND_AUTO_CREATE);

        Switch LEDSwitch = (Switch) findViewById(R.id.led_toggle);
        LEDSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(MeasureActivity.this, "Button Toggled", Toast.LENGTH_SHORT).show();
                Led LEDModule = null;
                try {
                    LEDModule = mwBoard.getModule(Led.class);
                } catch (UnsupportedModuleException e) {
                    e.printStackTrace();
                }
                if (isChecked) {


                    if (LEDModule != null) {
                        LEDModule.configureColorChannel(Led.ColorChannel.BLUE)
                                .setRiseTime((short) 0).setPulseDuration((short) 1000)
                                .setRepeatCount((byte) 5).setHighTime((short) 500)
                                .setHighIntensity((byte) 16).setLowIntensity((byte) 4)
                                .commit();
                        LEDModule.play(true);
                    }

                } else {
                    if (LEDModule != null) {
                        LEDModule.stop(true);
                    }
                }
            }
        });

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar_max);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                calMax = progress;
                ((TextView) findViewById(R.id.field_cal_max)).setText(String.format("%d", progress));
                if (((SeekBar) findViewById(R.id.seekBar_min)).getProgress() > calMax)
                    ((SeekBar) findViewById(R.id.seekBar_min)).setProgress(calMax);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekBar_min);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                calMin = progress;
                ((TextView) findViewById(R.id.field_cal_min)).setText(Integer.toString(progress));
                if (((SeekBar) findViewById(R.id.seekBar_max)).getProgress() < calMin) {
                    ((SeekBar) findViewById(R.id.seekBar_max)).setProgress(calMin);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mwBoard= ((MetaWearBleService.LocalBinder) service).getMetaWearBoard(btDevice);
        mwBoard.setConnectionStateHandler(connectionHandler);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onBackPressed() {
        mwBoard.setConnectionStateHandler(null);
        mwBoard.disconnect();

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mwBoard.setConnectionStateHandler(null);
        getApplicationContext().unbindService(this);
    }

    @Override
    public void onCalibrationChanged(int min, int max) {
        calMin = min;
        calMax = max;
    }

    @Override
    public BluetoothDevice getBtDevice() {
        return btDevice;
    }

    @Override
    public int getCalMax() {
        return calMax;
    }

    @Override
    public int getCalMin() {
        return calMin;
    }
}
