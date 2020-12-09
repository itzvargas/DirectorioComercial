package com.example.directoriocomercial;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.MenuAdapterContact;
import clases.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contactanos extends Fragment implements AdapterView.OnItemClickListener {

    String[][] contac;
    ListView contacto;
    TextView direccion;
    private ArrayList<MenuR> menuR;
    private MenuAdapterContact adapterR;
    private ProgressDialog dialog;
    public Contactanos() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_contactanos, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_contact));
        contacto = (ListView)rootview.findViewById(R.id.lv_contactoDirec);
        direccion = (TextView)rootview.findViewById(R.id.txt_direccionDirec);
        menuR=new ArrayList<MenuR>();
        contacto.setOnItemClickListener(this);

        dialog = ProgressDialog.show(getContext(),null,null);
        dialog.setContentView(R.layout.progressbar);
        mostrarInfo();
        return rootview;
    }

    public void mostrarInfo(){
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.CONTACTANOS_DIRECTORIO, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if (object.getBoolean("success")) {
                    JSONArray datos =  new JSONArray(String.valueOf(object.getJSONArray("contacto")));
                    contac = new String[datos.length()][2];
                    for (int i = 0; i < datos.length(); i++) {
                        JSONObject valor =  datos.getJSONObject(i);
                        contac[i][0] = valor.getString("dato");
                        contac[i][1] = valor.getString("valor");

                        if(contac[i][0].equals("Email"))
                            menuR.add(new MenuR(R.drawable.correo2, contac[i][1]+""));
                        if(contac[i][0].equals("Teléfono"))
                            menuR.add(new MenuR(R.drawable.llamada2, contac[i][1]+""));
                        if(contac[i][0].equals("WhatsApp"))
                            menuR.add(new MenuR(R.drawable.wa2, contac[i][1] + ""));
                        if(contac[i][0].equals("Web"))
                            menuR.add(new MenuR(R.drawable.web2, contac[i][1] + ""));
                        if(contac[i][0].equals("Facebook"))
                            menuR.add(new MenuR(R.drawable.face2, contac[i][1] + ""));
                        if(contac[i][0].equals("Instagram"))
                            menuR.add(new MenuR(R.drawable.insta2, contac[i][1] + ""));
                        if(valor.getString("dato").equals("Dirección"))
                            direccion.setText(contac[i][1] = valor.getString("valor"));
                    }
                    adapterR = new MenuAdapterContact(getContext(), menuR);
                    contacto.setAdapter(adapterR);
                }
                dialog.dismiss();
            }
            catch (JSONException e){
                Toast.makeText(getContext(), e.getMessage()+"",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        },error -> {
            Toast.makeText(getContext(), error.getMessage()+"",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent int1;
        int p = position+1;
        switch (contac[p][0]){
            case "Teléfono":
                String numero = contac[p][1];
                try{
                    String dial = "tel:" +numero;
                    if(ActivityCompat.checkSelfPermission(
                            getContext(), Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]
                                { Manifest.permission.CALL_PHONE,},1000);
                    }else{
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
                catch (Exception e){
                    Toast.makeText(getContext(),"Vuelve a intentarlo.", Toast.LENGTH_LONG).show();
                }
                break;
            case "WhatsApp":
                String number = contac[p][1];
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case "Web":
                int1 = new Intent(Intent.ACTION_VIEW, Uri.parse(contac[p][1]));
                startActivity(int1);
                break;
            case "Facebook":
                String FACEBOOK_URL = "https://www.facebook.com/"+contac[p][1];
                String FACEBOOK_PAGE_ID = contac[p][1];
                String uri;
                PackageManager packageManager = getContext().getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //versiones nuevas de facebook
                        uri = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    } else { //versiones antiguas de fb
                        uri = "fb://page/" + FACEBOOK_PAGE_ID;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    uri = FACEBOOK_URL; //normal web url
                }
                int1 = new Intent(Intent.ACTION_VIEW);
                int1.setData(Uri.parse(uri));
                startActivity(int1);
                break;
            case "Email":
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",contac[p][1]+"", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android APP - ");
                startActivity(Intent.createChooser(emailIntent,"Enviar con..."));
                break;
            case "Instagram":
                Uri uriI = Uri.parse("http://instagram.com/_u/"+contac[p][1]);
                Intent intent = new Intent(Intent.ACTION_VIEW, uriI);
                intent.setPackage("com.instagram.android");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //No encontró la aplicación, abre la versión web.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+contac[position][1])));
                }
                break;
        }
    }

    //Elaboración de la lista de Redes sociales
    public class MenuR {
        private int foto;
        private String nombre;

        public MenuR(int foto, String nombre) {
            this.foto = foto;
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public int getFoto() { return foto; }
        public void setFoto(int foto) { this.foto = foto; }
    }
}
