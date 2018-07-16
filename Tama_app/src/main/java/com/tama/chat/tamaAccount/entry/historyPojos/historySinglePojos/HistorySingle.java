
package com.tama.chat.tamaAccount.entry.historyPojos.historySinglePojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistorySingle implements Parcelable
{

    @SerializedName("data")
    @Expose
    private HistorySingleData data;
    public final static Creator<HistorySingle> CREATOR = new Creator<HistorySingle>() {


        @SuppressWarnings({
            "unchecked"
        })
        public HistorySingle createFromParcel(Parcel in) {
            return new HistorySingle(in);
        }

        public HistorySingle[] newArray(int size) {
            return (new HistorySingle[size]);
        }

    }
    ;

    protected HistorySingle(Parcel in) {
        this.data = ((HistorySingleData) in.readValue((HistorySingleData.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public HistorySingle() {
    }

    /**
     * 
     * @param data
     */
    public HistorySingle(HistorySingleData data) {
        super();
        this.data = data;
    }

    public HistorySingleData getData() {
        return data;
    }

    public void setData(HistorySingleData data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    public int describeContents() {
        return  0;
    }

}
