package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.adapters.CrossfittersManagementBaseAdapter;
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
    EditText newCrossfitterEmail;
    Button btnBack, btnAddCrossfitter;
    ArrayList<User> boxCrossfitters = new ArrayList<>();
    ArrayList<User> allCrossfitters = new ArrayList<>();
    ListView crosfittersListView;
    String userId;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfitters_management);
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

        //Email para añadir nuevo crossfitero
        newCrossfitterEmail = findViewById(R.id.crossfittersManagement_newUser);

        //Recupera las marcas personales del usuario actual
        dbReference = FirebaseDatabase.getInstance().getReference("Users/");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorre los crossfiteros de la aplicación
                for(DataSnapshot child : snapshot.getChildren()){
                    //Coge los datos del crossfitero
                    User user = new User();
                    user.setId(child.getKey());
                    user.setUsername(child.child("username").getValue(String.class));
                    user.setBoxId(child.child("boxId").getValue(String.class));
                    user.setEmail(child.child("email").getValue(String.class));

                    //Comprueba si es del mismo box. Si lo es lo agrega a los crossfiteros del box
                    if(user.getBoxId().equals(userId+"_box")){
                        user.setFee(Long.valueOf((Long) child.child("fee").getValue()).intValue());
                        boxCrossfitters.add(user);
                    }
                    //Lo añade al arraylist de crossfiteros
                    allCrossfitters.add(user);
                }
                //Muestra los crossfiteros del box
                CrossfittersManagementBaseAdapter customBaseAdapter = new CrossfittersManagementBaseAdapter(getApplicationContext(), boxCrossfitters);
                crosfittersListView.setAdapter(customBaseAdapter);
                crosfittersListView.getAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Listener que abre el videotutorial del ejercicio que seleccione el usuario
        crosfittersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Se pasa por parámetro la url del workout tutorial y muestra el vídeo en una nueva actividad
                Intent intent = new Intent(getApplicationContext(), UpdateBoxCrossfitter.class);
                intent.putExtra("userId", userId);
                intent.putExtra("crossfitterId", boxCrossfitters.get(i).getId());
                intent.putExtra("crossfitterUsername", boxCrossfitters.get(i).getUsername());
                intent.putExtra("crossfitterEmail", boxCrossfitters.get(i).getEmail());
                intent.putExtra("crossfitterFee", String.valueOf(boxCrossfitters.get(i).getFee()));
                startActivity(intent);
                finish();
            }
        });

        //Botón que añade un crossfitero
        btnAddCrossfitter = findViewById(R.id.crossfittersManagement_btn_addCrossfitter);
        btnAddCrossfitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Variable que controla si se ha encontrado el crossfitero
                boolean found = false;
                //Recorre la lista de usuarios de la aplicación
                for(int i=0;i<allCrossfitters.size();i++){
                    //Comprueba si el email del crossfitero introducido existe
                    if(allCrossfitters.get(i).getEmail().equals(String.valueOf(newCrossfitterEmail.getText()))){
                        //Toast.makeText(CrossfittersManagement.this, "El crossfitero existe: " + newCrossfitterEmail.getText(), Toast.LENGTH_SHORT).show();
                        if(allCrossfitters.get(i).getBoxId().equals(userId+"_box")){
                            Toast.makeText(CrossfittersManagement.this, "El crossfitero ya pertenece al box", Toast.LENGTH_SHORT).show();
                        }else{
                            //Si se ha encontrado al crossfitero y no forma parte del box, navega a la activity de añadir crossfitero.
                            Intent intent = new Intent(getApplicationContext(), AddCrossfitter.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("newCrossfitterId", allCrossfitters.get(i).getId());
                            intent.putExtra("newCrossfitterUsername", allCrossfitters.get(i).getUsername());
                            intent.putExtra("newCrossfitterEmail", allCrossfitters.get(i).getEmail());
                            startActivity(intent);
                            finish();
                        }
                        //Se marca que se ha encontrado al usuario
                        found = true;
                    }
                }
                //Si no se ha encontrado, avisa del error
                if(!found){
                    Toast.makeText(CrossfittersManagement.this, "No se encuentra un crossfitero con el email: " + newCrossfitterEmail.getText(), Toast.LENGTH_SHORT).show();
                }
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