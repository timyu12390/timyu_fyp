package com.example.timyu.timyu_fyp.Class;

import com.google.android.gms.location.places.Place;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by timyu on 26/2/2017.
 */

public class PlaceItem implements Serializable{
    //private Place placeName;
    private String placeId, placeName, placeInfo, placeCountry, placeAddress;
    private Double placeLat, placeLng;
    private String placeType;
    private Date startTime;
    private Date endTime;
    private  int suggestTime;
    private  int day = 1;
    private int placeNum;

    /*public Place getPlaceName() {
        return placeName;
    }


    public void setPlaceName(Place placeName) {
        this.placeName = placeName;
    }*/

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(int suggestTime) {
        this.suggestTime = suggestTime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public int getPlaceNum() {
        return placeNum;
    }

    public void setPlaceNum(int placeNum) {
        this.placeNum = placeNum;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public String getPlaceCountry() {
        return placeCountry;
    }

    public void setPlaceCountry(String placeCountry) {
        this.placeCountry = placeCountry;
    }


    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
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
}

