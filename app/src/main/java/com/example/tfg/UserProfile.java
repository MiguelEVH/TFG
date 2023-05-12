package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    TextView toolbarTitle, user, email, box, contractedClasses, availableClasses;
    Button btnBack;
    FirebaseAuth auth;
    FirebaseUser fbUser;
    DatabaseReference dbUsers, dbBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //Se crea la action bar
        toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        //Se pone el título de la activity
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.profile_title);
        //Pone el logo de la app en la toolbar
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Variables
        user = findViewById(R.id.profile_user_data);
        email = findViewById(R.id.profile_email_data);
        box = findViewById(R.id.profile_box_data);
        contractedClasses = findViewById(R.id.profile_contractedClasses_data);
        availableClasses = findViewById(R.id.profile_availableClasses_data);
        btnBack = findViewById(R.id.btn_profile_back);

        //Se instancia la autenticación de Firebase
        auth = FirebaseAuth.getInstance();
        //Coge el usuario actual
        fbUser = auth.getCurrentUser();

        //Si no hay un usuario con sesión iniciada, vuelve a la pantalla de login.
        if(fbUser == null){
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();
        }

        //Recupera los datos del usuario actual
        dbUsers = FirebaseDatabase.getInstance().getReference("Users/"+fbUser.getUid());
        dbUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                //Toast.makeText(UserProfile.this, user.getUsername(), Toast.LENGTH_SHORT).show();
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

        //Recupera el nombre del box
        String test = "Boxes/"+fbUser.getUid()+"_box";
        dbBox = FirebaseDatabase.getInstance().getReference(test);
        dbBox.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Box dbBox = snapshot.getValue(Box.class);
                box.setText(dbBox.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Listener que vuelve a la pantalla de login
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
}
