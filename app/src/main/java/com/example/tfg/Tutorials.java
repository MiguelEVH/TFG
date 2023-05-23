package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tfg.Adapters.TutorialsBaseAdapter;
import com.example.tfg.classes.WorkoutExercise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Tutorials extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    TextView toolbarTitle;
    Button btnBack;
    ArrayList<WorkoutExercise> workoutExercises = new ArrayList<>();
    ListView workoutTutorialListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.tutorials_title);

        //Comprueba que el usuario está autenticado, sino, lo redirige a la pantalla de login
        checkLoggedUser();

        //Buttons
        btnBack = findViewById(R.id.tutorials_btn_back);

        //Lee los workout tutorials del JSON
        addWorkoutTutorialsFromJSON();
        //Crea la lista de entrenamientos
        workoutTutorialListView = findViewById(R.id.tutorials_listView);
        TutorialsBaseAdapter customBaseAdapter = new TutorialsBaseAdapter(getApplicationContext(), workoutExercises);
        workoutTutorialListView.setAdapter(customBaseAdapter);

        //Listener que abre el videotutorial del ejercicio que seleccione el usuario
        workoutTutorialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Se pasa por parámetro la url del workout tutorial y muestra el vídeo en una nueva actividad
                Intent intent = new Intent(getApplicationContext(), VideoPlayer.class);
                intent.putExtra("workoutUrl", workoutExercises.get(i).getUrl());
                startActivity(intent);
            }
        });

        //Listener que vuelve a la pantalla de inicio
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //Método que comprueba si el usuario está logueado. Si no, lo manda a la pantalla de Login
    public void checkLoggedUser(){
        //Se instancia la autenticación de Firebase
        fbAuth = FirebaseAuth.getInstance();
        //Coge el usuario actual
        fbUser = fbAuth.getCurrentUser();

        //Si no hay un usuario con sesión iniciada, vuelve a la pantalla de login.
        if(fbUser == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Método que añade los tutoriales de entrenamiento de un JSON
    private void addWorkoutTutorialsFromJSON(){
        try {
            //Se declara el json
            String jsonData = readJSONDataFromFile();
            JSONArray jsonArray = null;
            //Variables que leerá
            String id, name, url;
            jsonArray = new JSONArray(jsonData);
            WorkoutExercise workoutExercise;
            //Recorre el array del json
            for(int i=0; i<jsonArray.length(); i++){
                //Coge el tutorial actual del JSON
                JSONObject object = jsonArray.getJSONObject(i);
                //Se declara y rellena el nuevo objeto de workout
                workoutExercise = new WorkoutExercise();
                workoutExercise.setId(object.getString("id"));
                workoutExercise.setName(object.getString("name"));
                workoutExercise.setUrl(object.getString("url"));
                workoutExercise.setImage(getResources().getIdentifier(workoutExercise.getId(),"drawable",this.getPackageName()));
                //Añade el workout tutorial
                workoutExercises.add(workoutExercise);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //Método que lee el json de tutoriales de workouts
    private String readJSONDataFromFile(){
        //Variables para la lectura
        InputStream inputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        String jsonString = null;
        try{
            //Lee el JSON de los tutoriales
            inputStream = getResources().openRawResource(R.raw.workouttutorials);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            //Lee el json mientras queden líneas
            while((jsonString = bufferedReader.readLine()) != null){
                stringBuilder.append(jsonString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //Si el input stream está abierto, lo cierra
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new String(stringBuilder);
    }
}