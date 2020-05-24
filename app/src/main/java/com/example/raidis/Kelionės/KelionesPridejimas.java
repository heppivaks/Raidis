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
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import com.example.raidis.Duomenys.Trip;
import com.example.raidis.Fragmentai.DateFragment;
import com.example.raidis.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class KelionesPridejimas extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    TextView kelionesData, kelionesLaikas;

    EditText addDestFrom, addDestTo,  addAutomobile, addFreeSeats;

    Button addTrip;

    FirebaseAuth firebaseAuth;

    FirebaseStorage storage;
    StorageReference reference;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keliones_pridejimas);

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = storage.getReference();
        databaseReference = database.getReference();

        addDestFrom = (EditText) findViewById(R.id.destFrom_laukas);
        addDestTo = (EditText) findViewById(R.id.destTo_laukas);
        kelionesData = (TextView) findViewById(R.id.datos_laukas);
        kelionesLaikas = (TextView) findViewById(R.id.time_laukas);
        addAutomobile = (EditText) findViewById(R.id.automobileName_laukas);
        addFreeSeats = (EditText) findViewById(R.id.vietuSk_laukas);

        addTrip = (Button) findViewById(R.id.pridejimoBtn);

        kelionesData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePik = new DateFragment();
                datePik.show(getSupportFragmentManager(), "date piker");
            }
        });

        kelionesLaikas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar k = Calendar.getInstance();

                int hour = k.get(Calendar.HOUR_OF_DAY);
                int minute = k.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(KelionesPridejimas.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        kelionesLaikas.setText(hourOfDay + ":" + minute);
                    }
                },hour, minute,android.text.format.DateFormat.is24HourFormat(KelionesPridejimas.this));
                timePickerDialog.show();
            }
        });

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destFrom = addDestFrom.getText().toString();
                String destTo = addDestTo.getText().toString();
                String destDate = kelionesData.getText().toString();
                String destLaikas = kelionesLaikas.getText().toString();
                String destAutomobile = addAutomobile.getText().toString();
                String destFreeSeats = addFreeSeats.getText().toString();

                if (destFrom.isEmpty()) {
                    addDestFrom.setError("Prašome įvesti išvykimo miestą");
                    addDestFrom.requestFocus();
                } else if (destTo.isEmpty()) {
                    addDestTo.setError("Prašome įvesti galutinį miestą");
                    addDestTo.requestFocus();
                } else if (destAutomobile.isEmpty()) {
                    addAutomobile.setError("Prašome įvesti automobilį");
                    addAutomobile.requestFocus();
                } else if (destFreeSeats.isEmpty()) {
                    addFreeSeats.setError("Prašome įvesti, kiek laisvų vietų turite");
                    addFreeSeats.requestFocus();
                } else if (destFrom.isEmpty() && destTo.isEmpty() && destDate.isEmpty() && destLaikas.isEmpty() && destAutomobile.isEmpty() && destFreeSeats.isEmpty()) {
                    Toast.makeText(KelionesPridejimas.this, "Laukai neužpildyti.", Toast.LENGTH_SHORT).show();
                } else if (!(destFrom.isEmpty() && destTo.isEmpty() && destDate.isEmpty()  && destLaikas.isEmpty() && destAutomobile.isEmpty() && destFreeSeats.isEmpty())) {
                    saveTripToFirebaseDatabase();
                    Intent i = new Intent(KelionesPridejimas.this, VartotojoKeliones.class);
                    startActivity(i);
                }
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

       kelionesData.setText(dateString);
    }



    private void saveTripToFirebaseDatabase(){
        String uid = firebaseAuth.getCurrentUser().getUid();

        if(uid == null) return;

        String tripID = databaseReference.push().getKey();

        Trip trip = new Trip(uid, addDestFrom.getText().toString(), addDestTo.getText().toString(), kelionesData.getText().toString(),  kelionesLaikas.getText().toString(), addAutomobile.getText().toString(),
        addFreeSeats.getText().toString(), tripID);

        databaseReference.child("trips").child(tripID).setValue(trip).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("KelionesPridejimas", "Išsaugojimas į database pavyko");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("KelionesPridejimas", "Klaida: " + e.getMessage());
                    }
                });
    }


}
