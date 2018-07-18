// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.authorization;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SignUpActivity$$ViewBinder<T extends com.tama.chat.ui.activities.authorization.SignUpActivity> extends com.tama.chat.ui.activities.authorization.BaseAuthActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755301, "field 'fullNameInputLayout'");
    target.fullNameInputLayout = finder.castView(view, 2131755301, "field 'fullNameInputLayout'");
    view = finder.findRequiredView(source, 2131755302, "field 'fullNameEditText' and method 'onTextChangedFullName'");
    target.fullNameEditText = finder.castView(view, 2131755302, "field 'fullNameEditText'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.onTextChangedFullName(p0);
        }
        @Override public void beforeTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void afterTextChanged(
          android.text.Editable p0
        ) {
          
        }
      });
    view = finder.findRequiredView(source, 2131755246, "field 'avatarImageView'");
    target.avatarImageView = finder.castView(view, 2131755246, "field 'avatarImageView'");
    view = finder.findRequiredView(source, 2131755338, "method 'selectAvatar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectAvatar(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755339, "method 'openUserAgreement'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.openUserAgreement(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.fullNameInputLayout = null;
    target.fullNameEditText = null;
    target.avatarImageView = null;
  }
}
