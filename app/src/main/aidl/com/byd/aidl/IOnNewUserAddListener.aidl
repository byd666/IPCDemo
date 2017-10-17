// IONNewUserAddListener.aidl
package com.byd.aidl;

// Declare any non-default types here with import statements
import com.byd.aidl.User;
interface IOnNewUserAddListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewUserAdd(in User user);
}
