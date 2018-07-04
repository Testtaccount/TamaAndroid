package com.tama.chat.gcm;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Avo on 26-Apr-17.
 */

public class MapBlinkElement implements Serializable{

    private String name;
    private String type;
    private String address;
    private String description;
    private int imageResId;
    private float rating;
    private ArrayList<PhotosRefList> photosRefList = new ArrayList<>();
    private String weekDey;
    private LatLngSer origin;
    private Boolean openNow;
    private static String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?";

    public MapBlinkElement(){}

    private MapBlinkElement(Builder builder){
        this.name = builder.name;
        this.type = builder.type;
        this.imageResId = builder.imageResId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setAddress(String str){
        this.address = str;
    }

    public String getAddress(){
        return address;
    }

    public void setRating(float rating){
        this.rating = rating;
    }

    public float getRating(){
        return rating;
    }

//    public void setPhotosRefList(ArrayList<Pair<String,Integer>> list){
//
//        this.photosRefList = list;
//    }

    public void setPhotosRefList(ArrayList<Pair<String,Integer>> list){
        for(int i = 0; i < list.size();++i){
            photosRefList.add(new PhotosRefList(list.get(i).first,list.get(i).second));
        }
    }

//    public String getPhotoUrlSmollSize(String api_key){
//        return PHOTO_URL+"maxwidth=" + 400 + "&photoreference=" + photosRefList.get(0).first + "&sensor=false&key=" + api_key;
//    }

    public String getPhotoUrl(String api_key, int index, int maxWidth){
        return PHOTO_URL+"maxwidth=" + maxWidth + "&photoreference=" + photosRefList.get(index).references + "&sensor=false&key=" + api_key;
    }

//    public ArrayList<Pair<String,Integer>> getPhotosRefList(){
//        return photosRefList;
//    }

    public ArrayList<PhotosRefList> getPhotosRefList(){
        return photosRefList;
    }

    public void setDescription(String str){
        this.description = str;
    }

    public String getDescription(){
        return description;
    }

    public void setWeekDay(String str){
        this.weekDey = str;
    }

    public String getWeekDay(){
        return weekDey;
    }

    public void setLatLng(Double lat, Double lng){
        origin = new LatLngSer(lat, lng);
    }

    public Double getLatitude(){
        return origin.latitude;
    }

    public Double getLongitude(){
        return origin.longitude;
    }

    public LatLngSer getOrigin(){
        return origin;
    }

    public void setOpenNow(boolean bool){
        this.openNow = bool;
    }

    public Boolean getOpenNow(){
        return openNow;
    }

    public void setImageResId(int id){
        this.imageResId = id;
    }

    public Integer getImageResId(){
        return imageResId == -1 ? null:imageResId;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

//    public String getAvatar(){
//        Uri uri = Uri.parse("android.resource://com.tama.chat/drawable/"+imageName);
//        return uri.toString();
//    }

    public static class Builder{
        private String name;
        private String type;
        private int imageResId = -1;

        public  Builder setType(String type){
            this.type = type;
            return this;
        }

        public  Builder setName(String name){
            this.name = name;
            return this;
        }

        public  Builder setImageResId(int id){
            this.imageResId = id;
            return this;
        }

        public MapBlinkElement build(){
            return new MapBlinkElement(this);
        }
    }

    class PhotosRefList implements Serializable{
        private String references;
        private Integer maxWith;

        public PhotosRefList(String ref, Integer with){
            references = ref;
            maxWith = with;
        }
    }
}
