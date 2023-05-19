package com.example.tfg.classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfg.R;

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
        workoutImage.setImageResource(workoutList.get(i).getImage());
        //Comprueba si el usuario ha registrado una marca personal
        weight.setText("-");
        //Variable que comprueba si se ha encontrado la marca personal
        boolean found = false;
        for(int index=0;index<workoutList.size();index++){
            //Sale de la iteración si ha encontrado la marca personal
            if(found){
               break;
            }
            //Comprueba si es el mismo ejercicio
            for(int j=0;j<personalBestRecords.size();j++){
                //Comprueba
                if(personalBestRecords.get(j).getId().equals(workoutList.get(index).getId())){
                    //Si lo es, pone el peso
                    weight.setText(String.valueOf(personalBestRecords.get(j).getWeight()));
                    found = true;
                }
                Log.i("PRUEBA", String.valueOf(workoutList.get(index).getName() + " " + String.valueOf(personalBestRecords.get(j).getWeight()) ));
                break;
            }
        }
        //Retorna la vista del workout
        return view;
    }
}
