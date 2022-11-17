package com.hardbug.productos.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardbug.productos.R;
import com.hardbug.productos.model.ListaUsers;
import com.hardbug.productos.model.UserType;

import java.util.ArrayList;

public class UserTypeCAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<UserType> listaProds;

    public UserTypeCAdapter(Context context, ArrayList<UserType> listaProds) {
        this.context = context;
        this.listaProds = listaProds;
    }

    @Override
    public int getCount() {
        return listaProds.size();
    }

    @Override
    public Object getItem(int position) {
        return listaProds.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserTypeCAdapter.HolderViewTU holderView;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.cardview_usuarios,
                    parent, false);
            holderView = new UserTypeCAdapter.HolderViewTU(convertView);
            convertView.setTag(holderView);
        }else{
            holderView = (UserTypeCAdapter.HolderViewTU) convertView.getTag();
        }

        UserType lista = listaProds.get(position);
        holderView.nombre_usuario.setText(lista.getName());
        return convertView;
    }

    private static class HolderViewTU{
        private final TextView nombre_usuario;

        public HolderViewTU(View view){
            nombre_usuario = view.findViewById(R.id.nombre_usuario);
        }
    }
}
