package com.example.tfg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tfg.R;
import com.example.tfg.classes.User;

import java.util.ArrayList;

public class CrossfittersManagementBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> crossfitters;
    private LayoutInflater inflater;

    public CrossfittersManagementBaseAdapter(Context context, ArrayList crossfitters){
        this.context = context;
        this.crossfitters = crossfitters;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return crossfitters.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Se genera la vista
        view = inflater.inflate(R.layout.activity_crossfitters_management_list_view, null);
        //Asigna las variables del item
        TextView username = (TextView) view.findViewById(R.id.crosfittersManagementListView_username);
        //Da valores al item
        username.setText(crossfitters.get(i).getUsername());
        //Retorna la vista de crossfiteros
        return view;
    }
}
