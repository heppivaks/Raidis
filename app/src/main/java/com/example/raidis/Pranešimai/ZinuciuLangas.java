package com.example.raidis.Pranešimai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.raidis.Duomenys.Chat;
import com.example.raidis.Duomenys.Vartotojas;
import com.example.raidis.R;
import com.example.raidis.adapteriai.ZinuciuAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ZinuciuLangas extends AppCompatActivity {

    private CircleImageView profile_image;

    private ImageButton atgalBtn;
    private TextView zinutes_gavejas;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Chat> zinuteList;

    private FirebaseUser fuser;
    private DatabaseReference reference;

    private Intent i;

    private ImageButton siustiZinuteBtn;
    private EditText zinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zinuciu_langas);

        zinuteList = new ArrayList<>();
        atgalBtn = (ImageButton) findViewById(R.id.btn_back);

        zinutes_gavejas = (TextView) findViewById(R.id.zinutes_user);

        profile_image = (CircleImageView) findViewById(R.id.profile_image_msg);

        siustiZinuteBtn = (ImageButton) findViewById(R.id.btn_send);
        zinute = (EditText) findViewById(R.id.text_send);

        recyclerView = (RecyclerView) findViewById(R.id.chat_log);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ZinuciuAdapter(ZinuciuLangas.this, zinuteList, profile_image.toString());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        atgalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent atgalIntent = new Intent(ZinuciuLangas.this, PranesimuLangas.class);
                startActivity(atgalIntent);
            }
        });

        i = getIntent();
        final String userId = i.getStringExtra("uid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        siustiZinuteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = zinute.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), userId, msg);
                } else {
                    Toast.makeText(ZinuciuLangas.this, "Negalite siųsti tuščios žinutės", Toast.LENGTH_SHORT).show();
                }
                zinute.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Vartotojas user = dataSnapshot.getValue(Vartotojas.class);
                zinutes_gavejas.setText(user.getName());

                Log.d("Zinutes", user.getProfileImage());

                if(user.getProfileImage().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(ZinuciuLangas.this).load(user.getProfileImage()).into(profile_image);
                }

                readMessage(fuser.getUid(), userId, user.getProfileImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String siuntejas, String gavejas, String zinutesTurinys){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("siuntejas", siuntejas);
        hashMap.put("gavejas", gavejas);
        hashMap.put("zinute", zinutesTurinys);

        reference.child("chats").push().setValue(hashMap);

    }

    private void readMessage(final String myid, final String gavejoId, final String imageurl){

        zinuteList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                zinuteList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat msg = snapshot.getValue(Chat.class);

                    if(msg.getGavejas().equals(myid) && msg.getSiuntejas().equals(gavejoId)
                || msg.getGavejas().equals(gavejoId) && msg.getSiuntejas().equals(myid)){
                        zinuteList.add(msg);
                    }
                    adapter = new ZinuciuAdapter(ZinuciuLangas.this, zinuteList, imageurl);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}



