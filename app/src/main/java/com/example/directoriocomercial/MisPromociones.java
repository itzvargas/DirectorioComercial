package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MisPromociones extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String op;
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_promociones);
        op = getIntent().getStringExtra("OPCION");
        if(op.equals("Promocion"))
            setTitle("Mis promociones");
        else
            setTitle("Mis cupones");
        lista = (ListView)findViewById(R.id.lv_miCuponera);
        lista.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
