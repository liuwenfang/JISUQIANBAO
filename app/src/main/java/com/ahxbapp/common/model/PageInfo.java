package com.ahxbapp.common.model;

import org.json.JSONObject;

import java.io.Serializable;

public class PageInfo implements Serializable {

    public String country = "";
    public String country_code = "";
    public String iso_code = "";

    public PageInfo() {
    }

    public PageInfo(JSONObject json) {
        country = json.optString("country", "");
        country_code = json.optString("country_code", "");
        iso_code = json.optString("iso_code", "");
    }

    public String getFirstLetter() {
        String letter = country.substring(0, 1).toUpperCase();
        if (0 <= letter.compareTo("A") && letter.compareTo("Z") <= 0) {
            return letter;
        }

        return "#";
    }

    public String getCountryCode() {
        return "+" + country_code;
    }

    public static PageInfo getChina() {
        PageInfo phoneCountry = new PageInfo();
        phoneCountry.country = "China";
        phoneCountry.country_code = "86";
        phoneCountry.iso_code = "cn";
        return phoneCountry;
    }

}
