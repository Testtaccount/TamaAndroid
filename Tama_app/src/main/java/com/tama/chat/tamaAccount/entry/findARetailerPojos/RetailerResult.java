
package com.tama.chat.tamaAccount.entry.findARetailerPojos;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetailerResult implements Parcelable
{

    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_address")
    @Expose
    private String shopAddress;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("area")
    @Expose
    private String area;
    public final static Creator<RetailerResult> CREATOR = new Creator<RetailerResult>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RetailerResult createFromParcel(Parcel in) {
            return new RetailerResult(in);
        }

        public RetailerResult[] newArray(int size) {
            return (new RetailerResult[size]);
        }

    }
    ;

    protected RetailerResult(Parcel in) {
        this.shopName = ((String) in.readValue((String.class.getClassLoader())));
        this.shopAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.zipCode = ((String) in.readValue((String.class.getClassLoader())));
        this.area = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public RetailerResult() {
    }

    /**
     * 
     * @param shopAddress
     * @param area
     * @param shopName
     * @param zipCode
     */
    public RetailerResult(String shopName, String shopAddress, String zipCode, String area) {
        super();
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.zipCode = zipCode;
        this.area = area;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(shopName);
        dest.writeValue(shopAddress);
        dest.writeValue(zipCode);
        dest.writeValue(area);
    }

    public int describeContents() {
        return  0;
    }

}
