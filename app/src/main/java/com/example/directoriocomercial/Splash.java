package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import bd.AyudanteBD;

public class Splash extends AppCompatActivity {

    AyudanteBD aBD;
    SQLiteDatabase db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        try{
            aBD=new AyudanteBD(this,"Directorio",null,1);
            db = aBD.getReadableDatabase();
            if (db!=null) {
                Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE actividad='activo'",null);
                if (cursor.moveToNext()){
                    cursor.close();
                    db.close();
                    return true;
                }
                else{
                    cursor.close();
                    db.close();
                    return false;
                }
            }//if
            else {
                return false;
            }
        }//try
        catch (Exception e) { return false; }//catch
    }
}
