package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ClassPlanning extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    TextView toolbarTitle;
    Button btnBack, btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday, btnSunday;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_planning);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.classPlanning_title);

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");

        //Comprueba que el usuario está autenticado, sino, lo redirige a la pantalla de login
        checkLoggedUser();

        //Se instancian los botones
        btnMonday = findViewById(R.id.classPlanning_btn_monday);
        btnTuesday = findViewById(R.id.classPlanning_btn_tuesday);
        btnWednesday = findViewById(R.id.classPlanning_btn_wednesday);
        btnThursday = findViewById(R.id.classPlanning_btn_thursday);
        btnFriday = findViewById(R.id.classPlanning_btn_friday);
        btnSaturday = findViewById(R.id.classPlanning_btn_saturday);
        btnSunday = findViewById(R.id.classPlanning_btn_sunday);

        //Hace click en el botón de lunes
        btnMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDayClasses("Monday");
            }
        });

        //Hace click en el botón de martes
        btnTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDayClasses("Tuesday");
            }
        });

        //Hace click en el botón de miércoles
        btnWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDayClasses("Wednesday");
            }
        });

        //Hace click en el botón de jueves
        btnThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDayClasses("Thursday");
            }
        });

        //Hace click en el botón de viernes
        btnFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDayClasses("Friday");
            }
        });

        //Hace click en el botón de sábado
        btnSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDayClasses("Saturday");
            }
        });

        //Hace click en el botón de domingo
        btnSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDayClasses("Sunday");
            }
        });

        //Botón para volver a la pantalla de gestión de box
        btnBack = findViewById(R.id.classPlanning_btn_back);
        //Listener que vuelve a la de gestión de box
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //Si el usuario no tiene una sesión iniciada, lo retorna a la pantalla de login
    public void checkLoggedUser(){
        //Se instancia la autenticación de Firebase
        fbAuth = FirebaseAuth.getInstance();
        //Coge el usuario actual
        fbUser = fbAuth.getCurrentUser();

        //Si no hay un usuario con sesión iniciada, vuelve a la pantalla de login.
        if(fbUser == null){
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();
        }
    }

    //Navega a la activity de DayClasses para mostrar las clases asociadas a ese día del box
    public void selectedDayClasses(String day){

        Toast.makeText(ClassPlanning.this, "Día: " + day, Toast.LENGTH_SHORT).show();



    }
}