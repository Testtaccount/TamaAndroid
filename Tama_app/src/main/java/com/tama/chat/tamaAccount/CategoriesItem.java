package com.tama.chat.tamaAccount;

public class CategoriesItem {

  public String category_id;
  public String parent_id;
  public String category_name;
  public String category_desc;
  public String category_image;
  public String trans_lang;
  public String sub_category;

  public CategoriesItem() {
  }

  public CategoriesItem(String category_id, String parent_id, String category_name,
      String category_desc, String category_image,String trans_lang, String sub_category) {
    this.category_id = category_id;
    this.parent_id = parent_id;
    this.category_name = category_name;
    this.category_desc = category_desc;
    this.category_image = category_image;
    this.trans_lang = trans_lang;
    this.sub_category = sub_category;
  }

  public boolean isSubCategoriesEmpty() {
    return sub_category.length() < 10;
  }

}
