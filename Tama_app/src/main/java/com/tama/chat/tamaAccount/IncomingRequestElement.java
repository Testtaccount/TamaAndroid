package com.tama.chat.tamaAccount;

/**
 * Created by Avo on 18-May-17.
 */

public class IncomingRequestElement {
    private String request_id;
    private String request_for;
    private String request_from;
    private String request_amount;
    private String message;
    private String image;
    private String request_status;

    public IncomingRequestElement(){
    }

    public IncomingRequestElement(String request_id, String request_for, String request_from,
                                  String request_amount, String dateTime, String message){
        this.request_id = request_id;
        this.request_for = request_for;
        this.request_from = request_from;
        this.request_amount = request_amount;
        this.message = message;
    }

    public String getRequestId(){
        return this.request_id;
    }

    public void setRequestId(String id){
        this.request_id = id;
    }

    public String getRequestFor(){
        return this.request_for;
    }

    public void setRequestFor(String request_for){
        this.request_for = request_for;
    }

    public String getRequestFrom(){
        return this.request_from;
    }

    public void setImageUrl(String url){
        this.image = url;
    }

    public String getImageUrl(){
        return this.image;
    }

    public void setRequestStatus(String request_status){
        this.request_status = request_status;
    }

    public String getRequestStatus(){
        return this.request_status;
    }

    public void setRequestFrom(String request_from){
        this.request_from = request_from;
    }

    public String getRequestAmount(){
        return this.request_amount;
    }

    public void setRequestAmount(String request_amount){
        this.request_amount = request_amount;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
