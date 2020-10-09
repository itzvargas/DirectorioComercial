package com.example.directoriocomercial;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;


/**
 * A simple {@link Fragment} subclass.
 */
public class insNegocio extends Fragment implements CheckBox.OnClickListener {

    CheckBox sociales;
    EditText pagina,faceN,instaN;
    //URL para google maps del negocio
    //https://maps.google.com/?q=
    public insNegocio() {
        // Required empty public constructor
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ins_negocio, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_inscribir));
        sociales = (CheckBox)rootView.findViewById(R.id.chk_redes);
        pagina = (EditText)rootView.findViewById(R.id.edt_paginaW);
        faceN = (EditText)rootView.findViewById(R.id.edt_faceNeg);
        instaN = (EditText)rootView.findViewById(R.id.edt_instaNeg);
        sociales.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.chk_redes){
            pagina.setVisibility(View.VISIBLE);
            faceN.setVisibility(View.VISIBLE);
            instaN.setVisibility(View.VISIBLE);
        }
        if(v.getId() == getFragmentManager().getBackStackEntryCount()){
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
        }
    }


}
