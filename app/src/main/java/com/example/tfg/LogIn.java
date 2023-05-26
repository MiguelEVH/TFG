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
    TextView textViewAlreadyRegistered, textViewForgotPassword;
    FirebaseAuth auth;
    ProgressBar progressBar;

    //Método que comprueba si el usuario ya ha iniciado sesión
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
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
        auth = FirebaseAuth.getInstance();

        //ProgressBar
        progressBar = findViewById(R.id.progresBar);

        //Variables
        editTextUser = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogIn = findViewById(R.id.btn_login);
        textViewAlreadyRegistered = findViewById(R.id.btn_areYouNew);
        textViewForgotPassword = findViewById(R.id.btn_forgotPassword);

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
                //Coge el email y la contraseña
                String email, password;
                email = String.valueOf(editTextUser.getText());
                password = String.valueOf(editTextPassword.getText());
                //Comprueba que se han introducido los datos requeridos
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LogIn.this, R.string.insertEmail, Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(password)) {
                    Toast.makeText(LogIn.this, R.string.insertPassword, Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Oculta la progressBar
                            progressBar.setVisibility(View.GONE);
                            //Si el login es correcto, navega a la pantalla de inicio. Si no, avisa de error
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LogIn.this, R.string.loginFailed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }));
            }
        });

        //Navega a la vista de recuperar la contraseña
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }
}