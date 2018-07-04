package com.tama.chat.utils.listeners;


import com.tama.q_municate_db.models.Attachment;

public interface OnMediaPickedListener {

    void onMediaPicked(int requestCode, Attachment.Type attachmentType, Object attachment);

    void onMediaPickError(int requestCode, Exception e);

    void onMediaPickClosed(int requestCode);
}