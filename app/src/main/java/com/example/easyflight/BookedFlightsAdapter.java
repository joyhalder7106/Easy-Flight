package com.example.easyflight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookedFlightsAdapter extends RecyclerView.Adapter<BookedFlightsAdapter.FlightViewHolder> {

    private List<Flight> flightList;

    public BookedFlightsAdapter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.routeTextView.setText(flight.getRoute());
        holder.flightNumberTextView.setText("Flight Number: " + flight.getFlightNumber());
        holder.airlinesTextView.setText("Airlines: " + flight.getAirlines());
        holder.travelTimeTextView.setText("Travel Time: " + flight.getTravelTime());
        holder.bookButton.setVisibility(View.GONE);  // Hide the book button as it's not needed in booked flights list
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView routeTextView, flightNumberTextView, airlinesTextView, travelTimeTextView;
        Button bookButton;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            routeTextView = itemView.findViewById(R.id.routeTextView);
            flightNumberTextView = itemView.findViewById(R.id.flightNumberTextView);
            airlinesTextView = itemView.findViewById(R.id.airlinesTextView);
            travelTimeTextView = itemView.findViewById(R.id.travelTimeTextView);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
}
