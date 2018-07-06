// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.profile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserProfileActivity$$ViewBinder<T extends com.tama.chat.ui.activities.profile.UserProfileActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755243, "field 'avatarImageView' and method 'showUserAvatar'");
    target.avatarImageView = finder.castView(view, 2131755243, "field 'avatarImageView'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showUserAvatar();
        }
      });
    view = finder.findRequiredView(source, 2131755244, "field 'nameTextView'");
    target.nameTextView = finder.castView(view, 2131755244, "field 'nameTextView'");
    view = finder.findRequiredView(source, 2131755363, "field 'timestampTextView'");
    target.timestampTextView = finder.castView(view, 2131755363, "field 'timestampTextView'");
    view = finder.findRequiredView(source, 2131755364, "field 'phoneView'");
    target.phoneView = view;
    view = finder.findRequiredView(source, 2131755365, "field 'phoneTextView'");
    target.phoneTextView = finder.castView(view, 2131755365, "field 'phoneTextView'");
    view = finder.findRequiredView(source, 2131755367, "method 'sendMessage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendMessage(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755368, "method 'audioCall'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.audioCall(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755369, "method 'videoCall'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.videoCall(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755370, "method 'deleteChatHistory'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deleteChatHistory(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755371, "method 'removeContactAndChatHistory'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.removeContactAndChatHistory(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.avatarImageView = null;
    target.nameTextView = null;
    target.timestampTextView = null;
    target.phoneView = null;
    target.phoneTextView = null;
  }
}
