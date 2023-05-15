package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    DatabaseReference dbReference;
    Button btnManageBox, btnCheckWod, btnReservations, btnPersonalBest, btnTutorials;
    TextView toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.main_title);

        //Comprueba que el usuario está autenticado, sino, lo redirige a la pantalla de login
        checkLoggedUser();
        //Comprueba el rol y las funcionalidades que puede realizar el usuario
        checkRol();

        //Botones de la activity
        btnManageBox = findViewById(R.id.main_btn_manageBox);
        btnCheckWod = findViewById(R.id.main_btn_checkWod);
        btnReservations = findViewById(R.id.main_btn_reservations);
        btnPersonalBest = findViewById(R.id.main_btn_personalBest);
        btnTutorials = findViewById(R.id.main_btn_tutorials);

        //Botón que navega a la activity de ver el entrenamiento diario del box
        btnCheckWod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WodTraining.class);
                startActivity(intent);
            }
        });
    }

    //Se declara y crea el menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Método que controla qué item se ha seleccionado del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        //Comprueba qué botón se ha pulsado
        switch(item.getItemId()){
            case R.id.menu_my_profile:
                intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                break;
            case R.id.menu_check_box:
                intent = new Intent(getApplicationContext(), BoxProfile.class);
                startActivity(intent);
                break;
            case R.id.menu_log_out:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_cancelar: //No hace nada.
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    btnManageBox.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
