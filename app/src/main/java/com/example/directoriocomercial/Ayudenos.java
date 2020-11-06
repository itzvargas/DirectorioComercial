package com.example.directoriocomercial;


import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class Ayudenos extends Fragment implements View.OnClickListener {

    EditText comentario;
    Button enviar;
    private SharedPreferences userPref;
    String token;
    int idUsuario;

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
        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token", "");
        idUsuario = userPref.getInt("id",0);
        enviar.setOnClickListener(this);
        return rootview;
    }

    @Override
    public void onClick(View v) {
        String com;
        if(v.getId() == R.id.btn_enviarCU){
            com = comentario.getText().toString();
            if(!com.isEmpty()){
                enviarComentario();
            }
            else{
                Toast.makeText(getContext(), "Escribe tu comentario",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void enviarComentario(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.AYUDANOS_LOGIN, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    comentario.setText("");
                    Toast.makeText(getContext(), "Gracias por tu comentario.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(getContext(), "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                map.put("user_id",idUsuario+"");
                map.put("comentario",comentario.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
