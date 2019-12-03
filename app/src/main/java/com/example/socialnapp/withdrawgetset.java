package com.example.socialnapp;

public class withdrawgetset {

    String earn, profileimage,username;

    public withdrawgetset(){

    }

    public withdrawgetset(String earn, String profileimage, String username) {
        this.earn = earn;
        this.profileimage = profileimage;
        this.username = username;
    }

    public String getEarn() {
        return earn;
    }

    public void setEarn(String earn) {
        this.earn = earn;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
