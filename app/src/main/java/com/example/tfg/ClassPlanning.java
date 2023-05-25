package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tfg.adapters.ClassPlanningBaseAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ClassPlanning extends AppCompatActivity {

    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    TextView toolbarTitle;
    ArrayList<String> daysOfWeek = new ArrayList<>();
    ListView daysOfWeekListView;
    Button btnBack;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_planning);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.classPlanning_title);

        //Se coge la id del crossfitero
        userId = getIntent().getStringExtra("userId");

        //Comprueba que el usuario está autenticado, sino, lo redirige a la pantalla de login
        checkLoggedUser();

        //Rellena el arrayList de dias de la semana
        fillDays();

        //Crea la lista de dias
        daysOfWeekListView = findViewById(R.id.classPlanning_listView);
        ClassPlanningBaseAdapter customBaseAdapter = new ClassPlanningBaseAdapter(getApplicationContext(), daysOfWeek);
        daysOfWeekListView.setAdapter(customBaseAdapter);

        //Listener que muestra las clases que hay un día en concreto
        daysOfWeekListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Se pasa por parámetro el día seleccionado
                Intent intent = new Intent(getApplicationContext(), ClassDay.class);
                intent.putExtra("dayOfWeekName", daysOfWeek.get(i));
                intent.putExtra("dayOfWeekNumber", String.valueOf(i));
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnBack = findViewById(R.id.classPlanning_btn_back);
        //Listener que vuelve a la pantalla de gestionar box
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //Si el usuario no tiene una sesión iniciada, lo retorna a la pantalla de login
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

    //Introduce los dias de la semana
    public void fillDays(){
        daysOfWeek.add(getResources().getString(R.string.classPlanning_monday));
        daysOfWeek.add(getResources().getString(R.string.classPlanning_tuesday));
        daysOfWeek.add(getResources().getString(R.string.classPlanning_wednesday));
        daysOfWeek.add(getResources().getString(R.string.classPlanning_thursday));
        daysOfWeek.add(getResources().getString(R.string.classPlanning_friday));
        daysOfWeek.add(getResources().getString(R.string.classPlanning_saturday));
        daysOfWeek.add(getResources().getString(R.string.classPlanning_sunday));
    }

}