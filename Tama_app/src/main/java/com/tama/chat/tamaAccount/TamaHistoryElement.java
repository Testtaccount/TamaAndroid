package com.tama.chat.tamaAccount;

/**
 * Created by Avo on 18-May-17.
 */

public class TamaHistoryElement {
    private String user_id;
    private String history_id;
    private String history_name;
    private String amount;
    private String mobile_no;
    private String image;
    private String header_status;
    private String status;
    private String updated_at;
    private String timestamp;

    public TamaHistoryElement(){
    }

    public TamaHistoryElement(String user_id, String history_id, String history_name, String amount,
                              String mobile_no, String image, String status,String updated_at){
        this.user_id = user_id;
        this.history_id = history_id;
        this.history_name = history_name;
        this.amount = amount;
        this.mobile_no = mobile_no;
        this.image = image;
        this.status = status;
        this.updated_at = updated_at;
    }

    public String getUser_id(){
        return this.user_id;
    }

    public void setUser_id(String id){
        this.user_id = id;
    }

    public String getHistoryId(){
        return this.history_id;
    }

    public void setHistoryID(String id){
        this.history_id = id;
    }

    public String getHistoryName(){
        return this.history_name;
    }

    public void setHistoryName(String nam){
        this.history_name = nam;
    }

    public String getAmount(){
        return this.amount;
    }

    public void setAmount(String am){
        this.amount = am;
    }

    public String getPhoneNumber(){
        return this.mobile_no;
    }

    public void setPhoneNumber(String num){
        this.mobile_no = num;
    }

    public String getImageUrl(){
        return this.image;
    }

    public void setImageUrl(String url){
        this.image = url;
    }

    public void setHeaderStatus(String num){
        this.header_status = num;
    }

    public String getHeaderStatus(){
        return header_status;
    }

    public void setStatus(String num){
        this.status = num;
    }

    public String getStatus(){
        return status;
    }

    public void setUpdatedAt(String updated_at){
        this.updated_at = updated_at;
    }

    public String getUpdatedAt(){
        return updated_at;
    }

    public void setTimeStamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getTimeStamp(){
        return timestamp;
    }


}
