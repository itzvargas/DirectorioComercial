package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.directoriocomercial.Negocio;
import com.example.directoriocomercial.R;

import java.util.ArrayList;

public class MenuAdapterC extends ArrayAdapter<Negocio.MenuC> {
    private Context context;
    private ArrayList<Negocio.MenuC> datos;

    public MenuAdapterC(Context context, ArrayList<Negocio.MenuC> datos) {
        super(context, R.layout.activity_item_comentario, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.activity_item_comentario, null);
        ImageView foto = (ImageView) item.findViewById(R.id.iv_usuario);
        foto.setImageResource(datos.get(position).getFoto());
        TextView nombre = (TextView) item.findViewById(R.id.tv_nombreComent);
        nombre.setText(datos.get(position).getNombre());
        TextView fecha = (TextView) item.findViewById(R.id.tv_fechaComent);
        fecha.setText(datos.get(position).getFecha());
        TextView coment = (TextView) item.findViewById(R.id.tv_coment);
        coment.setText(datos.get(position).getComentario());
        TextView eliminar = (TextView) item.findViewById(R.id.tv_eliminar);
        eliminar.setText(datos.get(position).getEliminar());
        if (datos.get(position).getEliminar().equals(""))
            eliminar.setVisibility(View.INVISIBLE);
        RatingBar ratingBar = (RatingBar)item.findViewById(R.id.rbar_comentarioU);
        ratingBar.setRating((float) datos.get(position).getValor());
        return item;
    }
}