package com.byd.test.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：byd666 on 2017/10/17 14:30
 * 邮箱：sdkjdxbyd@163.com
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="user_provider.db";
    public static final String USER_TABLE_NAME="user";
    private static final int VERSION=4;
    //建表语句
    private String CREATE_USER_TABLE="CREATE TABLE IF NOT EXISTS "+USER_TABLE_NAME+"(_id INTEGER PRIMARY KEY,name,sex)";
    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    //用于创建数据库表的方法，执行数据库建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }
    //每次都会调用，用于版本更新
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<newVersion){
            //删除旧表，建立新表
            db.execSQL("drop table if exists user");
            onCreate(db);
        }
    }
//    此时，并没有数据库，只有heper在调用：
//    getWritableDatabase();			//两个方法得到的db都可以读写操作，当磁盘满了
//    getReadableDatabase();			//第二种不会继续向内存中写，第一种会尝试去写
//    时才会创建

}
