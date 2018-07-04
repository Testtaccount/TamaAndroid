package com.tama.chat.tamaAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avo on 20-May-17.
 */

public class Denominations{

    String country;
    public String destination_currency;
    String operator;
    String mobileNumber;
    public List<String> productList = new ArrayList<>();
    public List<String> retilPriceList = new ArrayList<>();

    public Denominations(String country, String destination, String operator, String mobileNumber){
        this.country = country;
        this.destination_currency = destination;
        this.operator = operator;
        this.mobileNumber = mobileNumber;
    }
}
