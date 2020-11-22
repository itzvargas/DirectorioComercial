package com.example.directoriocomercial;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import clases.Constant;


public class misEventos extends Fragment implements AdapterView.OnClickListener{

    Spinner sp_misEventos;
    Button buscar_mievento, eliminar;
    TextView infomievento;
    String[] informacion;
    String[] nombres;

    ArrayAdapter<String> adNombres;
    int[] ids;
    private SharedPreferences userPref;
    String token;
    int idUsuario;
    public misEventos() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_mis_eventos, container, false);
        sp_misEventos = (Spinner) rootview.findViewById(R.id.sp_miseventos2);
        buscar_mievento = (Button)rootview.findViewById(R.id.btn_buscarmiEvento2);
        eliminar = (Button)rootview.findViewById(R.id.btn_eliminarRegistro);
        infomievento = (TextView)rootview.findViewById(R.id.txt_info_mievento2);
        buscar_mievento.setOnClickListener(this);
        eliminar.setOnClickListener(this);

        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token", "");
        idUsuario = userPref.getInt("id",0);
        llenarMisEventos();

        return rootview;
    }

    public void llenarMisEventos(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.MOSTRAR_MIS_EVENTO+idUsuario+"/eventos", response -> {
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
                    sp_misEventos.setAdapter(adNombres);
                }
                else{
                    Toast.makeText(getContext(), "No tienes eventos.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(getContext(), "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
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

    public void eliminarRegistro(){
        StringRequest request = new StringRequest(Request.Method.DELETE, Constant.REGISTRAR_EVENTO+ids[sp_misEventos.getSelectedItemPosition()]+"/delete", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(getContext(), "Registro eliminado.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "No tienes eventos.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(getContext(), "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_buscarmiEvento2){
            infomievento.setText(informacion[sp_misEventos.getSelectedItemPosition()]);
        }
        if(v.getId() == R.id.btn_eliminarRegistro){
            eliminarRegistro();
        }
    }
}
