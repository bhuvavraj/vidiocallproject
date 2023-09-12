package com.example.vidiocallproject.Activites.Model;

public class UserModel {
    String id,name,profile,city;
    long coins;
    public  UserModel(){}

    public UserModel(String id, String name, String profile, String city, long coins) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.city = city;
        this.coins = coins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }
}
