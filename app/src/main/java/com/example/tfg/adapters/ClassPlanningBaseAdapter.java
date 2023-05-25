package com.example.tfg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tfg.R;

import java.util.ArrayList;

public class ClassPlanningBaseAdapter extends BaseAdapter {

    //Variables
    private Context context;
    private ArrayList<String> daysOfWeek;
    private LayoutInflater inflater;

    public ClassPlanningBaseAdapter(Context context, ArrayList<String> daysOfWeek) {
        this.context = context;
        this.daysOfWeek = daysOfWeek;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return daysOfWeek.size();
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
        view = inflater.inflate(R.layout.activity_class_planning_list_view, null);
        //Asigna las variables del item
        TextView workoutText = (TextView) view.findViewById(R.id.classPlanning_day);
        //Da valores al item
        workoutText.setText(daysOfWeek.get(i));
        //Retorna la vista del workout
        return view;
    }
}
