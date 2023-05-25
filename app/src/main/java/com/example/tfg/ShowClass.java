package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowClass extends AppCompatActivity {

    TextView toolbarTitle, tvName, tvTrainingStarts, tvTrainingEnds, tvReservesDone, tvCapacity;
    String userId, name, trainingStarts, trainingEnds, reservesDone, capacity, dayOfWeekName;
    int dayOfWeekNumber;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.showClass_title);

        //Coge los datos de la clase
        userId = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("name");
        trainingStarts = getIntent().getStringExtra("trainingStarts");
        trainingEnds = getIntent().getStringExtra("trainingEnds");
        reservesDone = getIntent().getStringExtra("reservesDone");
        capacity = getIntent().getStringExtra("capacity");
        dayOfWeekName = getIntent().getStringExtra("dayOfWeekName");
        dayOfWeekNumber = Integer.parseInt(getIntent().getStringExtra("dayOfWeekNumber"));

        //Se enlaza con el layout
        tvName = findViewById(R.id.showClass_name);
        tvTrainingStarts = findViewById(R.id.showClass_scheduleStart);
        tvTrainingEnds = findViewById(R.id.showClass_scheduleEnd);
        tvReservesDone = findViewById(R.id.showClass_reservationsDone);
        tvCapacity = findViewById(R.id.showClass_capacityData);

        //Se rellena el layout
        tvName.setText(name);
        tvTrainingStarts.setText(trainingStarts);
        tvTrainingEnds.setText(trainingEnds);
        tvReservesDone.setText(reservesDone);
        tvCapacity.setText(capacity);


        btnBack = findViewById(R.id.showClass_btn_back);
        //Listener que vuelve a la pantalla de gestionar box
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se pasa por parámetro el día y userId seleccionado
                Intent intent = new Intent(getApplicationContext(), ClassDay.class);
                intent.putExtra("dayOfWeekName", dayOfWeekName);
                intent.putExtra("dayOfWeekNumber", String.valueOf(dayOfWeekNumber));
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });
    }
}