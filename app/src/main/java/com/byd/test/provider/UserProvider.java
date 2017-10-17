package com.byd.test.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.byd.test.model.db.DbOpenHelper;

/**
 * 作者：byd666 on 2017/10/17 11:05
 * 邮箱：sdkjdxbyd@163.com
 */

/**
 * 自定义一个ContentProvider，实现其六个抽象的方法，并添加打印信息
 * 观察其主要回调在那个线程
 */
public class UserProvider extends ContentProvider {
    private static final String TAG="UserProvider";
    private static final String AUTHORITY="com.byd.test.provider";
    private static final Uri USER_CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/user");
    private static final int USER_URI_CODE=0;
    private static UriMatcher matcher = null;
    private Context context;
    private SQLiteDatabase mDb;
    static {
        matcher=new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }
    private String getTableName(Uri uri){
        String tabName=null;
        switch(matcher.match(uri)){
            case USER_URI_CODE:
                tabName= DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tabName;
    }
    @Override
    public boolean onCreate() {
        Log.e(TAG,"onCreate,当前的线程："+Thread.currentThread().getName());
        context=getContext();
        //初始化数据库
        mDb=new DbOpenHelper(context).getWritableDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDb.execSQL("insert into user values(4,'jack','男');");
            }
        }).start();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG,"query,当前的线程："+Thread.currentThread().getName());
        String table=getTableName(uri);
        Cursor cursor=null;
        if(table == null){
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }
        cursor=mDb.query(table,projection,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e(TAG,"getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e(TAG,"insert");
        String table=getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }
        long id = mDb.insert(table, null, values);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG,"delete");
        String table=getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }
        int count= mDb.delete(table,selection,selectionArgs);
        if(count>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG,"update");
        String table=getTableName(uri);
        if(table == null){
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }
        int row=mDb.update(table,values,selection,selectionArgs);
        if(row>0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }
}
