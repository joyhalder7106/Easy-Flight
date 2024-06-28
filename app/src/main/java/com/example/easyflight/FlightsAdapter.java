package com.example.easyflight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.FlightViewHolder> {

    private List<Flight> flightList;
    private Context context;

    public FlightsAdapter(Context context, List<Flight> flightList) {
        this.context = context;
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
        holder.flightNumberTextView.setText(flight.getFlightNumber());
        holder.airlinesTextView.setText(flight.getAirlines());
        holder.travelTimeTextView.setText(flight.getTravelTime());

        holder.bookButton.setOnClickListener(v -> {
            checkIfFlightAlreadyBooked(flight);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {

        public TextView routeTextView;
        public TextView flightNumberTextView;
        public TextView airlinesTextView;
        public TextView travelTimeTextView;
        public Button bookButton;

        public FlightViewHolder(View itemView) {
            super(itemView);
            routeTextView = itemView.findViewById(R.id.routeTextView);
            flightNumberTextView = itemView.findViewById(R.id.flightNumberTextView);
            airlinesTextView = itemView.findViewById(R.id.airlinesTextView);
            travelTimeTextView = itemView.findViewById(R.id.travelTimeTextView);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }

    private void checkIfFlightAlreadyBooked(Flight flight) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bookedFlights");

        userRef.orderByChild("flightNumber").equalTo(flight.getFlightNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Flight is already booked
                    Toast.makeText(context, "Already booked this flight", Toast.LENGTH_SHORT).show();
                } else {
                    // Flight is not booked, proceed to book
                    bookFlight(flight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bookFlight(Flight flight) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bookedFlights");

        userRef.push().setValue(flight)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Flight booked successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to book flight.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
