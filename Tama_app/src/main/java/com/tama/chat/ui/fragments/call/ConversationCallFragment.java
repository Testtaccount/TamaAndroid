package com.tama.chat.ui.fragments.call;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.AppRTCAudioManager;
import com.quickblox.videochat.webrtc.BaseSession;
import com.quickblox.videochat.webrtc.QBMediaStreamManager;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.QBRTCTypes.QBConferenceType;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientVideoTracksCallbacks;
import com.quickblox.videochat.webrtc.callbacks.QBRTCSessionConnectionCallbacks;
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;
import com.quickblox.videochat.webrtc.view.QBRTCVideoTrack;
import com.tama.chat.R;
import com.tama.chat.ui.activities.call.CallActivity;
import com.tama.chat.ui.activities.others.AvatarImageAndTextView;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.helpers.SystemPermissionHelper;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.StartConversationReason;
import com.tama.q_municate_core.qb.commands.push.QBSendPushCommand;
import com.tama.q_municate_core.qb.helpers.QBFriendListHelper;
import com.tama.q_municate_core.service.QBServiceConsts;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.RendererCommon;
import org.webrtc.VideoRenderer;

//popoxac nael call video
/**
 * QuickBlox team
 */
public class ConversationCallFragment extends Fragment implements Serializable, QBRTCClientVideoTracksCallbacks<QBRTCSession>,
        QBRTCSessionConnectionCallbacks, CallActivity.QBRTCSessionUserCallback {

    private static final long TOGGLE_CAMERA_DELAY = 1000;
    private static final long LOCAL_TRACk_INITIALIZE_DELAY = 500;

    private String TAG = ConversationCallFragment.class.getSimpleName();

//    @Bind(R.id.avatar_imageview)
//    RoundedImageView photoImageView;

    private ArrayList<QBUser> opponents;
    private QBRTCTypes.QBConferenceType qbConferenceType;
    private StartConversationReason startConversationReason;

    private ToggleButton cameraToggle;
    private ToggleButton micToggleVideoCall;
    private ImageButton handUpVideoCall;
    private boolean isVideoCall = false;
    private boolean isAudioEnabled = true;
    private List<QBUser> allUsers = new ArrayList<>();
    private String callerName;
    private boolean isMessageProcessed;
    private QBRTCSurfaceView remoteVideoView;
    private LinearLayout.LayoutParams remoteVideoViewParam;
    private Map<Integer,QBRTCSurfaceView> remoteVideoViewsMap;
    private boolean isAddNewRemoteVideoView;
    private LinearLayout remote_video_view_list;
    private QBRTCSurfaceView localVideoView;
    private CameraState cameraState = CameraState.NONE;
    private boolean isPeerToPeerCall;
    private QBRTCVideoTrack localVideoTrack;
    private QBRTCVideoTrack remoteVideoTrack;
    private Handler mainHandler;
    private ImageView avatarImageview;
    private TextView callingToTextView;
    private FrameLayout avatarAndNameView;
    private LinearLayout avatarAndNameViewList;
    private boolean isFullScreen;
    private View elementSetVideoButtons;
    private boolean isFrontCameraSelected = true;
    private AppRTCAudioManager audioManager;
    private SystemPermissionHelper systemPermissionHelper;
    private boolean isAllViewsInitialized = true;
    private QBDialogType dialogType;
    private List<QMUser> qmUserList;

    //    public static ConversationCallFragment newInstance(List<QBUser> opponents, String callerName,//Avetik
    public static ConversationCallFragment newInstance(List<QBUser> opponents, String callerName,
//Avetik
        QBConferenceType qbConferenceType,
        StartConversationReason reason,
        String sessionId, QBDialogType dialogType,
        List<QMUser> qmUserList) {

        ConversationCallFragment fragment = new ConversationCallFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(QBServiceConsts.EXTRA_CONFERENCE_TYPE, qbConferenceType);
        bundle.putString(QBServiceConsts.EXTRA_CALLER_NAME, callerName);
        bundle.putSerializable(QBServiceConsts.EXTRA_OPPONENTS, (Serializable) opponents);
        bundle.putSerializable(QBServiceConsts.EXTRA_CHAT_TYPE, (Serializable) dialogType);
        bundle.putSerializable(QBServiceConsts.EXTRA_QM_LIST, (Serializable) qmUserList);

        bundle.putSerializable(QBServiceConsts.EXTRA_START_CONVERSATION_REASON_TYPE, reason);
        if (sessionId != null) {
            bundle.putString(QBServiceConsts.EXTRA_SESSION_ID, sessionId);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        Log.d(TAG, "Fragment. Thread id: " + Thread.currentThread().getId());

        if (getArguments() != null) {
            opponents = (ArrayList<QBUser>) getArguments().getSerializable(QBServiceConsts.EXTRA_OPPONENTS);
            dialogType = (QBDialogType) getArguments().getSerializable(QBServiceConsts.EXTRA_CHAT_TYPE);
            qmUserList = (List<QMUser>) getArguments().getSerializable(QBServiceConsts.EXTRA_QM_LIST);
            qbConferenceType = (QBRTCTypes.QBConferenceType) getArguments().getSerializable(QBServiceConsts.EXTRA_CONFERENCE_TYPE);
            startConversationReason = (StartConversationReason) getArguments().getSerializable(QBServiceConsts.EXTRA_START_CONVERSATION_REASON_TYPE);
            callerName = getArguments().getString(QBServiceConsts.EXTRA_CALLER_NAME);

            isPeerToPeerCall = opponents.size() == 1;
            isVideoCall = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO.equals(qbConferenceType);
            Log.d(TAG, "CALLER_NAME: " + callerName);
            Log.d(TAG, "opponents: " + opponents.toString());
        }

        systemPermissionHelper = new SystemPermissionHelper(getActivity());

        initViews(view);
        ((CallActivity)getActivity()).initActionBar();
        ((CallActivity)getActivity()).setCallActionBarTitle(StartConversationReason.INCOME_CALL_FOR_ACCEPTION
                .equals(startConversationReason) ? callerName : opponents.get(0).getFullName());
        initButtonsListener();
        initSessionListener();
        setUpUiByCallType();

        displayOpponentAvatar();
        initAudioManager();

        mainHandler = new FragmentLifeCycleHandler();
        return view;

    }

    private void initSessionListener() {
        ((CallActivity) getActivity()).addVideoTrackCallbacksListener(this);
    }

    private void setUpUiByCallType() {
        if (!isVideoCall) {
//            if(remoteVideoView!=null) {
                remoteVideoView.setVisibility(View.INVISIBLE);
//            }
            cameraToggle.setVisibility(View.GONE);
            cameraToggle.setEnabled(false);
            cameraToggle.setChecked(false);
        }

        correctButtonsVisibilityByGrantedPermissions();
    }

    private void correctButtonsVisibilityByGrantedPermissions (){
        if (!systemPermissionHelper.isAllPermissionsGrantedForCallByType(qbConferenceType)){
            if(!systemPermissionHelper.isMicrophonePermissionGranted()){
                micToggleVideoCall.setChecked(false);
                micToggleVideoCall.setEnabled(false);
            }

            if(!systemPermissionHelper.isCameraPermissionGranted()){
                cameraToggle.setChecked(false);
                cameraToggle.setEnabled(false);
            }
        }
    }
    private void displayOpponentAvatarAAAAAAAAAAAAAAAAAA() {
        QMUser opponent = ((CallActivity) getActivity()).getOpponentAsUserFromDB(opponents.get(0).getId());
        if (StartConversationReason.INCOME_CALL_FOR_ACCEPTION.equals(startConversationReason) && !isVideoCall){
            avatarAndNameView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(opponent.getAvatar(), avatarImageview, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
        } else if (StartConversationReason.OUTCOME_CALL_MADE.equals(startConversationReason)) {
            avatarAndNameView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(opponent.getAvatar(), avatarImageview, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
            callingToTextView.setText(getString(R.string.calling_to, opponent.getFullName()));
        }
    }


    private void displayOpponentAvatar() {//Avetik
//        QMUser opponent = ((CallActivity) getActivity()).getOpponentAsUserFromDB(opponents.get(0).getId());
//        if (StartConversationReason.INCOME_CALL_FOR_ACCEPTION.equals(startConversationReason) && !isVideoCall){
//            avatarAndNameView.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(opponent.getAvatar(), avatarImageview, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
//        } else if (StartConversationReason.OUTCOME_CALL_MADE.equals(startConversationReason)) {
//            avatarAndNameView.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(opponent.getAvatar(), avatarImageview, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
//            callingToTextView.setText(getString(R.string.calling_to, opponent.getFullName()));
//        }

// popoxac Hayk
        avatarAndNameViewList.setVisibility(View.VISIBLE);

//      QMUser opponent = ((CallActivity) getActivity()).getOpponentAsUserFromDB(opponents.get(0).getId());

//        ArrayList<QMUser> qmOp=new ArrayList<>();
////        QMUserService.getInstance().getUserCache().getUsersByIDs(.getOccupants());
//
//        for (QBUser user:opponents) {
//
//            QMUser opponent = ((CallActivity) getActivity()).getOpponentAsUserFromDB(user.getId())==null?convert(user):((CallActivity) getActivity()).getOpponentAsUserFromDB(user.getId());
//
////            QMUser opponent=convert(user);
//
//            qmOp.add(opponent);
//        }

        if(isVideoCall){
            loadAvatar(qmUserList.get(0).getAvatar());
        }

        for(QMUser opponent:qmUserList){
//            if(qbUser!=currentUser){
//            User opponent = ((CallActivity) getActivity()).getOpponentAsUserFromDB(qbUser.getId());
//                View avatarAndName = getLayoutInflater(savedInstanceState).inflate(isVideoCall ? R.layout.                                               view_avatar_and_name_horizontal : R.layout.view_avatar_and_name_vertical, null);
//                avatarAndNameViewList.addView(avatarAndName);
//                avatarImageview = (ImageView) avatarAndName.findViewById(R.id.avatar_imageview);
//                callingToTextView = (TextView) avatarAndName.findViewById(R.id.calling_to_text_view);
            AvatarImageAndTextView avatarAndName = new AvatarImageAndTextView(getContext(),isVideoCall);
            avatarAndNameViewList.addView(avatarAndName);
            avatarImageview = avatarAndName.avatarImage;
            callingToTextView = avatarAndName.avatarText;

            if (StartConversationReason.INCOME_CALL_FOR_ACCEPTION.equals(startConversationReason)  || isVideoCall){
//                ImageLoader.getInstance().displayImage(opponent.getAvatar(), avatarImageview, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
                loadAvatar(opponent.getAvatar());
            } else if (StartConversationReason.OUTCOME_CALL_MADE.equals(startConversationReason)) {
//                ImageLoader.getInstance().displayImage(opponent.getAvatar(), avatarImageview, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
                loadAvatar(opponent.getAvatar());

                callingToTextView.setText(getString(R.string.calling_to, opponent.getFullName()));
//                }

            }
        }
    }

//    public List<QMUser> getUsersForGroupChat(String dialogId, List<Integer> idsList) {
//        List<QMUser> usersList = new ArrayList<>();
//
//        List<QMUser> qmUsers = QMUserService.getInstance().getUserCache().getUsersByIDs(idsList);
//
//        List<DialogOccupant> dialogOccupants = dataManager.getDialogOccupantDataManager().getActualDialogOccupantsByDialog(dialogId);
//        Set<Integer> dialogOccupantIdsSet = new HashSet<>();
//        for (DialogOccupant dialogOccupant : dialogOccupants) {
//            dialogOccupantIdsSet.add(dialogOccupant.getUser().getId());
//        }
//
//        for (QMUser qmUser : qmUsers) {
//            if (dialogOccupantIdsSet.contains(qmUser.getId())) {
//                usersList.add(qmUser);
//            }
//        }
//        return usersList;
//    }

    private void loadAvatar(String photoUrl) {
        if (dialogType.equals(QBDialogType.GROUP)) {
            ImageLoader.getInstance().displayImage(photoUrl, avatarImageview, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
        } else {
            ImageLoader.getInstance().displayImage(photoUrl, avatarImageview, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
        }
    }

    private void actionsByConnectedToUser(){
        actionButtonsEnabled(true);
        ((CallActivity) getActivity()).startTimer();
        getActivity().invalidateOptionsMenu();
        hideAvatarIfNeed();
    }

    private void hideAvatarIfNeed() {
        for(int i = 0; i < avatarAndNameViewList.getChildCount(); ++i){//Avetik
            ((AvatarImageAndTextView)avatarAndNameViewList.getChildAt(i)).avatarText.setVisibility(View.INVISIBLE);
        } //Hayk
//        callingToTextView.setVisibility(View.INVISIBLE);//Avetik

        if (isVideoCall) {
//            avatarAndNameView.setVisibility(View.GONE);//Avetik
//            avatarAndNameViewList.setVisibility(View.GONE);//Avetik
            avatarAndNameView.setVisibility(View.GONE); //Hayk
        }
    }

    public void actionButtonsEnabled(boolean enability) {
        if (!isAllViewsInitialized){
            return;
        }

        if (isVideoCall){
            cameraToggle.setEnabled(enability);
        }
        micToggleVideoCall.setEnabled(enability);

        // inactivate toggle buttons
        if (isVideoCall) {
            cameraToggle.setActivated(enability);
        }
        micToggleVideoCall.setActivated(enability);

        correctButtonsVisibilityByGrantedPermissions();
    }

    @Override
    public void onStart() {
        super.onStart();
        QBRTCSession session = ((CallActivity) getActivity()).getCurrentSession();
        if (!isMessageProcessed) {
            if (startConversationReason == StartConversationReason.INCOME_CALL_FOR_ACCEPTION) {
                session.acceptCall(session.getUserInfo());
            } else {
                sendPushAboutCall();
                session.startCall(session.getUserInfo());
            }
            isMessageProcessed = true;
        }
        ((CallActivity) getActivity()).addTCClientConnectionCallback(this);
        ((CallActivity) getActivity()).addRTCSessionUserCallback(this);
    }

    private void sendPushAboutCall() {//Avetik
        if (isPeerToPeerCall) {
            QBFriendListHelper qbFriendListHelper = new QBFriendListHelper(getActivity());
            QBUser qbUser = opponents.get(0);

            if (!qbFriendListHelper.isUserOnline(qbUser.getId())) {
                String callMsg = getString(R.string.dlg_offline_call,
                        AppSession.getSession().getUser().getFullName());
                QBSendPushCommand.start(getActivity(), callMsg, qbUser.getId());
            }
        }
    }

//    private void sendPushAboutCall() {
//        if (isPeerToPeerCall) {
//            QBFriendListHelper qbFriendListHelper = new QBFriendListHelper(getActivity());
//            QBUser qbUser = opponents.get(0);
//            if (!qbFriendListHelper.isUserOnline(qbUser.getId())) {
//                String callMsg = getString(R.string.dlg_offline_call,
//                        AppSession.getSession().getUser().getFullName());
//                QBSendPushCommand.start(getActivity(), callMsg, qbUser.getId());
//            }
//        }
//    }//Avetik

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() from " + TAG);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initViews(View view) {//Avetik
        remote_video_view_list = (LinearLayout)view.findViewById(R.id.remote_video_view_list);
        remoteVideoView = (QBRTCSurfaceView)view.findViewById(R.id.remote_video_view);
        remoteVideoViewParam = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        remoteVideoViewParam.weight = 1;
        remoteVideoViewParam.gravity  = Gravity.CENTER;
        remoteVideoView.setLayoutParams(remoteVideoViewParam);
        isAddNewRemoteVideoView = false;
        remoteVideoViewsMap = new ArrayMap<>();

//        for(int i = 0; i<opponentCount; ++i){
//            QBRTCSurfaceView remVidView = new QBRTCSurfaceView(getContext());
//            remVidView.setLayoutParams(params);
//            remote_video_view_list.addView(remVidView);
//        }
//        remoteVideoView = (QBRTCSurfaceView)remote_video_view_list.getChildAt(0);

        localVideoView = (QBRTCSurfaceView) view.findViewById(R.id.local_video_view);
        localVideoView.setZOrderMediaOverlay(true);

        cameraToggle = (ToggleButton) view.findViewById(R.id.cameraToggle);

        micToggleVideoCall = (ToggleButton) view.findViewById(R.id.micToggleVideoCall);

        handUpVideoCall = (ImageButton) view.findViewById(R.id.handUpVideoCall);

        avatarAndNameView = (FrameLayout) view.findViewById(R.id.avatar_and_name);
        avatarAndNameViewList = (LinearLayout) view.findViewById(R.id.avatar_and_name_list);//Avetik

        avatarAndNameView.addView(getActivity().getLayoutInflater().inflate(isVideoCall ?
                        R.layout.view_avatar_and_name_horizontal : R.layout.view_avatar_and_name_vertical,
                avatarAndNameView, false));

        elementSetVideoButtons = view.findViewById(R.id.element_set_video_buttons);

//        avatarAndNameView.addView(getActivity().getLayoutInflater().inflate(isVideoCall ?
//                        R.layout.view_avatar_and_name_horizontal : R.layout.view_avatar_and_name_vertical,
//                avatarAndNameView, false));///esi tegapoxel
//
        avatarImageview = (ImageView) avatarAndNameView.findViewById(R.id.avatar_imageview);

        callingToTextView = (TextView) avatarAndNameView.findViewById(R.id.calling_to_text_view);

        isAllViewsInitialized = true;

        actionButtonsEnabled(false);
    }

//    private void initViews(View view) {
//        remoteVideoView = (QBRTCSurfaceView) view.findViewById(R.id.remote_video_view);
//
//        localVideoView = (QBRTCSurfaceView) view.findViewById(R.id.local_video_view);
//        localVideoView.setZOrderMediaOverlay(true);
//
//        cameraToggle = (ToggleButton) view.findViewById(R.id.cameraToggle);
//
//        micToggleVideoCall = (ToggleButton) view.findViewById(R.id.micToggleVideoCall);
//
//        handUpVideoCall = (ImageButton) view.findViewById(R.id.handUpVideoCall);
//
//        avatarAndNameView = (FrameLayout) view.findViewById(R.id.avatar_and_name);
//
//        avatarAndNameView.addView(getActivity().getLayoutInflater().inflate(isVideoCall ?
//                        R.layout.view_avatar_and_name_horizontal : R.layout.view_avatar_and_name_vertical,
//                avatarAndNameView, false));
//
//        elementSetVideoButtons = view.findViewById(R.id.element_set_video_buttons);
//
//        avatarImageview = (ImageView) avatarAndNameView.findViewById(R.id.avatar_imageview);
//
//        callingToTextView = (TextView) avatarAndNameView.findViewById(R.id.calling_to_text_view);
//
//        actionButtonsEnabled(false);
//    }

    @Override
    public void onResume() {
        super.onResume();

        // If user changed camera state few times and last state was CameraState.ENABLED_FROM_USER
        // than we turn on cam, else we nothing change
        if (cameraState != CameraState.DISABLED_FROM_USER
                && isVideoCall) {
            toggleCamera(true);
        }
    }

    @Override
    public void onPause() {
        // If camera state is CameraState.ENABLED_FROM_USER or CameraState.NONE
        // than we turn off cam
        if (cameraState != CameraState.DISABLED_FROM_USER && isVideoCall) {
            toggleCamera(false);
        }

        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((CallActivity) getActivity()).removeRTCClientConnectionCallback(this);
        ((CallActivity) getActivity()).removeRTCSessionUserCallback();
    }

    private void initButtonsListener() {
        cameraToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cameraState = isChecked ? CameraState.ENABLED_FROM_USER : CameraState.DISABLED_FROM_USER;
                toggleCamera(isChecked);
            }
        });

        micToggleVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CallActivity) getActivity()).getCurrentSession() != null) {
                    if (isAudioEnabled) {
                        Log.d(TAG, "Mic is off!");
                        ((CallActivity) getActivity()).getCurrentSession().getMediaStreamManager().setAudioEnabled(false);
                        isAudioEnabled = false;
                    } else {
                        Log.d(TAG, "Mic is on!");
                        ((CallActivity) getActivity()).getCurrentSession().getMediaStreamManager().setAudioEnabled(true);
                        isAudioEnabled = true;
                    }
                }
            }
        });

        handUpVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonsEnabled(false);
                handUpVideoCall.setEnabled(false);
                Log.d(TAG, "Call is stopped");

                ((CallActivity) getActivity()).hangUpCurrentSession();
                handUpVideoCall.setEnabled(false);
                handUpVideoCall.setActivated(false);

            }
        });

        remoteVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFullScreen();
            }
        });
    }

    void toggleFullScreen(){
        if(isFullScreen){
            setLocalVideoViewVisible(false);
            showSystemUI();
            ((CallActivity)getActivity()).showCallActionBar();
            elementSetVideoButtons.setVisibility(View.VISIBLE);
            isFullScreen = false;
        } else {
            setLocalVideoViewVisible(true);
            hideSystemUI();
            ((CallActivity) getActivity()).hideCallActionBar();
            elementSetVideoButtons.setVisibility(View.INVISIBLE);
            isFullScreen = true;
        }
    }

    private void setLocalVideoViewVisible(boolean visible){
        if (remoteVideoView != null && localVideoTrack != null){
            if (visible) {
                if (localVideoTrack.getRenderer() == null){
                    fillVideoView(localVideoView, localVideoTrack, true);
                }

                localVideoView.setVisibility(View.VISIBLE);
                Log.d(TAG, "fullscreen enabled");
            } else {
                localVideoTrack.cleanUp();
                localVideoView.setVisibility(View.INVISIBLE);
                Log.d(TAG, "fullscreen disabled");
            }
        }
    }

    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    private void showSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        QBRTCSession currentSession = ((CallActivity) getActivity()).getCurrentSession();
        if (currentSession == null) {
            return super.onOptionsItemSelected(item);
        }

        final QBMediaStreamManager mediaStreamManager = currentSession.getMediaStreamManager();
        if (mediaStreamManager == null) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.switch_camera_toggle:
                mediaStreamManager.switchCameraInput(new CameraVideoCapturer.CameraSwitchHandler() {
                    @Override
                    public void onCameraSwitchDone(boolean b) {
                        isFrontCameraSelected = b;
                        toggleCameraInternal();
                    }
                    @Override
                    public void onCameraSwitchError(String s) {

                    }
                });
                break;
            case R.id.switch_speaker_toggle:
                toggleAudioOutput();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        getActivity().invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    private void toggleAudioOutput() {
        if (!audioManager.getSelectedAudioDevice().equals(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE)) {
            audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
        } else {
            if(audioManager.getAudioDevices().contains(AppRTCAudioManager.AudioDevice.WIRED_HEADSET)){
                audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.WIRED_HEADSET);
            } else {
                audioManager.setAudioDevice(AppRTCAudioManager.AudioDevice.EARPIECE);
            }
        }
    }

    private void initAudioManager() {
        audioManager = AppRTCAudioManager.create(getActivity(), new AppRTCAudioManager.OnAudioManagerStateListener() {
            @Override
            public void onAudioChangedState(AppRTCAudioManager.AudioDevice audioDevice) {
                ToastUtils.shortToast("Audio device switched to  " + audioDevice);
            }
        });
        audioManager.init();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSpeakerToggle = menu.findItem(R.id.switch_speaker_toggle);
        if (itemSpeakerToggle != null){
            updateSpeakerToggleIcon(itemSpeakerToggle);
        }

        MenuItem itemCameraToggle = menu.findItem(R.id.switch_camera_toggle);
        if (itemCameraToggle != null){
            updateCameraToggleIcon(itemCameraToggle);
        }
    }

    private void updateSpeakerToggleIcon(MenuItem itemSpeakerToggle) {
        boolean speakerEnabled = audioManager.getSelectedAudioDevice().equals(AppRTCAudioManager.AudioDevice.SPEAKER_PHONE);
        itemSpeakerToggle.setIcon(speakerEnabled ? R.drawable.ic_phonelink_ring : R.drawable.ic_speaker_phone);
    }

    private void updateCameraToggleIcon(MenuItem itemCameraToggle) {
        if (((CallActivity) getActivity()).getCurrentSession() != null) {
            if (isVideoCall) {
                if (isVideoEnabled()) {
                    itemCameraToggle.setIcon(isFrontCameraSelected ? R.drawable.ic_camera_front_white : R.drawable.ic_camera_rear_white);
                }

                itemCameraToggle.setVisible(isVideoEnabled());
                itemCameraToggle.setEnabled(isVideoEnabled());
            } else {
                itemCameraToggle.setEnabled(false);
                itemCameraToggle.setVisible(false);
            }
        }
    }

    private boolean isVideoEnabled(){
        QBRTCSession currentSession = ((CallActivity) getActivity()).getCurrentSession();
        QBMediaStreamManager mediaStreamManager;
        if (currentSession != null) {
            mediaStreamManager = currentSession.getMediaStreamManager();
        } else {
            return false;
        }

        if (mediaStreamManager != null){
            return mediaStreamManager.isVideoEnabled();
        } else {
            return false;
        }
    }

    private void toggleCameraInternal(){
        getActivity().invalidateOptionsMenu();
        updateVideoView(remoteVideoTrack == null ? remoteVideoView : localVideoView, isFrontCameraSelected);
    }

    private void toggleCamera(boolean isNeedEnableCam) {
        QBRTCSession currentSession = ((CallActivity) getActivity()).getCurrentSession();
        if (currentSession != null && currentSession.getMediaStreamManager() != null){
            currentSession.getMediaStreamManager().setVideoEnabled(isNeedEnableCam);
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public void onLocalVideoTrackReceive(QBRTCSession qbrtcSession, final QBRTCVideoTrack videoTrack) {
        Log.d(TAG, "onLocalVideoTrackReceive() run");
        localVideoTrack = videoTrack;

        if (remoteVideoView != null && remoteVideoTrack == null) {
            fillVideoView(remoteVideoView, videoTrack, true);
        }
        if(getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public void onRemoteVideoTrackReceive(QBRTCSession session, QBRTCVideoTrack videoTrack, Integer userID) {
        Log.d(TAG, "onRemoteVideoTrackReceive for opponent= " + userID);
        remoteVideoTrack = videoTrack;
        if(isAddNewRemoteVideoView){
            remoteVideoView = new QBRTCSurfaceView(getContext());
            remoteVideoView.setLayoutParams(remoteVideoViewParam);
            remote_video_view_list.addView(remoteVideoView);
        }else{
            isAddNewRemoteVideoView = true;
        }

        remoteVideoViewsMap.put(userID,remoteVideoView);

        if (remoteVideoView != null) {//2
            if (localVideoTrack != null){
                localVideoTrack.cleanUp();
            }

            fillVideoView(remoteVideoView, videoTrack, false);//GTA Avetik
            updateVideoView(remoteVideoView, false);
        }

        //original Hayk-
//        if (remoteVideoView != null) {
//            if (localVideoTrack != null){
//                localVideoTrack.cleanUp();
//            }
//            fillVideoView(remoteVideoView, videoTrack, false);
//            updateVideoView(remoteVideoView, false);
//        }
    } //Hayk Aveti grac

    private void fillVideoView(QBRTCSurfaceView videoView, QBRTCVideoTrack videoTrack, boolean localRenderer) {
        videoTrack.cleanUp();
        videoTrack.addRenderer(new VideoRenderer(videoView));

        if (localRenderer) {
            updateVideoView(videoView, isFrontCameraSelected);
        }
        Log.d(TAG, (localRenderer ? "local" : "remote") + " Track is rendering");
    }

    protected void updateVideoView(QBRTCSurfaceView surfaceViewRenderer, boolean mirror) {
        updateVideoView(surfaceViewRenderer, mirror, RendererCommon.ScalingType.SCALE_ASPECT_FILL);
    }

    protected void updateVideoView(QBRTCSurfaceView surfaceViewRenderer, boolean mirror, RendererCommon.ScalingType scalingType) {
        Log.i(TAG, "updateVideoView mirror:" + mirror + ", scalintType = " + scalingType);
        surfaceViewRenderer.setScalingType(scalingType);
        surfaceViewRenderer.setMirror(mirror);
        surfaceViewRenderer.requestLayout();
    }

    @Override
    public void onStartConnectToUser(QBRTCSession qbrtcSession, Integer userId) {
//        setStatusForOpponent(userId, getString(R.string.checking));
    }

    @Override
    public void onStateChanged(QBRTCSession qbrtcSession, BaseSession.QBRTCSessionState qbrtcSessionState) {

    }

    @Override
    public void onConnectedToUser(QBRTCSession qbrtcSession,final Integer userId) {
        actionsByConnectedToUser();
    }


    @Override
    public void onConnectionClosedForUser(QBRTCSession qbrtcSession, Integer integer) {
//        setStatusForOpponent(integer, getString(R.string.closed));
    }

    @Override
    public void onDisconnectedFromUser(QBRTCSession qbrtcSession, Integer integer) {
//        setStatusForOpponent(integer, getString(R.string.disconnected));
    }

    @Override
    public void onDisconnectedTimeoutFromUser(QBRTCSession qbrtcSession, Integer integer) {
//        setStatusForOpponent(integer, getString(R.string.time_out));
    }

    @Override
    public void onConnectionFailedWithUser(QBRTCSession qbrtcSession, Integer integer) {
//        setStatusForOpponent(integer, getString(R.string.failed));
    }

//    @Override
//    public void onError(QBRTCSession qbrtcSession, QBRTCException e) {
//
//    }

    @Override
    public void onUserNotAnswer(QBRTCSession session, Integer userId) {
//        setStatusForOpponent(userId, getString(R.string.call_no_answer));
    }

    @Override
    public void onCallRejectByUser(QBRTCSession session, Integer userId, Map<String, String> userInfo) {
//        setStatusForOpponent(userId, getString(R.string.call_rejected));
    }

    @Override
    public void onCallAcceptByUser(QBRTCSession session, Integer userId, Map<String, String> userInfo) {
//        setStatusForOpponent(userId, getString(R.string.call_accepted));
    }

    @Override
    public void onReceiveHangUpFromUser(QBRTCSession session, Integer userId) {
        remote_video_view_list.removeView(remoteVideoViewsMap.get(userId));
//        setStatusForOpponent(userId, getString(R.string.call_hung_up));
    }



    private enum CameraState {
        NONE,
        DISABLED_FROM_USER,
        ENABLED_FROM_USER
    }

    class FragmentLifeCycleHandler extends Handler{

        @Override
        public void dispatchMessage(Message msg) {
            if (isAdded() && getActivity() != null) {
                super.dispatchMessage(msg);
            } else {
                Log.d(TAG, "Fragment under destroying");
            }
        }
    }

    class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public DividerItemDecoration(@NonNull Context context, @DimenRes int dimensionDivider) {
            this.space = context.getResources().getDimensionPixelSize(dimensionDivider);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(space, space, space, space);
        }
    }
}