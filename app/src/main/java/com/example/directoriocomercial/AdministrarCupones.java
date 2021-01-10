package com.example.directoriocomercial;


import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
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

import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdministrarCupones extends Fragment implements View.OnClickListener {


    EditText codigo, correo;
    Button envir, borrar;
    TextView mensaje;
    private ProgressDialog dialog;
    private SharedPreferences userPref;
    String token;

    public AdministrarCupones() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_administrar_cupones, container, false);
        codigo = (EditText)rootview.findViewById(R.id.edt_codigoCupon);
        correo = (EditText)rootview.findViewById(R.id.edt_correoUsCupon);
        envir = (Button)rootview.findViewById(R.id.btn_enviarCupon);
        borrar = (Button)rootview.findViewById(R.id.btn_borrarAdministrar);
        mensaje = (TextView)rootview.findViewById(R.id.txt_mensajeAdministrar);

        envir.setOnClickListener(this);
        borrar.setOnClickListener(this);

        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token","");

        codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codigo.getText().toString().isEmpty()){
                    codigo.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        correo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (correo.getText().toString().isEmpty()){
                    correo.setError(null);
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
        if (v.getId() == R.id.btn_enviarCupon){
            if(validate()){
                registrarCupon();
            }
        }
        if (v.getId() == R.id.btn_borrarAdministrar){
            codigo.setText("");
            correo.setText("");
            mensaje.setText("");
        }
    }

    public boolean validate(){
        if(codigo.getText().toString().isEmpty()){
            codigo.setError("Código requerido");
            return false;
        }
        if(correo.getText().toString().isEmpty()){
            correo.setError("Email requerido");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(correo.getText().toString()).matches()){
            correo.setError("Email invalido");
            return false;
        }
        return true;
    }

    public void registrarCupon(){
        dialog.setMessage("Canjeando cupón...");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.ADMINISTRAR_CUPON, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                String m = object.getString("message");
                if(object.getBoolean("success")){
                    mensaje.setText(m);
                }
                else {
                    mensaje.setText(m);
                }
            }
            catch (JSONException e){
                Toast.makeText(getContext(), "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }, error -> {
            Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_LONG).show();
            dialog.dismiss();
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
                map.put("codigo",codigo.getText().toString());
                map.put("email",correo.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
