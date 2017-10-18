package com.byd.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * 作者：byd666 on 2017/10/18 14:35
 * 邮箱：sdkjdxbyd@163.com
 */

public class TCPServerService extends Service {
    //定义一个布尔类型的值来记录Server是否被销毁 false:存活 true：被销毁
    private boolean mIsServiceDestory=false;
    private String [] mDefineMessages=new String[]{"你好啊。爱猴","你是哪里人","你是干啥的啊","你知道吗你可牛逼了"};
    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestory=true;
        super.onDestroy();
    }
    public class TcpServer implements Runnable{

        @SuppressWarnings("resource")
        @Override
        public void run() {
            ServerSocket serverSocket=null;
            try {
                //这里监听本地的8688端口
                serverSocket=new ServerSocket(8688);
            } catch (IOException e) {
                System.err.println("establish tcp server failed,port:8688");
                e.printStackTrace();
                return;
            }
            while(!mIsServiceDestory){
                //接收客户端请求
                try {
                    final Socket clientSocket=serverSocket.accept();
                    System.out.println("accept");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                responseClient(clientSocket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void responseClient(Socket client) throws IOException {
            //接受客户端信息
            BufferedReader in=new BufferedReader(new InputStreamReader(client.getInputStream()));
            //用于向客户端发送消息
            PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
            out.println("欢迎来到byd的聊天室");
            while(!mIsServiceDestory)
            {
                String str=in.readLine();
                System.out.println("msg from client:"+str);
                if (str== null){
                    //客户端断开连接
                    break;
                }
                int i=new Random().nextInt(mDefineMessages.length);
                String msg=mDefineMessages[i];
                out.println(msg);
                System.out.println("send:"+msg);
                System.out.println("client quit.");
                //关闭流
                out.close();
                in.close();
                client.close();
            }
        }
    }
}
