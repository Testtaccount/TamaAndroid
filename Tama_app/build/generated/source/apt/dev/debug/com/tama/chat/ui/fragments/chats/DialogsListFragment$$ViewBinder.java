// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DialogsListFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.chats.DialogsListFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755518, "field 'dialogsListView' and method 'startChat'");
    target.dialogsListView = finder.castView(view, 2131755518, "field 'dialogsListView'");
    ((android.widget.AdapterView<?>) view).setOnItemClickListener(
      new android.widget.AdapterView.OnItemClickListener() {
        @Override public void onItemClick(
          android.widget.AdapterView<?> p0,
          android.view.View p1,
          int p2,
          long p3
        ) {
          target.startChat(p2);
        }
      });
    view = finder.findRequiredView(source, 2131755519, "field 'emptyListTextView'");
    target.emptyListTextView = finder.castView(view, 2131755519, "field 'emptyListTextView'");
  }

  @Override public void unbind(T target) {
    target.dialogsListView = null;
    target.emptyListTextView = null;
  }
}
