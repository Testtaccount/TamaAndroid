package com.tama.q_municate_core.utils.call;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import com.quickblox.q_municate_core.R;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCMediaConfig;
import com.tama.q_municate_core.utils.helpers.CoreSharedHelper;
import com.tama.q_municate_db.utils.ErrorUtils;
import java.util.List;
//popoxac nael video
public class SettingsUtil {

    private static final String TAG = SettingsUtil.class.getSimpleName();


//    private static void setSettingsForMultiCall(List<QBUser> users) {Avetik
    private static void setSettingsForMultiCall(List<QBUser> users) {
        if (users.size() <= 2) {
            int width = QBRTCMediaConfig.getVideoWidth();
            if (width > QBRTCMediaConfig.VideoQuality.VGA_VIDEO.width) {
                QBRTCMediaConfig.setVideoWidth(QBRTCMediaConfig.VideoQuality.VGA_VIDEO.width);
                QBRTCMediaConfig.setVideoHeight(QBRTCMediaConfig.VideoQuality.VGA_VIDEO.height);
            }
        } else {
            //set to minimum settings
            QBRTCMediaConfig.setVideoWidth(QBRTCMediaConfig.VideoQuality.QBGA_VIDEO.width);
            QBRTCMediaConfig.setVideoHeight(QBRTCMediaConfig.VideoQuality.QBGA_VIDEO.height);
            QBRTCMediaConfig.setVideoHWAcceleration(false);
            QBRTCMediaConfig.setVideoCodec(null);
        }
    }

    public static void setSettingsStrategy(Context context, List<QBUser> usersList) {
        if (usersList.size() == 1) {
            setSettingsFromPreferences(context);
        } else {
            setSettingsForMultiCall(usersList);
        }
    }


    private static void setSettingsFromPreferences(Context context) {
        CoreSharedHelper coreSharedHelper = CoreSharedHelper.getInstance();
        // Check HW codec flag.
        boolean hwCodec = coreSharedHelper.getCallHwCodec(
                context.getResources().getBoolean(R.bool.call_hw_codec_default));

        QBRTCMediaConfig.setVideoHWAcceleration(hwCodec);

        // Get video resolution from settings.
        int resolutionItem = coreSharedHelper.getCallResolution(
                context.getResources().getInteger(R.integer.call_resolution_default));

        ErrorUtils.logError(TAG, "resolutionItem = " + resolutionItem);

//        setVideoQuality(resolutionItem);
        setVideoQuality(1);  //Hayk

//        setVideoQuality(context);//Avetik

        // Get start bitrate.
        String bitrateTypeDefault = context.getString(R.string.call_startbitrate_default);
        String bitrateType = coreSharedHelper.getCallStartbitrate(bitrateTypeDefault);
        if (!bitrateType.equals(bitrateTypeDefault)) {
            int bitrateValue = coreSharedHelper.getCallStartbitrateValue(
                    context.getResources().getInteger(R.integer.call_startbitrate_value_default));
            QBRTCMediaConfig.setVideoStartBitrate(bitrateValue);
        }

        int videoCodecItem = coreSharedHelper.getCallVideoCodec(
                context.getResources().getInteger(R.integer.call_video_codec_default));
        for (QBRTCMediaConfig.VideoCodec codec : QBRTCMediaConfig.VideoCodec.values()) {
            if (codec.ordinal() == videoCodecItem) {
                ErrorUtils.logError(TAG, "videoCodecItem = " + codec.getDescription());
                QBRTCMediaConfig.setVideoCodec(codec);
                break;
            }
        }

        String audioCodecDescription = coreSharedHelper.getCallAudioCodec(
                context.getString(R.string.call_audio_codec_default));
        QBRTCMediaConfig.AudioCodec audioCodec = QBRTCMediaConfig.AudioCodec.ISAC.getDescription()
                .equals(audioCodecDescription) ? QBRTCMediaConfig.AudioCodec.ISAC : QBRTCMediaConfig.AudioCodec.OPUS;
        ErrorUtils.logError(TAG, "audioCodec = " + audioCodec.getDescription());
        QBRTCMediaConfig.setAudioCodec(audioCodec);
    }

    //chka
    private static void setVideoQuality(Context context){
        PackageManager pm = context.getPackageManager();
        boolean frontCam = pm.hasSystemFeature("android.hardware.camera.front");
        Camera camera;
        if(frontCam) {
            camera = Camera.open(1);
        }else{
            camera = Camera.open(0);
        }
        android.hardware.Camera.Parameters params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size result = sizes.get(1);
        QBRTCMediaConfig.setVideoHeight(result.height);
        QBRTCMediaConfig.setVideoWidth(result.width);
    }

    private static void setVideoQuality(int resolutionItem) {
        if (resolutionItem != -1) {
            setVideoFromLibraryPreferences(resolutionItem);
        }
    }

    private static void setVideoFromLibraryPreferences(int resolutionItem) {
        for (QBRTCMediaConfig.VideoQuality quality : QBRTCMediaConfig.VideoQuality.values()) {
            if (quality.ordinal() == resolutionItem) {
                ErrorUtils.logError(TAG, "resolution = " + quality.height + ":" + quality.width);
                QBRTCMediaConfig.setVideoHeight(quality.height);
                QBRTCMediaConfig.setVideoWidth(quality.width);
//                break;
            }
        }
    }
}