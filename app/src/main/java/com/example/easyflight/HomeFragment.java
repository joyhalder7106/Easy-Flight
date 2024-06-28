package com.example.easyflight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView userProfileName, userLocationName;
    private RecyclerView recyclerView;
    private BookedFlightsAdapter bookedFlightsAdapter;
    private List<Flight> bookedFlightsList;
    private DatabaseReference userDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        userProfileName = view.findViewById(R.id.userProfile_name);
        userLocationName = view.findViewById(R.id.userLocation_name);
        recyclerView = view.findViewById(R.id.recycler_id);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookedFlightsList = new ArrayList<>();
        bookedFlightsAdapter = new BookedFlightsAdapter(bookedFlightsList);
        recyclerView.setAdapter(bookedFlightsAdapter);

        userDatabase = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        loadUserData();

        return view;
    }

    private void loadUserData() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        userProfileName.setText("Welcome, " + user.getName());
                        userLocationName.setText("Lives in, " + user.getLocation());
                        loadBookedFlights(dataSnapshot.child("bookedFlights"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBookedFlights(DataSnapshot bookedFlightsSnapshot) {
        bookedFlightsList.clear();
        for (DataSnapshot snapshot : bookedFlightsSnapshot.getChildren()) {
            Flight flight = snapshot.getValue(Flight.class);
            if (flight != null) {
                bookedFlightsList.add(flight);
            }
        }
        bookedFlightsAdapter.notifyDataSetChanged();
    }
}
