// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.profile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyProfileActivity$$ViewBinder<T extends com.tama.chat.ui.activities.profile.MyProfileActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755227, "field 'photoImageView'");
    target.photoImageView = finder.castView(view, 2131755227, "field 'photoImageView'");
    view = finder.findRequiredView(source, 2131755296, "field 'fullNameTextInputLayout'");
    target.fullNameTextInputLayout = finder.castView(view, 2131755296, "field 'fullNameTextInputLayout'");
    view = finder.findRequiredView(source, 2131755297, "field 'fullNameEditText' and method 'onTextChangedFullName'");
    target.fullNameEditText = finder.castView(view, 2131755297, "field 'fullNameEditText'");
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
    view = finder.findRequiredView(source, 2131755295, "method 'changePhoto'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.changePhoto(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.photoImageView = null;
    target.fullNameTextInputLayout = null;
    target.fullNameEditText = null;
  }
}
