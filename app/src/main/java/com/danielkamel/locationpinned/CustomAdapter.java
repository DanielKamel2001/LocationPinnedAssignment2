package com.danielkamel.locationpinned;

import static androidx.core.app.ActivityCompat.recreate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

   private Context context;
    private  Activity activity;
    ArrayList<String> ids, addresses, latitudes, longitudes;
    private ArrayList<String> ids_copy, addresses_copy, latitudes_copy, longitudes_copy;


    public CustomAdapter( Activity activity, Context context, ArrayList<String> ids, ArrayList<String> note_titles, ArrayList<String> note_texts, ArrayList<String> note_colours) {
        this.activity = activity;
        this.context = context;
        this.ids = ids;
        this.addresses = note_titles;
        this.latitudes = note_texts;
        this.longitudes = note_colours;


        this.ids_copy = (ArrayList<String>) ids.clone();
        this.addresses_copy = (ArrayList<String>) note_titles.clone();
        this.latitudes_copy = (ArrayList<String>) note_texts.clone();
        this.longitudes_copy = (ArrayList<String>) note_colours.clone();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.address_list_item,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        holder.note_title_text.setText(String.valueOf((addresses.get(holder.getAdapterPosition()))));
        holder.note_text_text.setText(String.valueOf((latitudes.get(holder.getAdapterPosition()))) + ","+String.valueOf((longitudes.get(holder.getAdapterPosition()))));


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDatabaseHelper db = new LocationDatabaseHelper(context);
                db.delete(v.getContext(),String.valueOf(ids.get(holder.getAdapterPosition())));

                recreate(activity);
            }

        });

        holder.row_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateAddress.class);
                intent.putExtra("ID",String.valueOf(ids.get(holder.getAdapterPosition())));
                intent.putExtra("Address",String.valueOf(addresses.get(holder.getAdapterPosition())));
                intent.putExtra("Latitude",String.valueOf(latitudes.get(holder.getAdapterPosition())));
                intent.putExtra("Longitude",String.valueOf(longitudes.get(holder.getAdapterPosition())));

//                context.startActivity(intent);
                activity.startActivityForResult(intent,1);
            }

        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }


    public void filter(String text) {
        this.ids.clear();
        this.addresses.clear();
        this.latitudes.clear();
        this.longitudes.clear();

        if(text.isEmpty()){
            ids.addAll(ids_copy);
            addresses.addAll(addresses_copy);
            latitudes.addAll(longitudes_copy);
            longitudes.addAll(longitudes_copy);
        } else {
            text = text.toLowerCase();

            for (int i = 0; i < ids_copy.size(); i++) {
                if(addresses_copy.get(i).toLowerCase().contains(text)  ){

                    ids.add(ids_copy.get(i));
                    addresses.add(addresses_copy.get(i));
                    latitudes.add(longitudes_copy.get(i));
                    longitudes.add(longitudes_copy.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView note_id_text, note_title_text, note_text_text,note_colour_text;

        ConstraintLayout note_holder;
        ImageButton deleteButton;


        LinearLayout row_item;
//        String note_colour;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            note_id_text = itemView.findViewById(R.id.)
            note_title_text = itemView.findViewById(R.id.titleTextView);
            note_text_text = itemView.findViewById(R.id.coordsTextView);

            note_holder = itemView.findViewById(R.id.noteHolder);
            row_item= itemView.findViewById(R.id.rowItem);
            deleteButton = itemView.findViewById(R.id.deleteButton);


        }


    }
}
