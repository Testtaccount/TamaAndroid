// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.forgotpassword;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ForgotPasswordActivity$$ViewBinder<T extends com.tama.chat.ui.activities.forgotpassword.ForgotPasswordActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755244, "field 'emailTextInputLayout'");
    target.emailTextInputLayout = finder.castView(view, 2131755244, "field 'emailTextInputLayout'");
    view = finder.findRequiredView(source, 2131755245, "field 'emailEditText' and method 'onTextChangedEmail'");
    target.emailEditText = finder.castView(view, 2131755245, "field 'emailEditText'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.onTextChangedEmail(p0);
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
    target.emailTextInputLayout = null;
    target.emailEditText = null;
  }
}
