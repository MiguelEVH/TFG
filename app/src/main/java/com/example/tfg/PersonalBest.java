package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tfg.adapters.PersonalBestBaseAdapter;
import com.example.tfg.classes.PersonalBestRecord;
import com.example.tfg.classes.WorkoutExercise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PersonalBest extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    TextView toolbarTitle;
    Button btnBack;
    String userId;
    ArrayList<WorkoutExercise> workoutExercises = new ArrayList<>();
    ArrayList<PersonalBestRecord> personalBestRecordsAux = new ArrayList<>();
    ArrayList<PersonalBestRecord> personalBestRecords = new ArrayList<>();
    ListView personalBestListView;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_best);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.personalBest_title);

        //Comprueba que el usuario está autenticado, sino, lo redirige a la pantalla de login
        checkLoggedUser();

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");

        //Botón para volver a la pantalla de inicio
        btnBack = findViewById(R.id.personalBest_btn_back);

        //Lee los workout  del JSON
        addWorkoutTutorialsFromJSON();
        //Crea la lista de marcas personales
        personalBestListView = findViewById(R.id.personalBest_listView);

        //Recupera las marcas personales del usuario actual
        dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/personalBestRecords");
        //setPersonalBestRecord("cm_deadlift", 100);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recupera las marcas personales del crossfitero
                for(DataSnapshot child : snapshot.getChildren()){
                    //Coge las marcas personales
                    PersonalBestRecord record = new PersonalBestRecord();
                    record.setId(child.getKey());
                    record.setWeight(Double.parseDouble(String.valueOf(child.getValue())));
                    personalBestRecordsAux.add(record);
                }
                //Variable que controla si el ejercicio coincide con la id de la marca personal
                boolean found = false;
                //Rellena los huecos de la lista de marcas personales
                for(int i=0;i<workoutExercises.size();i++){
                    //Reinicia la variable que controla si se ha encontrado una marca personal
                    found = false;
                    //Recorre la lista de marcas personales auxiliar. Si coincide con el ejercicio de la lista actual, lo agrega a las marcas personales
                    for(int j=0;j<personalBestRecordsAux.size();j++){
                        //Se rellena con el valor de la key si esta coincide. Si no, se marca como vacío
                        if (workoutExercises.get(i).getId().equals(personalBestRecordsAux.get(j).getId())){
                            //Guarda la marca personal en la lista de marcas personales
                            personalBestRecords.add(personalBestRecordsAux.get(j));
                            //Se ha encontrado una marca personal
                            found = true;
                            //Elimina el ejercicio de la lista auxiliar
                            personalBestRecordsAux.remove(j);
                            break;
                        }
                    }
                    //Si no se ha encontrado la marca personal, la rellena
                    if(!found){
                        //Coge las marcas personales
                        PersonalBestRecord record = new PersonalBestRecord();
                        record.setId(workoutExercises.get(i).getId());
                        record.setWeight(0);
                        personalBestRecords.add(record);
                    }
                }
                //Muestra el adaptador
                PersonalBestBaseAdapter customBaseAdapter = new PersonalBestBaseAdapter(getApplicationContext(), workoutExercises, personalBestRecords);
                personalBestListView.setAdapter(customBaseAdapter);
                personalBestListView.getAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Listener que abre modifica la marca personal del ejercicio seleccionado
        personalBestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Navega a la pantalla de actualizar la marca personal seleccionada
                Intent intent = new Intent(getApplicationContext(), PersonalBestUpdate.class);
                //Se le pasa el id del user, el ejercicio y la marca personal
                intent.putExtra("userId", fbUser.getUid());
                intent.putExtra("exerciseId", workoutExercises.get(i).getId());
                intent.putExtra("exerciseName", workoutExercises.get(i).getName());
                intent.putExtra("exerciseImage", workoutExercises.get(i).getImage());
                intent.putExtra("exercisePersonalBest", personalBestRecords.get(i).getWeight());
                startActivity(intent);
                finish();
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

    public void setPersonalBestRecord(String exerciseId, double personalBestRecord){
        //Se modifica el nuevo nombre de usuario en la base de datos
        //dbReference = FirebaseDatabase.getInstance().getReference("Users");
        dbReference.child(exerciseId).setValue(personalBestRecord);
    }
}