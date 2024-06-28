package com.example.easyflight;

import java.io.Serializable;

public class Flight implements Serializable {
    private String from;
    private String to;
    private String flightNumber;
    private String airlines;
    private String travelTime;
    private String route;

    public Flight() {
        // Default constructor required for calls to DataSnapshot.getValue(Flight.class)
    }

    public Flight(String route, String flightNumber, String airlines, String travelTime) {
        this.route = route;
        this.flightNumber = flightNumber;
        this.airlines = airlines;
        this.travelTime = travelTime;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirlines() {
        return airlines;
    }

    public void setAirlines(String airlines) {
        this.airlines = airlines;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }
}
