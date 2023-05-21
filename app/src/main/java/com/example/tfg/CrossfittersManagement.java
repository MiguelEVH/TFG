package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.classes.CrossfittersBaseAdapter;
import com.example.tfg.classes.PersonalBestBaseAdapter;
import com.example.tfg.classes.PersonalBestRecord;
import com.example.tfg.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CrossfittersManagement extends AppCompatActivity{

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    TextView toolbarTitle;
    Button btnBack, btnAddCrossfitter;
    ArrayList<User> boxCrossfitters = new ArrayList<>();
    ListView crosfittersListView;
    String userId;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfiters_management);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.crossfittersManagement_title);

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");

        //Comprueba que el usuario está autenticado, sino, lo redirige a la pantalla de login
        checkLoggedUser();

        //Crea la lista de crossfiteros
        crosfittersListView = findViewById(R.id.crossfittersManagement_listView);


        //Recupera las marcas personales del usuario actual
        dbReference = FirebaseDatabase.getInstance().getReference("Users/");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorre los crossfiteros de la aplicación
                for(DataSnapshot child : snapshot.getChildren()){
                    //Coge el boxId del crossfitero
                    User user = new User();
                    user.setBoxId(child.child("boxId").getValue(String.class));
                    //Comprueba si es del mismo box
                    if(user.getBoxId().equals(userId+"_box")){
                        //Toast.makeText(CrossfittersManagement.this, "Es del mismo box: " + user.getBoxId(), Toast.LENGTH_SHORT).show();
                        //Coge el id y email
                        user.setId(child.getKey());
                        user.setUsername(child.child("username").getValue(String.class));
                        Toast.makeText(CrossfittersManagement.this, "Es del mismo box: " + user.getUsername(), Toast.LENGTH_SHORT).show();
                        boxCrossfitters.add(user);
                    }
                }

                //Muestra los crossfiteros del box

                CrossfittersBaseAdapter customBaseAdapter = new CrossfittersBaseAdapter(getApplicationContext(), boxCrossfitters);
                crosfittersListView.setAdapter(customBaseAdapter);
                crosfittersListView.getAdapter();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Botón que añade un crossfitero
        btnAddCrossfitter = findViewById(R.id.crossfittersManagement_btn_addCrossfitter);
        btnAddCrossfitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCrossfitter.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

        //Boton que hace volver a la pantalla de gestión de box
        btnBack = findViewById(R.id.crossfittersManagement_btn_back);
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