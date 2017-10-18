package com.byd.test.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byd.test.R;
import com.byd.test.service.TCPServerService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TCPClientActivity extends AppCompatActivity {
    //收到新消息
    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    //确定socket是否连接
    private static final int MESSAGE_SOCKET_CONNECTED = 2;
    @BindView(R.id.show_msg)
    TextView showMsg;
    @BindView(R.id.input_msg_et)
    EditText inputMsgEt;
    @BindView(R.id.send_msg_btn)
    Button sendMsgBtn;

    private Socket mClientSocket;
    private PrintWriter mWriter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case MESSAGE_RECEIVE_NEW_MSG:
                    showMsg.setText(showMsg.getText()+(String) msg.obj);
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    sendMsgBtn.setEnabled(true);
                    break;
                default:
                 break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        ButterKnife.bind(this);
        //开启后端service
        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }

    /**
     * 当听关闭客户端时，关闭socket
     */
    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @OnClick(R.id.send_msg_btn)
    public void onViewClicked() {
        final String msg = inputMsgEt.getText().toString().trim();
        if (!TextUtils.isEmpty(msg)) {
            mWriter.println(msg);
            inputMsgEt.setText("");
            String time = new SimpleDateFormat("HH:MM:ss").format(new Date(System.currentTimeMillis()));
            final String showMessage = "self" + time + ":" + msg + "\n";
            showMsg.setText(showMsg.getText() + showMessage);
        }
    }


    //连接Server
    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8688);
                mClientSocket=socket;
                mWriter=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                //通过Handler通知已经连上了socket
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                System.out.println("connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("connect tcp server failed,retry...");
            }
        }
        try {
            //接收服务端的消息
            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //当客户端还未退出时
            while (!TCPClientActivity.this.isFinishing())
            {
                String msg = br.readLine();
                System.out.println("receive:"+msg);
                if(msg!=null)
                {
                    String time = new SimpleDateFormat("HH:MM:ss").format(new Date(System.currentTimeMillis()));
                    final String showMessage = "server" + time + ":" + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG,showMessage).sendToTarget();
                }
            }
            System.out.println("quit...");
            mWriter.close();
            br.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
