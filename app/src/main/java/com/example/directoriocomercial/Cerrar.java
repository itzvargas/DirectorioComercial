package com.example.directoriocomercial;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import bd.AyudanteBD;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cerrar extends Fragment {

    AyudanteBD aBD;
    SQLiteDatabase db=null;

    public Cerrar() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_cerrar, container, false);
        Intent intent;
        try{
            aBD=new AyudanteBD(getContext(),"Directorio",null,1);
            db = aBD.getReadableDatabase();
            if (db!=null) {
                db.execSQL("DELETE FROM usuarios WHERE actividad='activo'");
                db.close();
                intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getContext(),"Hasta luego.",Toast.LENGTH_LONG).show();
            }//if
            else
                intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getContext(),"Vuelve a intentarlo.",Toast.LENGTH_LONG).show();
        }//try
        catch (Exception e) {
            intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            Toast.makeText(getContext(),"Vuelve a intentarlo.",Toast.LENGTH_LONG).show();
        }//catch
        return rootview;
    }

}
