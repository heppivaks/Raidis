package com.example.raidis.adapteriai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.raidis.Duomenys.Chat;
import com.example.raidis.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ZinuciuAdapter extends RecyclerView.Adapter<ZinuciuAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> zinutes;
    private String imageUrl;

    private FirebaseUser fuser;

    public ZinuciuAdapter(Context context, List<Chat> zinutes, String imageUrl) {
        this.context = context;
        this.zinutes = zinutes;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ZinuciuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zinute_desineje, parent, false);
            return new ZinuciuAdapter.ViewHolder(itemLayoutView);
        } else {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zinute_kaireje, parent, false);
            return new ZinuciuAdapter.ViewHolder(itemLayoutView);
        }
    }

    @Override
    public void onBindViewHolder(final ZinuciuAdapter.ViewHolder holder, final int position) {

        Chat msg = zinutes.get(position);

        holder.show_message.setText(msg.getZinute());

        if(imageUrl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageUrl).into(holder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return zinutes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = (TextView) itemView.findViewById(R.id.show_message);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image2);
        }
    }

    @Override
    public int getItemViewType(int position) {
       fuser = FirebaseAuth.getInstance().getCurrentUser();
       if(zinutes.get(position).getGavejas().equals(fuser.getUid())){
           return MSG_TYPE_LEFT;
       } else {
           return MSG_TYPE_RIGHT;
       }
    }
}