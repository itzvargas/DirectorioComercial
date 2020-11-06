package com.example.directoriocomercial;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.MenuAdapterMisNegocios;
import bd.Negocio;
import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class MisNegocios extends Fragment implements AdapterView.OnItemClickListener {

    ListView lv;
    private SharedPreferences userPref;
    int idUsuario;
    int[] ids;
    String token;
    private ArrayList<MenuM> menu;
    private MenuAdapterMisNegocios adapterMisNegocios;

    public MisNegocios() {
        // Required empty public constructor
    }


    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mis_negocios, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_negocios));
        lv = (ListView)rootView.findViewById(R.id.lv_misnegocios);
        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        token = userPref.getString("token", "");
        idUsuario = userPref.getInt("id",0);
        menu = new ArrayList<MenuM>();
        lv.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bolsa = new Bundle();
        bolsa.putInt("ID_N",ids[position]);
        //Enviarlo al próximo intent
    }

    public void lista(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.MIS_NEGOCIOS, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray negocio = new JSONArray(String.valueOf(object.getJSONArray("negocios")));
                    ids = new int[negocio.length()];
                    for (int i = 0; i<negocio.length(); i++){
                        JSONObject post = negocio.getJSONObject(i);
                        ids[i] = post.getInt("id");
                        menu.add(new MenuM(R.drawable.tienda, post.getString("denominacion_soc"),
                                post.getString("giro"),"Editar"));
                    }
                    adapterMisNegocios = new MenuAdapterMisNegocios(getContext(), menu);
                    lv.setAdapter(adapterMisNegocios);
                }
                else{
                    Toast.makeText(getContext(), "No tienes negocios inscritos.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(getContext(), "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("token",token);
                map.put("user_id",idUsuario+"");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    //Elaboración de la lista
    public class MenuM {
        private int foto;
        private String nombre;
        private String giro;
        private String editar;

        public MenuM(int foto, String nombre, String giro, String editar) {
            this.foto = foto;
            this.nombre = nombre;
            this.giro = giro;
            this.editar = editar;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getGiro() {
            return giro;
        }

        public void setGiro(String giro) { this.giro = giro; }

        public int getFoto() { return foto; }

        public void setFoto(int foto) { this.foto = foto; }

        public String getEditar() {
            return editar;
        }

        public void setEditar(String editar) {
            this.editar = editar;
        }

    }
}
