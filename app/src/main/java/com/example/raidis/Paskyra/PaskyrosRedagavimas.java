package com.example.raidis.Paskyra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.raidis.Duomenys.Vartotojas;
import com.example.raidis.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaskyrosRedagavimas extends AppCompatActivity {

    CircleImageView profileImage;
    EditText profileName, profileEmail, profileMiestas;
    Button issaugotiBtn, nuotraukBtn;

    FirebaseAuth firebaseAuth;

    StorageReference reference;
    private static final int IMAGE_REQUEST = 1;
    private Uri filePath;
    private StorageTask uploadTask;

    FirebaseUser vartotojas;
    DatabaseReference referenceDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paskyros_redagavimas);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseStorage.getInstance().getReference("images");

        final String uid = firebaseAuth.getCurrentUser().getUid();

        profileImage = (CircleImageView) findViewById(R.id.paskyros_ft);
        profileName = (EditText) findViewById(R.id.paskyros_vardas);
        profileEmail = (EditText) findViewById(R.id.paskyros_email);
        profileMiestas = (EditText) findViewById(R.id.paskyros_miestas);

        nuotraukBtn = (Button) findViewById(R.id.nuotraukosBtn);
        issaugotiBtn = (Button) findViewById(R.id.issaugotiBtn);

        vartotojas = FirebaseAuth.getInstance().getCurrentUser();
        referenceDB = FirebaseDatabase.getInstance().getReference("users").child(vartotojas.getUid());

        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Vartotojas vart = dataSnapshot.getValue(Vartotojas.class);

                profileName.setText(vart.getName());
                profileEmail.setText(vart.getEmail());
                profileMiestas.setText(vart.getCity());

                if (vart.getProfileImage().equals("default")) {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(PaskyrosRedagavimas.this).load(vart.getProfileImage()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        issaugotiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditedUserToFirebaseDatabase();
            }
        });
    }

    private void openImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getBaseContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(PaskyrosRedagavimas.this);

        pd.setMessage("Įkeliama");

        pd.show();

        if(filePath != null) {
            final StorageReference fileRef = reference.child(System.currentTimeMillis()
            +"."+getFileExtension(filePath));

            uploadTask = fileRef.putFile(filePath);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri =  downloadUri.toString();

                        referenceDB = FirebaseDatabase.getInstance().getReference("users").child(vartotojas.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("profileImage", mUri);
                        referenceDB.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(PaskyrosRedagavimas.this, "Klaida", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PaskyrosRedagavimas.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(PaskyrosRedagavimas.this, "Nepasirinkta jokia nuotrauka", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(PaskyrosRedagavimas.this, "Vyksta įkėlimas", Toast.LENGTH_SHORT).show();
            } else
            {
                uploadImage();;
            }
        }
    }

    private void saveEditedUserToFirebaseDatabase() {

        String uid = firebaseAuth.getCurrentUser().getUid();

        if (uid == null) return;

        referenceDB.child("name").setValue(profileName.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(PaskyrosRedagavimas.this, PaskyrosLangas.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PaskyrosRedagavimas", "Klaida: " + e.getMessage());
                    }
                });

        referenceDB.child("city").setValue(profileMiestas.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(PaskyrosRedagavimas.this, PaskyrosLangas.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PaskyrosRedagavimas", "Klaida: " + e.getMessage());
                    }
                });

        referenceDB.child("email").setValue(profileEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(PaskyrosRedagavimas.this, PaskyrosLangas.class);
                startActivity(i);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PaskyrosRedagavimas", "Klaida: " + e.getMessage());
                    }
                });
    }


}
