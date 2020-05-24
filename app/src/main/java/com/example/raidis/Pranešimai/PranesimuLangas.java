package com.example.raidis.Pranešimai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.raidis.Duomenys.Chat;
import com.example.raidis.Duomenys.Vartotojas;
import com.example.raidis.Pagrindinis.PagrindinisPaieska;
import com.example.raidis.Paskyra.PaskyrosLangas;
import com.example.raidis.R;
import com.example.raidis.Kelionės.VartotojoKeliones;
import com.example.raidis.adapteriai.VartotojoAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PranesimuLangas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Vartotojas> userList;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> vartotojuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pranesimu_langas);

        userList = new ArrayList<>();
        vartotojuList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.lastZinutes_recycler);
        layoutManager = new LinearLayoutManager(this);
        adapter = new VartotojoAdapter(PranesimuLangas.this, userList);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DisplayLastMessages();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.pradzia_nav:
                        Intent pradzdiaIntent = new Intent(PranesimuLangas.this, PagrindinisPaieska.class);
                        startActivity(pradzdiaIntent);
                        break;

                    case R.id.pridetiKel_nav:
                        Intent pridetiIntent = new Intent(PranesimuLangas.this, VartotojoKeliones.class);
                        startActivity(pridetiIntent);
                        break;

                    case R.id.pranesimai_nav:
                        break;

                    case R.id.paskyra_nav:
                        Intent paskyrosIntent = new Intent(PranesimuLangas.this, PaskyrosLangas.class);
                        startActivity(paskyrosIntent);
                        break;

                }

                return false;
            }
        });
    }

    private void readMessages(){

        final Set<String> unikalusID = new HashSet<String>(vartotojuList);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vartotojas user = snapshot.getValue(Vartotojas.class);

                    for (String s : unikalusID) {

                        if (user.getUid().equals(s)) {

                            userList.add(user);

                        }
                        recyclerView.setAdapter(adapter);
                    }
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DisplayLastMessages(){
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vartotojuList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat msg = snapshot.getValue(Chat.class);

                    if(msg.getSiuntejas().equals(fuser.getUid())){
                        vartotojuList.add(msg.getGavejas());
                    }

                    if(msg.getGavejas().equals(fuser.getUid())){
                        vartotojuList.add(msg.getSiuntejas());
                    }
                }

                readMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
