package com.tama.chat.utils.listeners;

import java.io.File;

//chka
public interface OnImagePickedListener {

    void onImagePicked(int requestCode, File file);

    void onImagePickError(int requestCode, Exception e);

    void onImagePickClosed(int requestCode);
}