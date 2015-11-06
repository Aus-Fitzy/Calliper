package com.example.dean.calliper.app;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.GpsStatus;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.MetaWearBleService;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.module.Gpio;
import com.mbientlab.metawear.module.Led;

public class MeasureActivity extends AppCompatActivity implements ServiceConnection {
    public static final String EXTRA_BT_DEVICE= "com.example.dean.calliper.app.MeasureActivity.EXTRA_BT_DEVICE";

    private static final String TAG = "MeasureActivity";
    private MetaWearBoard mwBoard;
    private BluetoothDevice btDevice;
    private Gpio gpioModule;

    private final MetaWearBoard.ConnectionStateHandler connectionHandler= new MetaWearBoard.ConnectionStateHandler() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                                .setHighIntensity((byte) 16).setLowIntensity((byte) 16)
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
    }
    public void onMeasureClicked(View v) {
        int res;
        byte pin=0;
        Gpio.AnalogReadMode mode = Gpio.AnalogReadMode.ADC;
        TextView tv = (TextView) findViewById(R.id.field_measurement);
        res = Integer.valueOf((String) tv.getText());
        res += 1;
        tv.setText(Integer.toString(res));
        //tv.setText((Integer.valueOf((String) tv.getText())+1));

        gpioModule.routeData().fromAnalogIn(pin, mode).stream("pin0_adc").commit().onComplete(new AsyncOperation.CompletionHandler<RouteManager>() {
            @Override
            public void success(RouteManager result) {
                result.subscribe("pin0_adc", new RouteManager.MessageHandler() {
                    @Override
                    public void process(Message message) {
                        Log.i(TAG,"ADC" + message.getData(Short.class));
                    }
                });
            }
        });

        gpioModule.readAnalogIn(pin,mode);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mwBoard= ((MetaWearBleService.LocalBinder) service).getMetaWearBoard(btDevice);
        mwBoard.setConnectionStateHandler(connectionHandler);
        try {
            gpioModule = mwBoard.getModule(Gpio.class);
        } catch (UnsupportedModuleException e) {
            e.printStackTrace();
        }
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
}