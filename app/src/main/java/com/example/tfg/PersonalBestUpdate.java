package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.classes.PersonalBestRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalBestUpdate extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    DatabaseReference dbReference;
    TextView toolbarTitle, textViewExerciseName;
    EditText editTextExerciseWeight;
    ImageView imageViewExerciseImage;
    Button btnBack;
    ImageView btnEdit, btnSave;
    String userId, exerciseId, exerciseName, exerciseImage, exercisePersonalBest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_best_update);
        Resources.Theme activityTheme = this.getTheme();

        //Se pone el título de la activity
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.personalBest_title);

        //Comprueba si el usuario tiene una sesión iniciada
        checkLoggedUser();

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");
        exerciseId = getIntent().getStringExtra("exerciseId");
        exerciseName = getIntent().getStringExtra("exerciseName");
        exerciseImage = getIntent().getStringExtra("exerciseImage");
        exercisePersonalBest = getIntent().getStringExtra("exercisePersonalBest");

        textViewExerciseName = findViewById(R.id.personalBestUpdate_exerciseName);
        editTextExerciseWeight = findViewById(R.id.personalBestUpdate_exerciseRmData);
        imageViewExerciseImage = findViewById(R.id.personalBestUpdate_exerciseImage);

        //Rellena el texto con el nombre del ejercicio e imagen
        textViewExerciseName.setText(exerciseName);
        imageViewExerciseImage.setImageResource(getResources().getIdentifier(exerciseId,"drawable",this.getPackageName()));

        //Referencia al peso de este ejercicio en la base de datos
        dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/personalBestRecords/"+exerciseId);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Cogemos el peso del ejercicio
                if(snapshot.getValue() != null){
                    editTextExerciseWeight.setText(String.valueOf(snapshot.getValue()));
                }else{
                    editTextExerciseWeight.setText(String.valueOf(0));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Buttons
        btnBack = findViewById(R.id.personalBestUpdate_btn_back);
        btnEdit = findViewById(R.id.personalBestUpdate_btn_personalBest_edit);
        btnSave = findViewById(R.id.personalBestUpdate_btn_personalBest_save);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Oculta el botón de editar
                btnEdit.setVisibility(View.GONE);
                //Muestra el botón de guardar
                btnSave.setVisibility(View.VISIBLE);
                //Oscurece el fondo del texto del WOD
                editTextExerciseWeight.setBackgroundColor(getResources().getColor(R.color.grey40, activityTheme));
                editTextExerciseWeight.setEnabled(true);
            }
        });

        //Se guarda la nueva marca del usuario
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonalBestRecord personalBestRecord = new PersonalBestRecord();
                //Comprueba si el peso es válido
                if(personalBestRecord.validWeight(Double.parseDouble(String.valueOf(editTextExerciseWeight.getText())))){
                    //Oculta el botón de guardar
                    btnSave.setVisibility(View.GONE);
                    //Muestra el botón de editar
                    btnEdit.setVisibility(View.VISIBLE);
                    editTextExerciseWeight.setBackgroundColor(getResources().getColor(R.color.hollow, activityTheme));
                    editTextExerciseWeight.setEnabled(false);
                    //Se actualiza la rm del crossfitero del ejercicio seleccionado
                    dbReference.setValue(Double.valueOf(String.valueOf(editTextExerciseWeight.getText())));
                }else{
                    Toast.makeText(PersonalBestUpdate.this, "Peso no válido", Toast.LENGTH_SHORT).show();
                }


            }
        });



        //Listener que vuelve a la pantalla de inicio
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PersonalBest.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
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
}