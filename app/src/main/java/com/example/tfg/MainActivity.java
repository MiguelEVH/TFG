package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        //Se instancia la autenticación de Firebase
        auth = FirebaseAuth.getInstance();
        //Coge el usuario actual
        user = auth.getCurrentUser();

        //Si no hay un usuario con sesión iniciada, vuelve a la pantalla de login.
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();
        }
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
