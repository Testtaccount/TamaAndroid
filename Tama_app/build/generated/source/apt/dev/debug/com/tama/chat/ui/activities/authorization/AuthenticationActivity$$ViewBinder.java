// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.authorization;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AuthenticationActivity$$ViewBinder<T extends com.tama.chat.ui.activities.authorization.AuthenticationActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755207, "field 'firstNameTextInputLayout'");
    target.firstNameTextInputLayout = finder.castView(view, 2131755207, "field 'firstNameTextInputLayout'");
    view = finder.findRequiredView(source, 2131755208, "field 'firstNameEditText'");
    target.firstNameEditText = finder.castView(view, 2131755208, "field 'firstNameEditText'");
    view = finder.findRequiredView(source, 2131755209, "field 'lastNameTextInputLayout'");
    target.lastNameTextInputLayout = finder.castView(view, 2131755209, "field 'lastNameTextInputLayout'");
    view = finder.findRequiredView(source, 2131755210, "field 'lastNameEditText'");
    target.lastNameEditText = finder.castView(view, 2131755210, "field 'lastNameEditText'");
    view = finder.findRequiredView(source, 2131755205, "field 'avatarImageView'");
    target.avatarImageView = finder.castView(view, 2131755205, "field 'avatarImageView'");
    view = finder.findRequiredView(source, 2131755206, "field 'chooseImage' and method 'clickChooseImage'");
    target.chooseImage = finder.castView(view, 2131755206, "field 'chooseImage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickChooseImage();
        }
      });
    view = finder.findRequiredView(source, 2131755211, "field 'finishAuthentication' and method 'clickfinishAuthentication'");
    target.finishAuthentication = finder.castView(view, 2131755211, "field 'finishAuthentication'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickfinishAuthentication();
        }
      });
  }

  @Override public void unbind(T target) {
    target.firstNameTextInputLayout = null;
    target.firstNameEditText = null;
    target.lastNameTextInputLayout = null;
    target.lastNameEditText = null;
    target.avatarImageView = null;
    target.chooseImage = null;
    target.finishAuthentication = null;
  }
}
