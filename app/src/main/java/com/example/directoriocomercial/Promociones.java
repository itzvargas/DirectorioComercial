package com.example.directoriocomercial;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class Promociones extends Fragment implements AdapterView.OnItemClickListener {

    ListView lista;
    private ArrayList<MenuAdapterPromociones.MenuPromo> menu;
    private MenuAdapterPromociones adapter;

    public Promociones() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_promociones, container, false);
        lista = (ListView)rootview.findViewById(R.id.lv_promociones);
        menu=new ArrayList<MenuAdapterPromociones.MenuPromo>();
        mostrarInfo();
        return rootview;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void mostrarInfo(){
        //Metodo para mostrar las promociones
        StringRequest request = new StringRequest(Request.Method.GET, Constant.MOSTRAR_PROMOS, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray promociones = new JSONArray(String.valueOf(object.getJSONArray("promociones")));

                    if(promociones.length() == 0)
                        Toast.makeText(getContext(), "No hay promociones",Toast.LENGTH_LONG).show();
                    else {
                        for (int j = 0; j < promociones.length(); j++) {
                            JSONObject post = promociones.getJSONObject(j);
                            menu.add(new MenuAdapterPromociones.MenuPromo(post.getString("image")+"", post.getString("titulo") + "",
                                    post.getString("descripcion")+"", post.getString("fechaVigencia")+"","",
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
        },error -> {
            Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_LONG).show();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
