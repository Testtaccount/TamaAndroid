// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GroupDialogDetailsActivity$$ViewBinder<T extends com.tama.chat.ui.activities.chats.GroupDialogDetailsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755242, "field 'groupNameEditText' and method 'onGroupNameTextChanged'");
    target.groupNameEditText = finder.castView(view, 2131755242, "field 'groupNameEditText'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.onGroupNameTextChanged(p0);
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
    view = finder.findRequiredView(source, 2131755243, "field 'occupantsTextView'");
    target.occupantsTextView = finder.castView(view, 2131755243, "field 'occupantsTextView'");
    view = finder.findRequiredView(source, 2131755246, "field 'occupantsListView'");
    target.occupantsListView = finder.castView(view, 2131755246, "field 'occupantsListView'");
    view = finder.findRequiredView(source, 2131755245, "field 'onlineOccupantsTextView'");
    target.onlineOccupantsTextView = finder.castView(view, 2131755245, "field 'onlineOccupantsTextView'");
    view = finder.findRequiredView(source, 2131755241, "field 'photoImageView'");
    target.photoImageView = finder.castView(view, 2131755241, "field 'photoImageView'");
  }

  @Override public void unbind(T target) {
    target.groupNameEditText = null;
    target.occupantsTextView = null;
    target.occupantsListView = null;
    target.onlineOccupantsTextView = null;
    target.photoImageView = null;
  }
}
