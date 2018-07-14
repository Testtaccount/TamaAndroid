// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.profile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserProfileActivity$$ViewBinder<T extends com.tama.chat.ui.activities.profile.UserProfileActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755241, "field 'avatarImageView' and method 'showUserAvatar'");
    target.avatarImageView = finder.castView(view, 2131755241, "field 'avatarImageView'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showUserAvatar();
        }
      });
    view = finder.findRequiredView(source, 2131755242, "field 'nameTextView'");
    target.nameTextView = finder.castView(view, 2131755242, "field 'nameTextView'");
    view = finder.findRequiredView(source, 2131755366, "field 'timestampTextView'");
    target.timestampTextView = finder.castView(view, 2131755366, "field 'timestampTextView'");
    view = finder.findRequiredView(source, 2131755367, "field 'phoneView'");
    target.phoneView = view;
    view = finder.findRequiredView(source, 2131755368, "field 'phoneTextView'");
    target.phoneTextView = finder.castView(view, 2131755368, "field 'phoneTextView'");
    view = finder.findRequiredView(source, 2131755370, "method 'sendMessage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendMessage(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755371, "method 'audioCall'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.audioCall(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755372, "method 'videoCall'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.videoCall(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755373, "method 'deleteChatHistory'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deleteChatHistory(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755374, "method 'removeContactAndChatHistory'");
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
