package com.example.travelapp.Adapter;

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
import com.example.travelapp.Activity.BookingDetailActivity;
import com.example.travelapp.Domain.Booking;
import com.example.travelapp.R;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tourName.setText(booking.getTourName());
        holder.address.setText("Address: " + booking.getAddress());
        holder.date.setText("Date: " + booking.getDate());
        holder.guestName.setText("Guest: " + booking.getGuestName());
        holder.guestNumber.setText("Guests: " + booking.getGuestNumber());
        holder.total.setText("Total: $" + booking.getTotal());

        if (booking.getPic() != null) {
            Glide.with(context)
                    .load(booking.getPic())
                    .into(holder.image);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailActivity.class);
            intent.putExtra("booking", booking);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tourName, address, date, guestName, guestNumber, total;
        ImageView image;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tourName = itemView.findViewById(R.id.booking_tour_name);
            address = itemView.findViewById(R.id.booking_address);
            date = itemView.findViewById(R.id.booking_date);
            guestName = itemView.findViewById(R.id.booking_guest_name);
            guestNumber = itemView.findViewById(R.id.booking_guest_number);
            total = itemView.findViewById(R.id.booking_total);
            image = itemView.findViewById(R.id.booking_image);
        }
    }
}
