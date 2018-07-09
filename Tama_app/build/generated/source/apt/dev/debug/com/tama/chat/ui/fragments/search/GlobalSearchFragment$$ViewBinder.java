// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.search;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GlobalSearchFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.search.GlobalSearchFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755483, "field 'swipyRefreshLayout'");
    target.swipyRefreshLayout = finder.castView(view, 2131755483, "field 'swipyRefreshLayout'");
    view = finder.findRequiredView(source, 2131755484, "field 'contactsRecyclerView' and method 'touchContactsList'");
    target.contactsRecyclerView = finder.castView(view, 2131755484, "field 'contactsRecyclerView'");
    view.setOnTouchListener(
      new android.view.View.OnTouchListener() {
        @Override public boolean onTouch(
          android.view.View p0,
          android.view.MotionEvent p1
        ) {
          return target.touchContactsList(p0, p1);
        }
      });
  }

  @Override public void unbind(T target) {
    target.swipyRefreshLayout = null;
    target.contactsRecyclerView = null;
  }
}
