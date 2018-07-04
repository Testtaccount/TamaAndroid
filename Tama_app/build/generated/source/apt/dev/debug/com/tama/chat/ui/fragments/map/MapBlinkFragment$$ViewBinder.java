// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.map;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MapBlinkFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.map.MapBlinkFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755235, "field 'friendsRecyclerView'");
    target.friendsRecyclerView = finder.castView(view, 2131755235, "field 'friendsRecyclerView'");
  }

  @Override public void unbind(T target) {
    target.friendsRecyclerView = null;
  }
}
