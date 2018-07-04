package com.tama.chat.gcm;

import java.io.Serializable;

public class LatLngSer implements Serializable {
    public Double latitude;
    public Double longitude;

    public LatLngSer(){}

    public LatLngSer(Double lat, Double lng){
        latitude = lat;
        longitude = lng;
    }

}
