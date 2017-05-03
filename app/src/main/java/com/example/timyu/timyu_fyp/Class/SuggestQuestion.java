package com.example.timyu.timyu_fyp.Class;

/**
 * Created by timyu on 31/3/2017.
 */

public class SuggestQuestion {
    String placeName;
    String placeId;
    String placeCountry;
    String placeAddress;
    String placeType;
    Double placeLat, placeLng;
    int placeTime;
    boolean status = false;

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public Double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(Double placeLat) {
        this.placeLat = placeLat;
    }

    public Double getPlaceLng() {
        return placeLng;
    }

    public void setPlaceLng(Double placeLng) {
        this.placeLng = placeLng;
    }

    public int getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(int placeTime) {
        this.placeTime = placeTime;
    }

    public String getPlaceCountry() {
        return placeCountry;
    }

    public void setPlaceCountry(String placeCountry) {
        this.placeCountry = placeCountry;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceId(){
        return placeId;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }


}
