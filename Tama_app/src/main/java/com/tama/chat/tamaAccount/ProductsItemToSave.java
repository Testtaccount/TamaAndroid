package com.tama.chat.tamaAccount;

import java.io.Serializable;

public class ProductsItemToSave implements Serializable{

    public String country;
    public String product_id;
    public String product_name;
    public String product_image;
    public String product_cost;
    public String count;

    public ProductsItemToSave(){}

    public ProductsItemToSave(ProductsItem item){
        product_id = item.product_id;
        product_name = item.product_name;
        product_image = item.product_image;
        product_cost = item.product_cost;
        count = "1";
        country = item.country;
    }


}
