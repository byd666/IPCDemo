package com.byd.test.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/18.
 */

public class MyParUser implements Parcelable {
    public int id;
    public String userName;

    public MyParUser(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }


    protected MyParUser(Parcel in) {
        id = in.readInt();
        userName = in.readString();
    }

    public static final Creator<MyParUser> CREATOR = new Creator<MyParUser>() {
        @Override
        public MyParUser createFromParcel(Parcel in) {
            return new MyParUser(in);
        }

        @Override
        public MyParUser[] newArray(int size) {
            return new MyParUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userName);
    }
}
