package com.tama.chat.utils.listeners;

import com.tama.q_municate_core.service.QBService;

public interface ServiceConnectionListener {

    void onConnectedToService(QBService service);
}