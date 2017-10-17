package com.byd.test.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.byd.aidl.IOnNewUserAddListener;
import com.byd.aidl.IUserManager;
import com.byd.aidl.User;
import com.byd.test.R;
import com.byd.test.service.UserManagerService;

import java.util.List;

public class UserManagerActivity extends AppCompatActivity {
    private static final String TAG="UserManagerActivity";
    private static final int MESSAGE_NEW_USER_ADD=1;
    private IUserManager mUserManager;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_USER_ADD:
                    Log.e(TAG, "handleMessage: add new User:"+msg.obj );
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IUserManager userManager=IUserManager.Stub.asInterface(service);
            try {
                mUserManager=userManager;
                List<User> list=userManager.getUserList();
                Log.e(TAG,list.getClass().getCanonicalName());
                Log.e(TAG,"query user list:"+list.toString());
                User user=new User(3,"byd");
                userManager.addUser(user);
                Log.e(TAG,"add user list:"+user.toString());
                Log.e(TAG,"query user reList:"+userManager.getUserList().toString());
                userManager.registerListener(mOnNewUserAdd);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private IOnNewUserAddListener mOnNewUserAdd= new IOnNewUserAddListener.Stub() {
        @Override
        public void onNewUserAdd(User user) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_USER_ADD,user).sendToTarget();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        Intent intent=new Intent(this, UserManagerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onDestroy() {
        if(mUserManager!=null && mUserManager.asBinder().isBinderAlive()){
            Log.e(TAG,"unregister listener:"+mOnNewUserAdd);
            try {
                mUserManager.unregisterListener(mOnNewUserAdd);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}
