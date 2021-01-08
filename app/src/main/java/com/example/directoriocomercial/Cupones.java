package com.example.directoriocomercial;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.MenuAdapterPromociones;
import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cupones extends Fragment {

    ListView lista;
    private ArrayList<MenuAdapterPromociones.MenuPromo> menu;
    private MenuAdapterPromociones adapter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressDialog dialog;

    public Cupones() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_cupones, container, false);
        lista = (ListView)rootview.findViewById(R.id.lv_cupones);
        menu=new ArrayList<MenuAdapterPromociones.MenuPromo>();
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        mostrarInfo();
        refreshLayout = rootview.findViewById(R.id.swipeCupon);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                lista.setAdapter(null);
                mostrarInfo();
            }
        });
        return rootview;
    }

    public void mostrarInfo(){
        //Metodo para mostrar los cupones
        dialog.setMessage("Cargando cupones");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.MOSTRAR_CUPONES, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray promociones = new JSONArray(String.valueOf(object.getJSONArray("promociones")));

                    if(promociones.length() == 0)
                        Toast.makeText(getContext(), "No hay cupones",Toast.LENGTH_LONG).show();
                    else {
                        for (int j = 0; j < promociones.length(); j++) {
                            JSONObject post = promociones.getJSONObject(j);
                            menu.add(new MenuAdapterPromociones.MenuPromo(post.getString("image")+"", post.getString("titulo") + "",
                                    post.getString("descripcion")+"", post.getString("fechaVigencia")+"",post.getString("codigo")+"",
                                    post.getString("denominacion_soc")+""));
                        }
                        adapter = new MenuAdapterPromociones(getContext(), menu);
                        lista.setAdapter(adapter);
                    }
                }
                else{
                    Toast.makeText(getContext(), "Intentelo mas tarde",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
            refreshLayout.setRefreshing(false);
        },error -> {
            Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_LONG).show();
            dialog.dismiss();
            refreshLayout.setRefreshing(false);
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
