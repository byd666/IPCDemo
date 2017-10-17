package com.byd.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：byd666 on 2017/9/20 15:09
 * 邮箱：sdkjdxbyd@163.com
 */

public class User implements Parcelable{
    public int bookId;
    public String bookName;
    public User(){

    }
    public User(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }
    protected User(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }

    @Override
    public String toString() {
        return "User{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
