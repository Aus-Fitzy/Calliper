package com.example.dean.calliper.app;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
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
 * Activities that contain this fragment must implement the
 * {@link OnCalibrationChangedListener} interface
 * to handle interaction events.
 * Use the {@link CalibrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalibrationFragment extends Fragment implements ServiceConnection {

    private OnCalibrationChangedListener mListener;
    private int cal_0;
    private int cal_50;
    private MetaWearBoard mwBoard;
    private Gpio gpioModule;

    public CalibrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalibrationFragment.
     */
    public static CalibrationFragment newInstance() {
        return new CalibrationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calibration, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SeekBar seekBar0 = (SeekBar) view.findViewById(R.id.seekBar_0);
        final SeekBar seekBar50 = (SeekBar) view.findViewById(R.id.seekBar_50);


        view.findViewById(R.id.cal0_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpioModule.routeData().fromAnalogIn(MeasureActivity.pinADC, Gpio.AnalogReadMode.ADC).stream("pin0_adc").commit().onComplete(new AsyncOperation.CompletionHandler<RouteManager>() {
                    @Override
                    public void success(RouteManager result) {
                        result.subscribe("pin0_adc", new RouteManager.MessageHandler() {
                            @Override
                            public void process(Message message) {

                                Short signal = message.getData(Short.class);
                                seekBar0.setProgress(signal);
                            }
                        });
                    }
                });
                gpioModule.setDigitalOut(MeasureActivity.pinSource);
                gpioModule.readAnalogIn(MeasureActivity.pinADC, Gpio.AnalogReadMode.ADC);
                gpioModule.clearDigitalOut(MeasureActivity.pinSource);
            }
        });

        view.findViewById(R.id.cal50_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpioModule.routeData().fromAnalogIn(MeasureActivity.pinADC, Gpio.AnalogReadMode.ADC).stream("pin0_adc").commit().onComplete(new AsyncOperation.CompletionHandler<RouteManager>() {
                    @Override
                    public void success(RouteManager result) {
                        boolean worked = result.subscribe("pin0_adc", new RouteManager.MessageHandler() {
                            @Override
                            public void process(Message message) {

                                Short signal = message.getData(Short.class);
                                seekBar50.setProgress(signal);
                            }
                        });
                    }
                });
                gpioModule.setDigitalOut(MeasureActivity.pinSource);
                gpioModule.readAnalogIn(MeasureActivity.pinADC, Gpio.AnalogReadMode.ADC);
                gpioModule.clearDigitalOut(MeasureActivity.pinSource);
            }
        });


        seekBar0.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cal_0 = progress;
                ((TextView) view.findViewById(R.id.field_cal_0)).setText(String.format("%d", progress));
                if (((SeekBar) view.findViewById(R.id.seekBar_50)).getProgress() >= cal_0)
                    ((SeekBar) view.findViewById(R.id.seekBar_50)).setProgress(cal_0 - 1);
                if (mListener != null) {
                    mListener.onCalibrationChanged(cal_50, cal_0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBar50.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cal_50 = progress;
                ((TextView) view.findViewById(R.id.field_cal_50)).setText(Integer.toString(progress));
                if (((SeekBar) view.findViewById(R.id.seekBar_0)).getProgress() <= cal_50) {
                    ((SeekBar) view.findViewById(R.id.seekBar_0)).setProgress(cal_50 + 1);
                }
                if (mListener != null) {
                    mListener.onCalibrationChanged(cal_50, cal_0);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        context.getApplicationContext().bindService(new Intent(context, MetaWearBleService.class), this, Context.BIND_AUTO_CREATE);
        try {
            mListener = (OnCalibrationChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCalibrationChangedListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mwBoard = ((MetaWearBleService.LocalBinder) service).getMetaWearBoard(mListener.getBtDevice());
        try {
            gpioModule = mwBoard.getModule(Gpio.class);

        } catch (UnsupportedModuleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCalibrationChangedListener {
        BluetoothDevice getBtDevice();
        void onCalibrationChanged(int cal50mm, int cal0mm);
    }

}
