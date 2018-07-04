package com.tama.chat.gcm;


import java.util.ArrayList;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(ArrayList<Route> routes);
}
