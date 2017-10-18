package com.byd.test.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.byd.test.R;
import com.byd.test.model.User;

public class ProviderActivity extends AppCompatActivity {
    private static final String TAG="ProviderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        /**
         * 唯一标识了UserProvider，而这个标识正是我们在清单文件中为UserProvider添加的
         *authorities属性所指定的值，
         */
        Uri uri=Uri.parse("content://com.byd.test.provider/user");
        Cursor cursor = getContentResolver().query(uri, new String[]{"_id", "name", "sex"}, null, null, null);
        while(cursor.moveToNext())
        {
            User user=new User();
            user.userId=cursor.getInt(0);
            user.userName=cursor.getString(1);
            Log.e(TAG,user.toString());
        }
       cursor.close();
    }
}
