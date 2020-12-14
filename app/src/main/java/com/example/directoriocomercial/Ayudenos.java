package com.example.directoriocomercial;


import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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

    EditText comentario,asun;
    Button enviar;
    private SharedPreferences userPref;
    String nom,em,telefono;
    private ProgressDialog dialog;

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
        asun = (EditText)rootview.findViewById(R.id.edt_asunto);
        enviar = (Button)rootview.findViewById(R.id.btn_enviarCU);
        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        nom = userPref.getString("name", "");
        em = userPref.getString("email", "");
        telefono = userPref.getString("telefono", "");
        enviar.setOnClickListener(this);

        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        comentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!comentario.getText().toString().isEmpty()){
                    comentario.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        asun.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!asun.getText().toString().isEmpty()){
                    asun.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return rootview;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_enviarCU){
            if(validate()){
                enviarComentario();
            }
        }
    }

    public boolean validate(){
        if(asun.getText().toString().isEmpty()){
            asun.setError("Asunto requerido");
            return false;
        }
        if(comentario.getText().toString().isEmpty()){
            comentario.setError("Comentario requerido");
            return false;
        }
        return true;
    }

    public void enviarComentario(){
        dialog.setMessage("Enviando...");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.AYUDANOS_A_MEJORAR, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    asun.setText("");
                    comentario.setText("");
                    Toast.makeText(getContext(), "Gracias por tu comentario.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
            catch (JSONException e){
                Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        },error -> {
            Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("nombre",nom);
                map.put("email",em);
                map.put("telefono",telefono);
                map.put("asunto",asun.getText().toString());
                map.put("comentario",comentario.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
