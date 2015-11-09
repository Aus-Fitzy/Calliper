package com.example.dean.calliper.app;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mbientlab.metawear.AsyncOperation;
import com.mbientlab.metawear.Message;
import com.mbientlab.metawear.MetaWearBleService;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.RouteManager;
import com.mbientlab.metawear.UnsupportedModuleException;
import com.mbientlab.metawear.module.Gpio;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureFragment extends Fragment implements ServiceConnection {

    private static final String TAG = "MeasureFragment";
    private MeasureFragmentListener fragmentListener;
    private MetaWearBoard mwBoard;
    private Gpio gpioModule;

    public MeasureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        activity.getApplicationContext().bindService(new Intent(activity, MetaWearBleService.class), this, Context.BIND_AUTO_CREATE);
        fragmentListener = (MeasureFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_measure, container, false);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mwBoard = ((MetaWearBleService.LocalBinder) service).getMetaWearBoard(fragmentListener.getBtDevice());
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
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = (Button) view.findViewById(R.id.button_measurement);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View innerView) {
                onMeasureClicked(view);
            }
        });
    }

    public void onMeasureClicked(View v) {
        byte pin = 0;
        Gpio.AnalogReadMode mode = Gpio.AnalogReadMode.ADC;

        final TextView tvADC = (TextView) v.findViewById(R.id.field_measurement_adc);
        final TextView tvMM = (TextView) v.findViewById(R.id.field_measurement_mm);

        gpioModule.routeData().fromAnalogIn(pin, mode).stream("pin0_adc").commit().onComplete(new AsyncOperation.CompletionHandler<RouteManager>() {
            @Override
            public void success(RouteManager result) {
                result.subscribe("pin0_adc", new RouteManager.MessageHandler() {
                    @Override
                    public void process(Message message) {
                        //scale the reading to mm, 0-80mm range from cal_min to cal_max
                        int calMax = fragmentListener.getCalMax();
                        int calMin = fragmentListener.getCalMin();
                        Short signal = message.getData(Short.class);
                        float resolution = (float) (80.0 / (calMax - calMin));
                        int result = (int) ((signal - calMin) * resolution);
                        tvADC.setText(String.format("%d ADC", signal));
                        tvMM.setText(String.format("%d mm", result));
                        Log.i(TAG, "ADC" + message.getData(Short.class));
                        fragmentListener.onMeasureSkin(result);
                    }
                });
            }
        });

        gpioModule.readAnalogIn(pin, mode);
    }

    public interface MeasureFragmentListener {
        BluetoothDevice getBtDevice();

        int getCalMax();

        int getCalMin();

        void onMeasureSkin(int result);
    }
}
