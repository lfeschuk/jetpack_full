package com.example.leonid.jetpack.restoraunt.Interfaces;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public interface LocationFound {
    public void handleLocationFound(LatLng l);//or whatever args you want
}
