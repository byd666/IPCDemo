// IBookManager.aidl
package com.byd.aidl;

// Declare any non-default types here with import statements
import com.byd.aidl.User;
import com.byd.aidl.IOnNewUserAddListener;
interface IUserManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List <User> getUserList();
    void addUser(in User user);
    void registerListener(IOnNewUserAddListener listener);
    void unregisterListener(IOnNewUserAddListener listener);
 }
