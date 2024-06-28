package com.example.easyflight;

import android.os.Bundle;
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

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView flightsRecyclerView;
    private DatabaseReference mDatabase;
    private List<Flight> flightList;
    private FlightsAdapter flightsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        flightsRecyclerView = findViewById(R.id.flightsRecyclerView);
        flightsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flightList = new ArrayList<>();
        flightsAdapter = new FlightsAdapter(this, flightList);
        flightsRecyclerView.setAdapter(flightsAdapter);

        String fromLocation = getIntent().getStringExtra("FROM_LOCATION");
        String toLocation = getIntent().getStringExtra("TO_LOCATION");

        if (fromLocation != null && toLocation != null) {
            searchFlights(fromLocation, toLocation);
        } else {
            Toast.makeText(this, "Invalid search criteria", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void searchFlights(String from, String to) {
        mDatabase = FirebaseDatabase.getInstance().getReference("flights");
        mDatabase.orderByChild("route").equalTo(from + "-" + to).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flightList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Flight flight = snapshot.getValue(Flight.class);
                    flightList.add(flight);
                }
                flightsAdapter.notifyDataSetChanged();

                if (flightList.isEmpty()) {
                    Toast.makeText(SearchResultsActivity.this, "No flights found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchResultsActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
