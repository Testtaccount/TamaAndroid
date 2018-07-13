
package com.tama.chat.tamaAccount.entry.historyPojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatedAt implements Parcelable {

  @SerializedName("date")
  @Expose
  private String date;
  @SerializedName("timezone_type")
  @Expose
  private long timezoneType;
  @SerializedName("timezone")
  @Expose
  private String timezone;
  public final static Parcelable.Creator<UpdatedAt> CREATOR = new Creator<UpdatedAt>() {


    @SuppressWarnings({
        "unchecked"
    })
    public UpdatedAt createFromParcel(Parcel in) {
      return new UpdatedAt(in);
    }

    public UpdatedAt[] newArray(int size) {
      return (new UpdatedAt[size]);
    }

  };

  protected UpdatedAt(Parcel in) {
    this.date = ((String) in.readValue((String.class.getClassLoader())));
    this.timezoneType = ((long) in.readValue((long.class.getClassLoader())));
    this.timezone = ((String) in.readValue((String.class.getClassLoader())));
  }

  /**
   * No args constructor for use in serialization
   */
  public UpdatedAt() {
  }

  /**
   *
   * @param timezone
   * @param timezoneType
   * @param date
   */
  public UpdatedAt(String date, long timezoneType, String timezone) {
    super();
    this.date = date;
    this.timezoneType = timezoneType;
    this.timezone = timezone;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public UpdatedAt withDate(String date) {
    this.date = date;
    return this;
  }

  public long getTimezoneType() {
    return timezoneType;
  }

  public void setTimezoneType(long timezoneType) {
    this.timezoneType = timezoneType;
  }

  public UpdatedAt withTimezoneType(long timezoneType) {
    this.timezoneType = timezoneType;
    return this;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public UpdatedAt withTimezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(date);
    dest.writeValue(timezoneType);
    dest.writeValue(timezone);
  }

  public int describeContents() {
    return 0;
  }

}
