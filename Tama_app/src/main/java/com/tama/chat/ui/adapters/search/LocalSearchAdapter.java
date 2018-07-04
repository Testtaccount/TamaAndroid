package com.tama.chat.ui.adapters.search;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.adapters.base.BaseClickListenerViewHolder;
import com.tama.chat.ui.adapters.base.BaseFilterAdapter;
import com.tama.chat.ui.adapters.base.BaseViewHolder;
import com.tama.chat.ui.views.roundedimageview.RoundedImageView;
import com.tama.chat.utils.ChatDialogUtils;
import com.tama.chat.utils.DateUtils;
import com.tama.chat.utils.helpers.TextViewHelper;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.q_municate_core.models.DialogSearchWrapper;
import com.tama.q_municate_core.qb.helpers.QBFriendListHelper;
import com.tama.q_municate_core.utils.OnlineStatusUtils;
import com.tama.q_municate_db.managers.DataManager;
import java.util.List;

public class LocalSearchAdapter extends BaseFilterAdapter<DialogSearchWrapper, BaseClickListenerViewHolder<DialogSearchWrapper>> {

    private DataManager dataManager;
    private QBFriendListHelper qbFriendListHelper;

    public LocalSearchAdapter(BaseActivity baseActivity, List<DialogSearchWrapper> list) {
        super(baseActivity, list);
    }

    @Override
    protected boolean isMatch(DialogSearchWrapper item, String query) {
        String chatTitle = ChatDialogUtils.getTitleForChatDialog(item.getChatDialog(), dataManager);
        return chatTitle != null && chatTitle.toLowerCase().contains(query);
    }

    @Override
    public BaseClickListenerViewHolder<DialogSearchWrapper> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this, layoutInflater.inflate(R.layout.item_local_search, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseClickListenerViewHolder<DialogSearchWrapper> baseClickListenerViewHolder, int position) {
        DialogSearchWrapper dialogWrapper = getItem(position);
        ViewHolder viewHolder = (ViewHolder) baseClickListenerViewHolder;


        String label;

        if (QBDialogType.PRIVATE.equals(dialogWrapper.getChatDialog().getType())) {
            QMUser opponentUser = dialogWrapper.getOpponentUser();
            setOnlineStatus(viewHolder, opponentUser);
            displayAvatarImage(opponentUser.getAvatar(), viewHolder.avatarImageView);
            viewHolder.titleTextView.setText(opponentUser.getFullName());
        } else {
            label = dialogWrapper.getLabel();
            viewHolder.labelTextView.setText(label);
            viewHolder.labelTextView.setTextColor(resources.getColor(R.color.dark_gray));
            displayGroupPhotoImage(dialogWrapper.getChatDialog().getPhoto(), viewHolder.avatarImageView);
            viewHolder.titleTextView.setText(dialogWrapper.getChatDialog().getName());
        }

        if (!TextUtils.isEmpty(query)) {
            TextViewHelper.changeTextColorView(baseActivity, viewHolder.titleTextView, query);
        }
    }

    public void removeItemByDialogId(String dialogId){
        for (DialogSearchWrapper dialogSearchWrapper : getAllItems()){
            if (dialogSearchWrapper.getChatDialog().getDialogId().equals(dialogId)){
                removeItem(dialogSearchWrapper);
            }
        }
    }

    public void setFriendListHelper(QBFriendListHelper qbFriendListHelper) {
        this.qbFriendListHelper = qbFriendListHelper;
        notifyDataSetChanged();
    }

    private void setOnlineStatus(ViewHolder viewHolder, QMUser user) {
        boolean online = qbFriendListHelper != null && user.getId()!= null && qbFriendListHelper.isUserOnline(user.getId());

        if (online) {
            viewHolder.labelTextView.setText(OnlineStatusUtils.getOnlineStatus(online));
            viewHolder.labelTextView.setTextColor(resources.getColor(R.color.green));
        } else {
            viewHolder.labelTextView.setText(user.getLastRequestAt() == null ? null : resources.getString(R.string.last_seen,
                    DateUtils.toTodayYesterdayShortDateWithoutYear2(user.getLastRequestAt().getTime()),
                    DateUtils.formatDateSimpleTime(user.getLastRequestAt().getTime())));
            viewHolder.labelTextView.setTextColor(resources.getColor(R.color.dark_gray));
        }
    }

    private void displayGroupPhotoImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView,
                ImageLoaderUtils.UIL_GROUP_AVATAR_DISPLAY_OPTIONS);
    }

    protected static class ViewHolder extends BaseViewHolder<DialogSearchWrapper> {

        @Bind(R.id.avatar_imageview)
        RoundedImageView avatarImageView;

        @Bind(R.id.title_textview)
        TextView titleTextView;

        @Bind(R.id.label_textview)
        TextView labelTextView;

        public ViewHolder(LocalSearchAdapter adapter, View view) {
            super(adapter, view);
        }
    }
}

