package com.example.socialnapp;

public class FindFriendModel {

    String  image_uri, name, email_address , country;

    public FindFriendModel(){

    }

    public FindFriendModel(String image_uri, String name, String email_address, String country) {
        this.image_uri = image_uri;
        this.name = name;
        this.email_address = email_address;
        this.country = country;
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

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
