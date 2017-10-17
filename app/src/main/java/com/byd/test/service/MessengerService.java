package com.byd.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.byd.test.utils.MyContstants;

/**
 * 作者：byd666 on 2017/10/12 15:28
 * 邮箱：sdkjdxbyd@163.com
 */
public class MessengerService extends Service {
    private static final String TAG="MessengerService";
    /**
     * 用来处理客户端发来的信息，并从中取出文本信息打印。
     */
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyContstants.MSG_FROM_CLIENT:
                    Log.e(TAG,msg.getData().getString("msg"));
                    //接收message中传递过来的Messenger的对象，并通过其回复客户端
                    Messenger client=msg.replyTo;
                    Message message=Message.obtain(null,MyContstants.MSG_FROM_SERVICE);
                    Bundle bundle=new Bundle();
                    bundle.putString("reply","got it，i am service");
                    message.setData(bundle);
                    try {
                        client.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    /**
     * 将客户端发送来的消息传递给MessengerHandler处理
     */
    private final Messenger mMessenger=new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //返回的是Messenger中的Binder对象
        return mMessenger.getBinder();
    }
}
