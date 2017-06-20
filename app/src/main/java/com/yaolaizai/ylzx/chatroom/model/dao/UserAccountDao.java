package com.yaolaizai.ylzx.chatroom.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.model.db.UserAccountDB;

/**
 * Created by ylzx on 2017/6/19.
 */
public class UserAccountDao {

    private final UserAccountDB mHelper;

    public UserAccountDao(Context context) {
        mHelper = new UserAccountDB(context);
    }

    // 添加用户到数据库
    public void addAccount(UserInfo user){
        // 获取数据库连接
        SQLiteDatabase db = mHelper.getWritableDatabase();

        // 执行添加操作
        ContentValues values = new ContentValues();
        values.put(UserAccountTable.COL_HXID, user.getHxid());
        values.put(UserAccountTable.COL_NAME, user.getName());
        values.put(UserAccountTable.COL_NICK, user.getNick());
        values.put(UserAccountTable.COL_PHOTO, user.getPhoto());

        db.replace(UserAccountTable.TAB_NAME, null,values);
    }

    // 获取用户
    public UserInfo getAccount(String appUser){
        // 获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行查询操作
        String sql = "select * from "+ UserAccountTable.TAB_NAME +" where "+UserAccountTable.COL_NAME+"=?";

        Cursor cursor = db.rawQuery(sql, new String[]{appUser});
        UserInfo userInfo = null;

        if(cursor.moveToNext()) {
            userInfo = new UserInfo();

            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO)));
        }
        // 关闭资源
        cursor.close();

        // 返回出查询结果
        return  userInfo;
    }

    // 根据环信id获取所有用户信息
    public UserInfo getAccountByHxId(String hxId){
        // 获取数据库连接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行查询操作
        String sql = "select * from "+ UserAccountTable.TAB_NAME +" where "+UserAccountTable.COL_HXID+"=?";

        Cursor cursor = db.rawQuery(sql, new String[]{hxId});
        UserInfo userInfo = null;

        if(cursor.moveToNext()) {
            userInfo = new UserInfo();

            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO)));
        }
        // 关闭资源
        cursor.close();

        // 返回出查询结果
        return  userInfo;
    }



}
