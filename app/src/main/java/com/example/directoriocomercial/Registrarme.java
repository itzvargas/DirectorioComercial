package com.example.directoriocomercial;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Adapters.MenuAdapterC;
import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class Registrarme extends Fragment implements View.OnClickListener {

    Spinner eventos;
    TextView info;
    Button registro,buscar;
    String[] informacion;
    String[] nombres;

    ArrayAdapter<String> adNombres;
    int[] ids;
    private SharedPreferences userPref;
    String token;
    int idUsuario;

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
        buscar = (Button)rootview.findViewById(R.id.btn_buscarEvento);
        registro.setOnClickListener(this);
        buscar.setOnClickListener(this);
        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token", "");
        idUsuario = userPref.getInt("id",0);
        llenarSpinner();
        return rootview;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_registrarme){
            if(eventos.getSelectedItemPosition()==0){
                Toast.makeText(getContext(), "Selecciona un evento.",Toast.LENGTH_LONG).show();
            }
            else {
                registrarme();
            }
        }
        if(v.getId() == R.id.btn_buscarEvento){
            if(eventos.getSelectedItemPosition()==0){
                Toast.makeText(getContext(), "Selecciona un evento.",Toast.LENGTH_LONG).show();
            }
            else {
                info.setText(informacion[eventos.getSelectedItemPosition()]);
            }
        }
    }

    public void llenarSpinner(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.EVENTOS, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray evento = new JSONArray(String.valueOf(object.getJSONArray("eventos")));
                    nombres = new String[evento.length()+1];
                    ids = new int[evento.length()+1];
                    informacion = new String[evento.length()+1];
                    informacion[0] = "No hay";
                    nombres[0]="Selecciona un evento";
                    ids[0] = 0;
                    for (int i = 0; i<evento.length(); i++){
                        JSONObject post = evento.getJSONObject(i);
                        String cadena = "";
                        ids[i+1] = post.getInt("id");
                        nombres[i+1] = post.getString("titulo");
                        cadena += "Tipo: " + post.getString("tipo") +"\n";
                        if(!post.getString("descripcion").equals("null"))
                            cadena += post.getString("descripcion") +"\n";
                        if(!post.getString("asistentes").equals("null"))
                            cadena += "Lugares disponibles: " + post.getInt("asistentes") +"\n";
                        cadena += "Horario: " + post.getString("horario") +"\n";
                        cadena += "Fecha: " + post.getString("fecha_inicio");
                        if(!post.getString("fecha_final").equals("null"))
                            cadena += " - " + post.getString("fecha_final");
                        cadena += "\nLugar: " + post.getString("lugar");
                        informacion[i+1] = cadena;
                    }
                    adNombres = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,nombres);
                    eventos.setAdapter(adNombres);
                }
                else{
                }
            }
            catch (JSONException e){
                Toast.makeText(getContext(), "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    public void registrarme(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTRAR_EVENTO, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(getContext(), "Gracias por registrarte.",Toast.LENGTH_LONG).show();
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
                map.put("evento_id",ids[eventos.getSelectedItemPosition()]+"");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
