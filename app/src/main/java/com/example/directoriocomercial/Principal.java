package com.example.directoriocomercial;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Principal extends Fragment implements View.OnClickListener {


    public Principal() {
        // Required empty public constructor
    }

    Button directorio, descargar;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_principal, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_principal));
        directorio = (Button)rootView.findViewById(R.id.btn_diectorioO);
        descargar = (Button)rootView.findViewById(R.id.btn_descargar);
        directorio.setOnClickListener(this);
        descargar.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.btn_diectorioO){
            intent = new Intent(getContext(), Negocios.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.btn_descargar){
            Toast.makeText(getContext(), "Descargando...", Toast.LENGTH_LONG).show();
        }
    }
}
