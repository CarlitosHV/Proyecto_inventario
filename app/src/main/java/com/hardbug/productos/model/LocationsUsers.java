package com.hardbug.productos.model;

import android.location.Location;

public class LocationsUsers {
    private double Latitude;
    private double Longitude;
    private String idUser;
    private String Email;

    public LocationsUsers(double latitude, double longitude, String idUser, String email) {
        Latitude = latitude;
        Longitude = longitude;
        this.idUser = idUser;
        Email = email;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
