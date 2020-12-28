package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

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

public class Splash extends AppCompatActivity {

    private SharedPreferences userPref;
    boolean tokenU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(verificacion()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },3000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();
                    Intent intent = new Intent(Splash.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            },3000);
        }
    }

    String email, pass;
    public boolean verificacion(){
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(userPref.getBoolean("isLoggedIn",false)){
            email = userPref.getString("email", "");
            pass = userPref.getString("password", "");
            SharedPreferences.Editor editor = userPref.edit();
            editor.clear();
            editor.apply();
            editor.commit();
            login();
            return true;
        }
        else{
            return false;
        }
    }

    public void login(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.USERS,response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject user = new JSONObject(String.valueOf(object.getJSONObject("user")));
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putInt("id",user.getInt("id"));
                    editor.putString("name",user.getString("name"));
                    editor.putString("telefono",user.getString("telefono"));
                    editor.putString("email",user.getString("email"));
                    editor.putString("password",pass);
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                }
                else{
                }
            }
            catch (JSONException e){
            }
        },error -> {
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("email",email);
                map.put("password",pass);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Splash.this);
        queue.add(request);
    }
}
