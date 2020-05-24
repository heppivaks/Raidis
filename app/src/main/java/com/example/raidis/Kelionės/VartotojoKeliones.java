package com.example.raidis.Kelionės;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.raidis.Duomenys.Trip;
import com.example.raidis.Pagrindinis.PagrindinisPaieska;
import com.example.raidis.Paskyra.PaskyrosLangas;
import com.example.raidis.Pranešimai.PranesimuLangas;
import com.example.raidis.R;
import com.example.raidis.adapteriai.KelionesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VartotojoKeliones extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference dbReference;
    private List<Trip> trip;

    Button kelPrideBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vartotojo_keliones);

        trip = new ArrayList<>();

        dbReference = FirebaseDatabase.getInstance().getReference("trips");

        recyclerView = (RecyclerView) findViewById(R.id.vartotjoKelrecycler);
        layoutManager = new LinearLayoutManager(this);
        adapter = new KelionesAdapter(VartotojoKeliones.this, trip);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        readTrips();

        kelPrideBt = (Button) findViewById(R.id.pridejimoBtn);

        kelPrideBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VartotojoKeliones.this, KelionesPridejimas.class);
                startActivity(i);
           }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.pradzia_nav:
                        Intent pradzdiaIntent = new Intent(VartotojoKeliones.this, PagrindinisPaieska.class);
                        startActivity(pradzdiaIntent);
                        break;

                    case R.id.pridetiKel_nav:
                        break;

                    case R.id.pranesimai_nav:
                        Intent pranesimuIntent = new Intent(VartotojoKeliones.this, PranesimuLangas.class);
                        startActivity(pranesimuIntent);
                        break;

                    case R.id.paskyra_nav:
                        Intent paskyrosIntent = new Intent(VartotojoKeliones.this, PaskyrosLangas.class);
                        startActivity(paskyrosIntent);
                        break;
                }

                return false;
            }
        });
    }
    private void readTrips() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("trips");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                        trip.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Trip keliones = snapshot.getValue(Trip.class);

                            if ((keliones.getUid().equals(user.getUid())) ) {
                                trip.add(keliones);
                            }
                        }

                        recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
