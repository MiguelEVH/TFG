package com.example.tfg.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfg.R;
import com.example.tfg.classes.WorkoutExercise;

import java.util.ArrayList;

public class TutorialsBaseAdapter extends BaseAdapter {

    //Variables
    private Context context;
    private ArrayList<WorkoutExercise> workoutList;
    private LayoutInflater inflater;

    //Constructor
    public TutorialsBaseAdapter(Context context, ArrayList workoutList) {
        this.context = context;
        this.workoutList = workoutList;
        inflater = LayoutInflater.from(context);
    }

    //Devuelve el número de tutoriales de ejercicios
    @Override
    public int getCount() {
        return workoutList.size();
    }

    //Devuelve el item seleccionado
    @Override
    public Object getItem(int i) {
        return null;
    }

    //Devuelve el id del item seleccionado
    @Override
    public long getItemId(int i) {
        return 0;
    }

    //Devuelve la vista del item
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Se genera la vista
        view = inflater.inflate(R.layout.activity_tutorials_list_view, null);
        //Asigna las variables del item
        TextView workoutText = (TextView) view.findViewById(R.id.tutorials_workoutName);
        ImageView workoutImage = (ImageView) view.findViewById(R.id.tutorials_workoutImage);
        //Da valores al item
        workoutText.setText(workoutList.get(i).getName());
        workoutImage.setImageResource(workoutList.get(i).getImage());
        //Retorna la vista del workout
        return view;
    }
}
