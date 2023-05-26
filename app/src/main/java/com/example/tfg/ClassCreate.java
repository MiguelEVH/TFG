package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.example.tfg.classes.Training;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class ClassCreate extends AppCompatActivity {

    TextView toolbarTitle;
    EditText capacity;
    DatabaseReference dbReference;
    String dayOfWeekName, userId;
    Button btnBack, createClass, btnStartTime, btnEndTime;
    int dayOfWeekNumber, startHour, startMinute, endHour, endMinute;
    Spinner classTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_create);
        //Se crea la action bar
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.classCreate_title);

        //Se coge la id del crossfitero
        dayOfWeekName = getIntent().getStringExtra("dayOfWeekName");
        dayOfWeekNumber = Integer.parseInt(getIntent().getStringExtra("dayOfWeekNumber"));
        userId = getIntent().getStringExtra("userId");

        //Referencia a la capacidad del formulario
        capacity = findViewById(R.id.classCreate_capacity);

        //Coge los elementos del formulario
        classTypeSpinner = findViewById(R.id.classCreate_classSpinner);
        //Crea el desplegable de tipos de clase
        ArrayAdapter<CharSequence> classTypeAdapter = ArrayAdapter.createFromResource(this, R.array.class_types, android.R.layout.simple_dropdown_item_1line);
        classTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        classTypeSpinner.setAdapter(classTypeAdapter);

        //Boton para coger la hora y minuto de inicio de clase
        btnStartTime = findViewById(R.id.classCreate_btn_startTime);
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
                TimePickerDialog startTimePickerDialog = new TimePickerDialog(ClassCreate.this, onTimeSetListener, startHour, startMinute, true);
                startTimePickerDialog.show();
            }
        });

        //Boton para coger la hora y minuto de fin de clase
        btnEndTime = findViewById(R.id.classCreate_btn_endTime);
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
                TimePickerDialog endTimePickerDialog = new TimePickerDialog(ClassCreate.this, onTimeSetListener, endHour, endMinute, true);
                endTimePickerDialog.show();
            }
        });

        //Listener que crea una clase
        createClass = findViewById(R.id.classCreate_btn_createClass);
        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Comprueba si se ha rellenado el formulario
                if((btnStartTime.getText() == null) || (btnEndTime.getText() == null)){
                    Toast.makeText(ClassCreate.this, R.string.classCreate_insertClassHour, Toast.LENGTH_SHORT).show();
                } else if (String.valueOf(capacity.getText()).equals("")) {
                    Toast.makeText(ClassCreate.this, R.string.classCreate_insertClassCapacity, Toast.LENGTH_SHORT).show();
                }else{
                    //Se crea el id de la clase
                    Calendar calendar = Calendar.getInstance();
                    String trainingId = startHour+ "_" + startMinute+"_"+calendar.getTimeInMillis();
                    //Se crea el entrenamiento
                    Training newTraining = new Training();
                    newTraining.setName(classTypeSpinner.getSelectedItem().toString());
                    newTraining.setTrainingStarts(fixTime(startHour)+ ":" + fixTime(startMinute));
                    newTraining.setTrainingEnds(fixTime(endHour)+ ":"+ fixTime(endMinute));
                    newTraining.setCapacity(Integer.valueOf(String.valueOf(capacity.getText())));
                    //Referencia a la clase en la base de datos
                    dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+userId+"_box"+"/classes/" + dayOfWeekNumber + "/" + trainingId);
                    //Crea la clase
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

        btnBack = findViewById(R.id.classCreate_btn_back);
        //Listener que vuelve a la pantalla de clases del día
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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