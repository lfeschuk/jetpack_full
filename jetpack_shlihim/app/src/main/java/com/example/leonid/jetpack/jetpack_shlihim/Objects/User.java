package com.example.leonid.jetpack.jetpack_shlihim.Objects;

public class User {

    public String username;
    public String phone;
    public String indexString ;
    public Boolean enable_notification = false;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(User ua) {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        this.username = ua.getUsername();
        this.enable_notification = ua.getEnable_notification();
        this.phone = ua.getPhone();
        this.indexString = ua.getIndexString();
    }

    public User(String username,String phone,String indexString) {
        this.username = username;
        this.phone = phone;
        this.indexString = indexString;
    }

    public String getIndexString() {
        return indexString;
    }

    public void setIndexString(String indexString) {
        this.indexString = indexString;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Boolean getEnable_notification() {
        return enable_notification;
    }

    public void setEnable_notification(Boolean enable_notification) {
        this.enable_notification = enable_notification;
    }
}
