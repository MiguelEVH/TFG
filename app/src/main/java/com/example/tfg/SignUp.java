package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class SignUp extends AppCompatActivity {

    TextInputEditText editTextUser, editTextEmail, editTextPassword, editTextRepeatPassword;
    Button buttonSignUp;
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
        setContentView(R.layout.activity_sign_up);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //ProgressBar
        progressBar = findViewById(R.id.progresBar);

        //Variables
        editTextUser = findViewById(R.id.user);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextRepeatPassword = findViewById(R.id.repeatPassword);
        buttonSignUp = findViewById(R.id.btn_signup);
        textViewAlreadyRegistered = findViewById(R.id.btn_alreadyRegistered);

        //Go to LogIn Activity
        textViewAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        //Sign up button listener
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Muestra la progressbar
                progressBar.setVisibility(View.VISIBLE);

                String user, email, password, repeatPassword;
                user = String.valueOf(editTextUser);
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                repeatPassword = String.valueOf(editTextRepeatPassword.getText());

                //Comprueba que se han introducido los datos requeridos
                if(TextUtils.isEmpty(user)){
                    Toast.makeText(SignUp.this, "Introduzca un usuario", Toast.LENGTH_SHORT).show();
                    //Oculta la progressbar
                    progressBar.setVisibility(View.GONE);
                    return;
                }else if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUp.this, "Introduzca un email", Toast.LENGTH_SHORT).show();
                    //Oculta la progressbar
                    progressBar.setVisibility(View.GONE);
                    return;
                } else if(TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUp.this, "Introduzca una contraseña", Toast.LENGTH_SHORT).show();
                    //Oculta la progressbar
                    progressBar.setVisibility(View.GONE);
                    return;
                }else if(TextUtils.isEmpty(repeatPassword)) {
                    Toast.makeText(SignUp.this, "Repita la contraseña", Toast.LENGTH_SHORT).show();
                    //Oculta la progressbar
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(!password.equals(repeatPassword)){
                    Toast.makeText(SignUp.this, "Repita la contraseña corectamente", Toast.LENGTH_SHORT).show();
                    //Oculta la progressbar
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //Oculta la progressbar
                                progressBar.setVisibility(View.GONE);

                                if(task.isSuccessful()){
                                    Toast.makeText(SignUp.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(SignUp.this, "Ha habido un problema", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}