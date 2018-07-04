package com.tama.chat.utils.listeners;

public interface UserStatusChangingListener {

    void onChangedUserStatus(int userId, boolean online);
}