package com.tama.chat.ui.activities.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tama.chat.R;
import com.tama.chat.ui.views.roundedimageview.RoundedImageView;

/**
 * Created by Avo on 22-Mar-17.
 */

public class AvatarImageAndTextView extends LinearLayout{

    public TextView avatarText;
    public RoundedImageView avatarImage;

    public AvatarImageAndTextView( Context context, boolean isVideoCall) {
        super(context);
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mInflater != null) {
            mInflater.inflate(isVideoCall ? R.layout.view_avatar_and_name_horizontal : R.layout.view_avatar_and_name_vertical, this, true);
        }

        avatarText = (TextView) findViewById(R.id.calling_to_text_view);
        avatarImage = (RoundedImageView)findViewById(R.id.avatar_imageview);
    }
}

