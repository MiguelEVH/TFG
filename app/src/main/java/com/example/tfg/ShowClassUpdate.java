package com.example.tfg;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tfg.classes.Training;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class ShowClassUpdate extends AppCompatActivity {

    TextView toolbarTitle;
    EditText etCapacity;
    DatabaseReference dbReference;
    String dayOfWeekName, userId, trainingId, trainingName, trainingStarts, trainingEnds;
    Button btnBack, btnUpdateClass, btnDeleteClass, btnStartTime, btnEndTime;
    int dayOfWeekNumber, startHour, startMinute, endHour, endMinute, capacity;
    Spinner classTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class_update);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.showClassUpdate_title);

        //Coge los datos de la clase
        userId = getIntent().getStringExtra("userId");
        trainingId = getIntent().getStringExtra("trainingId");
        trainingName = getIntent().getStringExtra("name");
        trainingStarts = getIntent().getStringExtra("trainingStarts");
        trainingEnds = getIntent().getStringExtra("trainingEnds");
        capacity = Integer.parseInt(getIntent().getStringExtra("capacity"));
        dayOfWeekName = getIntent().getStringExtra("dayOfWeekName");
        dayOfWeekNumber = Integer.parseInt(getIntent().getStringExtra("dayOfWeekNumber"));

        //Coge la hora y minuto en la que empieza la clase
        String[] strTimeArray = trainingStarts.split(":");
        int[] intTimeArray = new int[strTimeArray.length];
        for(int i = 0; i < strTimeArray.length; i++) {
            intTimeArray[i] = Integer.parseInt(strTimeArray[i]);
        }
        startHour = intTimeArray[0];
        startMinute = intTimeArray[1];

        //Coge la hora y minuto en la que acaba la clase
        strTimeArray = trainingStarts.split(":");
        intTimeArray = new int[strTimeArray.length];
        for(int i = 0; i < strTimeArray.length; i++) {
            intTimeArray[i] = Integer.parseInt(strTimeArray[i]);
        }
        endHour = intTimeArray[0];
        endMinute = intTimeArray[1];

        //Coge los elementos del formulario
        classTypeSpinner = findViewById(R.id.showClassUpdate_classSpinner);
        //Crea el desplegable de tipos de clase
        ArrayAdapter<CharSequence> classTypeAdapter = ArrayAdapter.createFromResource(this, R.array.class_types, android.R.layout.simple_dropdown_item_1line);
        classTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        classTypeSpinner.setAdapter(classTypeAdapter);
        classTypeSpinner.setSelection(classTypeAdapter.getPosition(trainingName));

        //Se enlaza con el layout
        btnStartTime = findViewById(R.id.showClassUpdate_btn_startTime);
        btnEndTime = findViewById(R.id.showClassUpdate_btn_endTime);
        etCapacity = findViewById(R.id.showClassUpdate_capacity);

        //Se rellena el layout
        btnStartTime.setText(trainingStarts);
        btnEndTime.setText(trainingEnds);
        etCapacity.setText(String.valueOf(capacity));

        //Boton para coger la hora y minuto de inicio de clase
        btnStartTime = findViewById(R.id.showClassUpdate_btn_startTime);
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourSelected, int minuteSelected) {
                        startHour = hourSelected;
                        startMinute = minuteSelected;
                        //Se le da formato al timer
                        btnStartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));
                    }
                };
                //Se le da formato de spinner
                TimePickerDialog startTimePickerDialog = new TimePickerDialog(ShowClassUpdate.this, onTimeSetListener, startHour, startMinute, true);
                startTimePickerDialog.show();
            }
        });

        //Boton para coger la hora y minuto de fin de clase
        btnEndTime = findViewById(R.id.showClassUpdate_btn_endTime);
        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourSelected, int minuteSelected) {
                        endHour = hourSelected;
                        endMinute = minuteSelected;
                        //Se le da formato al timer
                        btnEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute));
                    }
                };

                //Se le da formato de spinner
                TimePickerDialog endTimePickerDialog = new TimePickerDialog(ShowClassUpdate.this, onTimeSetListener, endHour, endMinute, true);
                endTimePickerDialog.show();
            }
        });

        //Listener que elimina la clase
        btnDeleteClass = findViewById(R.id.showClassUpdate_btn_deleteClass);
        btnDeleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Referencia a la clase en la base de datos
                dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+userId+"_box"+"/classes/" + dayOfWeekNumber + "/" + trainingId);
                //Elimina la clase
                dbReference.removeValue();
                Toast.makeText(ShowClassUpdate.this, "Se ha eliminado la clase", Toast.LENGTH_SHORT).show();
                //Cambia de activity
                Intent intent = new Intent(getApplicationContext(), ClassDay.class);
                intent.putExtra("dayOfWeekName", dayOfWeekName);
                intent.putExtra("dayOfWeekNumber", String.valueOf(dayOfWeekNumber));
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

        //Listener que actualiza la clase
        btnUpdateClass = findViewById(R.id.showClassUpdate_btn_updateClass);
        btnUpdateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprueba que se ha introducido un aforo
                if(Integer.valueOf(String.valueOf(etCapacity.getText())) == null){
                    Toast.makeText(ShowClassUpdate.this, "Introduzca el aforo de la clase", Toast.LENGTH_SHORT).show();
                }else{
                    //Se crea el entrenamiento
                    Training newTraining = new Training();
                    newTraining.setName(classTypeSpinner.getSelectedItem().toString());
                    newTraining.setTrainingStarts(fixTime(startHour)+ ":" + fixTime(startMinute));
                    newTraining.setTrainingEnds(fixTime(endHour)+ ":"+ fixTime(endMinute));
                    newTraining.setCapacity(Integer.valueOf(String.valueOf(etCapacity.getText())));
                    //Referencia a la clase en la base de datos
                    dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+userId+"_box"+"/classes/" + dayOfWeekNumber + "/" + trainingId);
                    //Actualiza la clase
                    dbReference.setValue(newTraining);
                    //Cambia de activity
                    Intent intent = new Intent(getApplicationContext(), ClassDay.class);
                    intent.putExtra("dayOfWeekName", dayOfWeekName);
                    intent.putExtra("dayOfWeekNumber", String.valueOf(dayOfWeekNumber));
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnBack = findViewById(R.id.showClassUpdate_btn_back);
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

    //Variable que arregla el tiempo para que tenga 2 cifras en string
    public String fixTime(int time){
        String fixedTime;
        //Si el tiempo es menor a 10, le agrega un 0. Sino, no hace nada
        if(time < 10){
            fixedTime = "0"+time;
            return fixedTime;
        }else{
            return String.valueOf(time);
        }
    }
}