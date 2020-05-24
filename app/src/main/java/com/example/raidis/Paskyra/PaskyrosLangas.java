package com.example.raidis.Paskyra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.raidis.Duomenys.Vartotojas;
import com.example.raidis.Pagrindinis.PagrindinisPaieska;
import com.example.raidis.Pranešimai.PranesimuLangas;
import com.example.raidis.Prisijungimas.PrisijungimoLangas;
import com.example.raidis.R;
import com.example.raidis.Kelionės.VartotojoKeliones;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaskyrosLangas extends AppCompatActivity {

    CircleImageView profileImage;
    TextView profileName, profileEmail, profileMiestas;
    ImageView paskyrosKeitBtn;

    Button atsBtn;

    FirebaseUser vartotojas;
    DatabaseReference reference;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paskyros__langas);

        atsBtn = (Button) findViewById(R.id.atsijungimoBtn);

        profileImage = (CircleImageView) findViewById(R.id.paskyros_ft);
        profileName = (TextView) findViewById(R.id.paskyros_vardas);
        profileEmail = (TextView) findViewById(R.id.paskyros_email);
        profileMiestas = (TextView) findViewById(R.id.miestas);
        paskyrosKeitBtn = (ImageView) findViewById(R.id.paskyros_keitimas);

        paskyrosKeitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaskyrosLangas.this, PaskyrosRedagavimas.class);
                startActivity(i);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("images");

        vartotojas = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(vartotojas.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Vartotojas user = dataSnapshot.getValue(Vartotojas.class);

                profileName.setText(user.getName());

                profileEmail.setText(user.getEmail());

                profileMiestas.setText(user.getCity());

                if(user.getProfileImage().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(PaskyrosLangas.this).load(user.getProfileImage()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        atsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent();
                i = new Intent(PaskyrosLangas.this, PrisijungimoLangas.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.pradzia_nav:
                        Intent pradzdiaIntent = new Intent(PaskyrosLangas.this, PagrindinisPaieska.class);
                        startActivity(pradzdiaIntent);
                        break;

                    case R.id.pridetiKel_nav:
                        Intent pridetiIntent = new Intent(PaskyrosLangas.this, VartotojoKeliones.class);
                        startActivity(pridetiIntent);
                        break;

                    case R.id.pranesimai_nav:
                        Intent pranesimuIntent = new Intent(PaskyrosLangas.this, PranesimuLangas.class);
                        startActivity(pranesimuIntent);
                        break;

                    case R.id.paskyra_nav:
                        break;

                }
                return false;
            }
        });
    }

}
