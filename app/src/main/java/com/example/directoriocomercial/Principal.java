package com.example.directoriocomercial;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Principal extends Fragment {


    public Principal() {
        // Required empty public constructor
    }


    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_principal, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_principal));
        return rootView;
    }

}
