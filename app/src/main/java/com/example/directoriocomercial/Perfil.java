package com.example.directoriocomercial;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import Adapters.MenuAdapterR;
import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment implements View.OnClickListener {

    CheckBox editar;
    TextView email;
    EditText nombre, telefono, face;
    Button editarP;
    private ProgressDialog dialog;
    private SharedPreferences userPref;
    int idUsuario;
    String token;

    public Perfil() {
        // Required empty public constructor
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_perfil, container, false);
        email = (TextView)rootView.findViewById(R.id.txt_perfilCorreo);
        editar = (CheckBox)rootView.findViewById(R.id.chk_perfilEditar);
        nombre = (EditText)rootView.findViewById(R.id.edt_perfilNombre);
        telefono = (EditText)rootView.findViewById(R.id.edt_perfilTelefono);
        face = (EditText)rootView.findViewById(R.id.edt_perfilFace);
        editarP = (Button)rootView.findViewById(R.id.btn_perfilEditar);

        editar.setOnClickListener(this);
        editarP.setOnClickListener(this);

        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        idUsuario = userPref.getInt("id", 0);
        token = userPref.getString("token","");

        dialog = ProgressDialog.show(getContext(),null,null);
        dialog.setContentView(R.layout.progressbar);
        mostrarInfo();

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!nombre.getText().toString().isEmpty()){
                    nombre.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!telefono.getText().toString().isEmpty()){
                    telefono.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.chk_perfilEditar:
                if(editar.isChecked()){
                    nombre.setEnabled(true);
                    telefono.setEnabled(true);
                    face.setEnabled(true);
                    editarP.setVisibility(View.VISIBLE);
                }
                else{
                    nombre.setEnabled(false);
                    telefono.setEnabled(false);
                    face.setEnabled(false);
                    editarP.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btn_perfilEditar:
                if(validate()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("¿Estás seguro de modificar la información?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editarPerfil();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nombre.setEnabled(false);
                            telefono.setEnabled(false);
                            face.setEnabled(false);
                            editarP.setVisibility(View.INVISIBLE);
                            editar.setChecked(false);
                        }
                    });
                    builder.show();
                }
                break;
        }
    }

    private boolean validate(){
        if(nombre.getText().toString().isEmpty()){
            nombre.setError("Nombre requerido");
            return false;
        }
        if(telefono.getText().toString().isEmpty()){
            telefono.setError("Teléfono requerido");
            return false;
        }
        if(telefono.getText().toString().length() < 10){
            telefono.setError("Teléfono debe ser igual a 10 o 12 caracteres");
            return false;
        }
        if(telefono.getText().toString().length() == 11 ){
            telefono.setError("Teléfono debe ser igual a 10 o 12 caracteres");
            return false;
        }
        return true;
    }

    public void mostrarInfo(){
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.PERFIL_USER+idUsuario, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                //if(object.getBoolean("success")){
                    //JSONObject usuario = object.getJSONObject("user");
                    email.setText(object.getString("email"));
                    nombre.setText(object.getString("name"));
                    telefono.setText(object.getString("telefono"));
                    if(!object.getString("facebook").equals("null"))
                        face.setText(object.getString("facebook"));
                /*}
                else{
                    Toast.makeText(getContext(), "Intentelo más tarde",Toast.LENGTH_LONG).show();
                }*/
                dialog.dismiss();
            }
            catch (JSONException e){
                Toast.makeText(getContext(), e.getMessage()+"",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        },error -> {
            Toast.makeText(getContext(), error.getMessage()+"",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    public void editarPerfil(){
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.PUT, Constant.PERFIL_USER+idUsuario+"/update", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    nombre.setEnabled(false);
                    telefono.setEnabled(false);
                    face.setEnabled(false);
                    editarP.setVisibility(View.INVISIBLE);
                    editar.setChecked(false);
                    Toast.makeText(getContext(), "se ha modificado con éxito",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Intentelo más tarde",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
            catch (JSONException e){
                Toast.makeText(getContext(), e.getMessage()+"",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        },error -> {
            Toast.makeText(getContext(), error.getMessage()+"",Toast.LENGTH_LONG).show();
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
                map.put("name",nombre.getText().toString()+"");
                map.put("telefono",telefono.getText().toString()+"");
                map.put("facebook",face.getText().toString()+"");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
