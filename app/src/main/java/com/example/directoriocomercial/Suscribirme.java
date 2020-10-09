package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Suscribirme extends AppCompatActivity {

    String ruta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscribirme);
        setTitle(getString(R.string.menu_suscribirme));

        Bundle bolsaR = getIntent().getExtras();
    }
}
