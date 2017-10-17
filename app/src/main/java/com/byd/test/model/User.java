package com.byd.test.model;

import java.io.Serializable;

/**
 * Created by byd666 on 2016/9/16.
 */

public class User implements Serializable{
    private static final long serialVersionUID=516234562142136451L;

    public String userId;
    private String usereName;
    public User(String userId, String usereName) {
        this.userId = userId;
        this.usereName = usereName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsereName() {
        return usereName;
    }

    public void setUsereName(String usereName) {
        this.usereName = usereName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", usereName='" + usereName + '\'' +
                '}';
    }
}
