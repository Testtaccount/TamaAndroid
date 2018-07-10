package com.tama.chat.tamaAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avo on 20-May-17.
 */

public class Denominations{

    public String mobile_number;
    public String country;
    public String operator;
    public String destination_currency;
    public List<String> product_list = new ArrayList<>();
    public List<String> retail_price_list = new ArrayList<>();

    public Denominations(String mobile_number,String country, String operator,String destination ){
        this.mobile_number = mobile_number;
        this.country = country;
        this.operator = operator;
        this.destination_currency = destination;
    }
}