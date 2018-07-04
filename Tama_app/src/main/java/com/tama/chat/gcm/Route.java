package com.tama.chat.gcm;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLngSer endLocation;
    public String startAddress;
    public LatLngSer startLocation;

    public ArrayList<LatLngSer> points;
}
