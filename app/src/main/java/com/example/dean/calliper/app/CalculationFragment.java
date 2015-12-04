package com.example.dean.calliper.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculationFragment extends Fragment {

    private TextView site3, site7;

    public CalculationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        site3 = (TextView) getActivity().findViewById(R.id.site3_value);
        site7 = (TextView) getActivity().findViewById(R.id.site7_value);
    }

    public void updateCalculation(ArrayList<HashMap<String, Object>> data) {
        int sum3 = 0, sum7 = 0;
        float bf;
        int age = 30;

        //Abdomen, Suprailiac, Triceps
        // bf% = (0.39287 * sum3) - (0.00105 * sum3^2) + (0.15772 * age)
        for (HashMap<String, Object> sitemap : data) {
            if (sitemap.get("site") == (getString(R.string.site_abdominal)) ||
                    sitemap.get("site") == (getString(R.string.site_suprailiac)) ||
                    sitemap.get("site") == (getString(R.string.site_triceps)))
                sum3 += (int) sitemap.get("measurement");
        }
        bf = (float) ((0.39287 * sum3) - (0.00105 * sum3 * sum3) + (0.15772 * age));
        Log.i("TAGGGGGGGGGG", String.format("BF = %f", bf));
    }
}
