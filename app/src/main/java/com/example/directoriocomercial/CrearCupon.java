package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class CrearCupon extends AppCompatActivity {

    private SharedPreferences userPref;
    int idNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cupon);
        setTitle("Crear cupón");
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        idNegocio = getIntent().getIntExtra("ID_N",0);
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
                intent = new Intent(CrearCupon.this, EditarNegocio.class);
                intent.putExtra("ID_N",idNegocio);
                startActivity(intent);
                finish();
                break;
            case R.id.negocio_promocion:
                intent = new Intent(CrearCupon.this, CrearPromocion.class);
                intent.putExtra("ID_N",idNegocio);
                startActivity(intent);
                finish();
                break;
            case R.id.negocio_cupon:
                intent = new Intent(CrearCupon.this, CrearCupon.class);
                intent.putExtra("ID_N",idNegocio);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
