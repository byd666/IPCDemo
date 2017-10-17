package com.byd.test.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.byd.test.R;
import com.byd.test.service.MessengerService;
import com.byd.test.utils.MyContstants;

public class MessengerActivity extends AppCompatActivity {
    private static final String TAG="MessengerActivity";
    private Messenger mService;
    /**
     * 处理服务端接回复过来的消息
     */
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyContstants.MSG_FROM_SERVICE:
                    Log.e(TAG,msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    /**
     * 将messenger传递过来的消息交给Handler处理
      */
    private Messenger messenger=new Messenger(new MessengerHandler());

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //根据服务端返回的binder对象创建Messenger对象。
            mService=new Messenger(service);
            Message message=Message.obtain(null, MyContstants.MSG_FROM_CLIENT);
            //一个可以类似Map集合的存储数据的容器
            Bundle bundle=new Bundle();
            bundle.putString("msg","hello my server,i am client.");
            message.setData(bundle);
            //当客户端发送消息的时候，需要将接收服务端回复的Messenger通过Message的replyTo参数传给服务端
            message.replyTo=messenger;
            try {
                //使用此对象向服务端发送消息
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        //绑定远程MessengerService
        Intent intent=new Intent(this, MessengerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        //销毁时解绑
        unbindService(mConnection);
        super.onDestroy();
    }
}
