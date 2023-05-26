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
import com.example.tfg.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class Reservations extends AppCompatActivity {

    TextView toolbarTitle;
    Button btnBack;
    ArrayList<Training> trainings = new ArrayList<>();
    String dayOfWeekName, userId;
    int dayOfWeekNumber;
    ListView reservationsListView;
    DatabaseReference dbReference, dbUsersReference, dbTrainingsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.reservations_title);

        //Coge la id del usuario y el día de hoy
        userId = getIntent().getStringExtra("userId");
        dayOfWeekName = LocalDate.now().getDayOfWeek().name();
        dayOfWeekNumber = fixDayOfWeekNumber(LocalDate.now().getDayOfWeek().getValue());
        //Botón para volver a la pantalla de inicio
        btnBack = findViewById(R.id.reservations_btn_back);
        //Listener que vuelve a la pantalla de inicio
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Recupera el box del crossfitero
        dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/boxId");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Coge la id del box
                String boxId = String.valueOf(snapshot.getValue());
                //Comprueba si tiene box. Si tiene, coge las clases de ese box del día
                if(boxId.isEmpty()){
                    Toast.makeText(Reservations.this, R.string.reservations_userNonEnrolledInBox, Toast.LENGTH_SHORT).show();
                }else{
                    //Comprueba si hay que limpiar las clases de las reservas viejas
                    dbReference = FirebaseDatabase.getInstance().getReference("LastTrainingDay/"+dayOfWeekNumber);
                    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Coge la fecha actual formateada "dd:mm:yyyy"
                            String currentTrainingDay = fixedDate();
                            //Comprueba si es el mismo día
                            if(!currentTrainingDay.equals(String.valueOf(snapshot.getValue()))){
                                //Actualiza el día
                                dbReference.setValue(currentTrainingDay);
                                //Si es lunes, devuelve los créditos disponibles
                                if(dayOfWeekNumber == 0){
                                    dbUsersReference = FirebaseDatabase.getInstance().getReference("Users/");
                                    dbUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //Recorre los crossfiteros de la aplicación
                                            for(DataSnapshot child : snapshot.getChildren()){
                                                //Coge los datos del crossfitero
                                                User user = new User();
                                                user.setId(child.getKey());
                                                user.setFee(Integer.parseInt(String.valueOf(child.child("fee").getValue())));
                                                //Devuelve los créditos que tenga contratados
                                                dbUsersReference.child(child.getKey()).child("availableCredits").setValue(user.getFee());
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                                //Elimina las reservas del día anterior
                                dbTrainingsReference = FirebaseDatabase.getInstance().getReference("Boxes/"+boxId+"/classes/"+dayOfWeekNumber);
                                dbTrainingsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Recorre los entrenamientos
                                        for(DataSnapshot child : snapshot.getChildren()) {
                                            //Limpia las reservas del anterior día
                                            dbTrainingsReference.child(child.getKey()).child("reservations").removeValue();
                                            dbTrainingsReference.child(child.getKey()).child("reservesDone").setValue(0);
                                        }
                                        //Reinicia la activity
                                        Intent intent = new Intent(getApplicationContext(), Reservations.class);
                                        intent.putExtra("userId", userId);
                                        startActivity(intent);
                                        finish();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                            retrieveTodayClasses(boxId);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //Método que modifica el día de la semana para que coincida con el backend
    public int fixDayOfWeekNumber(int dayOfWeekNumber){
        if(dayOfWeekNumber != 0){
            return dayOfWeekNumber - 1;
        }else{
            dayOfWeekNumber = 6;
            return dayOfWeekNumber;
        }
    }

    //Método que retorna una fecha formateada "dd:mm:yyyy"
    public String fixedDate(){
        LocalDateTime currentDate = LocalDateTime.now();
        String day, month;

        //Arregla el día
        if(currentDate.getDayOfMonth() < 10){
            day = "0" + currentDate.getDayOfMonth();
        }else{
            day = String.valueOf(currentDate.getDayOfMonth());
        }
        //Arregla el mes
        if(currentDate.getMonthValue() < 10){
            month = "0" + currentDate.getMonthValue();
        }else{
            month = String.valueOf(currentDate.getMonthValue());
        }
        //Fecha formateada
        String fixedDate = day + ":" + month + ":" + currentDate.getYear();
        return fixedDate;
    }


    public void retrieveTodayClasses(String boxId){
        //Recupera las clases del día
        dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+boxId+"/classes/"+dayOfWeekNumber);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorre los entrenamientos uno a uno
                for(DataSnapshot child : snapshot.getChildren()) {
                    //Rellena un entrenamiento
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
                    Toast.makeText(Reservations.this, R.string.reservations_nonAvailableTrainings, Toast.LENGTH_SHORT).show();
                }else{
                    //Ordena los entrenamientos por hora de inicio
                    Collections.sort(trainings, (o1, o2) -> o1.getTrainingStarts().compareTo(o2.getTrainingStarts()));
                    //Crea la lista de clases
                    reservationsListView = findViewById(R.id.reservations_listView);
                    ClassDayBaseAdapter customBaseAdapter = new ClassDayBaseAdapter(getApplicationContext(), trainings);
                    reservationsListView.setAdapter(customBaseAdapter);
                    //Listener que muestra las clases que hay un día en concreto
                    reservationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Se pasa por parámetro los datos de la clase, el día seleccionado y el user
                            Intent intent = new Intent(getApplicationContext(), ShowClass.class);
                            intent.putExtra("name", trainings.get(i).getName());
                            intent.putExtra("trainingId", trainings.get(i).getId());
                            intent.putExtra("trainingStarts", trainings.get(i).getTrainingStarts());
                            intent.putExtra("trainingEnds", trainings.get(i).getTrainingEnds());
                            intent.putExtra("reservesDone", String.valueOf(trainings.get(i).getReservesDone()));
                            intent.putExtra("capacity", String.valueOf(trainings.get(i).getCapacity()));
                            intent.putExtra("dayOfWeekName", dayOfWeekName);
                            intent.putExtra("dayOfWeekNumber", String.valueOf(dayOfWeekNumber));
                            intent.putExtra("userId", userId);
                            intent.putExtra("boxId", boxId);
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
    }
}