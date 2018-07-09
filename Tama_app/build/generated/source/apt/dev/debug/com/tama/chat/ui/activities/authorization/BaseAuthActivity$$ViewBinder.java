// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.authorization;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BaseAuthActivity$$ViewBinder<T extends com.tama.chat.ui.activities.authorization.BaseAuthActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131755239, null);
    target.emailTextInputLayout = finder.castView(view, 2131755239, "field 'emailTextInputLayout'");
    view = finder.findOptionalView(source, 2131755240, null);
    target.emailEditText = finder.castView(view, 2131755240, "field 'emailEditText'");
    if (view != null) {
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
    view = finder.findOptionalView(source, 2131755264, null);
    target.passwordTextInputLayout = finder.castView(view, 2131755264, "field 'passwordTextInputLayout'");
    view = finder.findOptionalView(source, 2131755265, null);
    target.passwordEditText = finder.castView(view, 2131755265, "field 'passwordEditText'");
    if (view != null) {
      ((android.widget.TextView) view).addTextChangedListener(
        new android.text.TextWatcher() {
          @Override public void onTextChanged(
            java.lang.CharSequence p0,
            int p1,
            int p2,
            int p3
          ) {
            target.onTextChangedPassword(p0);
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
  }

  @Override public void unbind(T target) {
    target.emailTextInputLayout = null;
    target.emailEditText = null;
    target.passwordTextInputLayout = null;
    target.passwordEditText = null;
  }
}
