package com.example.directoriocomercial;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import clases.Constant;
import clases.InputStreamVolleyRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class Principal extends Fragment implements View.OnClickListener {

    String Url;
    String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final static int REQUEST_CODE = 1010;
    private final static int REQUEST_PERMISSION_SETTING = 1211;
    ProgressDialog progressDialog;
    Context context;

    public Principal() {
        // Required empty public constructor
    }

    Button directorio, descargar;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_principal, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_principal));
        directorio = (Button)rootView.findViewById(R.id.btn_diectorioO);
        descargar = (Button)rootView.findViewById(R.id.btn_descargar);
        directorio.setOnClickListener(this);
        descargar.setOnClickListener(this);
        obtenerURK();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.btn_diectorioO){
            intent = new Intent(getContext(), Negocios.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.btn_descargar){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("downloading.....");
            progressDialog.setCancelable(false);


            //TODO DownloadFile only if Permission is Granted
            if(ActivityCompat.checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                downloadFile();
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
    }

    public void obtenerURK(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.URL_DESCARGA, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                Url = object.getString("url");
                //Toast.makeText(this, Url,Toast.LENGTH_LONG).show();
                //descargar();

            }
            catch (JSONException e){
                Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(getContext(), "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){

        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    public void abrirPDF(File name)
    {
        Uri path = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",name);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(getContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            if(ActivityCompat.checkSelfPermission(getContext().getApplicationContext(),WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                //ALL OKAY
                //downloadFile();
            }
            else
            if (ActivityCompat.checkSelfPermission(getContext().getApplicationContext(),WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                boolean showRationale = shouldShowRequestPermissionRationale( WRITE_EXTERNAL_STORAGE );
                if (! showRationale) {
                    // user also CHECKED "never ask again"
                    //Take user to settings screen
                    Toast.makeText(getContext().getApplicationContext(),"Storage Permission is required to complete the task",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);

                } else {
                    // user did NOT check "never ask again"
                    Toast.makeText(getContext().getApplicationContext(),"Storage Permission is required to complete the task",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(getActivity(),new String[]{WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
                }
            }
        }
    }

    private void downloadFile() {
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, Constant.DESCARGA+Url,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        progressDialog.dismiss();
                        // TODO handle the response
                        try {
                            if (response!=null) {
                                String name="DirectorioComercial.pdf";
                                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                path.mkdirs();
                                File file = new File(path,name);
                                FileOutputStream stream = new FileOutputStream(file);
                                try {
                                    if(file.exists()) {
                                        file.delete();
                                        stream.close();
                                    }
                                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                    path.mkdirs();
                                    file = new File(path,name);
                                    stream = new FileOutputStream(file);
                                    stream.write(response);
                                } finally {
                                    stream.close();
                                }
                                Toast.makeText(getContext(), "Download complete.", Toast.LENGTH_LONG).show();
                                abrirPDF(file);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("ERROR!!", "NOT DOWNLOADED");
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                error.printStackTrace();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext(), new HurlStack());
        mRequestQueue.add(request);
        progressDialog.show();
    }
}
