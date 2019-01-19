package com.example.leonid.jetpack.restoraunt.Interfaces;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public interface LocationFound {
    public void handleLocationFound(LatLng l,Boolean different_adress);//or whatever args you want
}
