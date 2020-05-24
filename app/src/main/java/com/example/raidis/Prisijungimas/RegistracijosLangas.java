package com.example.raidis.Prisijungimas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.raidis.Duomenys.Vartotojas;
import com.example.raidis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegistracijosLangas extends AppCompatActivity {

    ImageView circleImg;

    EditText regVardPav, regEmail, regSlapt, regSlaptR, regPlace;

    Button regBtn, nuotraukBtn;

    FirebaseAuth firebaseAuth;

    FirebaseStorage storage;
    StorageReference reference;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracijos_langas);

    // registračija su duomenų baze
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = storage.getReference();
        databaseReference = database.getReference();

        regVardPav = (EditText) findViewById(R.id.name_laukas);
        regEmail = (EditText) findViewById(R.id.email_laukas);
        regSlapt = (EditText) findViewById(R.id.password_laukas);
        regSlaptR = (EditText) findViewById(R.id.passwordRepeat_laukas);
        regPlace = (EditText) findViewById(R.id.miestas_laukas) ;

        regBtn = (Button) findViewById(R.id.prisijungimoBtn);
        nuotraukBtn = (Button) findViewById(R.id.nuotraukosBtn);
        circleImg = (ImageView) findViewById(R.id.circle_image_reg);

        nuotraukBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,0);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                String vardPav = regVardPav.getText().toString();
                String email = regEmail.getText().toString();
                String slapt = regSlapt.getText().toString();
                String slaptR = regSlaptR.getText().toString();
                String city = regPlace.getText().toString();

                if (vardPav.isEmpty()){
                    regVardPav.setError("Prašome įvesti vardą ir pavardę");
                    regVardPav.requestFocus();
                }
                else if (city.isEmpty()){
                    regPlace.setError("Prašome įvesti miestą");
                    regPlace.requestFocus();
                }
                 else if (email.isEmpty()){
                    regEmail.setError("Prašome įvesti el. paštą");
                    regEmail.requestFocus();
                }
                else if (slapt.isEmpty()){
                    regSlapt.setError("Prašome įvesti slaptažodį");
                    regSlapt.requestFocus();
                }
                else if (slaptR.isEmpty()){
                    regSlaptR.setError("Prašome pakartoti slaptažodį");
                    regSlaptR.requestFocus();
                }
                else if (!slapt.equals(slaptR)){
                    regSlaptR.setError("Slaptažodžiai nesutampa");
                    regSlaptR.requestFocus();
                }
                else if (vardPav.isEmpty() && city.isEmpty() && email.isEmpty() && slapt.isEmpty() && slaptR.isEmpty()){
                    Toast.makeText(RegistracijosLangas.this, "Laukai neužpildyti.", Toast.LENGTH_SHORT).show();
                }
                else if (!(vardPav.isEmpty() && city.isEmpty() && email.isEmpty() && slapt.isEmpty() && slaptR.isEmpty())){
                    firebaseAuth.createUserWithEmailAndPassword(email, slapt).addOnCompleteListener(RegistracijosLangas.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                        Toast.makeText(RegistracijosLangas.this, "Registracija sėkminga. Dabar galite prisijungti", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegistracijosLangas.this, PrisijungimoLangas.class);
                                startActivity(i);
                                }
                            })
                    .addOnFailureListener(RegistracijosLangas.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistracijosLangas.this, "Klaida" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            // nustato, kokią nuotrauką pasirinko vartotojas
            filePath = data.getData();

            try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
              // BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

                circleImg.setImageBitmap(bitmap);

                nuotraukBtn.setAlpha(0f);

             //  nuotraukBtn.setBackground(bitmapDrawable);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage(){
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Įkeliama...");
            progressDialog.show();

            final StorageReference ref = reference.child("images/"+UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("RegistracijosLangas", "File location: " + uri.toString());
                                    saveUserToFirebaseDatabase(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegistracijosLangas.this, "Klaida" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                                    taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Įkelta: " + (int)progress + "%");
                        }
                    });
        }
    }

    private void saveUserToFirebaseDatabase(String locationURI){
        String uid = firebaseAuth.getUid();

        if(uid == null) return;

        Vartotojas user = new Vartotojas(uid, regVardPav.getText().toString(), locationURI, regEmail.getText().toString(), regPlace.getText().toString());

        databaseReference.child("users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("RegistracijosLangas", "Išsaugojimas į database pavyko");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RegistracijosLangas", "Klaida: " + e.getMessage());
            }
        });
    }

 }
