package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

import clases.Constant;

public class EditarNegocio extends AppCompatActivity implements View.OnClickListener {

    CheckBox sociales;
    //int id_propietario[] = {R.id.edt_nombreUsuario,R.id.edt_telefonoUsuario,R.id.edt_emailUsuario,R.id.edt_fechaNacUsuario,R.id.edt_faceUsuario};
    int id_negocio[] = {R.id.edt_denominacionEditar,R.id.edt_giroEditar,R.id.edt_descripcionEditar,R.id.edt_productosEditar};
    int id_domicilio[] = {R.id.edt_calleEditar,R.id.edt_noExtEditar,R.id.edt_noIntEditar,R.id.edt_coloniaEditar,R.id.edt_codigoPosEditar,R.id.edt_municipioEditar,R.id.edt_estadoEditar};
    int id_contacto[] = {R.id.edt_emailNegEditar,R.id.edt_telefonoNegEditar,R.id.edt_horarioEditar};
    EditText pagina,faceN,instaN;
    //EditText propietario[] = new EditText[5];
    EditText negocio[] = new EditText[4];
    EditText domicilio[] = new EditText[7];
    EditText contacto[] = new EditText[3];
    Button inscr;
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
    int idNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_negocio);
        setTitle("Editar mi negocio");
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        idNegocio = getIntent().getIntExtra("ID_N",0);
        for (int i = 0; i<4; i++)
            negocio[i] = (EditText)findViewById(id_negocio[i]);
        for (int i = 0; i<7; i++)
            domicilio[i] = (EditText)findViewById(id_domicilio[i]);
        for (int i = 0; i<3; i++)
            contacto[i] = (EditText)findViewById(id_contacto[i]);
        sociales = (CheckBox)findViewById(R.id.chk_redesEditar);
        pagina = (EditText)findViewById(R.id.edt_paginaWEditar);
        faceN = (EditText)findViewById(R.id.edt_faceNegEditar);
        instaN = (EditText)findViewById(R.id.edt_instaNegEditar);
        inscr = (Button)findViewById(R.id.btn_editar);

        mostrarDatos();

        inscr.setOnClickListener(this);
        sociales.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.opciones_negocios, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.negocio_editar:
                intent = new Intent(EditarNegocio.this, EditarNegocio.class);
                intent.putExtra("ID_N",idNegocio);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.chk_redesEditar:
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
            case R.id.btn_editar:
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

                if(!denom.isEmpty() && !giro.isEmpty() &&
                        !calle.isEmpty() && !noE.isEmpty() && !colonia.isEmpty() && !codigo.isEmpty() && !munic.isEmpty() && !estado.isEmpty() &&
                        !emailN.isEmpty() && !telefonoN.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("¿Estás seguro de modificar la información?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editarDatos();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
                else{
                    Toast.makeText(this, "Faltan campos por llenar",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void mostrarDatos(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.NEGOCIO_INDIVIDUAL+idNegocio, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject neg = object.getJSONObject("negocio");
                    JSONObject contact = neg.getJSONObject("contacto");
                    JSONObject direcc = neg.getJSONObject("direccion");

                    negocio[0].setText((neg.getString("denominacion_soc") + ""));
                    negocio[1].setText((neg.getString("giro") + ""));
                    if(!(neg.getString("descripcion") + "").equals("null"))
                        negocio[2].setText((neg.getString("descripcion")));
                    if(!(neg.getString("principales_prod") + "").equals("null"))
                        negocio[3].setText((neg.getString("principales_prod")));

                    domicilio[0].setText((direcc.getString("calle") + ""));
                    domicilio[1].setText((direcc.getString("no_ext") + ""));
                    if(!(direcc.getString("no_int") + "").equals("null"))
                        domicilio[2].setText((direcc.getString("no_int")));
                    domicilio[3].setText((direcc.getString("colonia") + ""));
                    domicilio[4].setText((direcc.getString("cp") + ""));
                    domicilio[5].setText((direcc.getString("municipio") + ""));
                    domicilio[6].setText((direcc.getString("estado") + ""));

                    contacto[0].setText((contact.getString("email") + ""));
                    contacto[1].setText((contact.getString("telefono") + ""));
                    if(!(contact.getString("horario") + "").equals("null"))
                        contacto[2].setText((contact.getString("horario")));

                    if(!(contact.getString("web") + "").equals("null"))
                        pagina.setText((contact.getString("web")));
                    if(!(contact.getString("facebook") + "").equals("null"))
                        faceN.setText((contact.getString("facebook")));
                    if(!(contact.getString("instagram") + "").equals("null"))
                        instaN.setText((contact.getString("instagram")));
                }
                else{
                    Toast.makeText(this, "No hay",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, e.getMessage()+"",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(this, error.getMessage()+"",Toast.LENGTH_LONG).show();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(EditarNegocio.this);
        queue.add(request);
    }

    public void editarDatos(){
        dialog.setMessage("Editando Negocio");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.PUT, Constant.EDITAR_NEGOCIO+idNegocio+"/update", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Se modificó con éxito.",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Hubo un error, revisa tu información e intenta de nuevo.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }, error -> {
            Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show();
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
        RequestQueue queue = Volley.newRequestQueue(EditarNegocio.this);
        queue.add(request);
    }
}
