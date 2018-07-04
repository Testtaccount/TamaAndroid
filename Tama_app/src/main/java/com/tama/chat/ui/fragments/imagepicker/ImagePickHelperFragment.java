package com.tama.chat.ui.fragments.imagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.tama.chat.tasks.GetFilepathFromUriTask;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.utils.image.ImageUtils;
import com.tama.chat.utils.listeners.OnImagePickedListener;
import com.tama.chat.utils.listeners.OnMediaPickedListener;

//chka
public class ImagePickHelperFragment extends Fragment {

    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String ARG_PARENT_FRAGMENT = "parentFragment";
    private static Context context;

    private static final String TAG = ImagePickHelperFragment.class.getSimpleName();
    private static final String TAGG = "myLogs";

    private OnImagePickedListener listener;

    public ImagePickHelperFragment() {
    }

    public static ImagePickHelperFragment start(Fragment fragment, int requestCode) {
        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putString(ARG_PARENT_FRAGMENT, fragment.getClass().getSimpleName());

        return start(fragment.getActivity().getSupportFragmentManager(), args);
    }

    public static ImagePickHelperFragment start(FragmentActivity activity, int requestCode) {
        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);

        return start(activity.getSupportFragmentManager(), args);
    }

    private static ImagePickHelperFragment start(FragmentManager fm, Bundle args) {
        ImagePickHelperFragment fragment = (ImagePickHelperFragment) fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new ImagePickHelperFragment();
            fm.beginTransaction().add(fragment, TAG).commitAllowingStateLoss();
            fragment.setArguments(args);
        }
        return fragment;
    }


    public static void stop(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(TAG);
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isResultFromImagePick(requestCode, resultCode, data)) {//het gal
            if (requestCode == ImageUtils.CAMERA_REQUEST_CODE && (data == null || data.getData() == null)) {
                // Hacky way to get EXTRA_OUTPUT param to work.
                // When setting EXTRA_OUTPUT param in the camera intent there is a chance that data will return as null
                // So we just pass temporary camera file as a data, because RESULT_OK means that photo was written in the file.
                data = new Intent();

                Uri uri = Uri.fromFile(ImageUtils.getLastUsedCameraFile());

                data.setData(uri);

                Log.d(TAGG,"steg chi galis");
            }//content://media/external/video/media/2163

            new GetFilepathFromUriTask(getChildFragmentManager(), (OnMediaPickedListener) listener,
                    getArguments().getInt(ARG_REQUEST_CODE)).execute(data);
            Log.d(TAGG,"steg galis a");
        } else {
            stop(getChildFragmentManager());
            if (listener != null) {
                listener.onImagePickClosed(getArguments().getInt(ARG_REQUEST_CODE));
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

//    public static Context getAppContext(){
//        return context;
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Fragment fragment = ((BaseActivity) activity).getSupportFragmentManager()
                .findFragmentByTag(getArguments().getString(ARG_PARENT_FRAGMENT));
        if (fragment != null) {
            if (fragment instanceof OnImagePickedListener) {
                listener = (OnImagePickedListener) fragment;
            }
        } else {
            if (activity instanceof OnImagePickedListener) {
                listener = (OnImagePickedListener) activity;
            }
        }

        if (listener == null) {
            throw new IllegalStateException(
                    "Either activity or fragment should implement OnImagePickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void setListener(OnImagePickedListener listener) {
        this.listener = listener;
    }

    private boolean isResultFromImagePick(int requestCode, int resultCode, Intent data) {
        return resultCode == Activity.RESULT_OK && ((requestCode == ImageUtils.CAMERA_REQUEST_CODE) || (requestCode == ImageUtils.GALLERY_REQUEST_CODE && data != null));
    }
}