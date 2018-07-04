// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.changepassword;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChangePasswordActivity$$ViewBinder<T extends com.tama.chat.ui.activities.changepassword.ChangePasswordActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755219, "field 'oldPasswordTextInputLayout'");
    target.oldPasswordTextInputLayout = finder.castView(view, 2131755219, "field 'oldPasswordTextInputLayout'");
    view = finder.findRequiredView(source, 2131755220, "field 'oldPasswordEditText' and method 'onTextChangedOldPassword'");
    target.oldPasswordEditText = finder.castView(view, 2131755220, "field 'oldPasswordEditText'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.onTextChangedOldPassword(p0);
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
    view = finder.findRequiredView(source, 2131755222, "field 'newPasswordTextInputLayout'");
    target.newPasswordTextInputLayout = finder.castView(view, 2131755222, "field 'newPasswordTextInputLayout'");
    view = finder.findRequiredView(source, 2131755221, "field 'newPasswordEditText' and method 'onTextChangedNewPassword'");
    target.newPasswordEditText = finder.castView(view, 2131755221, "field 'newPasswordEditText'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.onTextChangedNewPassword(p0);
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
  }

  @Override public void unbind(T target) {
    target.oldPasswordTextInputLayout = null;
    target.oldPasswordEditText = null;
    target.newPasswordTextInputLayout = null;
    target.newPasswordEditText = null;
  }
}
