package com.example.demo.models;

import java.util.ArrayList;
public class Country {
 private ArrayList<String> countryList =new ArrayList<>();
    public ArrayList<String> getCountryList() {
        countryList.add("+90");
        countryList.add("+41");
        countryList.add("+47");
        return countryList;
    }
}
