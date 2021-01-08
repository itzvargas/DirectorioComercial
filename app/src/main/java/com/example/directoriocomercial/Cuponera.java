package com.example.directoriocomercial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;



public class Cuponera extends Fragment {

    FragmentTabHost tabhost;

    public Cuponera() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_cuponera, container, false);
        tabhost= (FragmentTabHost) rootview.findViewById(android.R.id.tabhost);
        tabhost.setup(getContext(), getActivity().getSupportFragmentManager(),android.R.id.tabcontent);
        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("Promocion"), Promociones.class, null);
        tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("Cupon"), Cupones.class, null);
        tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("Administrar"), AdministrarCupones.class, null);
        return rootview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_cuponera, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getContext(), MisPromociones.class);
        switch (item.getItemId()){
            //Aquí pones tantos 'case' como items tengas en el menú
            case R.id.promocion_eliminar:
                intent.putExtra("OPCION","Promocion");
                startActivity(intent);
                return true;
            case R.id.cupon_eliminar:
                intent.putExtra("OPCION","Cupon");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
