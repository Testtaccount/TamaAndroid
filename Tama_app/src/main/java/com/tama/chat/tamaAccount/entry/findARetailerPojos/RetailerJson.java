
package com.tama.chat.tamaAccount.entry.findARetailerPojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetailerJson implements Parcelable{

    @SerializedName("data")
    @Expose
    private RetailerData data;
    public final static Creator<RetailerJson> CREATOR = new Creator<RetailerJson>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RetailerJson createFromParcel(Parcel in) {
            return new RetailerJson(in);
        }

        public RetailerJson[] newArray(int size) {
            return (new RetailerJson[size]);
        }

    }
    ;

    protected RetailerJson(Parcel in) {
        this.data = ((RetailerData) in.readValue((RetailerData.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public RetailerJson() {
    }

    /**
     * 
     * @param data
     */
    public RetailerJson(RetailerData data) {
        super();
        this.data = data;
    }

    public RetailerData getData() {
        return data;
    }

    public void setData(RetailerData data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    public int describeContents() {
        return  0;
    }

}
