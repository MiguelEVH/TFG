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
import android.widget.Toast;

import com.example.tfg.adapters.ClassDayBaseAdapter;
import com.example.tfg.classes.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ClassDay extends AppCompatActivity {

    TextView toolbarTitle;
    String dayOfWeekName, userId;
    int dayOfWeekNumber;
    Button btnBack, createClassDay;
    ArrayList<Training> trainings = new ArrayList<>();
    ListView classesListView;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_day);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        //Se coge la id del crossfitero
        dayOfWeekName = getIntent().getStringExtra("dayOfWeekName");
        dayOfWeekNumber = Integer.parseInt(getIntent().getStringExtra("dayOfWeekNumber"));
        userId = getIntent().getStringExtra("userId");
        toolbarTitle.setText(dayOfWeekName);

        //Recupera las clases del día
        dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+userId+"_box/classes/"+dayOfWeekNumber);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorre los entrenamientos
                for(DataSnapshot child : snapshot.getChildren()) {
                    Training training = new Training();
                    training.setId(child.getKey());
                    training.setName(String.valueOf(child.child("name").getValue()));
                    training.setTrainingStarts(String.valueOf(child.child("trainingStarts").getValue()));
                    training.setTrainingEnds(String.valueOf(child.child("trainingEnds").getValue()));
                    training.setReservesDone(Integer.parseInt(String.valueOf(child.child("reservesDone").getValue())));
                    training.setCapacity(Integer.parseInt(String.valueOf(child.child("capacity").getValue())));
                    //Se añade el entrenamiento a la lista de entrenamientos
                    trainings.add(training);
                }
                if(trainings.isEmpty()){
                    Toast.makeText(ClassDay.this, R.string.classDay_noAvailableClasses, Toast.LENGTH_SHORT).show();
                }else{
                    //Ordena los entrenamientos por hora de inicio
                    Collections.sort(trainings, (o1, o2) -> o1.getTrainingStarts().compareTo(o2.getTrainingStarts()));
                    //Crea la lista de clases
                    classesListView = findViewById(R.id.classDay_listView);
                    ClassDayBaseAdapter customBaseAdapter = new ClassDayBaseAdapter(getApplicationContext(), trainings);
                    classesListView.setAdapter(customBaseAdapter);
                    //Listener que muestra las clases que hay un día en concreto
                    classesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Se pasa por parámetro los datos de la clase, el día seleccionado y el user
                            Intent intent = new Intent(getApplicationContext(), ShowClassUpdate.class);
                            intent.putExtra("name", trainings.get(i).getName());
                            intent.putExtra("trainingId", trainings.get(i).getId());
                            intent.putExtra("trainingStarts", trainings.get(i).getTrainingStarts());
                            intent.putExtra("trainingEnds", trainings.get(i).getTrainingEnds());
                            intent.putExtra("reservesDone", String.valueOf(trainings.get(i).getReservesDone()));
                            intent.putExtra("capacity", String.valueOf(trainings.get(i).getCapacity()));
                            intent.putExtra("dayOfWeekName", dayOfWeekName);
                            intent.putExtra("dayOfWeekNumber", String.valueOf(dayOfWeekNumber));
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Listener que crea una clase
        createClassDay = findViewById(R.id.classDay_btn_createClass);
        createClassDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se pasa por parámetro el día y userId seleccionado
                Intent intent = new Intent(getApplicationContext(), ClassCreate.class);
                intent.putExtra("dayOfWeekName", dayOfWeekName);
                intent.putExtra("dayOfWeekNumber", String.valueOf(dayOfWeekNumber));
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

        btnBack = findViewById(R.id.classDay_btn_back);
        //Listener que vuelve a la pantalla de gestionar box
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}