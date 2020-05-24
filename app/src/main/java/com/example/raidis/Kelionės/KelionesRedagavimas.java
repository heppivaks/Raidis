package com.example.raidis.Kelionės;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.raidis.Duomenys.Trip;
import com.example.raidis.Fragmentai.DateFragment;
import com.example.raidis.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Calendar;

public class KelionesRedagavimas extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextView addDestDate, addDestTime;
    EditText addDestFrom, addDestTo, addAutomobile, addFreeSeats;

    Button addTrip;

    FirebaseAuth firebaseAuth;
    FirebaseUser vartotojas;

    FirebaseStorage storage;
    StorageReference reference;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keliones_redagavimas);

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = storage.getReference();
        databaseReference = database.getReference();

        addDestFrom = (EditText) findViewById(R.id.destFrom_laukas);
        addDestTo = (EditText) findViewById(R.id.destTo_laukas);
        addDestDate = (TextView) findViewById(R.id.datos_laukas);
        addDestTime = (TextView) findViewById(R.id.time_laukas);
        addAutomobile = (EditText) findViewById(R.id.automobileName_laukas);
        addFreeSeats = (EditText) findViewById(R.id.vietuSk_laukas);

        addDestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePik = new DateFragment();
                datePik.show(getSupportFragmentManager(), "date piker");
            }
        });

        addDestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar k = Calendar.getInstance();

                int hour = k.get(Calendar.HOUR_OF_DAY);
                int minute = k.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(KelionesRedagavimas.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        addDestTime.setText(hourOfDay + ":" + minute);
                    }
                },hour, minute,android.text.format.DateFormat.is24HourFormat(KelionesRedagavimas.this));
                timePickerDialog.show();
            }
        });

        addTrip = (Button) findViewById(R.id.pridejimoBtn);

        vartotojas = FirebaseAuth.getInstance().getCurrentUser();

        i = getIntent();
        final String userId = i.getStringExtra("uid");

        databaseReference = FirebaseDatabase.getInstance().getReference("trips").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Trip vart = dataSnapshot.getValue(Trip.class);

                addDestFrom.setText(vart.getDestFrom());
                addDestTo.setText(vart.getDestTo());
                addDestDate.setText(vart.getTripDate());
                addDestTime.setText(vart.getTripTime());
                addAutomobile.setText(vart.getCarName());
                addFreeSeats.setText(vart.carFreeSeats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditedTripToFirebaseDatabase();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar k = Calendar.getInstance();
        k.set(Calendar.YEAR, year);
        k.set(Calendar.MONTH, month);
        k.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dateString = DateFormat.getDateInstance().format(k.getTime());

        addDestDate.setText(dateString);
    }

    private void saveEditedTripToFirebaseDatabase() {

        String uid = firebaseAuth.getCurrentUser().getUid();

        if (uid == null) return;

        databaseReference.child("carFreeSeats").setValue(addFreeSeats.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(KelionesRedagavimas.this, VartotojoKeliones.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("KelionesRedagavimas", "Klaida: " + e.getMessage());
                    }
                });

        databaseReference.child("carName").setValue(addAutomobile.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(KelionesRedagavimas.this, VartotojoKeliones.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("KelionesRedagavimas", "Klaida: " + e.getMessage());
                    }
                });

        databaseReference.child("destFrom").setValue(addDestFrom.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(KelionesRedagavimas.this, VartotojoKeliones.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("KelionesRedagavimas", "Klaida: " + e.getMessage());
                    }
                });

        databaseReference.child("destTo").setValue(addDestTo.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(KelionesRedagavimas.this, VartotojoKeliones.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("KelionesRedagavimas", "Klaida: " + e.getMessage());
                    }
                });

        databaseReference.child("tripDate").setValue(addDestDate.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(KelionesRedagavimas.this, VartotojoKeliones.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("KelionesRedagavimas", "Klaida: " + e.getMessage());
                    }
                });

        databaseReference.child("tripTime").setValue(addDestTime.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(KelionesRedagavimas.this, VartotojoKeliones.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("KelionesRedagavimas", "Klaida: " + e.getMessage());
                    }
                });

    }

}

