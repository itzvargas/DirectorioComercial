package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class Negocios extends AppCompatActivity implements View.OnClickListener {

    ListView negocios;
    EditText buscar;
    Button bBuscar;
    String palabra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios);
        setTitle(R.string.menu_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        negocios = (ListView)findViewById(R.id.lv_negocios);
        buscar = (EditText)findViewById(R.id.edt_buscar);
        bBuscar = (Button)findViewById(R.id.btn_buscar);
    }

    @Override
    public void onClick(View v) {
        if(buscar.getText().toString() != ""){

        }
        else{
            Toast.makeText(this, "Escribe lo que deseas buscar",Toast.LENGTH_LONG).show();
        }
    }
}
