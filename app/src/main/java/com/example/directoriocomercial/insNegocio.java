package com.example.directoriocomercial;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
    int id_domicilio[] = {R.id.edt_calle,R.id.edt_noInt,R.id.edt_noExt,R.id.edt_colonia,R.id.edt_codigoPos,R.id.edt_municipio,R.id.edt_estado};
    int id_contacto[] = {R.id.edt_emailNeg,R.id.edt_telefonoNeg,R.id.edt_horario};
    EditText pagina,faceN,instaN;
    //EditText propietario[] = new EditText[5];
    EditText negocio[] = new EditText[4];
    EditText domicilio[] = new EditText[7];
    EditText contacto[] = new EditText[3];
    Button inscr;
    TextView seleccionar;
    ImageView logo;
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
        seleccionar = (TextView)rootView.findViewById(R.id.seleccionarLogo);
        logo = (ImageView)rootView.findViewById(R.id.logoNeg);
        inscr.setOnClickListener(this);
        sociales.setOnClickListener(this);
        seleccionar.setOnClickListener(this);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
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
                noI = domicilio[1].getText().toString();
                noE = domicilio[2].getText().toString();
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

                if(!denom.isEmpty() && !giro.isEmpty() &&
                        !calle.isEmpty() && !noI.isEmpty() && !colonia.isEmpty() && !codigo.isEmpty() && !munic.isEmpty() && !estado.isEmpty() &&
                        !emailN.isEmpty() && !telefonoN.isEmpty()){
                    guardarDatos();
                }
                else{
                    Toast.makeText(getContext(), "Faltan campos por llenar",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.seleccionarLogo:
                cargarImagen();
                break;
        }
    }

    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicación"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode == 10){
           Uri path = data.getData();
           logo.setImageURI(path);
           try {
               bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), path);
           }
           catch (IOException e){

           }
           Toast.makeText(getContext(), "Imagen seleccionada.",Toast.LENGTH_LONG).show();
        }
    }

    public void guardarDatos(){
        dialog.setMessage("Inscribiendo Negocio");
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
                    logo.setImageResource(R.drawable.imagenlogo);
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
            Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
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
                map.put("user_id",userPref.getInt("id",0)+"");
                map.put("denominacion_soc",denom);
                //slug
                map.put("image",bitmapToString(bitmap));
                map.put("giro",giro);
                //if(!descrip.isEmpty())
                map.put("descripcion",descrip);
                map.put("principales_prod",producto);
                map.put("autorizado",0+"");
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

    private String bitmapToString(Bitmap bitmap) {
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
    }
}
