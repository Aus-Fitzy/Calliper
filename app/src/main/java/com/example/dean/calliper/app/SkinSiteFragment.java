package com.example.dean.calliper.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 **/

public class SkinSiteFragment extends Fragment {

    //    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private SimpleAdapter listAdaptor;
    private ArrayList<HashMap<String, String>> data;

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


        data = new ArrayList<HashMap<String, String>>();
        String[] from = {"site", "measurement"};
        int[] to = {R.id.skin_site_text, R.id.skin_site_measurement};


        data.add(putMap(from, "Site 1", "mm"));
        data.add(putMap(from, "Site 2", "mm"));
        data.add(putMap(from, "Site 3", "mm"));
        data.add(putMap(from, "Site 4", "mm"));


        listAdaptor = new SimpleAdapter(getActivity(), data, R.layout.skinsite_row, from, to);
        //listAdaptor = new ArrayAdapter<String>(getActivity(), R.layout.skinsite_row, R.id.skinSite_text, siteArrayList);
        listView.setAdapter(listAdaptor);

    }

    private HashMap<String, String> putMap(String[] from, String first, String second) {
        HashMap<String, String> res = new HashMap<String, String>();
        res.put(from[0], first);
        res.put(from[1], second);
        return res;
    }

    public void updateMeasurement(int result) {
        int viewpos = listView.getCheckedItemPosition();
        if (viewpos != AbsListView.INVALID_POSITION) {
            //TODO
            //viewpos maps to the data model directly atm, this may change
            HashMap<String, String> inner = data.get(viewpos);
            inner.put("measurement", String.format("%d mm", result));
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
