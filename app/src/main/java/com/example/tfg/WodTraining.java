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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.classes.Box;
import com.example.tfg.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WodTraining extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    DatabaseReference dbReference;
    TextView toolbarTitle;
    EditText wodText;
    Button btnBack;
    String userId;
    ImageButton btnEdit, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wod_training);
        Resources.Theme activityTheme = this.getTheme();

        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.checkWod_title);

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");

        //Comprueba que el usuario está autenticado, sino, lo redirige a la pantalla de login
        checkLoggedUser();
        //Comprueba el rol y las funcionalidades que puede realizar el usuario
        checkRol();

        //Botones y texto de la activity
        btnBack = findViewById(R.id.checkWod_btnBack);
        btnEdit = findViewById(R.id.checkWod_btnEdit);
        btnSave = findViewById(R.id.checkWod_btnSave);
        wodText = findViewById(R.id.checkWod_WodText);

        //Desactiva el editText del texto del WOD
        wodText.setEnabled(false);

        //Muestra el entrenamiento del día. Para ello, primero comprueba si el usuario tiene box
        dbReference = FirebaseDatabase.getInstance().getReference("Users/"+userId);
        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    //Coge los datos del usuario
                    User currentUser = new User();
                    if(child.getKey().equals("boxId")){
                        currentUser.setBoxId(String.valueOf(child.getValue()));
                        //Si el usuario tiene un box, coge la dirección
                        if(String.valueOf(child.getValue()).isEmpty()) {
                            //Si no tiene box, muestra el mensaje
                            Toast.makeText(WodTraining.this, R.string.checkWod_userWithoutBox, Toast.LENGTH_SHORT).show();
                        }else{
                            //Se hace referencia al box del usuario
                            dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+currentUser.getBoxId());
                            dbReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //Se cogen los datos del box
                                    Box box = snapshot.getValue(Box.class);
                                    if(box.getWod() != null){
                                        wodText.setText(box.getWod());
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Listener del botón de editar WOD
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clarea el fondo del texto del WOD
                wodText.setBackgroundColor(getResources().getColor(R.color.white, activityTheme));
                //El texto puede ser editable
                wodText.setEnabled(true);
            }
        });

        //Listener del botón de guardar WOD
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Oscurece el fondo del texto del WOD
                wodText.setBackgroundColor(getResources().getColor(R.color.darkGrey, activityTheme));
                //El texto deja de poder ser editable
                wodText.setEnabled(false);
                //Actualiza el entrenamiento del día
                dbReference = FirebaseDatabase.getInstance().getReference("Boxes");
                dbReference.child(userId+"_box").child("wod").setValue(String.valueOf(wodText.getText()));
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

    //Método las funcionalidades que puede realizar el usuario según su rol
    public void checkRol(){
        //Comprueba el rol del usuario.
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si no es entrenador, oculta el botón de gestionar box
                if(!snapshot.child(fbUser.getUid()).child("coach").getValue(boolean.class)){
                    btnEdit.setVisibility(View.GONE);
                    btnSave.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}