package com.hardbug.productos.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hardbug.productos.R;
import com.hardbug.productos.model.Herramientas;
import com.hardbug.productos.model.UserType;

import java.util.ArrayList;

public class CustomAdapterHerramientas extends BaseAdapter {

    private Context context;
    private ArrayList<Herramientas> herramientas;

    public CustomAdapterHerramientas(Context context, ArrayList<Herramientas> herramientas){
        this.context = context;
        this.herramientas = herramientas;
    }

    @Override
    public int getCount() {
        return this.herramientas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.herramientas.get(position);
    }

    @Override
    public long getItemId(int id) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomAdapterHerramientas.HolderViewTU holderView;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_herramientas,
                    parent, false);
            holderView = new CustomAdapterHerramientas.HolderViewTU(convertView);
            convertView.setTag(holderView);
        }else{
            holderView = (CustomAdapterHerramientas.HolderViewTU) convertView.getTag();
        }

        Herramientas lista = herramientas.get(position);
        holderView.code_herrCon.setText(lista.getCode());
        return convertView;
    }

    private static class HolderViewTU{
        private final TextView code_herrCon;
        public HolderViewTU(View view){
            code_herrCon = view.findViewById(R.id.code_herrCon);
        }
    }
}
