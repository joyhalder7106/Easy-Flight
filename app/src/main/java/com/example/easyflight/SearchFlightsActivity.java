package com.example.easyflight;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFlightsActivity extends AppCompatActivity {

    private Spinner fromSpinner, toSpinner;
    private Button searchButton;
    private RecyclerView flightsRecyclerView;
    private FlightsAdapter flightsAdapter;
    private List<Flight> flightList;

    private DatabaseReference flightsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);

        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        searchButton = findViewById(R.id.searchButton);
        flightsRecyclerView = findViewById(R.id.flightsRecyclerView);

        flightsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flightList = new ArrayList<>();
        flightsAdapter = new FlightsAdapter(this, flightList);  // Pass the context here
        flightsRecyclerView.setAdapter(flightsAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.locations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        flightsDatabase = FirebaseDatabase.getInstance().getReference("flights");

        searchButton.setOnClickListener(v -> {
            String from = fromSpinner.getSelectedItem().toString();
            String to = toSpinner.getSelectedItem().toString();
            searchFlights(from, to);
        });
    }

    private void searchFlights(String from, String to) {
        flightsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flightList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Flight flight = snapshot.getValue(Flight.class);
                    if (flight != null && isRouteValid(flight.getRoute(), from, to)) {
                        flightList.add(flight);
                    }
                }
                flightsAdapter.notifyDataSetChanged();
                if (flightList.isEmpty()) {
                    Toast.makeText(SearchFlightsActivity.this, "No Flights Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchFlightsActivity.this, "Failed to load flights", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isRouteValid(String route, String from, String to) {
        String[] locations = route.split("-");
        boolean fromFound = false;

        for (int i = 0; i < locations.length; i++) {
            if (locations[i].equals(from)) {
                fromFound = true;
            } else if (fromFound && locations[i].equals(to)) {
                // Check if 'to' is not the last location in the route for connecting flights
                return i == locations.length - 1;
            }
        }
        return false;
    }
}
