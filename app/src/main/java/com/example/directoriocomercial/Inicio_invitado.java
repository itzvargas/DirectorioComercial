package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

public class Inicio_invitado extends AppCompatActivity  implements View.OnClickListener {

    Button direc, desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_invitado);
        setTitle(getString(R.string.menu_principal));
        direc = (Button)findViewById(R.id.btn_directorioInvi);
        desc = (Button)findViewById(R.id.btn_descargarInvit);
        direc.setOnClickListener(this);
        desc.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            case R.id.menu_suscribirme:
                Bundle bolsa = new Bundle();
                bolsa.putString("RUTA","inicio_invitado");
                intent = new Intent(Inicio_invitado.this, Suscribirme.class);
                intent.putExtras(bolsa);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_contactar:
                intent = new Intent(Inicio_invitado.this, Contactanos2.class);
                startActivity(intent);
                break;
            case R.id.menu_ayudanos:
                intent = new Intent(Inicio_invitado.this, Ayudanos2.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_ayuda:
                intent = new Intent(Inicio_invitado.this, Ayuda2.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.btn_directorioInvi){
            intent = new Intent(Inicio_invitado.this, Negocios.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.btn_descargarInvit){
            Toast.makeText(this, "Descargando...", Toast.LENGTH_LONG).show();
        }
    }
}
