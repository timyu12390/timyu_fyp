package com.example.timyu.timyu_fyp.Class;

/**
 * Created by timyu on 31/3/2017.
 */

public class SuggestQuestion {
    String placeName,placeId;
    boolean status = false;




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
