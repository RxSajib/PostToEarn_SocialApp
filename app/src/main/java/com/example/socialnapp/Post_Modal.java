package com.example.socialnapp;

public class Post_Modal {

    String profile_image, postimage, time, post_descptrion, username;

    public Post_Modal(){

    }


    public Post_Modal(String profile_image, String postimage, String time, String post_descptrion, String username) {
        this.profile_image = profile_image;
        this.postimage = postimage;
        this.time = time;
        this.post_descptrion = post_descptrion;
        this.username = username;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPost_descptrion() {
        return post_descptrion;
    }

    public void setPost_descptrion(String post_descptrion) {
        this.post_descptrion = post_descptrion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
