package com.yaolaizai.ylzx.chatroom.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yaolaizai.ylzx.chatroom.model.bean.GroupInfo;
import com.yaolaizai.ylzx.chatroom.model.bean.InvitationInfo;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
// 邀请信息表的操作类
public class InviteTableDao {
    DBHelper mHelper;

    public InviteTableDao(DBHelper helper) {
        mHelper = helper;
    }

    // 添加邀请
    public void addInvitation(InvitationInfo invitationInfo) {
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行操作
        ContentValues values = new ContentValues();

        //
        values.put(InviteTable.COL_REASON, invitationInfo.getReason());// 原因
        values.put(InviteTable.COL_STATUS, invitationInfo.getStatus().ordinal());// 状态

        // 获取群的id
        UserInfo user = invitationInfo.getUser();


        if (user != null) {
            // 个人邀请信息
            values.put(InviteTable.COL_USER_HXID, invitationInfo.getUser().getHxid());
            values.put(InviteTable.COL_USER_NAME, invitationInfo.getUser().getName());

        } else {
            // 群组相关邀请信息
            values.put(InviteTable.COL_GROUP_HXID, invitationInfo.getGroup().getGroupId());
            values.put(InviteTable.COL_GROUP_NAME, invitationInfo.getGroup().getGroupName());
            values.put(InviteTable.COL_USER_HXID, invitationInfo.getGroup().getInvitePerson());
        }

        db.replace(InviteTable.TAB_NAME, null, values);

    }

    // 获取所有邀请信息
    public List<InvitationInfo> getInvitations() {

        List<InvitationInfo> invitations = new ArrayList<>();

        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        // 执行查询语句
        String sql = "select * from " + InviteTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            InvitationInfo invitationInfo = new InvitationInfo();

            // 原因  状态
            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.COL_REASON)));
            invitationInfo.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex(InviteTable.COL_STATUS))));

            // 个人还是群
            String groupId = cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID));

            if (groupId == null) {
                UserInfo userInfo = new UserInfo();

                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));

                // 个人
                invitationInfo.setUser(userInfo);
            } else {
                // 群
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setGroupId(groupId);
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_NAME)));
                groupInfo.setInvitePerson(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));

                invitationInfo.setGroup(groupInfo);
            }

            // 添加每一个对象
            invitations.add(invitationInfo);
        }

        // 关闭cursor
        cursor.close();

        // 返回数据
        return invitations;
    }

    // 将int类型状态转换为邀请的状态
    private InvitationInfo.InvitationStatus int2InviteStatus(int intStatus) {

        if (intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUPO_ACCEPT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUPO_ACCEPT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE.ordinal()) {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE;
        }

        return null;
    }

    // 删除邀请
    public void removeInvitation(String hxId) {
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getWritableDatabase();

        // 执行操作
        db.delete(InviteTable.TAB_NAME, InviteTable.COL_USER_HXID + "=?", new String[]{hxId});

    }

    // 更新邀请状态
    public void updateInvitationStatus(InvitationInfo.InvitationStatus invitationStatus, String hxId) {
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getWritableDatabase();

        // 执行更新操作
        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_STATUS, invitationStatus.ordinal());

        db.update(InviteTable.TAB_NAME, values, InviteTable.COL_USER_HXID + "=?", new String[]{hxId});

    }
}
