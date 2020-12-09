package com.example.directoriocomercial;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bd.AyudanteBD;
import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cerrar extends Fragment {

    private SharedPreferences userPref;
    String token;

    public Cerrar() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_cerrar, container, false);
        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Cerrar Sesión");
        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().hide();
        }
        userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Estás seguro de cerrar sesión?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cerrarSesion();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder.show();
        return rootview;
    }

    public void cerrarSesion(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.USERS_LOGAOUT, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                    ((MainActivity)getContext()).finish();
                }
            }
            catch (JSONException e){
                SharedPreferences.Editor editor = userPref.edit();
                editor.clear();
                editor.apply();
                editor.commit();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                ((MainActivity)getContext()).finish();
            }
        },error -> {
            SharedPreferences.Editor editor = userPref.edit();
            editor.clear();
            editor.apply();
            editor.commit();
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
            ((MainActivity)getContext()).finish();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("token",token);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

}
