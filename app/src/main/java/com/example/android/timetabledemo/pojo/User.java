package com.example.android.timetabledemo.pojo;

public class User {
    private String fullName;
    private String username;
    private String password;
    private String type;

    public User() { }

    public User (String fullName, String username, String password, String type) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
