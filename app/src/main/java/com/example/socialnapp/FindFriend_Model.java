package com.example.socialnapp;

public class FindFriend_Model  {

    String image_uri, name, country, username;

    public FindFriend_Model(){

    }


    public FindFriend_Model(String image_uri, String name, String country, String username) {
        this.image_uri = image_uri;
        this.name = name;
        this.country = country;
        this.username = username;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
