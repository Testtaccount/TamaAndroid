package com.tama.chat.utils.helpers;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.tama.chat.ui.fragments.imagepicker.ImagePickHelperFragment;
import com.tama.chat.ui.fragments.imagepicker.ImageSourcePickDialogFragment;
//chka
public class ImagePickHelper {

//    public void pickAnImage(Fragment fragment, int requestCode) {
//        ImagePickHelperFragment imagePickHelperFragment = ImagePickHelperFragment
//                .start(fragment, requestCode);
//        showImageSourcePickerDialog(fragment.getChildFragmentManager(), imagePickHelperFragment);
//    }
//
//    public void pickAnImage(FragmentActivity activity, int requestCode) {
//        ImagePickHelperFragment imagePickHelperFragment = ImagePickHelperFragment
//                .start(activity, requestCode);
//        showImageSourcePickerDialog(activity.getSupportFragmentManager(), imagePickHelperFragment);
//    }

    public void pickAnImage(FragmentActivity activity, int requestCode, int arrayId) {
        ImagePickHelperFragment imagePickHelperFragment = ImagePickHelperFragment
                .start(activity, requestCode);
        showImageSourcePickerDialog(activity.getSupportFragmentManager(), imagePickHelperFragment, arrayId);
    }

//    private void showImageSourcePickerDialog(FragmentManager fm, ImagePickHelperFragment fragment) {
//        ImageSourcePickDialogFragment.show(fm,
//                new ImageSourcePickDialogFragment.LoggableActivityImageSourcePickedListener(fragment));
//    }

    private void showImageSourcePickerDialog(FragmentManager fm, ImagePickHelperFragment fragment, int arrayId) {
        ImageSourcePickDialogFragment.show(fm,
                new ImageSourcePickDialogFragment.LoggableActivityImageSourcePickedListener(fragment), arrayId);
    }
}