package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.directoriocomercial.Contactanos2;
import com.example.directoriocomercial.R;

import java.util.ArrayList;

public class MenuAdapterContact2 extends ArrayAdapter<Contactanos2.MenuR> {
    private Context context;
    private ArrayList<Contactanos2.MenuR> datos;

    public MenuAdapterContact2(Context context, ArrayList<Contactanos2.MenuR> datos) {
        super(context, R.layout.activity_item_redes, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.activity_item_redes, null);
        ImageView foto = (ImageView) item.findViewById(R.id.iv_redS);
        foto.setImageResource(datos.get(position).getFoto());
        TextView nombre = (TextView) item.findViewById(R.id.tv_redS);
        nombre.setText(datos.get(position).getNombre());
        return item;
    }
}