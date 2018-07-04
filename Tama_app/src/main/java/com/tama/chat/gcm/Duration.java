package com.tama.chat.gcm;

import java.io.Serializable;

public class Duration implements Serializable {
    public String text;
    public int value;

    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
    }
}
