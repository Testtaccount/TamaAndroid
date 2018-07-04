package com.tama.chat.ui.fragments.call;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.q_municate_user_service.QMUserService;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCSessionDescription;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.tama.chat.R;
import com.tama.chat.ui.activities.call.CallActivity;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.StartConversationReason;
import com.tama.q_municate_core.service.QBServiceConsts;
import com.tama.q_municate_core.utils.call.RingtonePlayer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//noric nael
public class IncomingCallFragment extends Fragment implements Serializable, View.OnClickListener {

    public static final String TAG = IncomingCallFragment.class.getSimpleName();

    private static final long CLICK_DELAY = TimeUnit.SECONDS.toMillis(2);
    private TextView typeIncCall;
    private TextView callerName;
    private ImageView avatarImageView;
    private ImageButton rejectBtn;
    private ImageButton takeBtn;
    private LinearLayout avatarAndNameViewList;
    private ImageView avatarImageview;
    private TextView callingToTextView;
    private FrameLayout avatarAndNameView;

    private ArrayList<Integer> opponents;
    private ArrayList<Integer> qmUserList;
    private List<QBUser> opponentsFromCall = new ArrayList<>();
    private QBRTCSessionDescription sessionDescription;
    private Vibrator vibrator;
    private QBRTCTypes.QBConferenceType qbConferenceType;
    private View view;
    private long lastClickTime = 0l;
    private RingtonePlayer ringtonePlayer;
    private QBDialogType dialogType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);

        Log.d(TAG, "onCreate() from IncomeCallFragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            opponents = getArguments().getIntegerArrayList(QBServiceConsts.EXTRA_OPPONENTS);
            dialogType = (QBDialogType) getArguments().getSerializable(QBServiceConsts.EXTRA_CHAT_TYPE);

            qmUserList = getArguments().getIntegerArrayList(QBServiceConsts.EXTRA_QM_LIST);
            sessionDescription = (QBRTCSessionDescription) getArguments().getSerializable(QBServiceConsts.EXTRA_SESSION_DESCRIPTION);
            qbConferenceType = (QBRTCTypes.QBConferenceType) getArguments().getSerializable(QBServiceConsts.EXTRA_CONFERENCE_TYPE);

            Log.d(TAG, qbConferenceType.toString() + "From onCreateView()");
        }

        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_income_call, container, false);

            initUI(view);
            setDisplayedTypeCall(qbConferenceType);
            initButtonsListener();
        }

        ringtonePlayer = new RingtonePlayer(getActivity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        startCallNotification();
    }

    public void onStop() {
        stopCallNotification();
        super.onStop();
        Log.d(TAG, "onDestroy() from IncomeCallFragment");
    }

    private void initButtonsListener() {
        rejectBtn.setOnClickListener(this);
        takeBtn.setOnClickListener(this);
    }

    private void initUI(View view) {//Avetik

        //original Hayk-
//        typeIncCall = (TextView) view.findViewById(R.id.type_inc_call);
//
//        avatarImageView = (ImageView) view.findViewById(R.id.avatar_imageview);
//
//        callerName = (TextView) view.findViewById(R.id.calling_to_text_view);
//        callerName.setLines(1);
//
//        rejectBtn = (ImageButton) view.findViewById(R.id.rejectBtn);
//        takeBtn = (ImageButton) view.findViewById(R.id.takeBtn);
//
//        setOpponentAvatarAndName();

        //avelacrac Hayk /////////////// listy ste stanal !!!!!!!
        typeIncCall = (TextView) view.findViewById(R.id.type_inc_call);
//        avatarImageview = (ImageView) avatarAndNameView.findViewById(R.id.avatar_imageview);
//
//        callingToTextView = (TextView) avatarAndNameView.findViewById(R.id.calling_to_text_view);
//        avatarAndNameView = (FrameLayout) view.findViewById(R.id.avatar_and_name);
//        avatarAndNameViewList = (LinearLayout) view.findViewById(R.id.avatar_and_name_list);//Avetik
//        avatarAndNameView.addView(getActivity().getLayoutInflater().inflate(false ?
//                R.layout.view_avatar_and_name_horizontal : R.layout.view_avatar_and_name_vertical,
//            avatarAndNameView, false));

        LinearLayout avatar_and_name_list = (LinearLayout)view.findViewById(R.id.avatar_and_name_list);
        Integer currentUserId = AppSession.getSession().getUser().getId();
        List<Integer> opponentsID = sessionDescription.getOpponents();
        opponentsID.remove(currentUserId);
        opponentsID.add(sessionDescription.getCallerID());
//        List<QMUser> userList = ((CallActivity) getActivity()).getOpponentAsUserFromDBServer(opponentsID);
        List<QMUser> qmUsers = QMUserService.getInstance().getUserCache().getUsersByIDs(opponentsID);
        for(QMUser opponent : qmUsers){
            @SuppressLint("RestrictedApi")
            View child = getLayoutInflater(null).inflate(R.layout.view_avatar_and_name_vertical, null);
            avatarImageView = (ImageView) child.findViewById(R.id.avatar_imageview);
            callerName = (TextView) child.findViewById(R.id.calling_to_text_view);
            callerName.setLines(1);
            setOpponentAvatarAndName(opponent);
            avatar_and_name_list.addView(child);
        }

//        for(QMUser opponent:qmUserList){
////            if(qbUser!=currentUser){
////            User opponent = ((CallActivity) getActivity()).getOpponentAsUserFromDB(qbUser.getId());
////                View avatarAndName = getLayoutInflater(savedInstanceState).inflate(isVideoCall ? R.layout.                                               view_avatar_and_name_horizontal : R.layout.view_avatar_and_name_vertical, null);
////                avatarAndNameViewList.addView(avatarAndName);
////                avatarImageview = (ImageView) avatarAndName.findViewById(R.id.avatar_imageview);
////                callingToTextView = (TextView) avatarAndName.findViewById(R.id.calling_to_text_view);
//            AvatarImageAndTextView avatarAndName = new AvatarImageAndTextView(getContext(),false);
//            avatarAndNameViewList.addView(avatarAndName);
//            avatarImageview = avatarAndName.avatarImage;
//            callingToTextView = avatarAndName.avatarText;
//
//            loadAvatar(opponent.getAvatar());
//
//            callingToTextView.setText(getString(R.string.calling_to, opponent.getFullName()));
//
//
//        }

        rejectBtn = (ImageButton) view.findViewById(R.id.rejectBtn);
        takeBtn = (ImageButton) view.findViewById(R.id.takeBtn);
    }

    private void loadAvatar(String photoUrl) {
        if (dialogType.equals(QBDialogType.GROUP)) {
            ImageLoader.getInstance().displayImage(photoUrl, avatarImageView, ImageLoaderUtils.UIL_GROUP_AVATAR_DISPLAY_OPTIONS);
        } else {
            ImageLoader.getInstance().displayImage(photoUrl, avatarImageView, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
        }
    }
    private void setOpponentAvatarAndName(){
        QMUser opponent = ((CallActivity) getActivity()).getOpponentAsUserFromDB(sessionDescription.getCallerID());
        ImageLoader.getInstance().displayImage(opponent.getAvatar(), avatarImageView, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
        callerName.setText(opponent.getFullName());
    }

    private void setOpponentAvatarAndName(QMUser op){//Avetik
//        QMUser opponent = ((CallActivity) getActivity()).getOpponentAsUserFromDB(op.getId());

//        ImageLoader.getInstance().displayImage(opponent.getAvatar(), avatarImageView, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
       loadAvatar(op.getAvatar());
        callerName.setText(op.getFullName());
    }

    public void startCallNotification() {
        ringtonePlayer.play(false);

        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        long[] vibrationCycle = {0, 1000, 1000};
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(vibrationCycle, 1);
        }
    }

    private void stopCallNotification() {
        if (ringtonePlayer != null) {
            ringtonePlayer.stop();
        }

        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    private void setDisplayedTypeCall(QBRTCTypes.QBConferenceType conferenceType) {
        typeIncCall.setText(getString(QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(conferenceType) ?
                R.string.call_incoming_video_call : R.string.call_incoming_audio_call));
        takeBtn.setImageResource(QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(conferenceType) ?
                R.drawable.ic_video_white : R.drawable.ic_call);
    }

    @Override
    public void onClick(View v) {

        if ((SystemClock.uptimeMillis() - lastClickTime) < CLICK_DELAY) {
            return;
        }
        lastClickTime = SystemClock.uptimeMillis();

        switch (v.getId()) {
            case R.id.rejectBtn:
                reject();
                break;
            case R.id.takeBtn:
                accept();
                break;
            default:
                break;
        }
    }

    private void accept() {
        takeBtn.setClickable(false);
        stopCallNotification();

        ((CallActivity) getActivity())
                .checkPermissionsAndStartCall(StartConversationReason.INCOME_CALL_FOR_ACCEPTION);
        Log.d(TAG, "Call is started");
    }

    private void reject() {
        rejectBtn.setClickable(false);
        Log.d(TAG, "Call is rejected");

        stopCallNotification();

        ((CallActivity) getActivity()).rejectCurrentSession();
    }
}