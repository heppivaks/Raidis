package com.example.raidis.adapteriai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.raidis.Duomenys.Chat;
import com.example.raidis.Pranešimai.PranesimuLangas;
import com.example.raidis.R;
import com.example.raidis.Duomenys.Vartotojas;
import com.example.raidis.Pranešimai.ZinuciuLangas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class VartotojoAdapter extends RecyclerView.Adapter<VartotojoAdapter.ViewHolder> {

    private Context context;
    private List<Vartotojas> users;

    private String theLastMsg;

    public VartotojoAdapter(Context context, List<Vartotojas> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public VartotojoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vartotoju_isvedimas, parent, false);

        return new VartotojoAdapter.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final VartotojoAdapter.ViewHolder holder, final int position) {

        final Vartotojas user = users.get(position);

        holder.vardasPav.setText(users.get(position).getName());

        lastMessage(users.get(position).getUid(), holder.last_msg);

        if(user.getProfileImage().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getProfileImage()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof PranesimuLangas) {

                    Intent i = new Intent(context, ZinuciuLangas.class);
                    i.putExtra("uid", users.get(position).getUid());
                    context.startActivity(i);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView vardasPav;
        private TextView last_msg;
        private ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vardasPav = (TextView) itemView.findViewById(R.id.username);
            last_msg = (TextView) itemView.findViewById(R.id.last_msg);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image_pranesimai);
        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
        theLastMsg = "default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat msg = snapshot.getValue(Chat.class);
                    if(msg.getGavejas().equals(firebaseUser.getUid()) && msg.getSiuntejas().equals(userid) ||
                            msg.getGavejas().equals(userid) && msg.getSiuntejas().equals(firebaseUser.getUid())){
                        theLastMsg = msg.getZinute();
                    }
                }

                switch (theLastMsg){
                    case "default":
                        last_msg.setText("No message");
                        break;

                    default:
                        last_msg.setText(theLastMsg);
                        break;
                }

                theLastMsg = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}