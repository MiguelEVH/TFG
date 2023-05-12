package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText editTextEmail;
    Button btnResetPassword, btnBack;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //Variables
        editTextEmail = findViewById(R.id.email);
        btnResetPassword = findViewById(R.id.btn_resetPassword);
        btnBack = findViewById(R.id.btn_back);
        //Instancia de firebase
        mAuth = FirebaseAuth.getInstance();
        //ProgressBar
        progressBar = findViewById(R.id.progresBar);

        //Listener que resetea la contraseña
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Muestra la progressbar
                progressBar.setVisibility(View.VISIBLE);

                //Comprueba si el usuario ha introducido un email. Sino, avisa al usuario
                if(editTextEmail.getText().toString().isEmpty()){
                    Toast.makeText(ResetPassword.this, "Por favor, introduzca su email", Toast.LENGTH_SHORT).show();
                }else{
                    //Avisa de que el lenguaje es el español
                    mAuth.setLanguageCode("es");
                    //Manda el email al correo indicado por el usuario
                    mAuth.sendPasswordResetEmail(editTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //Hide the progressBar
                            progressBar.setVisibility(View.GONE);

                            //Avisa al usuario del resultado
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassword.this, "Revise su email para reestablecer la contraseña", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ResetPassword.this, "No se ha podido enviar el correo debido a un error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        //Listener que vuelve a la pantalla de login
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

    }
}