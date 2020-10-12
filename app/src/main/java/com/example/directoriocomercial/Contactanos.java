package com.example.directoriocomercial;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contactanos extends Fragment implements View.OnClickListener {


    TextView telefono,whats,page,face,correo;
    public Contactanos() {
        // Required empty public constructor
    }

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_contactanos, container, false);

        telefono = (TextView)rootview.findViewById(R.id.txt_telefono);
        whats = (TextView)rootview.findViewById(R.id.txt_whats);
        page = (TextView)rootview.findViewById(R.id.txt_pagina);
        face = (TextView)rootview.findViewById(R.id.txt_face);
        correo = (TextView)rootview.findViewById(R.id.txt_correo);

        telefono.setOnClickListener(this);
        whats.setOnClickListener(this);
        page.setOnClickListener(this);
        face.setOnClickListener(this);
        correo.setOnClickListener(this);
        return rootview;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String ruta;
        Intent int1;
        switch (id){
            case R.id.txt_telefono:
                String numero = "7353947436";
                try{
                    String dial = "tel:" +numero;
                    if(ActivityCompat.checkSelfPermission(
                            getContext(), Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]
                                { Manifest.permission.CALL_PHONE,},1000);
                    }else{
                    };
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
                catch (Exception e){
                    Toast.makeText(getContext(),"Vuelve a intentarlo.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txt_whats:
                String number = "+52 7351907488";
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.txt_pagina:
                ruta = "https://www.ca.ccesian.com";
                int1 = new Intent(Intent.ACTION_VIEW, Uri.parse(ruta));
                startActivity(int1);
                break;
            case R.id.txt_face:
                String FACEBOOK_URL = "https://www.facebook.com/compraaquiorg";
                String FACEBOOK_PAGE_ID = "compraaquiorg";
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
            case R.id.txt_correo:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","contacto@compraaqui.org", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android APP - ");
                startActivity(Intent.createChooser(emailIntent,"Enviar con..."));
                break;
        }
    }
}
