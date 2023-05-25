package com.example.tfg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tfg.R;
import com.example.tfg.classes.Training;

import java.util.ArrayList;

public class ClassDayBaseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Training> trainings;
    private LayoutInflater inflater;

    public ClassDayBaseAdapter(Context context, ArrayList<Training> classes) {
        this.context = context;
        this.trainings = classes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return trainings.size();
    }

    @Override
    public Object getItem(int i) {
        return trainings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Se genera la vista
        view = inflater.inflate(R.layout.activity_class_day_list_view, null);
        //Asigna las variables de la clase
        TextView traningStarts = view.findViewById(R.id.classDay_start);
        TextView trainingEnds = view.findViewById(R.id.classDay_end);
        TextView trainingName = view.findViewById(R.id.classDay_classType);
        TextView trainingReserves = view.findViewById(R.id.classDay_reservesDone);
        TextView traningCapacity = view.findViewById(R.id.classDay_totalReserves);

        //Da valores al item
        traningStarts.setText(trainings.get(i).getTrainingStarts());
        trainingEnds.setText(trainings.get(i).getTrainingEnds());
        trainingName.setText(trainings.get(i).getName());
        trainingReserves.setText(String.valueOf(trainings.get(i).getReservesDone()));
        traningCapacity.setText(String.valueOf(trainings.get(i).getCapacity()));

        //Retorna la vista de la clase
        return view;
    }
}
