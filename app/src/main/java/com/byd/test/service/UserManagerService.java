package com.byd.test.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.byd.aidl.IOnNewUserAddListener;
import com.byd.aidl.IUserManager;
import com.byd.aidl.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 作者：byd666 on 2017/10/14 09:58
 * 邮箱：sdkjdxbyd@163.com
 */
public class UserManagerService extends Service {
    private static final String TAG="UserManagerService";
    //支持并发读写，自动的进行线程同步
    /*****************************************************************************
     * 虽然AIDL中只能支持的List是ArrayList，但是CopyOnWriteArrayList为什么能正常工作呢，那是由于AIDL中所支持
     * 的是抽象的List，而List只是一个接口，虽然服务端返回的是CopyOnWriteArrayList，但是Binder中会按照List的
     * 规范去访问数据并最终会形成一个新的ArrayList返回给客户端，类似的还有ConcurrentHashMap。
     */
    private AtomicBoolean mIsServiceDestoryed=new AtomicBoolean(false);
    private int userId=3;
    private CopyOnWriteArrayList<User> mUserList=new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewUserAddListener> mListenerList=new RemoteCallbackList<>();
    private Binder mBinder=new IUserManager.Stub(){
        @Override
        public List<User> getUserList() throws RemoteException {
            return mUserList;
        }
        @Override
        public void addUser(User user) throws RemoteException {
            mUserList.add(user);
        }
        @Override
        public void registerListener(IOnNewUserAddListener listener) throws RemoteException {
                mListenerList.register(listener);
        }
        @Override
        public void unregisterListener(IOnNewUserAddListener listener) throws RemoteException {
               mListenerList.unregister(listener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mUserList.add(new User(1,"Tom"));
        mUserList.add(new User(2,"Jack"));
        //开启一个新的线程，每隔5秒添加一个新的用户
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    private void onNewUserAdd(User user) throws RemoteException {
        int len=mListenerList.beginBroadcast();
        for (int i=0;i<len;i++){
            IOnNewUserAddListener listener=mListenerList.getBroadcastItem(i);
            Log.e(TAG,"onNewUserAdd,notify listener#"+listener);
            if(listener!=null)
            {
                listener.onNewUserAdd(user);
            }
        }
        mListenerList.finishBroadcast();
    }
    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            //当Service还未销毁时
            while(!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ++userId;
                User user=new User(userId,"new User#"+userId);
                try {
                    onNewUserAdd(user);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check=checkCallingOrSelfPermission("com.byd.test.permission.ACCESS_BOOK_SERVICE");
        if(check== PackageManager.PERMISSION_DENIED){
            return null;
        }
        return mBinder;
    }
}
