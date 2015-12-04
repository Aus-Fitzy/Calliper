package com.example.dean.calliper.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 **/

public class SkinSiteFragment extends Fragment {

    private ListView listView;
    private SimpleAdapter listAdaptor;
    private ArrayList<HashMap<String, Object>> data;

    public SkinSiteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_skin_site, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.skinSite_list);

        data = new ArrayList<HashMap<String, Object>>();
        String[] from = {"site", "measurement"};
        int[] to = {R.id.skin_site_text, R.id.skin_site_measurement};

        data.add(putMap(from, getString(R.string.site_abdominal), 0));
        data.add(putMap(from, getString(R.string.site_suprailiac), 0));
        data.add(putMap(from, getString(R.string.site_triceps), 0));
        data.add(putMap(from, getString(R.string.site_chest), 0));
        data.add(putMap(from, getString(R.string.site_thigh), 0));
        data.add(putMap(from, getString(R.string.site_midaxillary), 0));
        data.add(putMap(from, getString(R.string.site_subscapular), 0));

        listAdaptor = new SimpleAdapter(getActivity(), data, R.layout.skinsite_row, from, to);
        listAdaptor.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view.getId() == R.id.skin_site_measurement) {
                    TextView text = (TextView) view;
                    text.setText(String.format("%s mm", textRepresentation));
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(listAdaptor);

    }

    private HashMap<String, Object> putMap(String[] from, String first, int second) {
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put(from[0], first);
        res.put(from[1], second);
        return res;
    }

    public void updateMeasurement(int result) {
        int pos = listView.getCheckedItemPosition();
        if (pos != AbsListView.INVALID_POSITION) {
            //view modal to data modal for pos
            pos = (int) listAdaptor.getItemId(pos);
            HashMap<String, Object> inner = data.get(pos);
            inner.put("measurement", result);
            listAdaptor.notifyDataSetChanged();
        }
    }

    public ArrayList<HashMap<String, Object>> getData() {
        return data;
    }
}
