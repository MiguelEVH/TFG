package com.example.tfg.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfg.R;
import com.example.tfg.classes.PersonalBestRecord;
import com.example.tfg.classes.WorkoutExercise;

import java.util.ArrayList;

public class PersonalBestBaseAdapter extends BaseAdapter {

    //Variables
    private Context context;
    private ArrayList<WorkoutExercise> workoutList;
    private ArrayList<PersonalBestRecord> personalBestRecords;
    private LayoutInflater inflater;

    //Constructor
    public PersonalBestBaseAdapter(Context context, ArrayList workoutList, ArrayList personalBestRecords) {
        this.context = context;
        this.workoutList = workoutList;
        this.personalBestRecords = personalBestRecords;
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
        view = inflater.inflate(R.layout.activity_personal_best_list_view, null);
        //Asigna las variables del item
        TextView workoutText = (TextView) view.findViewById(R.id.personalBest_workoutName);
        TextView weight = (TextView) view.findViewById(R.id.personalBest_weight);
        ImageView workoutImage = (ImageView) view.findViewById(R.id.personalBest_workoutImage);
        //Da valores al item
        workoutText.setText(workoutList.get(i).getName());
        workoutImage.setImageResource(Integer.valueOf(workoutList.get(i).getImage()) );
        //Comprueba si el usuario ha registrado una marca personal
        weight.setText(String.valueOf(personalBestRecords.get(i).getWeight())+" Kg");
        //Retorna la vista del workout
        return view;
    }
}
