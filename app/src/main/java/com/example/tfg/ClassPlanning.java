package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ClassPlanning extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    TextView toolbarTitle;
    Button btnBack;
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
}