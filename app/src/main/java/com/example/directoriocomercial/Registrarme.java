package com.example.directoriocomercial;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Registrarme extends Fragment {


    public Registrarme() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_registrarme, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_registrarme));
        return rootview;
    }

}
