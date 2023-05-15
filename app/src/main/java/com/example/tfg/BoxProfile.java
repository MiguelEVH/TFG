package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.classes.Box;
import com.example.tfg.classes.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BoxProfile extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;
    TextView toolbarTitle;
    EditText boxName, boxAddress;
    Button btnBack;
    ImageButton btnEditBoxName, btnSaveBoxName, btnEditBoxAddress, btnSaveBoxAddress;
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    DatabaseReference dbReference;
    GoogleMap boxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_profile);
        Resources.Theme activityTheme = this.getTheme();
        //Se crea la action bar
        toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        //Se pone el título de la activity
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.profile_title);
        //Pone el logo de la app en la toolbar
        toolbar.setLogo(R.mipmap.ic_launcher);

        //Variables
        btnBack = findViewById(R.id.profilebox_btn_back);
        boxName = findViewById(R.id.profilebox_name_data);
        boxAddress = findViewById(R.id.profilebox_address_data);
        btnEditBoxName = findViewById(R.id.profilebox_btn_name_edit);
        btnSaveBoxName = findViewById(R.id.profilebox_btn_name_save);
        btnEditBoxAddress = findViewById(R.id.profilebox_btn_address_edit);
        btnSaveBoxAddress = findViewById(R.id.profilebox_btn_address_save);

        //Fragment del mapa del box
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.profilebox_boxLocationMap);
        mapFragment.getMapAsync(this);

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

        //Comprueba el rol del usuario.
        dbReference = FirebaseDatabase.getInstance().getReference("Users/"+fbUser.getUid());
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Coge los datos del usuario
                User currentUser = snapshot.getValue(User.class);
                //Oculta las opciones de edición si el usuario no es entrenador
                if(!snapshot.child("coach").getValue(boolean.class)){
                    btnEditBoxName.setVisibility(View.GONE);
                    btnEditBoxAddress.setVisibility(View.GONE);
                }
                //Si el usuario tiene un box, muestra los datos de este
                if(currentUser.getBoxId() != null){
                    //Se hace referencia al box del usuario
                    dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+currentUser.getBoxId());
                    dbReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Box box = snapshot.getValue(Box.class);
                            boxName.setText(box.getName());
                            boxAddress.setText(box.getAddress());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Permite a un entrenador el editar el nombre de un box
        btnEditBoxName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Oculta el botón de editar
                btnEditBoxName.setVisibility(View.GONE);
                //Muestra el botón de guardar
                btnSaveBoxName.setVisibility(View.VISIBLE);
                //Clarea el fondo del texto del WOD
                boxName.setBackgroundColor(getResources().getColor(R.color.white, activityTheme));
                boxName.setEnabled(true);
            }
        });

        //Permite a un entrenador el guardar el nombre de un box
        btnSaveBoxName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Oculta el botón de guardar
                btnSaveBoxName.setVisibility(View.GONE);
                //Muestra el botón de editar
                btnEditBoxName.setVisibility(View.VISIBLE);
                boxName.setBackgroundColor(getResources().getColor(R.color.hollow, activityTheme));
                boxName.setEnabled(false);
                //Se modifica el nuevo nombre del box en la base de datos
                dbReference = FirebaseDatabase.getInstance().getReference("Boxes");
                dbReference.child(fbUser.getUid()+"_box").child("name").setValue(String.valueOf(boxName.getText()));
            }
        });

        btnEditBoxAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Muestra el botón de editar
                btnSaveBoxAddress.setVisibility(View.VISIBLE);
                //Oculta el botón de guardar
                btnEditBoxAddress.setVisibility(View.GONE);
                boxAddress.setBackgroundColor(getResources().getColor(R.color.white, activityTheme));
                boxAddress.setEnabled(true);
            }
        });

        btnSaveBoxAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Muestra el botón de editar
                btnEditBoxAddress.setVisibility(View.VISIBLE);
                //Oculta el botón de guardar
                btnSaveBoxAddress.setVisibility(View.GONE);
                boxAddress.setBackgroundColor(getResources().getColor(R.color.hollow, activityTheme));
                boxAddress.setEnabled(false);
                //Se modifica el nuevo nombre de usuario en la base de datos
                dbReference = FirebaseDatabase.getInstance().getReference("Boxes");
                dbReference.child(fbUser.getUid()+"_box").child("address").setValue(String.valueOf(boxAddress.getText()));
            }
        });

        //Listener que vuelve a la pantalla de inicio
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        //Comprueba si el usuario tiene box
        dbReference = FirebaseDatabase.getInstance().getReference("Users/"+fbUser.getUid());
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Coge los datos del usuario
                User currentUser = snapshot.getValue(User.class);
                //Si el usuario tiene un box, coge la dirección
                if(currentUser.getBoxId() != null){
                    //Se hace referencia al box del usuario
                    dbReference = FirebaseDatabase.getInstance().getReference("Boxes/"+currentUser.getBoxId());
                    dbReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Se cogen los datos del box
                            Box box = snapshot.getValue(Box.class);
                            //Se declara el mapa y se muestra la localización del box según la dirección de este
                            boxMap = googleMap;
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                //Coge la geolocalización del box según su dirección
                                List<Address> addressList = geocoder.getFromLocationName(String.valueOf(box.getAddress()), 1);
                                //Si la dirección no es válida, avisa del error
                                if(addressList.isEmpty()){
                                    Toast.makeText(BoxProfile.this, "Dirección no válida", Toast.LENGTH_SHORT).show();
                                }else{
                                    //Coge las coordenadas del box
                                    LatLng boxCoordinates = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                                    //Añade un marcador con las coordenadas del box y mueve la cámara a este
                                    boxMap.addMarker(new MarkerOptions().position(boxCoordinates));
                                    boxMap.moveCamera(CameraUpdateFactory.newLatLng(boxCoordinates));
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}