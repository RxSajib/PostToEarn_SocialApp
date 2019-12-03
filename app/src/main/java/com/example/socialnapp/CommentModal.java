package com.example.socialnapp;

public class CommentModal {

  private String comments_text, date, name, time, userID;

  public CommentModal(){

  }

    public CommentModal(String comments_text, String date, String name, String time, String userID) {
        this.comments_text = comments_text;
        this.date = date;
        this.name = name;
        this.time = time;
        this.userID = userID;
    }

    public String getComments_text() {
        return comments_text;
    }

    public void setComments_text(String comments_text) {
        this.comments_text = comments_text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
