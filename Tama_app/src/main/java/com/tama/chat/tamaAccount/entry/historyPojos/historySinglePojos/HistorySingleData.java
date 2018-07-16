
package com.tama.chat.tamaAccount.entry.historyPojos.historySinglePojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryResult;

public class HistorySingleData implements Parcelable
{

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
    private HistoryResult result;
    public final static Creator<HistorySingleData> CREATOR = new Creator<HistorySingleData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public HistorySingleData createFromParcel(Parcel in) {
            return new HistorySingleData(in);
        }

        public HistorySingleData[] newArray(int size) {
            return (new HistorySingleData[size]);
        }

    }
    ;

    protected HistorySingleData(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.httpCode = ((long) in.readValue((long.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.result = ((HistoryResult) in.readValue((HistoryResult.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public HistorySingleData() {
    }

    /**
     * 
     * @param message
     * @param result
     * @param httpCode
     * @param code
     */
    public HistorySingleData(String code, long httpCode, String message, HistoryResult result) {
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

    public long getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(long httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HistoryResult getResult() {
        return result;
    }

    public void setResult(HistoryResult result) {
        this.result = result;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(httpCode);
        dest.writeValue(message);
        dest.writeValue(result);
    }

    public int describeContents() {
        return  0;
    }

}
