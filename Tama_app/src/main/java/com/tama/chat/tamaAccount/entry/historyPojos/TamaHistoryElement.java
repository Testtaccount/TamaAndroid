
package com.tama.chat.tamaAccount.entry.historyPojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TamaHistoryElement implements Parcelable {


  @SerializedName("data")
  @Expose
  private HistoryData data;
  public final static Parcelable.Creator<TamaHistoryElement> CREATOR = new Creator<TamaHistoryElement>() {


    @SuppressWarnings({
        "unchecked"
    })
    public TamaHistoryElement createFromParcel(Parcel in) {
      return new TamaHistoryElement(in);
    }

    public TamaHistoryElement[] newArray(int size) {
      return (new TamaHistoryElement[size]);
    }

  };

  protected TamaHistoryElement(Parcel in) {
    this.data = ((HistoryData) in.readValue((HistoryData.class.getClassLoader())));
  }

  /**
   * No args constructor for use in serialization
   */
  public TamaHistoryElement() {
  }

  /**
   *
   * @param data
   */
  public TamaHistoryElement(HistoryData data) {
    super();
    this.data = data;
  }

  public HistoryData getData() {
    return data;
  }

  public void setData(HistoryData data) {
    this.data = data;
  }

  public TamaHistoryElement withData(HistoryData data) {
    this.data = data;
    return this;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(data);
  }

  public int describeContents() {
    return 0;
  }

}
