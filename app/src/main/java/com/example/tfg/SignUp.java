package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.classes.Box;
import com.example.tfg.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    TextInputEditText editTextUser, editTextEmail, editTextPassword, editTextRepeatPassword;
    RadioGroup radioGroup;
    RadioButton radioButton_no;
    Button btnSignUp;
    TextView textViewAlreadyRegistered;
    ImageView crossWOD;
    ProgressBar progressBar;
    DatabaseReference dbUsers, dbBox;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    boolean isCoach;

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
        setContentView(R.layout.activity_sign_up);

        //Firebase
        auth = FirebaseAuth.getInstance();

        //ProgressBar
        progressBar = findViewById(R.id.progresBar);

        //Variables
        crossWOD = findViewById(R.id.signUp_crossWOD);
        editTextUser = findViewById(R.id.user);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextRepeatPassword = findViewById(R.id.repeatPassword);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_no = findViewById(R.id.rb_no);
        btnSignUp = findViewById(R.id.btn_signup);
        textViewAlreadyRegistered = findViewById(R.id.btn_alreadyRegistered);
        isCoach = false;

        //Comprueba los píxeles de la pantalla
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //Si es una pantalla pequeña, oculta el logo
        if(metrics.densityDpi < 700){
            crossWOD.setVisibility(View.GONE);
        }



        //Conecta con los usuarios y boxes de la base de datos
        dbUsers = FirebaseDatabase.getInstance().getReference("Users");
        dbBox = FirebaseDatabase.getInstance().getReference("Boxes");

        //Go to LogIn Activity
        textViewAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        //Comprueba si el usuario selecciona si es entrenador o no
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_no:
                        isCoach = false;
                        break;
                    case R.id.rb_yes:
                        isCoach = true;
                        break;
                }
            }
        });

        //Sign up button listener
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Muestra la progressbar
                progressBar.setVisibility(View.VISIBLE);

                //Declara las variables y coge los valores del formulario
                String username, email, password, repeatPassword;

                username = String.valueOf(editTextUser.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                repeatPassword = String.valueOf(editTextRepeatPassword.getText());

                //Comprueba que se han introducido los datos requeridos
                if(!userEnteredCorrectly(username)){
                    Toast.makeText(SignUp.this, "Introduzca un usuario", Toast.LENGTH_SHORT).show();
                    //Oculta la progressbar
                    progressBar.setVisibility(View.GONE);
                    return;
                }else if(!emailEnteredCorrectly(email)){
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
                }else if(!validPassword(password, repeatPassword)){
                    Toast.makeText(SignUp.this, "Repita la contraseña correctamente", Toast.LENGTH_SHORT).show();
                    //Oculta la progressbar
                    progressBar.setVisibility(View.GONE);
                    return;
                }


                //Crea el usuario mediante email y password
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(SignUp.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                                    //Usuario actual
                                    currentUser = auth.getCurrentUser();
                                    //Si es entrenador, le asigna un box que se creará posteriormente
                                    String boxId = "";
                                    if(isCoach){
                                        boxId = currentUser.getUid() + "_box";
                                    }
                                    //Crea el usuario
                                    User user = new User(username, email, isCoach, boxId, 0, 0);
                                    //Se crea el usuario en "Users"
                                    dbUsers.child(currentUser.getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        //Si es entrenador le crea un box asociado al entrenador
                                                        if(isCoach){
                                                            //Se crea el box
                                                            Box box = new Box(user.getUsername()+"'s box", "Box address");
                                                            dbBox.child(currentUser.getUid() + "_box")
                                                                    .setValue(box);
                                                        }
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUp.this, "No se ha podido crear el usuario", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    //Oculta la progressbar
                                    progressBar.setVisibility(View.GONE);
                                    //Se crea el box y se asigna al entrenador
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    //Oculta la progressbar
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUp.this, "Ha habido un problema", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    public boolean userEnteredCorrectly(String username){
        if(TextUtils.isEmpty(username)){
            return false;
        }else{
            return true;
        }
    }

    //Método que comprueba si el email es válido
    public boolean emailEnteredCorrectly(String email){
        //Patron de un email
        String regex = "^(.+)@(.+)$";
        //Se introduce el patron y email
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        //Comprueba que el email introducido es válido
        if(matcher.matches()){
            return true;
        }else{
            return false;
        }
    }

    //Método que comprueba si la contraseña se ha introducido correctamente
    private boolean validPassword(String password, String repeatPassword) {
        if(password.equals(repeatPassword)){
           return true;
        }else{
            return false;
        }
    }

}