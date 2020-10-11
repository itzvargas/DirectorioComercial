package com.example.directoriocomercial;


import android.app.UiAutomation;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class Ayudenos extends Fragment implements View.OnClickListener {

    EditText comentario;
    Button enviar;

    public Ayudenos() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_ayudenos, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_ayudanos));
        comentario = (EditText)rootview.findViewById(R.id.edt_comentarioU);
        enviar = (Button)rootview.findViewById(R.id.btn_enviarCU);
        enviar.setOnClickListener(this);
        return rootview;
    }

    @Override
    public void onClick(View v) {
        String com;
        if(v.getId() == R.id.btn_enviarCU){
            com = comentario.getText().toString();
            if(!com.isEmpty()){
                comentario.setText("");
                Toast.makeText(getContext(), "Gracias por tu comentario",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getContext(), "Escribe tu comentario",Toast.LENGTH_LONG).show();
            }
        }
    }
}
