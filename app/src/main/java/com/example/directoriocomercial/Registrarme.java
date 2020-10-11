package com.example.directoriocomercial;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Registrarme extends Fragment implements View.OnClickListener {

    Spinner eventos;
    TextView info;
    Button registro;

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
        eventos = (Spinner)rootview.findViewById(R.id.sp_eventos);
        info = (TextView)rootview.findViewById(R.id.txt_info_evento);
        registro = (Button)rootview.findViewById(R.id.btn_registrarme);
        registro.setOnClickListener(this);
        return rootview;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_registrarme){

        }
    }
}
