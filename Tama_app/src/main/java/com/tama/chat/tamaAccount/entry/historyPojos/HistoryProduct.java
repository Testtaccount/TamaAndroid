
package com.tama.chat.tamaAccount.entry.historyPojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryProduct implements Parcelable {


  @SerializedName("product_id")
  @Expose
  private long productId;
  @SerializedName("product_name")
  @Expose
  private String productName;
  @SerializedName("product_desc")
  @Expose
  private String productDesc;
  @SerializedName("qty")
  @Expose
  private long qty;
  @SerializedName("total")
  @Expose
  private String total;
  @SerializedName("product_tags")
  @Expose
  private String productTags;
  @SerializedName("category_name")
  @Expose
  private String categoryName;
  @SerializedName("product_image")
  @Expose
  private String productImage;
  @SerializedName("product_country")
  @Expose
  private String productCountry;
  @SerializedName("currency")
  @Expose
  private String currency;
  @SerializedName("product_cost")
  @Expose
  private String productCost;
  @SerializedName("product_cost_ws")
  @Expose
  private String productCostWs;
  @SerializedName("shipping_available")
  @Expose
  private String shippingAvailable;
  @SerializedName("free_shipping")
  @Expose
  private String freeShipping;
  @SerializedName("min_to_order")
  @Expose
  private long minToOrder;
  @SerializedName("max_to_order")
  @Expose
  private long maxToOrder;
  @SerializedName("shipping_cost")
  @Expose
  private String shippingCost;
  @SerializedName("shipping_cost_ws")
  @Expose
  private String shippingCostWs;
  @SerializedName("lang")
  @Expose
  private String lang;
  public final static Parcelable.Creator<HistoryProduct> CREATOR = new Creator<HistoryProduct>() {


    @SuppressWarnings({
        "unchecked"
    })
    public HistoryProduct createFromParcel(Parcel in) {
      return new HistoryProduct(in);
    }

    public HistoryProduct[] newArray(int size) {
      return (new HistoryProduct[size]);
    }

  };

  protected HistoryProduct(Parcel in) {
    this.productId = ((long) in.readValue((long.class.getClassLoader())));
    this.productName = ((String) in.readValue((String.class.getClassLoader())));
    this.productDesc = ((String) in.readValue((String.class.getClassLoader())));
    this.qty = ((long) in.readValue((long.class.getClassLoader())));
    this.total = ((String) in.readValue((String.class.getClassLoader())));
    this.productTags = ((String) in.readValue((String.class.getClassLoader())));
    this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
    this.productImage = ((String) in.readValue((String.class.getClassLoader())));
    this.productCountry = ((String) in.readValue((String.class.getClassLoader())));
    this.currency = ((String) in.readValue((String.class.getClassLoader())));
    this.productCost = ((String) in.readValue((String.class.getClassLoader())));
    this.productCostWs = ((String) in.readValue((String.class.getClassLoader())));
    this.shippingAvailable = ((String) in.readValue((String.class.getClassLoader())));
    this.freeShipping = ((String) in.readValue((String.class.getClassLoader())));
    this.minToOrder = ((long) in.readValue((long.class.getClassLoader())));
    this.maxToOrder = ((long) in.readValue((long.class.getClassLoader())));
    this.shippingCost = ((String) in.readValue((String.class.getClassLoader())));
    this.shippingCostWs = ((String) in.readValue((String.class.getClassLoader())));
    this.lang = ((String) in.readValue((String.class.getClassLoader())));
  }

  /**
   * No args constructor for use in serialization
   */
  public HistoryProduct() {
  }

  /**
   *
   * @param total
   * @param shippingCostWs
   * @param freeShipping
   * @param productImage
   * @param shippingCost
   * @param qty
   * @param lang
   * @param productId
   * @param currency
   * @param productDesc
   * @param categoryName
   * @param productCountry
   * @param maxToOrder
   * @param productCostWs
   * @param productTags
   * @param productCost
   * @param shippingAvailable
   * @param minToOrder
   * @param productName
   */
  public HistoryProduct(long productId, String productName, String productDesc, long qty,
      String total, String productTags, String categoryName, String productImage,
      String productCountry, String currency, String productCost, String productCostWs,
      String shippingAvailable, String freeShipping, long minToOrder, long maxToOrder,
      String shippingCost, String shippingCostWs, String lang) {
    super();
    this.productId = productId;
    this.productName = productName;
    this.productDesc = productDesc;
    this.qty = qty;
    this.total = total;
    this.productTags = productTags;
    this.categoryName = categoryName;
    this.productImage = productImage;
    this.productCountry = productCountry;
    this.currency = currency;
    this.productCost = productCost;
    this.productCostWs = productCostWs;
    this.shippingAvailable = shippingAvailable;
    this.freeShipping = freeShipping;
    this.minToOrder = minToOrder;
    this.maxToOrder = maxToOrder;
    this.shippingCost = shippingCost;
    this.shippingCostWs = shippingCostWs;
    this.lang = lang;
  }

  public long getProductId() {
    return productId;
  }

  public void setProductId(long productId) {
    this.productId = productId;
  }

  public HistoryProduct withProductId(long productId) {
    this.productId = productId;
    return this;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public HistoryProduct withProductName(String productName) {
    this.productName = productName;
    return this;
  }

  public String getProductDesc() {
    return productDesc;
  }

  public void setProductDesc(String productDesc) {
    this.productDesc = productDesc;
  }

  public HistoryProduct withProductDesc(String productDesc) {
    this.productDesc = productDesc;
    return this;
  }

  public long getQty() {
    return qty;
  }

  public void setQty(long qty) {
    this.qty = qty;
  }

  public HistoryProduct withQty(long qty) {
    this.qty = qty;
    return this;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public HistoryProduct withTotal(String total) {
    this.total = total;
    return this;
  }

  public String getProductTags() {
    return productTags;
  }

  public void setProductTags(String productTags) {
    this.productTags = productTags;
  }

  public HistoryProduct withProductTags(String productTags) {
    this.productTags = productTags;
    return this;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public HistoryProduct withCategoryName(String categoryName) {
    this.categoryName = categoryName;
    return this;
  }

  public String getProductImage() {
    return productImage;
  }

  public void setProductImage(String productImage) {
    this.productImage = productImage;
  }

  public HistoryProduct withProductImage(String productImage) {
    this.productImage = productImage;
    return this;
  }

  public String getProductCountry() {
    return productCountry;
  }

  public void setProductCountry(String productCountry) {
    this.productCountry = productCountry;
  }

  public HistoryProduct withProductCountry(String productCountry) {
    this.productCountry = productCountry;
    return this;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public HistoryProduct withCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public String getProductCost() {
    return productCost;
  }

  public void setProductCost(String productCost) {
    this.productCost = productCost;
  }

  public HistoryProduct withProductCost(String productCost) {
    this.productCost = productCost;
    return this;
  }

  public String getProductCostWs() {
    return productCostWs;
  }

  public void setProductCostWs(String productCostWs) {
    this.productCostWs = productCostWs;
  }

  public HistoryProduct withProductCostWs(String productCostWs) {
    this.productCostWs = productCostWs;
    return this;
  }

  public String getShippingAvailable() {
    return shippingAvailable;
  }

  public void setShippingAvailable(String shippingAvailable) {
    this.shippingAvailable = shippingAvailable;
  }

  public HistoryProduct withShippingAvailable(String shippingAvailable) {
    this.shippingAvailable = shippingAvailable;
    return this;
  }

  public String getFreeShipping() {
    return freeShipping;
  }

  public void setFreeShipping(String freeShipping) {
    this.freeShipping = freeShipping;
  }

  public HistoryProduct withFreeShipping(String freeShipping) {
    this.freeShipping = freeShipping;
    return this;
  }

  public long getMinToOrder() {
    return minToOrder;
  }

  public void setMinToOrder(long minToOrder) {
    this.minToOrder = minToOrder;
  }

  public HistoryProduct withMinToOrder(long minToOrder) {
    this.minToOrder = minToOrder;
    return this;
  }

  public long getMaxToOrder() {
    return maxToOrder;
  }

  public void setMaxToOrder(long maxToOrder) {
    this.maxToOrder = maxToOrder;
  }

  public HistoryProduct withMaxToOrder(long maxToOrder) {
    this.maxToOrder = maxToOrder;
    return this;
  }

  public String getShippingCost() {
    return shippingCost;
  }

  public void setShippingCost(String shippingCost) {
    this.shippingCost = shippingCost;
  }

  public HistoryProduct withShippingCost(String shippingCost) {
    this.shippingCost = shippingCost;
    return this;
  }

  public String getShippingCostWs() {
    return shippingCostWs;
  }

  public void setShippingCostWs(String shippingCostWs) {
    this.shippingCostWs = shippingCostWs;
  }

  public HistoryProduct withShippingCostWs(String shippingCostWs) {
    this.shippingCostWs = shippingCostWs;
    return this;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public HistoryProduct withLang(String lang) {
    this.lang = lang;
    return this;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(productId);
    dest.writeValue(productName);
    dest.writeValue(productDesc);
    dest.writeValue(qty);
    dest.writeValue(total);
    dest.writeValue(productTags);
    dest.writeValue(categoryName);
    dest.writeValue(productImage);
    dest.writeValue(productCountry);
    dest.writeValue(currency);
    dest.writeValue(productCost);
    dest.writeValue(productCostWs);
    dest.writeValue(shippingAvailable);
    dest.writeValue(freeShipping);
    dest.writeValue(minToOrder);
    dest.writeValue(maxToOrder);
    dest.writeValue(shippingCost);
    dest.writeValue(shippingCostWs);
    dest.writeValue(lang);
  }

  public int describeContents() {
    return 0;
  }

}
