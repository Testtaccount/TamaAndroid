package com.tama.chat.tamaAccount;

import java.io.Serializable;

public class ProductsItem implements Serializable{
    public String product_id;
    public String product_name;
    public String product_tags;
    public String product_desc;
    public String category_name;
    public String product_image;
    public String product_country;
    public String product_cost;
    public String product_cost_ws;
    public String shipping_available;
    public String free_shipping;
    public String min_to_order;
    public String max_to_order;
    public String shipping_cost;
    public String shipping_cost_ws;
    public String lang;

    public ProductsItem() {
    }

    public ProductsItem(String product_id, String product_name, String product_tags,
        String product_desc, String category_name, String product_image,
        String product_country, String product_cost, String product_cost_ws,
        String shipping_available, String free_shipping, String min_to_order,
        String max_to_order, String shipping_cost, String shipping_cost_ws, String lang) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_tags = product_tags;
        this.product_desc = product_desc;
        this.category_name = category_name;
        this.product_image = product_image;
        this.product_country = product_country;
        this.product_cost = product_cost;
        this.product_cost_ws = product_cost_ws;
        this.shipping_available = shipping_available;
        this.free_shipping = free_shipping;
        this.min_to_order = min_to_order;
        this.max_to_order = max_to_order;
        this.shipping_cost = shipping_cost;
        this.shipping_cost_ws = shipping_cost_ws;
        this.lang = lang;
    }
}