package com.example.dean.calliper.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCalibrationChangedListener} interface
 * to handle interaction events.
 * Use the {@link CalibrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalibrationFragment extends Fragment {

    private OnCalibrationChangedListener mListener;
    private int cal_max;
    private int cal_min;

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
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar_max);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cal_max = progress;
                ((TextView) view.findViewById(R.id.field_cal_max)).setText(String.format("%d", progress));
                if (((SeekBar) view.findViewById(R.id.seekBar_min)).getProgress() > cal_max)
                    ((SeekBar) view.findViewById(R.id.seekBar_min)).setProgress(cal_max);
                if (mListener != null) {
                    mListener.onCalibrationChanged(cal_min, cal_max);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar = (SeekBar) view.findViewById(R.id.seekBar_min);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cal_min = progress;
                ((TextView) view.findViewById(R.id.field_cal_min)).setText(Integer.toString(progress));
                if (((SeekBar) view.findViewById(R.id.seekBar_max)).getProgress() < cal_min) {
                    ((SeekBar) view.findViewById(R.id.seekBar_max)).setProgress(cal_min);
                }
                if (mListener != null) {
                    mListener.onCalibrationChanged(cal_min, cal_max);
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
        void onCalibrationChanged(int min, int max);
    }

}
