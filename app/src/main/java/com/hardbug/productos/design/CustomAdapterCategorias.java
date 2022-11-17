package com.hardbug.productos.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hardbug.productos.R;
import com.hardbug.productos.model.ListaHerramientas;

import java.util.ArrayList;

public class CustomAdapterCategorias extends BaseAdapter {

    private final Context context;
    private final ArrayList<ListaHerramientas> listaHerramientas;

    public CustomAdapterCategorias(Context context, ArrayList<ListaHerramientas> listaProds) {
        this.context = context;
        this.listaHerramientas = listaProds;
    }

    @Override
    public int getCount() {
        return listaHerramientas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaHerramientas.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.cardview_categorias,
                    parent, false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        }else{
            holderView = (HolderView) convertView.getTag();
        }

        ListaHerramientas lista = listaHerramientas.get(position);
        holderView.categorianame.setText(lista.getNomprod());
        return convertView;
    }

    private static class HolderView{
        private final TextView categorianame;

        public HolderView(View view){
            categorianame = view.findViewById(R.id.nombre_prodcategoria);
        }
    }
}
