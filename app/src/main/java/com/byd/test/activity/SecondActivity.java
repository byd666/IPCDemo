package com.byd.test.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.byd.test.R;
import com.byd.test.model.User;
import com.byd.test.utils.MyContstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
public class SecondActivity extends AppCompatActivity {
    private static String TAG="SecondActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onResume() {
        super.onResume();
        removeFromFile();
    }

    private void removeFromFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user=null;
                File cacheFile=new File(MyContstants.CHAPTER_2_PATH);
                ObjectInputStream inputStream=null;
                try {
                    inputStream=new ObjectInputStream(new FileInputStream(cacheFile));
                    user= (User) inputStream.readObject();
                    Log.e(TAG,"removeFromFile user:"+user);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(inputStream!=null)
                        {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
