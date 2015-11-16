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

    //    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private SimpleAdapter listAdaptor;
    private ArrayList<HashMap<String, Object>> data;
    //private TextView

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


        data.add(putMap(from, "Abdominal", 0));
        data.add(putMap(from, "Triceps", 0));
        data.add(putMap(from, "Chest", 0));
        data.add(putMap(from, "Midaxillary", 0));
        data.add(putMap(from, "Subscapular", 0));
        data.add(putMap(from, "Suprailiac ", 0));
        data.add(putMap(from, "Thigh", 0));


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
        //listAdaptor = new ArrayAdapter<String>(getActivity(), R.layout.skinsite_row, R.id.skinSite_text, siteArrayList);
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
            inner.put("measurement", String.format("%d", result));
            listAdaptor.notifyDataSetChanged();
        }
    }

    /*    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }*/

}
