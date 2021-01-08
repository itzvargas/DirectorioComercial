package Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.directoriocomercial.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import clases.Constant;

public class MenuAdapterPromociones extends ArrayAdapter<MenuAdapterPromociones.MenuPromo> {

    private Context context;
    private ArrayList<MenuPromo> datos;

    public MenuAdapterPromociones(Context context, ArrayList<MenuAdapterPromociones.MenuPromo> datos) {
        super(context, R.layout.activity_item_promocion, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.datos = datos;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.activity_item_promocion, null);
        ImageView foto = (ImageView) item.findViewById(R.id.img_itemBanner);
        if(!datos.get(position).getFoto().equals("null")) {
            Picasso.get().load(Constant.FOTO + datos.get(position).getFoto()).into(foto);
            foto.setVisibility(View.VISIBLE);
        }
        else {
            foto.cancelDragAndDrop();
        }
        TextView titulo = (TextView) item.findViewById(R.id.txt_itemTituloPromo);
        titulo.setText(datos.get(position).getTitulo());
        TextView descripcion = (TextView) item.findViewById(R.id.txt_itemDescPromo);
        descripcion.setText(datos.get(position).getDescripcion());
        if (!datos.get(position).getFechaV().equals(""))
            descripcion.append("\nFecha de vigencia:"+datos.get(position).getFechaV());
        TextView codigo = (TextView) item.findViewById(R.id.txt_itemCodigoPromo);
        if(!datos.get(position).getCodigo().equals("")) {
            codigo.setText(datos.get(position).getCodigo());
            codigo.setVisibility(View.VISIBLE);
        }
        TextView neg = (TextView) item.findViewById(R.id.txt_itemNegocioPromo);
        neg.setText(datos.get(position).getNegocio());
        return item;
    }

    public static class MenuPromo {
        private String foto;
        private String titulo;
        private String descripcion;
        private String fechaV;
        private String codigo;
        private String negocio;

        public MenuPromo(String foto, String titulo, String descripcion, String fechaV, String codigo, String negocio) {
            this.foto = foto;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.fechaV = fechaV;
            this.codigo = codigo;
            this.negocio = negocio;
        }

        public String getTitulo() {
            return titulo;
        }
        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }
        public String getFoto() { return foto; }
        public void setFoto(String foto) { this.foto = foto; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public String getFechaV() { return fechaV; }
        public void setFechaV(String fechaV) { this.fechaV = fechaV; }
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        public String getNegocio() { return negocio; }
        public void setNegocio(String negocio) { this.negocio = negocio; }
    }
}

