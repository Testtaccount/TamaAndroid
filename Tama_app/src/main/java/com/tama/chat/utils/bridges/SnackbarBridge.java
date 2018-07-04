package com.tama.chat.utils.bridges;

import android.view.View;

public interface SnackbarBridge {

    enum Priority{
        NORMAL, MAX
    }

    void createSnackBar(int titleResId, int duration);

    void showSnackbar(int titleResId, int duration);

    void showSnackbar(int titleResId, int duration, Priority priority);

    void showSnackbar(String title, int duration, int buttonTitleResId, View.OnClickListener onClickListener);

    void hideSnackBar();

//    void showSnackbar(int titleResId, int duration, Priority priority);
    void hideSnackBar(int titleResId);
}