
package com.tama.chat.tamaAccount.entry.historyPojos;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryData implements Parcelable {

  @SerializedName("code")
  @Expose
  private String code;
  @SerializedName("http_code")
  @Expose
  private long httpCode;
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("result")
  @Expose
  private List<HistoryResult> result = new ArrayList<HistoryResult>();
  public final static Parcelable.Creator<HistoryData> CREATOR = new Creator<HistoryData>() {


    @SuppressWarnings({
        "unchecked"
    })
    public HistoryData createFromParcel(Parcel in) {
      return new HistoryData(in);
    }

    public HistoryData[] newArray(int size) {
      return (new HistoryData[size]);
    }

  };

  protected HistoryData(Parcel in) {
    this.code = ((String) in.readValue((String.class.getClassLoader())));
    this.httpCode = ((long) in.readValue((long.class.getClassLoader())));
    this.message = ((String) in.readValue((String.class.getClassLoader())));
    in.readList(this.result, (HistoryResult.class.getClassLoader()));
  }

  /**
   * No args constructor for use in serialization
   */
  public HistoryData() {
  }

  /**
   *
   * @param message
   * @param result
   * @param httpCode
   * @param code
   */
  public HistoryData(String code, long httpCode, String message, List<HistoryResult> result) {
    super();
    this.code = code;
    this.httpCode = httpCode;
    this.message = message;
    this.result = result;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public HistoryData withCode(String code) {
    this.code = code;
    return this;
  }

  public long getHttpCode() {
    return httpCode;
  }

  public void setHttpCode(long httpCode) {
    this.httpCode = httpCode;
  }

  public HistoryData withHttpCode(long httpCode) {
    this.httpCode = httpCode;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public HistoryData withMessage(String message) {
    this.message = message;
    return this;
  }

  public List<HistoryResult> getResult() {
    return result;
  }

  public void setResult(List<HistoryResult> result) {
    this.result = result;
  }

  public HistoryData withResult(List<HistoryResult> result) {
    this.result = result;
    return this;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(code);
    dest.writeValue(httpCode);
    dest.writeValue(message);
    dest.writeList(result);
  }

  public int describeContents() {
    return 0;
  }

}
