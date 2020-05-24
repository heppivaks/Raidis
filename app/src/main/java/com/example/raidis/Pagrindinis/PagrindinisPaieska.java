package com.example.raidis.Pagrindinis;

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
import android.widget.EditText;

import com.example.raidis.Duomenys.Trip;
import com.example.raidis.Paskyra.PaskyrosLangas;
import com.example.raidis.Pranešimai.PranesimuLangas;
import com.example.raidis.R;
import com.example.raidis.Kelionės.VartotojoKeliones;
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

public class PagrindinisPaieska extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Button ieskosBtn;
    private EditText vaz_I, vaz_is;

    private List<Trip> trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagrindinis_paieska);

        vaz_I = (EditText) findViewById(R.id.vaziuosiu_i);
        vaz_is = (EditText) findViewById(R.id.vaziuoti_is);

        trip = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.paieskos_recycler);
        layoutManager = new LinearLayoutManager(this);
        adapter = new KelionesAdapter(PagrindinisPaieska.this, trip);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ieskosBtn = (Button) findViewById(R.id.paieskosBtn);

        readTrips();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.pradzia_nav:
                        break;

                    case R.id.pridetiKel_nav:
                        Intent pridetiIntent = new Intent(PagrindinisPaieska.this, VartotojoKeliones.class);
                        startActivity(pridetiIntent);
                        break;

                    case R.id.pranesimai_nav:
                        Intent pranesimuIntent = new Intent(PagrindinisPaieska.this, PranesimuLangas.class);
                        startActivity(pranesimuIntent);
                        break;

                    case R.id.paskyra_nav:
                        Intent paskyrosIntent = new Intent(PagrindinisPaieska.this, PaskyrosLangas.class);
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

                ieskosBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trip.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Trip keliones = snapshot.getValue(Trip.class);
                            final String isKur = vaz_is.getText().toString();
                            final String iKur = vaz_I.getText().toString();

                                if (!(keliones.getUid().equals(user.getUid())) && keliones.getDestFrom().equals(isKur) && keliones.getDestTo().equals(iKur)
                                || !(keliones.getUid().equals(user.getUid())) && keliones.getDestFrom().equals(isKur)
                                        || !(keliones.getUid().equals(user.getUid())) && keliones.getDestTo().equals(iKur) ) {
                                    trip.add(keliones);
                                }
                            }
                        recyclerView.setAdapter(adapter);
                    }
                });

                trip.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Trip keliones = snapshot.getValue(Trip.class);
                    final String isKur = vaz_is.getText().toString();
                    final String iKur = vaz_I.getText().toString();

                    if (!(keliones.getUid().equals(user.getUid())) && isKur.isEmpty() && iKur.isEmpty()) {
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
