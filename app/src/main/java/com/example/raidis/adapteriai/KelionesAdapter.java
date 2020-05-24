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

import com.example.raidis.Kelionės.KelionesRedagavimas;
import com.example.raidis.Pagrindinis.PagrindinisPaieska;
import com.example.raidis.R;
import com.example.raidis.Duomenys.Trip;
import com.example.raidis.Kelionės.VartotojoKeliones;
import com.example.raidis.Pranešimai.ZinuciuLangas;

import java.util.List;

public class KelionesAdapter extends RecyclerView.Adapter<KelionesAdapter.ViewHolder> {

    private Context context;
    private List<Trip> trips;

    public KelionesAdapter(Context context, List<Trip> trips) {
        this.context = context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public KelionesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rastos_keliones, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final KelionesAdapter.ViewHolder holder, final int position) {

        holder.destFrom.setText(trips.get(position).getDestFrom());
        holder.destTo.setText(trips.get(position).getDestTo());
        holder.destDate.setText(trips.get(position).getTripDate());
        holder.destVietuSk.setText(trips.get(position).getCarFreeSeats());
        holder.destLaikas.setText(trips.get(position).getTripTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof PagrindinisPaieska){

                holder.redagavimas.setImageResource(R.drawable.ic_message);

                Intent i = new Intent(context, ZinuciuLangas.class);
                i.putExtra("uid", trips.get(position).getUid());
                context.startActivity(i);

            }
                else if (context instanceof VartotojoKeliones){

                    holder.redagavimas.setImageResource(R.drawable.settings);

                    Intent i = new Intent(context, KelionesRedagavimas.class);
                    i.putExtra("uid", trips.get(position).getKelionesID());
                    context.startActivity(i);

                }
        }}
        );
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView destFrom, destTo, destDate, destVietuSk, destLaikas;
        private ImageView profile_image, redagavimas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            destFrom = (TextView) itemView.findViewById(R.id.kelione_from);
            destTo = (TextView) itemView.findViewById(R.id.kelione_dest);
            destDate = (TextView) itemView.findViewById(R.id.kelione_data);
            destLaikas = (TextView) itemView.findViewById(R.id.kelione_laikas);
            destVietuSk = (TextView) itemView.findViewById(R.id.kelione_vietuSk);
            profile_image = (ImageView) itemView.findViewById(R.id.profile_image_trip);
            redagavimas = (ImageView) itemView.findViewById(R.id.keliones_keitimas);
        }
    }
}
