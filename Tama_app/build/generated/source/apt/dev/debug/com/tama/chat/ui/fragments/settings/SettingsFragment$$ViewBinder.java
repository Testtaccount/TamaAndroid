// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingsFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.settings.SettingsFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755246, "field 'avatarImageView'");
    target.avatarImageView = finder.castView(view, 2131755246, "field 'avatarImageView'");
    view = finder.findRequiredView(source, 2131755302, "field 'fullNameTextView'");
    target.fullNameTextView = finder.castView(view, 2131755302, "field 'fullNameTextView'");
    view = finder.findRequiredView(source, 2131755555, "field 'pushNotificationSwitch' and method 'enablePushNotification'");
    target.pushNotificationSwitch = finder.castView(view, 2131755555, "field 'pushNotificationSwitch'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.enablePushNotification(p1);
        }
      });
    view = finder.findRequiredView(source, 2131755563, "field 'savePhotoVideoSwitch' and method 'enableSavePhotoVideo'");
    target.savePhotoVideoSwitch = finder.castView(view, 2131755563, "field 'savePhotoVideoSwitch'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.enableSavePhotoVideo(p1);
        }
      });
    view = finder.findRequiredView(source, 2131755561, "field 'snapChatSpinner' and method 'snapChatTimeChange'");
    target.snapChatSpinner = finder.castView(view, 2131755561, "field 'snapChatSpinner'");
    ((android.widget.AdapterView<?>) view).setOnItemSelectedListener(
      new android.widget.AdapterView.OnItemSelectedListener() {
        @Override public void onItemSelected(
          android.widget.AdapterView<?> p0,
          android.view.View p1,
          int p2,
          long p3
        ) {
          target.snapChatTimeChange(p0, p1, p2, p3);
        }
        @Override public void onNothingSelected(
          android.widget.AdapterView<?> p0
        ) {
          
        }
      });
    view = finder.findRequiredView(source, 2131755564, "field 'appLanguageSpinner' and method 'setAppLanguageSpinnerChange'");
    target.appLanguageSpinner = finder.castView(view, 2131755564, "field 'appLanguageSpinner'");
    ((android.widget.AdapterView<?>) view).setOnItemSelectedListener(
      new android.widget.AdapterView.OnItemSelectedListener() {
        @Override public void onItemSelected(
          android.widget.AdapterView<?> p0,
          android.view.View p1,
          int p2,
          long p3
        ) {
          target.setAppLanguageSpinnerChange(p0, p1, p2, p3);
        }
        @Override public void onNothingSelected(
          android.widget.AdapterView<?> p0
        ) {
          
        }
      });
    view = finder.findRequiredView(source, 2131755553, "method 'editProfile'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.editProfile();
        }
      });
    view = finder.findRequiredView(source, 2131755557, "method 'inviteFriends'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.inviteFriends();
        }
      });
    view = finder.findRequiredView(source, 2131755559, "method 'giveFeedback'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.giveFeedback();
        }
      });
    view = finder.findRequiredView(source, 2131755565, "method 'logout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.logout();
        }
      });
    view = finder.findRequiredView(source, 2131755567, "method 'deleteAccount'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deleteAccount();
        }
      });
  }

  @Override public void unbind(T target) {
    target.avatarImageView = null;
    target.fullNameTextView = null;
    target.pushNotificationSwitch = null;
    target.savePhotoVideoSwitch = null;
    target.snapChatSpinner = null;
    target.appLanguageSpinner = null;
  }
}
