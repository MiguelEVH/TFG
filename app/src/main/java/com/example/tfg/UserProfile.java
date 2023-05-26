package com.example.tfg;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tfg.classes.Box;
import com.example.tfg.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbarTitle, email, box, contractedClasses, availableClasses;
    EditText user;
    Button btnBack;
    ImageButton btnEditUsername, btnSaveUsername;
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    DatabaseReference dbReference, dbBoxReference;
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Resources.Theme activityTheme = this.getTheme();
        //Se crea la action bar
        toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");

        //Se pone el título de la activity
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.profile_title);

        //Variables de la vista
        user = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email_data);
        box = findViewById(R.id.profile_box_data);
        contractedClasses = findViewById(R.id.profile_contractedClasses_data);
        availableClasses = findViewById(R.id.profile_availableClasses_data);
        btnBack = findViewById(R.id.profile_btn_back);
        btnEditUsername = findViewById(R.id.profile_btn_username_edit);
        btnSaveUsername = findViewById(R.id.profile_btn_username_save);

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

        //Recupera los datos del usuario actual
        dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Coge los datos del usuario
                User currentUser = new User();
                for(DataSnapshot child : snapshot.getChildren()) {
                    //Si es el boxId, guarda su valor
                    if (child.getKey().equals("username")) {
                        currentUser.setUsername(String.valueOf(child.getValue()));
                    } else if (child.getKey().equals("email")) {
                        currentUser.setEmail(String.valueOf(child.getValue()));
                    } else if (child.getKey().equals("availableCredits")) {
                        currentUser.setAvailableCredits(Integer.valueOf(String.valueOf(child.getValue())));
                    } else if (child.getKey().equals("fee")) {
                        currentUser.setFee(Integer.valueOf(String.valueOf(child.getValue())));
                    }else if (child.getKey().equals("boxId")){
                        //Comprueba que tiene box
                        if(!String.valueOf(child.getValue()).isEmpty()){
                            currentUser.setBoxId(String.valueOf(child.getValue()));
                            //Recupera el nombre del box
                            dbBoxReference = FirebaseDatabase.getInstance().getReference("Boxes/"+currentUser.getBoxId());
                            dbBoxReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Box dbBox = snapshot.getValue(Box.class);
                                    //Si el usuario está adscrito a un box, lo muestra
                                    if(dbBox != null){
                                        box.setText(dbBox.getName());
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                }
                //Damos valor a los TextView
                user.setText(currentUser.getUsername());
                email.setText(currentUser.getEmail());
                contractedClasses.setText(String.valueOf(currentUser.getFee()));
                availableClasses.setText(String.valueOf(currentUser.getAvailableCredits()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Listener que vuelve a la pantalla de inicio
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Listener que permite editar el nombre del usuario actual
        btnEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Oculta el botón de editar
                btnEditUsername.setVisibility(View.GONE);
                //Muestra el botón de guardar
                btnSaveUsername.setVisibility(View.VISIBLE);
                //Oscurece el fondo del texto del WOD
                user.setBackgroundColor(getResources().getColor(R.color.darkGrey, activityTheme));
                user.setEnabled(true);
            }
        });

        //Listener que guarda el nuevo nombre de usuario
        btnSaveUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprueba que el username es válido
                if(String.valueOf(user.getText()).length() < 6){
                    Toast.makeText(UserProfile.this, R.string.profile_nameMustHaveMinCharacters, Toast.LENGTH_LONG).show();
                }else{
                    //Oculta el botón de guardar
                    btnSaveUsername.setVisibility(View.GONE);
                    //Muestra el botón de editar
                    btnEditUsername.setVisibility(View.VISIBLE);
                    user.setBackgroundColor(getResources().getColor(R.color.hollow, activityTheme));
                    user.setEnabled(false);
                    //Se modifica el nuevo nombre de usuario en la base de datos
                    dbReference = FirebaseDatabase.getInstance().getReference("Users");
                    dbReference.child(userId).child("username").setValue(String.valueOf(user.getText()));
                }
            }
        });
    }
}
