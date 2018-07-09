package com.tama.chat.tamaAccount;

import java.io.Serializable;

public class CountriesItem implements Serializable {

  private static final long serialVersionUID = 5648345444566840243L;
  public String country_name;
  public String country_img_path;
  public String url;

  public CountriesItem() {
  }

  public CountriesItem(String country_name, String country_img_path, String url) {
    this.country_name = country_name;
    this.country_img_path = country_img_path;
    this.url = url;
  }
}
