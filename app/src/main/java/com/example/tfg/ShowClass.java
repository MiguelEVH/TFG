package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.adapters.ShowClassBaseAdapter;
import com.example.tfg.classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowClass extends AppCompatActivity {

    TextView toolbarTitle, tvName, tvTrainingStarts, tvTrainingEnds, tvReservesDone, tvCapacity;
    String userId, boxId, name, trainingId, trainingStarts, trainingEnds, reservesDone, capacity, dayOfWeekName;
    int dayOfWeekNumber, availableCredits;
    ArrayList<User> crossfitters = new ArrayList<>();
    ListView crossfittersListView;
    Button btnBack, btnReserve, btnCancelReserve;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.showClass_title);

        //Coge los datos de la clase
        userId = getIntent().getStringExtra("userId");
        boxId = getIntent().getStringExtra("boxId");
        name = getIntent().getStringExtra("name");
        trainingId = getIntent().getStringExtra("trainingId");
        trainingStarts = getIntent().getStringExtra("trainingStarts");
        trainingEnds = getIntent().getStringExtra("trainingEnds");
        reservesDone = getIntent().getStringExtra("reservesDone");
        capacity = getIntent().getStringExtra("capacity");
        dayOfWeekName = getIntent().getStringExtra("dayOfWeekName");
        dayOfWeekNumber = Integer.parseInt(getIntent().getStringExtra("dayOfWeekNumber"));

        //Se enlaza con el layout
        tvName = findViewById(R.id.showClass_name);
        tvTrainingStarts = findViewById(R.id.showClass_scheduleStart);
        tvTrainingEnds = findViewById(R.id.showClass_scheduleEnd);
        tvReservesDone = findViewById(R.id.showClass_reservationsDone);
        tvCapacity = findViewById(R.id.showClass_capacityData);

        //Se rellena el layout
        tvName.setText(name);
        tvTrainingStarts.setText(trainingStarts);
        tvTrainingEnds.setText(trainingEnds);
        tvReservesDone.setText(reservesDone);
        tvCapacity.setText(capacity);

        //Recupera los crossfiteros que han realizado una reserva
        dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+boxId+"/classes/"+dayOfWeekNumber+"/"+trainingId+"/reservations");
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorre los crossfiteros de la aplicación
                for(DataSnapshot child : snapshot.getChildren()){
                    //Coge los datos del crossfitero
                    User user = new User();
                    user.setId(child.getKey());
                    //Comprueba si el crossfitero ya está apuntado
                    if(user.getId().equals(userId)){
                        btnReserve.setVisibility(View.GONE);
                        btnCancelReserve.setVisibility(View.VISIBLE);
                    }
                    user.setUsername(String.valueOf(child.getValue()));
                    //Lo añade al arraylist de crossfiteros
                    crossfitters.add(user);
                }
                //Comprueba si hay crossfiteros con reserva
                if(!crossfitters.isEmpty()) {
                    crossfittersListView = findViewById(R.id.showClass_crossfittersReservationsListView);
                    //Muestra los crossfiteros del box que han realizado una reserva
                    ShowClassBaseAdapter customBaseAdapter = new ShowClassBaseAdapter(getApplicationContext(), crossfitters);
                    crossfittersListView.setAdapter(customBaseAdapter);
                    crossfittersListView.getAdapter();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Listener que reserva una clase
        btnReserve = findViewById(R.id.showClass_btn_reserve);
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprueba que no se haya llenado la clase
                if(Integer.parseInt(reservesDone) >= Integer.parseInt(capacity)){
                    Toast.makeText(ShowClass.this, R.string.showClass_fullCapacity, Toast.LENGTH_SHORT).show();
                }else{
                    //Comprueba que tiene créditos disponibles
                    dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/availableCredits");
                    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Coge los créditos del usuario
                            User user = new User();
                            availableCredits = Integer.parseInt(String.valueOf(snapshot.getValue()));
                            user.setAvailableCredits(availableCredits);
                            //Realiza la reserva si tiene créditos disponibles
                            if(user.getAvailableCredits() > 0){
                                //Referencia al box del usuario y lo actualiza
                                dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/username");
                                //Se actualiza la rm del crossfitero del ejercicio seleccionado
                                dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //Nombre del crossfitero
                                        String crossfitterName = String.valueOf(snapshot.getValue());
                                        //Referencia a las reservas de la clase para añadir el nuevo crossfitero
                                        dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+boxId+"/classes/"+dayOfWeekNumber+"/"+trainingId+"/reservations");
                                        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                //Variable que controla si se ha encontrado el usuario
                                                boolean found = false;
                                                //Recorre los crossfiteros de la clase
                                                for(DataSnapshot child : snapshot.getChildren()) {
                                                    //Si es el boxId, guarda su valor
                                                    if (child.getKey().equals(userId)) {
                                                        found = true;
                                                        break;
                                                    }
                                                }
                                                //Si el crossfitero no está apuntado, realiza la reserva
                                                if(!found){
                                                    //Se añade el crossfitero a la reserva
                                                    dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+boxId+"/classes/"+dayOfWeekNumber+"/"+trainingId+"/reservations/"+userId);
                                                    dbReference.setValue(crossfitterName);
                                                    //Aumenta en 1 las reservas
                                                    dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+boxId+"/classes/"+dayOfWeekNumber+"/"+trainingId+"/reservesDone");
                                                    reservesDone = String.valueOf(Integer.parseInt(reservesDone)+1);
                                                    tvReservesDone.setText(reservesDone);
                                                    dbReference.setValue(Integer.parseInt(reservesDone));
                                                    //Actualiza los créditos disponibles
                                                    dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/availableCredits");
                                                    dbReference.setValue((availableCredits-1));
                                                    Toast.makeText(ShowClass.this, R.string.showClass_enrolledInTraining, Toast.LENGTH_SHORT).show();
                                                    //Se pasa por parámetro userId
                                                    Intent intent = new Intent(getApplicationContext(), Reservations.class);
                                                    intent.putExtra("userId", userId);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                            }else{
                                Toast.makeText(ShowClass.this, R.string.showClass_trainingCreditsSoldOut, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });

        //Listener que cancela la reserva de una clase
        btnCancelReserve = findViewById(R.id.showClass_btn_cancelReservation);
        btnCancelReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Referencia a la clase en la base de datos
                dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+boxId+"/classes/"+dayOfWeekNumber+"/"+trainingId+"/reservations/"+userId);
                //Elimina la reserva
                dbReference.removeValue();
                Toast.makeText(ShowClass.this, R.string.showClass_trainingCancelled, Toast.LENGTH_SHORT).show();
                //Reduce en 1 las reservas
                dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+boxId+"/classes/"+dayOfWeekNumber+"/"+trainingId+"/reservesDone");
                reservesDone = String.valueOf(Integer.parseInt(reservesDone)-1);
                tvReservesDone.setText(reservesDone);
                dbReference.setValue(Integer.parseInt(reservesDone));
                //Comprueba que tiene créditos disponibles
                dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/availableCredits");
                dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Devuelve el crédito de la clase cancelada
                        availableCredits = Integer.parseInt(String.valueOf(snapshot.getValue()));
                        dbReference.setValue((availableCredits+1));
                        //Se pasa por parámetro userId
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
        });

        //Listener que vuelve a la pantalla de reservas
        btnBack = findViewById(R.id.showClass_btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se pasa por parámetro userId
                Intent intent = new Intent(getApplicationContext(), Reservations.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });
    }
}