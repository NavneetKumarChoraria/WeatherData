package com.assignment.nv.novopayassignment;

/**
 * Created by LightiningGuard on 5/21/2017.
 */

public class WeatherData {

    private String mCity,mCountry , mTempreture;

    public WeatherData(final String city, final String country, final String temp) {
        mCity = city;
       mCountry = country;
        mTempreture = temp;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getTempreture() {
        return mTempreture;
    }

    public void setTempreture(String mTempreture) {
        this.mTempreture = mTempreture;
    }
}
