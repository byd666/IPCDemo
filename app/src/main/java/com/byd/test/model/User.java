package com.byd.test.model;

import java.io.Serializable;

/**
 * Created by byd666 on 2016/9/16.
 */

public class User implements Serializable{
    private static final long serialVersionUID=516234562142136451L;

    public int  userId;
    public String userName;
    public User(){}
    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsereName() {
        return userName;
    }

    public void setUsereName(String usereName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", usereName='" + userName + '\'' +
                '}';
    }
}
