package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
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
public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText usuario,contra;
    Button login,invitado;
    TextView recuperar,suscribirte;
    String user,pass;
    SQLiteDatabase db=null;
    boolean email,passw;
    int id = 0;
    private ProgressDialog dialog;
    String nombre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        usuario = (EditText)findViewById(R.id.edt_usuario);
        contra = (EditText)findViewById(R.id.edt_contrasena);
        login = (Button)findViewById(R.id.btn_login);
        invitado = (Button)findViewById(R.id.btn_invitado);
        recuperar = (TextView)findViewById(R.id.txt_recuperar);
        suscribirte = (TextView)findViewById(R.id.txt_suscribirse);

        login.setOnClickListener(this);
        invitado.setOnClickListener(this);
        recuperar.setOnClickListener(this);
        suscribirte.setOnClickListener(this);

        dialog = new ProgressDialog(Login.this);
        dialog.setCancelable(false);

        usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!usuario.getText().toString().isEmpty()){
                    usuario.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!contra.getText().toString().isEmpty()){
                    contra.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private boolean validate(){
        if(user.isEmpty()){
            usuario.setError("Email requerido");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user).matches()){
            usuario.setError("Email invalido");
            return false;
        }
        if(pass.isEmpty()){
            contra.setError("Contraseña requerida");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        user = usuario.getText().toString().trim();
        pass = contra.getText().toString();
        Intent intent;
        Bundle bolsa = new Bundle();
        switch (v.getId()) {
            case R.id.btn_login:
                if(validate()) {
                    bolsa.putString("USER",user);
                    bolsa.putString("PASS",pass);
                    verificacion();
                }
                break;
            case R.id.btn_invitado:
                intent = new Intent(Login.this, Inicio_invitado.class);
                startActivity(intent);
                finish();
                break;
            case R.id.txt_recuperar:
                intent = new Intent(Login.this, Recuperacion.class);
                startActivity(intent);
                finish();
                break;
            case R.id.txt_suscribirse:
                intent = new Intent(Login.this, Suscribirme.class);
                bolsa.putString("RUTA","login");
                intent.putExtras(bolsa);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void registrarBDLOGIN(){
        Intent intent;
        Toast.makeText(this, "Bienvenido " + nombre,Toast.LENGTH_SHORT).show();
        intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void verificacion(){
        dialog.setMessage("Verificando");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.USERS,response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject user = new JSONObject(String.valueOf(object.getJSONObject("user")));
                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("user",Login.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putInt("id",user.getInt("id"));
                    id = user.getInt("id");
                    editor.putString("name",user.getString("name"));
                    editor.putString("telefono",user.getString("telefono"));
                    editor.putString("email",user.getString("email"));
                    editor.putString("password",pass);
                    nombre = user.getString("name");
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                    registrarBDLOGIN();
                }
                else{
                    Toast.makeText(this, "Correo o contraseña incorrectos.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        },error -> {
            Toast.makeText(this, "Correo o contraseña incorrectos.",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("email",user);
                map.put("password",pass);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(request);
    }
}
