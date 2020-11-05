package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import bd.AyudanteBD;
import bd.Contacto;
import bd.Negocio;
import bd.Direccion;

public class Splash extends AppCompatActivity {

    AyudanteBD aBD;
    Negocio neg;
    Direccion direc;
    Contacto contact;

    SQLiteDatabase db=null;
    String url = "http://elsitioKOCE.com/base";
    String denominacion, giro, descripcion,principales_Prod;
    int id,autorizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        actualizacion();
        boolean verif = verificacion();
        if(verif == true) {
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
                    Intent intent = new Intent(Splash.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            },3000);
        }
    }

    public boolean verificacion(){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return userPref.getBoolean("isLoggedIn",false);
    }

    public void actualizacion(){
        SQLiteDatabase db2=null;
        SQLiteDatabase db3=null;
        SQLiteDatabase db4=null;
        try{
            neg =new Negocio(this,"Directorio",null,1);
            direc =new Direccion(this,"Directorio",null,1);
            contact =new Contacto(this,"Directorio",null,1);
            db2 = neg.getReadableDatabase();
            db3 = direc.getReadableDatabase();
            db4 = contact.getReadableDatabase();
            if (db2!=null || db3!=null || db4!=null) {
                neg.onUpgrade(db2,0,1);
                direc.onUpgrade(db3,0,1);
                contact.onUpgrade(db4,0,1);
            }//if
            else {

            }
            db2.close();
            db3.close();
            db4.close();
        }//try
        catch (Exception e) {
            Toast.makeText(this, "Sin conexión a Internet",Toast.LENGTH_LONG).show();
        }//catch
    }

}
