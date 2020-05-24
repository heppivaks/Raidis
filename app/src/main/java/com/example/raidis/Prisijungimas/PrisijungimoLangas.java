package com.example.raidis.Prisijungimas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raidis.Pagrindinis.PagrindinisPaieska;
import com.example.raidis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrisijungimoLangas extends AppCompatActivity {

    TextView registraija;
    Button prisijungimoBtn;

    EditText priEmail, priSlapt;

    Button priBtn;

    FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisijungimo_langas);

        // prisijungimas su duomenų baze
        firebaseAuth = FirebaseAuth.getInstance();

        priEmail = (EditText) findViewById(R.id.email_laukas);
        priSlapt = (EditText) findViewById(R.id.password_laukas);

        priBtn = (Button) findViewById(R.id.prisijungimoBtn);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Intent i = new Intent(PrisijungimoLangas.this, PagrindinisPaieska.class);
                    startActivity(i);
                }
                else {

                }
            }
        };

        registraija = (TextView) findViewById(R.id.registraijaLink);
        prisijungimoBtn = (Button) findViewById(R.id.prisijungimoBtn);

        registraija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(PrisijungimoLangas.this, RegistracijosLangas.class);
                startActivity(registerIntent);
            }
        });

        prisijungimoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = priEmail.getText().toString();
                String slapt = priSlapt.getText().toString();

                 if (email.isEmpty()){
                    priEmail.setError("Prašome įvesti el. paštą");
                    priEmail.requestFocus();
                }
                else if (slapt.isEmpty()){
                    priSlapt.setError("Prašome įvesti slaptažodį");
                    priSlapt.requestFocus();
                }
                else if (email.isEmpty() && slapt.isEmpty()){
                    Toast.makeText(PrisijungimoLangas.this, "Laukai neužpildyti.", Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && slapt.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(email, slapt).addOnCompleteListener(PrisijungimoLangas.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(PrisijungimoLangas.this, "Klaida. Bandykite dar kartą", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent i = new Intent(PrisijungimoLangas.this, PagrindinisPaieska.class);
                                startActivity(i);
                            }
                        }
                    });
                }
                 else {
                     Toast.makeText(PrisijungimoLangas.this, "Klaida. Bandykite dar kartą", Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
