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
        site3 = (TextView) view.findViewById(R.id.site3_value);
        site7 = (TextView) view.findViewById(R.id.site7_value);
    }

    public void updateCalculation(ArrayList<HashMap<String, Object>> data) {
        int sum3 = 0, sum7 = 0;
        float bf3, bf7;
        int age = 30;

        // Abdomen, Suprailiac, Triceps
        // bf3% = (0.39287 * sum3) - (0.00105 * sum3^2) + (0.15772 * age)

        // bd7 density = (1.112 – (0.00043499 * sum7) + (0.00000055 * sum7^2) – (0.00028826 * age)
        // bf7% = (4.95 ÷ bd7) – 4.50 for a white 18-59yo male
        for (HashMap<String, Object> sitemap : data) {
            sum7 += (int) sitemap.get("measurement");
            if (sitemap.get("site") == (getString(R.string.site_abdominal)) ||
                    sitemap.get("site") == (getString(R.string.site_suprailiac)) ||
                    sitemap.get("site") == (getString(R.string.site_triceps)))
                sum3 += (int) sitemap.get("measurement");
        }
        bf3 = (float) ((0.39287 * sum3) - (0.00105 * sum3 * sum3) + (0.15772 * age));
        bf7 = (float) (1.112 - (0.00043499 * sum7) + (0.00000055 * sum7 * sum7) - (0.00028826 * age));
        bf7 = (float) (4.95 / bf7 - 4.5);
        bf7 *= 100;

        Log.i("TAGGGGGGGGGG", String.format("BF3 = %f\tBF7 = %f", bf3, bf7));

        site3.setText(String.format("%.1f%%", bf3));
        site7.setText(String.format("%.1f%%", bf7));
    }
}
