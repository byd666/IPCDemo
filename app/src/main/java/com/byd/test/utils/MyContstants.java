package com.byd.test.utils;

import android.os.Environment;

import java.io.File;

/**
 * 作者：byd666 on 2017/10/10 17:39
 * 邮箱：sdkjdxbyd@163.com
 */

public class MyContstants {
    public static final  String CACHE_File_PATH= Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final  String CHAPTER_2_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"path.txt";
    public static final int  MSG_FROM_CLIENT=10010;
    public static final int  MSG_FROM_SERVICE=10011;
}
