package com.example.directoriocomercial;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MisNegocios extends Fragment {

    ListView lv;

    public MisNegocios() {
        // Required empty public constructor
    }


    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mis_negocios, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_negocios));
        lv = (ListView)rootView.findViewById(R.id.lv_misnegocios);
        return rootView;
    }

}
