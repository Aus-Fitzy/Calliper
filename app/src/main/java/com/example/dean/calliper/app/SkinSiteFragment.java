package com.example.dean.calliper.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SkinSiteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SkinSiteFragment extends Fragment {

    //    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private ArrayAdapter<String> listAdaptor;

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

        String[] someSites = new String[]{"Site 1", "Site 2", "Site 3"};
        ArrayList<String> siteArrayList = new ArrayList<String>();

        siteArrayList.addAll(Arrays.asList(someSites));

        listAdaptor = new ArrayAdapter<String>(getActivity(), R.layout.skinsite_row, R.id.skinSite_text, siteArrayList);
        listView.setAdapter(listAdaptor);

        //view.setOnClickListener((View.OnClickListener) getActivity());
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
