package com.dee.rentalmanagement.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dee.rentalmanagement.R;
import com.dee.rentalmanagement.model.Room;
import com.dee.rentalmanagement.url.Url;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RoomsAdapter  extends RecyclerView.Adapter<RoomsAdapter.roomViewHolder> {
    Context context;
    List<Room> roomsList;

    public RoomsAdapter(Context context, List<Room> roomsList) {
        this.context = context;
        this.roomsList = roomsList;
    }

    @NonNull
    @Override
    public roomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_rooms, parent, false);
        return new roomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull roomViewHolder holder, int position) {
        final Room room = roomsList.get(position);
        holder.tvTitle.setText(room.getTitle());
holder.tvLocation.setText(room.getLocation());
holder.tvPhone.setText(room.getPhone());
holder.tvPrices.setText(room.getPrices()+"");
holder.tvDetails.setText(room.getDetails());

        String imgae= room.getImage();
        String imgPath= Url.imagePath+imgae;
        Picasso.get().load(imgPath).into(holder.imgRooms);
    }

    @Override
    public int getItemCount() {
        return roomsList.size();
    }

    public class roomViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation,  tvPhone, tvPrices,tvDetails;
        ImageView imgRooms;


        public roomViewHolder(@NonNull View itemView) {
            super(itemView);
tvTitle = itemView.findViewById(R.id.tvTitle);
tvLocation = itemView.findViewById(R.id.tvlocation);
tvPhone = itemView.findViewById(R.id.tvPhone);
tvPrices = itemView.findViewById(R.id.tvPrice);
tvDetails = itemView.findViewById(R.id.tvdetails);
imgRooms = itemView.findViewById(R.id.imgRoom);
        }
    }

    }

