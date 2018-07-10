// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.search;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LocalSearchFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.search.LocalSearchFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755491, "field 'dialogsRecyclerView' and method 'touchList'");
    target.dialogsRecyclerView = finder.castView(view, 2131755491, "field 'dialogsRecyclerView'");
    view.setOnTouchListener(
      new android.view.View.OnTouchListener() {
        @Override public boolean onTouch(
          android.view.View p0,
          android.view.MotionEvent p1
        ) {
          return target.touchList(p0, p1);
        }
      });
  }

  @Override public void unbind(T target) {
    target.dialogsRecyclerView = null;
  }
}
