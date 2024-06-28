package com.example.easyflight;

public class User {
    private String name;
    private String location;
    private String email;
    private String userId;
    private String bookedFlight;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String location, String email, String userId) {
        this.name = name;
        this.location = location;
        this.email = email;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookedFlight() {
        return bookedFlight;
    }

    public void setBookedFlight(String bookedFlight) {
        this.bookedFlight = bookedFlight;
    }
}
