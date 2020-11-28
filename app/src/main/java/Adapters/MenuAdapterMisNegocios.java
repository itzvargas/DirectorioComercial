package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.directoriocomercial.MisNegocios;
import com.example.directoriocomercial.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import clases.Constant;


public class MenuAdapterMisNegocios extends ArrayAdapter<MisNegocios.MenuM> {
    private Context context;
    private ArrayList<MisNegocios.MenuM> datos;

    public MenuAdapterMisNegocios(Context context, ArrayList<MisNegocios.MenuM> datos) {
        super(context, R.layout.activity_item_minegocio, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.activity_item_minegocio, null);


        // A partir de la vista, recogeremos los controles que contiene para
        // poder manipularlos.
        // Recogemos el ImageView y le asignamos una foto.
        ImageView foto = (ImageView) item.findViewById(R.id.img_negocioEditar2);
        foto.setImageResource(datos.get(position).getFoto());

        // Recogemos el TextView para mostrar el nombre y establecemos el
        // nombre.
        TextView nombre = (TextView) item.findViewById(R.id.tv_nombre2);
        nombre.setText(datos.get(position).getNombre());

        // Recogemos el TextView para mostrar el número de celda y lo
        // establecemos.
        TextView giro = (TextView) item.findViewById(R.id.tv_giro2);
        giro.setText(datos.get(position).getGiro());

        if(!datos.get(position).getURL().equals("null"))
            Picasso.get().load(Constant.FOTO+datos.get(position).getURL()).into(foto);

        // Devolvemos la vista para que se muestre en el ListView.
        return item;
    }
}