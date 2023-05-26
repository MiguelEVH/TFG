package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCrossfitter extends AppCompatActivity {

    DatabaseReference dbReference, dbReferenceCredits;
    TextView toolbarTitle, usernameData, emailData;
    EditText feeData;
    Button btnBack, btnAddCrossfitter;
    String userId, newCrossfitterId, newCrossfitterUsername, newCrossfitterEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crossfitter);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.crossfittersManagement_title);

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");
        newCrossfitterId = getIntent().getStringExtra("newCrossfitterId");
        newCrossfitterUsername = getIntent().getStringExtra("newCrossfitterUsername");
        newCrossfitterEmail = getIntent().getStringExtra("newCrossfitterEmail");

        //Coge los textviews y edittexts de la activity
        usernameData = findViewById(R.id.addCrossfitter_usernameData);
        emailData = findViewById(R.id.addCrossfitter_emailData);
        feeData = findViewById(R.id.addCrossfitter_feeData);

        //Pone el nombre del usuario y email
        usernameData.setText(newCrossfitterUsername);
        emailData.setText(newCrossfitterEmail);

        //Botón de añadir crossfiteros
        btnAddCrossfitter = findViewById(R.id.addCrossfitter_btn_addCrossfitter);
        btnAddCrossfitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Coge la tarifa introducida
                if(String.valueOf(feeData.getText()).equals("")){
                    Toast.makeText(AddCrossfitter.this, R.string.addCrossfitter_insertWeekClasses, Toast.LENGTH_SHORT).show();
                }else{
                    int fee = Integer.valueOf(String.valueOf(feeData.getText()));
                    //Referencia a las clases del usuario en la base de datos y se actualiza
                    dbReference = FirebaseDatabase.getInstance().getReference("Users/"+newCrossfitterId+"/fee");
                    dbReference.setValue(fee);
                    //Referencia a las reservas disponibles del usuario
                    dbReference = FirebaseDatabase.getInstance().getReference("Users/"+newCrossfitterId+"/availableCredits");
                    dbReference.setValue(fee);
                    //Referencia al box del usuario y lo actualiza
                    dbReference = FirebaseDatabase.getInstance().getReference("Users/"+newCrossfitterId+"/boxId");
                    dbReference.setValue(userId+"_box");
                    //Vuelve a la activity de gestión de crossfiteros
                    Intent intent = new Intent(getApplicationContext(), CrossfittersManagement.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //Boton que hace volver a la pantalla de gestión de box
        btnBack = findViewById(R.id.addCrossfitter_btn_back);
        //Listener que vuelve a la de gestión de box
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CrossfittersManagement.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });
    }
}