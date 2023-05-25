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

import com.example.tfg.classes.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateBoxCrossfitter extends AppCompatActivity {

    DatabaseReference dbReference;
    TextView toolbarTitle, usernameData, emailData;
    EditText feeData;
    Button btnBack, btnUpdateFee, btnDeleteCrossfitterFromBox;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_box_crossfitter);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.updateBoxCrossfitter_title);

        //Id del entrenador
        userId = getIntent().getStringExtra("userId");

        //Se cogen los datos del crossfitero
        User crossfitter = new User();
        crossfitter.setId(getIntent().getStringExtra("crossfitterId"));
        crossfitter.setUsername(getIntent().getStringExtra("crossfitterUsername"));
        crossfitter.setEmail(getIntent().getStringExtra("crossfitterEmail"));
        crossfitter.setFee(Integer.parseInt(getIntent().getStringExtra("crossfitterFee")));

        //Referencias a los textview y edittext de la activity
        usernameData = findViewById(R.id.updateBoxCrossfitter_usernameData);
        emailData = findViewById(R.id.updateBoxCrossfitter_emailData);
        feeData = findViewById(R.id.updateBoxCrossfitter_feeData);

        //Se rellena el formulario
        usernameData.setText(crossfitter.getUsername());
        emailData.setText(crossfitter.getEmail());
        feeData.setText(String.valueOf(crossfitter.getFee()));

        //Listener que borra el crossfitero actual
        btnDeleteCrossfitterFromBox = findViewById(R.id.updateBoxCrossfitter_btn_deleteCrossfitter);
        btnDeleteCrossfitterFromBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId.equals(crossfitter.getId())){
                    Toast.makeText(UpdateBoxCrossfitter.this, "No se puede eliminar al entrenador de su box", Toast.LENGTH_SHORT).show();
                }else{
                    //Referencia a las clases del usuario en la base de datos y elimina la referencia al box
                    dbReference = FirebaseDatabase.getInstance().getReference("Users/"+crossfitter.getId()+"/boxId");
                    dbReference.removeValue();
                    Toast.makeText(UpdateBoxCrossfitter.this, "Crossfitero eliminado: " + crossfitter.getUsername(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CrossfittersManagement.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnUpdateFee = findViewById(R.id.updateBoxCrossfitter_btn_updateFee);
        btnUpdateFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Coge la nueva tarifa
                crossfitter.setFee(Integer.parseInt( String.valueOf(feeData.getText())));
                //Referencia a las clases del usuario en la base de datos y se actualiza
                dbReference = FirebaseDatabase.getInstance().getReference("Users/"+crossfitter.getId()+"/fee");
                dbReference.setValue(crossfitter.getFee());
                Toast.makeText(UpdateBoxCrossfitter.this, "Tarifa actualizada", Toast.LENGTH_SHORT).show();
            }
        });

        //Boton que hace volver a la pantalla de gestión de box
        btnBack = findViewById(R.id.updateBoxCrossfitter_btn_back);
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