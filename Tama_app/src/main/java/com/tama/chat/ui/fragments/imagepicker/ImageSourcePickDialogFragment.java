package com.tama.chat.ui.fragments.imagepicker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseLoggableActivity;
import com.tama.chat.utils.image.ImageUtils;

//chka
public class ImageSourcePickDialogFragment extends DialogFragment {

    private static final int POSITION_GALLERY = 0;
    private static final int POSITION_CAMERA_PHOTO = 1;
    private static final int POSITION_CAMERA_VIDEO = 2;

    private OnImageSourcePickedListener onImageSourcePickedListener;
    private int arrayId;

//    public static void show(FragmentManager fragmentManager, OnImageSourcePickedListener onImageSourcePickedListener) {
//        ImageSourcePickDialogFragment fragment = new ImageSourcePickDialogFragment();
//        fragment.setOnImageSourcePickedListener(onImageSourcePickedListener);
//        fragment.show(fragmentManager, ImageSourcePickDialogFragment.class.getSimpleName());
//    }
    private void setArrayId(int arrayId){
        this.arrayId = arrayId;
    }

    public static void show(FragmentManager fragmentManager, OnImageSourcePickedListener onImageSourcePickedListener,int arrayId) {
        ImageSourcePickDialogFragment fragment = new ImageSourcePickDialogFragment();
        fragment.setArrayId(arrayId);
        fragment.setOnImageSourcePickedListener(onImageSourcePickedListener);
        fragment.show(fragmentManager, ImageSourcePickDialogFragment.class.getSimpleName());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.dlg_choose_image_from);
        builder.items(arrayId);
        builder.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i,
                    CharSequence charSequence) {
                switch (i) {
                    case POSITION_GALLERY:
                        onImageSourcePickedListener.onImageSourcePicked(ImageSource.GALLERY);
                        break;
                    case POSITION_CAMERA_PHOTO:
                        onImageSourcePickedListener.onImageSourcePicked(ImageSource.CAMERA_PHOTO);
                        break;
                    case POSITION_CAMERA_VIDEO:
                        onImageSourcePickedListener.onImageSourcePicked(ImageSource.CAMERA_VIDEO);
                        break;
                }
            }
        });

        MaterialDialog dialog = builder.build();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    public void setOnImageSourcePickedListener(OnImageSourcePickedListener onImageSourcePickedListener) {
        this.onImageSourcePickedListener = onImageSourcePickedListener;
    }

    public static enum ImageSource {
        GALLERY,
        CAMERA_PHOTO,
        CAMERA_VIDEO
    }

    public static interface OnImageSourcePickedListener {

        void onImageSourcePicked(ImageSource source);
    }

    public static class LoggableActivityImageSourcePickedListener implements OnImageSourcePickedListener {

        private static final String TAG = "myLogs";
        private Activity activity;
        private Fragment fragment;

        public LoggableActivityImageSourcePickedListener(Activity activity) {
            this.activity = activity;
        }

        public LoggableActivityImageSourcePickedListener(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onImageSourcePicked(ImageSource source) {
            switch (source) {
                case GALLERY:
                    if (fragment != null) {
                        Activity activity = fragment.getActivity();
                        setupActivityToBeNonLoggable(activity);
                        ImageUtils.startImagePicker(fragment);
                    } else {
                        setupActivityToBeNonLoggable(activity);
                        ImageUtils.startImagePicker(activity);
                    }
                    break;
                case CAMERA_PHOTO:
                    if (fragment != null) {
                        Activity activity = fragment.getActivity();
                        setupActivityToBeNonLoggable(activity);
                        ImageUtils.startCameraForResultPhoto(fragment);
                        Log.d(TAG, "CAMERA_PHOTO");
                    } else {
                        setupActivityToBeNonLoggable(activity);
                        ImageUtils.startCameraForResultPhoto(activity);
                    }
                    break;
                case CAMERA_VIDEO:
                    if (fragment != null) {
                        Activity activity = fragment.getActivity();
                        setupActivityToBeNonLoggable(activity);
                        ImageUtils.startCameraForResultVideo(fragment);
                    } else {
                        setupActivityToBeNonLoggable(activity);
                        ImageUtils.startCameraForResultVideo(activity);
                    }
                    break;
            }
        }

        private void setupActivityToBeNonLoggable(Activity activity) {
            if (activity instanceof BaseLoggableActivity) {
                BaseLoggableActivity loggableActivity = (BaseLoggableActivity) activity;
                loggableActivity.canPerformLogout.set(false);
            }
        }
    }
}