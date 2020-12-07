package com.example.directoriocomercial;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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
    //TextView info;
    Button registro,buscar;
    //String[] informacion;
    String[] tipo;
    String[] desc;
    String[] horario;
    String[] fecha;
    String[] lugar;
    String[] nombres;
    String[] foto;
    ImageView img_even;
    TableLayout tabla;
    TextView descripcion;

    ArrayAdapter<String> adNombres;
    int[] ids;
    private SharedPreferences userPref;
    String token;

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
        //info = (TextView)rootview.findViewById(R.id.txt_info_evento);
        tabla = (TableLayout)rootview.findViewById(R.id.tabla_info_evento);
        registro = (Button)rootview.findViewById(R.id.btn_registrarme);
        buscar = (Button)rootview.findViewById(R.id.btn_buscarEvento);
        img_even = (ImageView)rootview.findViewById(R.id.im_evento);
        descripcion = (TextView)rootview.findViewById(R.id.txt_descripcion_evento);
        registro.setOnClickListener(this);
        buscar.setOnClickListener(this);
        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token", "");
        //idUsuario = userPref.getInt("id",0);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Estás seguro de registrarte?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registrarme();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        }
        if(v.getId() == R.id.btn_buscarEvento){
            img_even.setVisibility(View.INVISIBLE);
            descripcion.setText("");
            int count = tabla.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = tabla.getChildAt(i);
                if (child instanceof TableRow)
                    ((ViewGroup) child).removeAllViews();
            }
            if(eventos.getSelectedItemPosition()==0){
                Toast.makeText(getContext(), "Selecciona un evento.",Toast.LENGTH_LONG).show();
            }
            else {
                //info.setText(informacion[eventos.getSelectedItemPosition()]);
                tablaInformacion();
                if(!(foto[eventos.getSelectedItemPosition()]).equals("null")) {
                    Picasso.get().load(Constant.FOTO + foto[eventos.getSelectedItemPosition()]).into(img_even);
                    img_even.setVisibility(View.VISIBLE);
                }
                registro.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void tablaInformacion(){
        TableRow tbrow = new TableRow(getContext());
        TextView t1v = new TextView(getContext());
        t1v.setText("Tipo: ");
        t1v.setTextColor(Color.GRAY);
        t1v.setGravity(Gravity.LEFT);
        t1v.setTextSize(20);
        tbrow.addView(t1v);
        TextView valor = new TextView(getContext());
        valor.setTextColor(Color.BLACK);
        valor.setGravity(Gravity.LEFT);
        valor.setTextSize(20);
        //valor.setMaxWidth(100);
        valor.setText(tipo[eventos.getSelectedItemPosition()]+"");
        tbrow.addView(valor);
        tabla.addView(tbrow);

        if(!desc[eventos.getSelectedItemPosition()].equals("null")) {
            descripcion.setText(desc[eventos.getSelectedItemPosition()]+"");
            /*
            TableRow tbrow2 = new TableRow(getContext());
            TextView t2v = new TextView(getContext());
            t2v.setText("Descripcion: ");
            t2v.setTextSize(20);
            t2v.setTextColor(Color.GRAY);
            t2v.setGravity(Gravity.LEFT);
            tbrow2.addView(t2v);
            TextView valor2 = new TextView(getContext());
            valor2.setTextColor(Color.BLACK);
            valor2.setTextSize(20);
            //valor2.setMaxWidth(150);
            valor2.setGravity(Gravity.LEFT);
            valor2.setText(desc[eventos.getSelectedItemPosition()]+"");
            tbrow2.addView(valor2);
            tabla.addView(tbrow2);*/
        }

        TableRow tbrow3 = new TableRow(getContext());
        TextView t3v = new TextView(getContext());
        t3v.setText("Horario: ");
        t3v.setTextColor(Color.GRAY);
        t3v.setGravity(Gravity.LEFT);
        t3v.setTextSize(20);
        tbrow3.addView(t3v);
        TextView valor3 = new TextView(getContext());
        valor3.setTextColor(Color.BLACK);
        valor3.setGravity(Gravity.LEFT);
        valor3.setTextSize(20);
        //valor3.setMaxWidth(100);
        valor3.setText(horario[eventos.getSelectedItemPosition()]+"");
        tbrow3.addView(valor3);
        tabla.addView(tbrow3);

        TableRow tbrow4 = new TableRow(getContext());
        TextView t4v = new TextView(getContext());
        t4v.setText("Fecha: ");
        t4v.setTextColor(Color.GRAY);
        t4v.setTextSize(20);
        t4v.setGravity(Gravity.LEFT);
        tbrow4.addView(t4v);
        TextView valor4 = new TextView(getContext());
        valor4.setTextColor(Color.BLACK);
        valor4.setTextSize(20);
        valor4.setGravity(Gravity.LEFT);
        //valor4.setMaxWidth(100);
        valor4.setText(fecha[eventos.getSelectedItemPosition()]+"");
        tbrow4.addView(valor4);
        tabla.addView(tbrow4);

        TableRow tbrow5 = new TableRow(getContext());
        TextView t5v = new TextView(getContext());
        t5v.setText("Lugar: ");
        t5v.setTextColor(Color.GRAY);
        t5v.setTextSize(20);
        t5v.setGravity(Gravity.LEFT);
        tbrow5.addView(t5v);
        TextView valor5 = new TextView(getContext());
        valor5.setTextColor(Color.BLACK);
        valor5.setGravity(Gravity.LEFT);
        valor5.setTextSize(20);
        //valor5.setMaxWidth(100);
        valor5.setText(lugar[eventos.getSelectedItemPosition()]+"");
        tbrow5.addView(valor5);
        tabla.addView(tbrow5);
    }

    public void llenarSpinner(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.EVENTOS, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray evento = new JSONArray(String.valueOf(object.getJSONArray("eventos")));
                    nombres = new String[evento.length()+1];
                    ids = new int[evento.length()+1];
                    //informacion = new String[evento.length()+1];
                    tipo = new String[evento.length()+1];
                    desc = new String[evento.length()+1];
                    horario = new String[evento.length()+1];
                    fecha = new String[evento.length()+1];
                    lugar = new String[evento.length()+1];
                    foto = new String[evento.length()+1];
                    //informacion[0] = "No hay";
                    tipo[0] = "No hay";
                    desc[0] = "No hay";
                    horario[0] = "No hay";
                    fecha[0] = "No hay";
                    lugar[0] = "No hay";
                    foto[0] = "No hay";
                    nombres[0]="Selecciona un evento";
                    ids[0] = 0;
                    for (int i = 0; i<evento.length(); i++){
                        JSONObject post = evento.getJSONObject(i);
                        //String cadena = "";
                        ids[i+1] = post.getInt("id");
                        nombres[i+1] = post.getString("titulo");
                        foto[i+1] = post.getString("image");
                        tipo[i+1] = post.getString("tipo");
                        //if(!post.getString("descripcion").equals("null"))
                            desc[i+1] = post.getString("descripcion");
                        //if(!post.getString("asistentes").equals("null"))
                            //cadena += "Lugares disponibles: " + post.getInt("asistentes") +"\n";
                        horario[i+1] = post.getString("horario");
                        fecha[i+1] = post.getString("fecha_inicio");
                        if(!post.getString("fecha_final").equals("null"))
                            fecha[i+1] += " al " + post.getString("fecha_final");
                        lugar[i+1] = post.getString("lugar");
                        //informacion[i+1] = cadena;
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
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTRAR_EVENTO+ids[eventos.getSelectedItemPosition()]+"", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    registro.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Gracias por registrarte.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Ya estás registrado.",Toast.LENGTH_LONG).show();
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
