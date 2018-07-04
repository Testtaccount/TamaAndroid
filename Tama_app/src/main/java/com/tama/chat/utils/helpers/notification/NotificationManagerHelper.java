package com.tama.chat.utils.helpers.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.q_municate_user_service.QMUserService;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.tama.chat.R;
import com.tama.chat.ui.activities.authorization.SplashActivity;
import com.tama.chat.utils.SystemUtils;
import com.tama.q_municate_core.models.NotificationEvent;
import com.tama.q_municate_core.service.QBServiceConsts;
import com.tama.q_municate_db.managers.DataManager;

public class NotificationManagerHelper {

    public final static int NOTIFICATION_ID = NotificationManagerHelper.class.hashCode();
    private static final String TAG = NotificationManagerHelper.class.getSimpleName();

    public static void sendChatNotificationEvent(Context context, int userId, String dialogId,
                                                 NotificationEvent notificationEvent) {

        QBChatDialog chatDialog = DataManager.getInstance().getQBChatDialogDataManager()
                .getByDialogId(dialogId);
        QMUser user = QMUserService.getInstance().getUserCache().get((long) userId);

        Log.d(TAG, "chatDialog for opening by push: " + chatDialog + " user: " + user);

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(QBServiceConsts.EXTRA_SHOULD_OPEN_DIALOG, true);
        sendChatNotificationEvent(context, intent, notificationEvent);
        sendNotifyIncomingMessage(context, dialogId);
    }

    private static void sendNotifyIncomingMessage(Context context, String dialogId) {
        Intent intent = new Intent(QBServiceConsts.UPDATE_CHAT_DIALOG_ACTION);
        intent.putExtra(QBServiceConsts.EXTRA_DIALOG_ID, dialogId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    //chka
    public static void sendNotificationEvent(Context context, NotificationEvent notificationEvent) {
        Intent intent = SystemUtils.getPreviousIntent(context);
        if (intent == null) {
            intent = new Intent(context, SplashActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        sendNotificationEvent(context, intent, notificationEvent);
    }

    public static void sendCommonNotificationEvent(Context context,
                                             NotificationEvent notificationEvent) {

        Intent intent = new Intent(context, SplashActivity.class);
        sendChatNotificationEvent(context, intent, notificationEvent);

    }

    private static void sendChatNotificationEvent(Context context, Intent intent, NotificationEvent notificationEvent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(getNotificationIcon())
            .setContentTitle(notificationEvent.getTitle())
            .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationEvent.getSubject()))
            .setContentText(notificationEvent.getBody())
            .setAutoCancel(true)
            .setContentIntent(contentIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    //chka
    private static void sendNotificationEvent(Context context, Intent intent, NotificationEvent notificationEvent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(notificationEvent.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationEvent.getSubject()))
                .setContentText(notificationEvent.getBody())
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private static int getNotificationIcon() {
        boolean whiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        // TODO need to add other icon
        return whiteIcon ? R.drawable.ic_launcher : R.drawable.ic_launcher;
    }

    public static void clearNotificationEvent(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}