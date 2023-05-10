package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    TextInputEditText editTextUser, editTextPassword;
    Button buttonLogIn;
    TextView textViewAlreadyRegistered;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    //Método que comprueba si el usuario ya ha iniciado sesión
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Si el usuario ya ha iniciado sesión, navega a la página de inicio.
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //ProgressBar
        progressBar = findViewById(R.id.progresBar);

        //Variables
        editTextUser = findViewById(R.id.user);
        editTextPassword = findViewById(R.id.password);
        buttonLogIn = findViewById(R.id.btn_login);
        textViewAlreadyRegistered = findViewById(R.id.btn_areYouNew);

        //Go to LogIn Activity
        textViewAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Muestra la progressbar
                progressBar.setVisibility(View.VISIBLE);

                String email, password;
                email = String.valueOf(editTextUser.getText());
                password = String.valueOf(editTextPassword.getText());

                //Comprueba que se han introducido los datos requeridos
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LogIn.this, "Introduzca su email", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(password)) {
                    Toast.makeText(LogIn.this, "Introduzca su contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //Hide the progressBar
                                progressBar.setVisibility(View.GONE);

                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LogIn.this, "No se ha podido iniciar sesión", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));

            }
        });

    }
}