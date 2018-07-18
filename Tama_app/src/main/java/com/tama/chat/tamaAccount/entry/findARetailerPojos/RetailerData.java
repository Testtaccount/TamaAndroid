
package com.tama.chat.tamaAccount.entry.findARetailerPojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RetailerData implements Parcelable
{

    @SerializedName("code")
    @Expose
    private long code;
    @SerializedName("http_code")
    @Expose
    private long httpCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<RetailerResult> result = null;
    public final static Creator<RetailerData> CREATOR = new Creator<RetailerData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RetailerData createFromParcel(Parcel in) {
            return new RetailerData(in);
        }

        public RetailerData[] newArray(int size) {
            return (new RetailerData[size]);
        }

    }
    ;

    protected RetailerData(Parcel in) {
        this.code = ((long) in.readValue((long.class.getClassLoader())));
        this.httpCode = ((long) in.readValue((long.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.result, (RetailerResult.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public RetailerData() {
    }

    /**
     * 
     * @param message
     * @param result
     * @param httpCode
     * @param code
     */
    public RetailerData(long code, long httpCode, String message, List<RetailerResult> result) {
        super();
        this.code = code;
        this.httpCode = httpCode;
        this.message = message;
        this.result = result;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
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

    public List<RetailerResult> getResult() {
        return result;
    }

    public void setResult(List<RetailerResult> result) {
        this.result = result;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(httpCode);
        dest.writeValue(message);
        dest.writeList(result);
    }

    public int describeContents() {
        return  0;
    }

}
