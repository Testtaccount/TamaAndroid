
package com.tama.chat.tamaAccount.entry.historyPojos;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryResult implements Parcelable {

  @SerializedName("user_id")
  @Expose
  private long userId;
  @SerializedName("history_id")
  @Expose
  private long historyId;
  @SerializedName("history_name")
  @Expose
  private String historyName;
  @SerializedName("amount")
  @Expose
  private String amount;
  @SerializedName("mobile_no")
  @Expose
  private Object mobileNo;
  @SerializedName("image")
  @Expose
  private String image;
  @SerializedName("header_status")
  @Expose
  private String headerStatus;
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("order_status")
  @Expose
  private String orderStatus;
  @SerializedName("products")
  @Expose
  private List<HistoryProduct> products = new ArrayList<HistoryProduct>();
  @SerializedName("sender_name")
  @Expose
  private String senderName;
  @SerializedName("sender_mobile")
  @Expose
  private String senderMobile;
  @SerializedName("receiver_name")
  @Expose
  private String receiverName;
  @SerializedName("receiver_mobile")
  @Expose
  private String receiverMobile;
  @SerializedName("promo_used")
  @Expose
  private String promoUsed;
  @SerializedName("promo_image")
  @Expose
  private String promoImage;
  @SerializedName("pin")
  @Expose
  private String pin;
  @SerializedName("updated_at")
  @Expose
  private UpdatedAt updatedAt;
  @SerializedName("timestamp")
  @Expose
  private String timestamp;
  public final static Parcelable.Creator<HistoryResult> CREATOR = new Creator<HistoryResult>() {


    @SuppressWarnings({
        "unchecked"
    })
    public HistoryResult createFromParcel(Parcel in) {
      return new HistoryResult(in);
    }

    public HistoryResult[] newArray(int size) {
      return (new HistoryResult[size]);
    }

  };

  protected HistoryResult(Parcel in) {
    this.userId = ((long) in.readValue((long.class.getClassLoader())));
    this.historyId = ((long) in.readValue((long.class.getClassLoader())));
    this.historyName = ((String) in.readValue((String.class.getClassLoader())));
    this.amount = ((String) in.readValue((String.class.getClassLoader())));
    this.mobileNo = ((Object) in.readValue((Object.class.getClassLoader())));
    this.image = ((String) in.readValue((String.class.getClassLoader())));
    this.headerStatus = ((String) in.readValue((String.class.getClassLoader())));
    this.status = ((String) in.readValue((String.class.getClassLoader())));
    this.orderStatus = ((String) in.readValue((String.class.getClassLoader())));
    in.readList(this.products, (HistoryProduct.class.getClassLoader()));
    this.senderName = ((String) in.readValue((String.class.getClassLoader())));
    this.senderMobile = ((String) in.readValue((String.class.getClassLoader())));
    this.receiverName = ((String) in.readValue((String.class.getClassLoader())));
    this.receiverMobile = ((String) in.readValue((String.class.getClassLoader())));
    this.promoUsed = ((String) in.readValue((String.class.getClassLoader())));
    this.promoImage = ((String) in.readValue((String.class.getClassLoader())));
    this.pin = ((String) in.readValue((String.class.getClassLoader())));
    this.updatedAt = ((UpdatedAt) in.readValue((UpdatedAt.class.getClassLoader())));
    this.timestamp = ((String) in.readValue((String.class.getClassLoader())));
  }

  /**
   * No args constructor for use in serialization
   */
  public HistoryResult() {
  }

  /**
   *
   * @param status
   * @param historyId
   * @param historyName
   * @param receiverMobile
   * @param image
   * @param headerStatus
   * @param orderStatus
   * @param timestamp
   * @param updatedAt
   * @param promoUsed
   * @param amount
   * @param senderName
   * @param pin
   * @param userId
   * @param promoImage
   * @param receiverName
   * @param senderMobile
   * @param products
   * @param mobileNo
   */
  public HistoryResult(long userId, long historyId, String historyName, String amount,
      Object mobileNo, String image, String headerStatus, String status, String orderStatus,
      List<HistoryProduct> products, String senderName, String senderMobile, String receiverName,
      String receiverMobile, String promoUsed, String promoImage, String pin, UpdatedAt updatedAt,
      String timestamp) {
    super();
    this.userId = userId;
    this.historyId = historyId;
    this.historyName = historyName;
    this.amount = amount;
    this.mobileNo = mobileNo;
    this.image = image;
    this.headerStatus = headerStatus;
    this.status = status;
    this.orderStatus = orderStatus;
    this.products = products;
    this.senderName = senderName;
    this.senderMobile = senderMobile;
    this.receiverName = receiverName;
    this.receiverMobile = receiverMobile;
    this.promoUsed = promoUsed;
    this.promoImage = promoImage;
    this.pin = pin;
    this.updatedAt = updatedAt;
    this.timestamp = timestamp;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public HistoryResult withUserId(long userId) {
    this.userId = userId;
    return this;
  }

  public long getHistoryId() {
    return historyId;
  }

  public void setHistoryId(long historyId) {
    this.historyId = historyId;
  }

  public HistoryResult withHistoryId(long historyId) {
    this.historyId = historyId;
    return this;
  }

  public String getHistoryName() {
    return historyName;
  }

  public void setHistoryName(String historyName) {
    this.historyName = historyName;
  }

  public HistoryResult withHistoryName(String historyName) {
    this.historyName = historyName;
    return this;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public HistoryResult withAmount(String amount) {
    this.amount = amount;
    return this;
  }

  public Object getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(Object mobileNo) {
    this.mobileNo = mobileNo;
  }

  public HistoryResult withMobileNo(Object mobileNo) {
    this.mobileNo = mobileNo;
    return this;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public HistoryResult withImage(String image) {
    this.image = image;
    return this;
  }

  public String getHeaderStatus() {
    return headerStatus;
  }

  public void setHeaderStatus(String headerStatus) {
    this.headerStatus = headerStatus;
  }

  public HistoryResult withHeaderStatus(String headerStatus) {
    this.headerStatus = headerStatus;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public HistoryResult withStatus(String status) {
    this.status = status;
    return this;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public HistoryResult withOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
    return this;
  }

  public List<HistoryProduct> getProducts() {
    return products;
  }

  public void setProducts(List<HistoryProduct> products) {
    this.products = products;
  }

  public HistoryResult withProducts(List<HistoryProduct> products) {
    this.products = products;
    return this;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public HistoryResult withSenderName(String senderName) {
    this.senderName = senderName;
    return this;
  }

  public String getSenderMobile() {
    return senderMobile;
  }

  public void setSenderMobile(String senderMobile) {
    this.senderMobile = senderMobile;
  }

  public HistoryResult withSenderMobile(String senderMobile) {
    this.senderMobile = senderMobile;
    return this;
  }

  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  public HistoryResult withReceiverName(String receiverName) {
    this.receiverName = receiverName;
    return this;
  }

  public String getReceiverMobile() {
    return receiverMobile;
  }

  public void setReceiverMobile(String receiverMobile) {
    this.receiverMobile = receiverMobile;
  }

  public HistoryResult withReceiverMobile(String receiverMobile) {
    this.receiverMobile = receiverMobile;
    return this;
  }

  public String getPromoUsed() {
    return promoUsed;
  }

  public void setPromoUsed(String promoUsed) {
    this.promoUsed = promoUsed;
  }

  public HistoryResult withPromoUsed(String promoUsed) {
    this.promoUsed = promoUsed;
    return this;
  }

  public String getPromoImage() {
    return promoImage;
  }

  public void setPromoImage(String promoImage) {
    this.promoImage = promoImage;
  }

  public HistoryResult withPromoImage(String promoImage) {
    this.promoImage = promoImage;
    return this;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public HistoryResult withPin(String pin) {
    this.pin = pin;
    return this;
  }

  public UpdatedAt getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(UpdatedAt updatedAt) {
    this.updatedAt = updatedAt;
  }

  public HistoryResult withUpdatedAt(UpdatedAt updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public HistoryResult withTimestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(userId);
    dest.writeValue(historyId);
    dest.writeValue(historyName);
    dest.writeValue(amount);
    dest.writeValue(mobileNo);
    dest.writeValue(image);
    dest.writeValue(headerStatus);
    dest.writeValue(status);
    dest.writeValue(orderStatus);
    dest.writeList(products);
    dest.writeValue(senderName);
    dest.writeValue(senderMobile);
    dest.writeValue(receiverName);
    dest.writeValue(receiverMobile);
    dest.writeValue(promoUsed);
    dest.writeValue(promoImage);
    dest.writeValue(pin);
    dest.writeValue(updatedAt);
    dest.writeValue(timestamp);
  }

  public int describeContents() {
    return 0;
  }

}
