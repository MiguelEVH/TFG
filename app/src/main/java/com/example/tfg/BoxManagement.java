package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BoxManagement extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    Button btnClassPlanning, btnManageCrossfiters, btnBack;
    TextView toolbarTitle;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_management);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.boxManagement_title);

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");

        //Comprueba que el usuario está autenticado, sino, lo redirige a la pantalla de login
        checkLoggedUser();

        btnClassPlanning = findViewById(R.id.boxManagement_btn_classPlanning);
        btnManageCrossfiters = findViewById(R.id.boxManagement_btn_manageCrossfiters);
        btnBack = findViewById(R.id.boxManagement_btn_back);

        //listener que navega a la activiad de planificar clases
        btnClassPlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClassPlanning.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        //Listener que navega a la activity de gestión de crossfiteros
        btnManageCrossfiters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CrossfittersManagement.class);
                intent.putExtra("userId", userId);
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