/*
* import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.adapters.base.BaseClickListenerViewHolder;
import com.tama.chat.ui.adapters.base.BaseFilterAdapter;
import com.tama.chat.ui.adapters.base.BaseViewHolder;
import com.tama.chat.ui.views.roundedimageview.RoundedImageView;
import com.tama.chat.utils.DateUtils;
import com.tama.chat.utils.helpers.TextViewHelper;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.qb.helpers.QBFriendListHelper;
import com.tama.q_municate_core.utils.ChatUtils;
import com.tama.q_municate_core.utils.OnlineStatusUtils;
import com.tama.q_municate_core.utils.UserFriendUtils;
import com.tama.q_municate_db.managers.DataManager;
import com.tama.q_municate_db.models.Dialog;
import com.tama.q_municate_db.models.DialogNotification;
import com.tama.q_municate_db.models.DialogOccupant;
import com.tama.q_municate_db.models.Message;
import java.util.List;

public class LocalSearchAdapter extends BaseFilterAdapter<Dialog, BaseClickListenerViewHolder<Dialog>> {

    private DataManager dataManager;
    private QBFriendListHelper qbFriendListHelper;

    public LocalSearchAdapter(BaseActivity baseActivity, List<Dialog> list) {
        super(baseActivity, list);
        dataManager = DataManager.getInstance();
    }

    @Override
    protected boolean isMatch(Dialog item, String query) {
        return item.getTitle() != null && item.getTitle().toLowerCase().contains(query);
    }

    @Override
    public BaseClickListenerViewHolder<Dialog> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this, layoutInflater.inflate(R.layout.item_local_search, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseClickListenerViewHolder<Dialog> baseClickListenerViewHolder, int position) {
        Dialog dialog = getItem(position);
        ViewHolder viewHolder = (ViewHolder) baseClickListenerViewHolder;

        List<DialogOccupant> dialogOccupantsList = dataManager.getDialogOccupantDataManager()
            .getDialogOccupantsListByDialogId(dialog.getDialogId());

        String label;

        if (QBDialogType.PRIVATE.equals(dialog.getType())) {
            QMUser currentUser =  UserFriendUtils.createLocalUser(AppSession.getSession().getUser());
            QMUser opponentUser = ChatUtils.getOpponentFromPrivateDialog(currentUser, dialogOccupantsList);
            setOnlineStatus(viewHolder, opponentUser);
            displayAvatarImage(opponentUser.getAvatar(), viewHolder.avatarImageView);
        } else {
            List<Long> dialogOccupantsIdsList = ChatUtils.getIdsFromDialogOccupantsList(dialogOccupantsList);
            Message message = dataManager.getMessageDataManager().getLastMessageWithTempByDialogId(dialogOccupantsIdsList);
            DialogNotification dialogNotification = dataManager.getDialogNotificationDataManager()
                .getLastDialogNotificationByDialogId(dialogOccupantsIdsList);
            label = ChatUtils.getDialogLastMessage(
                resources.getString(R.string.cht_notification_message),
                resources.getString(R.string.snap_text),
                message,
                dialogNotification);

            viewHolder.labelTextView.setText(label);
            viewHolder.labelTextView.setTextColor(resources.getColor(R.color.dark_gray));

            displayGroupPhotoImage(dialog.getPhoto(), viewHolder.avatarImageView);
        }

        viewHolder.titleTextView.setText(dialog.getTitle());

        if (!TextUtils.isEmpty(query)) {
            TextViewHelper.changeTextColorView(baseActivity, viewHolder.titleTextView, query);
        }
    }

    public void setFriendListHelper(QBFriendListHelper qbFriendListHelper) {
        this.qbFriendListHelper = qbFriendListHelper;
        notifyDataSetChanged();
    }

    private void setOnlineStatus(ViewHolder viewHolder, QMUser user) {
        boolean online = qbFriendListHelper != null && qbFriendListHelper.isUserOnline(user.getId());

        if (online) {
            viewHolder.labelTextView.setText(OnlineStatusUtils.getOnlineStatus(online));
            viewHolder.labelTextView.setTextColor(resources.getColor(R.color.green));
        } else {
            viewHolder.labelTextView.setText(resources.getString(R.string.last_seen,
                DateUtils.toTodayYesterdayShortDateWithoutYear2(Long.parseLong(user.getStatus())),//dzel hanel longy
                DateUtils.formatDateSimpleTime(Long.parseLong(user.getStatus()))));//dzel Hayk
            viewHolder.labelTextView.setTextColor(resources.getColor(R.color.dark_gray));
        }
    }

    private void displayGroupPhotoImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView,
            ImageLoaderUtils.UIL_GROUP_AVATAR_DISPLAY_OPTIONS);
    }

    protected static class ViewHolder extends BaseViewHolder<Dialog> {

        @Bind(R.id.avatar_imageview)
        RoundedImageView avatarImageView;

        @Bind(R.id.title_textview)
        TextView titleTextView;

        @Bind(R.id.label_textview)
        TextView labelTextView;

        public ViewHolder(LocalSearchAdapter adapter, View view) {
            super(adapter, view);
        }
    }
}
* */