package com.example.directoriocomercial;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class insNegocio extends Fragment implements CheckBox.OnClickListener {

    CheckBox sociales;
    //int id_propietario[] = {R.id.edt_nombreUsuario,R.id.edt_telefonoUsuario,R.id.edt_emailUsuario,R.id.edt_fechaNacUsuario,R.id.edt_faceUsuario};
    int id_negocio[] = {R.id.edt_denominacion,R.id.edt_giro,R.id.edt_descripcion,R.id.edt_productos};
    int id_domicilio[] = {R.id.edt_calle,R.id.edt_noExt,R.id.edt_noInt,R.id.edt_colonia,R.id.edt_codigoPos,R.id.edt_municipio,R.id.edt_estado};
    int id_contacto[] = {R.id.edt_emailNeg,R.id.edt_telefonoNeg,R.id.edt_horario};
    EditText pagina,faceN,instaN;
    //EditText propietario[] = new EditText[5];
    EditText negocio[] = new EditText[4];
    EditText domicilio[] = new EditText[7];
    EditText contacto[] = new EditText[3];
    Button inscr;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;

    //Propietario
    //String nombre,telefono,email,fecha,face;
    //Negocio
    String denom,giro,descrip="",producto="";
    //Domicilio
    String calle,noI,noE="",colonia,codigo,munic,estado;
    //Contacto
    String emailN,telefonoN,horario="",page="",faceNe="",instaNe="";

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
        //for (int i = 0; i<5; i++)
            //propietario[i] = (EditText) rootView.findViewById(id_propietario[i]);
        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        for (int i = 0; i<4; i++)
            negocio[i] = (EditText) rootView.findViewById(id_negocio[i]);
        for (int i = 0; i<7; i++)
            domicilio[i] = (EditText) rootView.findViewById(id_domicilio[i]);
        for (int i = 0; i<3; i++)
            contacto[i] = (EditText) rootView.findViewById(id_contacto[i]);
        sociales = (CheckBox)rootView.findViewById(R.id.chk_redes);
        pagina = (EditText)rootView.findViewById(R.id.edt_paginaW);
        faceN = (EditText)rootView.findViewById(R.id.edt_faceNeg);
        instaN = (EditText)rootView.findViewById(R.id.edt_instaNeg);
        inscr = (Button)rootView.findViewById(R.id.btn_inscribir);
        inscr.setOnClickListener(this);
        sociales.setOnClickListener(this);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        negocio[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!negocio[0].getText().toString().isEmpty()){
                    negocio[0].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        negocio[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!negocio[1].getText().toString().isEmpty()){
                    negocio[1].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[0].getText().toString().isEmpty()){
                    domicilio[0].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[1].getText().toString().isEmpty()){
                    domicilio[1].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[3].getText().toString().isEmpty()){
                    domicilio[3].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[4].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[4].getText().toString().isEmpty()){
                    domicilio[4].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[5].getText().toString().isEmpty()){
                    domicilio[5].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[6].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[6].getText().toString().isEmpty()){
                    domicilio[6].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contacto[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!contacto[0].getText().toString().isEmpty()){
                    contacto[0].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contacto[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!contacto[1].getText().toString().isEmpty()){
                    contacto[1].setError(null);
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
            case R.id.chk_redes:
                if(sociales.isChecked()) {
                    pagina.setVisibility(View.VISIBLE);
                    faceN.setVisibility(View.VISIBLE);
                    instaN.setVisibility(View.VISIBLE);
                }
                else {
                    pagina.setVisibility(View.INVISIBLE);
                    faceN.setVisibility(View.INVISIBLE);
                    instaN.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btn_inscribir:
                /*nombre = propietario[0].getText().toString();
                telefono = propietario[1].getText().toString();
                email = propietario[2].getText().toString();
                fecha = propietario[3].getText().toString();
                face = propietario[4].getText().toString();*/

                denom = negocio[0].getText().toString();
                giro = negocio[1].getText().toString();
                descrip = negocio[2].getText().toString();
                producto = negocio[3].getText().toString();

                calle = domicilio[0].getText().toString();
                noE = domicilio[1].getText().toString();
                noI = domicilio[2].getText().toString();
                colonia = domicilio[3].getText().toString();
                codigo = domicilio[4].getText().toString();
                munic = domicilio[5].getText().toString();
                estado = domicilio[6].getText().toString();

                emailN = contacto[0].getText().toString();
                telefonoN = contacto[1].getText().toString();
                horario = contacto[2].getText().toString();

                page = pagina.getText().toString();
                faceNe = faceN.getText().toString();
                instaNe = instaN.getText().toString();

                if(validate()){
                    guardarDatos();
                }
                break;
        }
    }

    private boolean validate(){
        if(denom.isEmpty()){
            negocio[0].setError("Denominación requerida");
            return false;
        }
        if(giro.isEmpty()){
            negocio[1].setError("Giro requerido");
            return false;
        }
        if(calle.isEmpty()){
            domicilio[0].setError("Calle requerida");
            return false;
        }
        if(noE.isEmpty()){
            domicilio[1].setError("Numero exterior requerido");
            return false;
        }
        if(colonia.isEmpty()){
            domicilio[3].setError("Colonia requerida");
            return false;
        }
        if(codigo.isEmpty()){
            domicilio[4].setError("Código postal requerido");
            return false;
        }
        if(codigo.length() != 5){
            domicilio[4].setError("Código postal debe ser igual a 5 caracteres");
            return false;
        }
        if(munic.isEmpty()){
            domicilio[5].setError("Municipio requerido");
            return false;
        }
        if(estado.isEmpty()){
            domicilio[6].setError("Estado requerido");
            return false;
        }
        if(emailN.isEmpty()){
            contacto[0].setError("Email requerido");
            return false;
        }
        if(telefonoN.isEmpty()){
            contacto[1].setError("Teléfono requerido");
            return false;
        }
        if(telefonoN.length() < 10){
            contacto[1].setError("Teléfono debe ser igual a 10 o 12 caracteres");
            return false;
        }
        if(telefonoN.length() == 11 ){
            contacto[1].setError("Teléfono debe ser igual a 10 o 12 caracteres");
            return false;
        }
        return true;
    }

    public void guardarDatos(){
        dialog.setMessage("Inscribiendo negocio");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.INSCRIBIR,response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    for (int i = 0; i<4; i++)
                        negocio[i].setText("");
                    for (int i = 0; i<7; i++)
                        domicilio[i].setText("");
                    for (int i = 0; i<3; i++)
                        contacto[i].setText("");
                    sociales.setChecked(false);
                    pagina.setText("");
                    faceN.setText("");
                    instaN.setText("");
                    Toast.makeText(getContext(), "Tu información será revisada. ¡Gracias!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Hubo un error, revisa tu información e intenta de nuevo.",Toast.LENGTH_LONG).show();
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
                String token = userPref.getString("token","");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("token",userPref.getString("token",""));
                //map.put("user_id",userPref.getInt("id",0)+"");
                map.put("denominacion_soc",denom);
                //slug
                map.put("giro",giro);
                //if(!descrip.isEmpty())
                map.put("descripcion",descrip);
                map.put("principales_prod",producto);
                //map.put("autorizado",0+"");
                map.put("calle",calle);
                map.put("no_ext",noE);
                //if(!noI.isEmpty())
                map.put("no_int",noI);
                map.put("colonia",colonia);
                map.put("cp",codigo);
                map.put("municipio",munic);
                map.put("estado",estado);
                map.put("email",emailN);
                map.put("telefono",telefonoN);
                map.put("web",page);
                //if(!horario.isEmpty())
                map.put("horario",horario);
                //if(!faceNe.isEmpty())
                map.put("facebook",faceNe);
                //if(!instaNe.isEmpty())
                map.put("instagram",instaNe);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    /*
    private String bitmapToString(Bitmap bitmap) {
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
    }*/
}
