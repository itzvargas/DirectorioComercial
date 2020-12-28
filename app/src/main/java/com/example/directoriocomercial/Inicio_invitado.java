package com.example.directoriocomercial;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


public class Inicio_invitado extends AppCompatActivity  implements View.OnClickListener {

    Button direc, desc;
    String Url = "";
    String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final static int REQUEST_CODE = 1010;
    private final static int REQUEST_PERMISSION_SETTING = 1211;
    ProgressDialog progressDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_invitado);
        setTitle(getString(R.string.menu_principal));
        direc = (Button)findViewById(R.id.btn_directorioInvi);
        desc = (Button)findViewById(R.id.btn_descargarInvit);
        direc.setOnClickListener(this);
        desc.setOnClickListener(this);
        obtenerURK();
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
            //obtenerURK();
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("downloading.....");
            progressDialog.setCancelable(false);


            //TODO DownloadFile only if Permission is Granted
            if(ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                downloadFile();
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
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
                //Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
        },error -> {
            //Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){

        };
        RequestQueue queue = Volley.newRequestQueue(Inicio_invitado.this);
        queue.add(request);
    }

    public void abrirPDF(File name)
    {
        Uri path = FileProvider.getUriForFile(Inicio_invitado.this, BuildConfig.APPLICATION_ID + ".provider",name);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(Inicio_invitado.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            if(ActivityCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                //ALL OKAY
                //downloadFile();
            }
            else
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                boolean showRationale = shouldShowRequestPermissionRationale( WRITE_EXTERNAL_STORAGE );
                if (! showRationale) {
                    // user also CHECKED "never ask again"
                    //Take user to settings screen
                    Toast.makeText(getApplicationContext(),"Storage Permission is required to complete the task",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);

                } else {
                    // user did NOT check "never ask again"
                    Toast.makeText(getApplicationContext(),"Storage Permission is required to complete the task",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(Inicio_invitado.this,new String[]{WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
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
                                Toast.makeText(Inicio_invitado.this, "Descarga completa.", Toast.LENGTH_LONG).show();
                                abrirPDF(file);
                            }
                            else
                                Toast.makeText(Inicio_invitado.this, "No hay directorio comercial.", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("ERROR!!", "NOT DOWNLOADED");
                            //Toast.makeText(Inicio_invitado.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            //e.printStackTrace();
                        }
                    }
                } ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                //error.printStackTrace();
                //Toast.makeText(Inicio_invitado.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(Inicio_invitado.this, new HurlStack());
        mRequestQueue.add(request);
        progressDialog.show();
    }

}
