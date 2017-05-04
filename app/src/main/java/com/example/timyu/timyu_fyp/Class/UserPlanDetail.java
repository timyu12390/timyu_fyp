package com.example.timyu.timyu_fyp.Class;

/**
 * Created by timyu on 5/5/2017.
 */

public class UserPlanDetail {
    String planId, planTitle, planAddress, planName, planCountry;
    int planDay, planTime, planStart, planEnd, userId;
    double planLat, planLng;

    public UserPlanDetail(String planId, String planTitle, String planAddress, String planName, String planCountry, int planDay, int planTime, int planStart, int planEnd, int userId, double planLat, double planLng) {
        this.planId = planId;
        this.planTitle = planTitle;
        this.planAddress = planAddress;
        this.planName = planName;
        this.planCountry = planCountry;
        this.planDay = planDay;
        this.planTime = planTime;
        this.planStart = planStart;
        this.planEnd = planEnd;
        this.userId = userId;
        this.planLat = planLat;
        this.planLng = planLng;
    }


    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanAddress() {
        return planAddress;
    }

    public void setPlanAddress(String planAddress) {
        this.planAddress = planAddress;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanCountry() {
        return planCountry;
    }

    public void setPlanCountry(String planCountry) {
        this.planCountry = planCountry;
    }

    public int getPlanDay() {
        return planDay;
    }

    public void setPlanDay(int planDay) {
        this.planDay = planDay;
    }

    public int getPlanTime() {
        return planTime;
    }

    public void setPlanTime(int planTime) {
        this.planTime = planTime;
    }

    public int getPlanStart() {
        return planStart;
    }

    public void setPlanStart(int planStart) {
        this.planStart = planStart;
    }

    public int getPlanEnd() {
        return planEnd;
    }

    public void setPlanEnd(int planEnd) {
        this.planEnd = planEnd;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getPlanLat() {
        return planLat;
    }

    public void setPlanLat(double planLat) {
        this.planLat = planLat;
    }

    public double getPlanLng() {
        return planLng;
    }

    public void setPlanLng(double planLng) {
        this.planLng = planLng;
    }


}
