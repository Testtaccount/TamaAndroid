// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TamaHistoryFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.tamaaccount.TamaHistoryFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755543, "field 'mRecyclerView'");
    target.mRecyclerView = finder.castView(view, 2131755543, "field 'mRecyclerView'");
    view = finder.findRequiredView(source, 2131755260, "field 'errorMessageText'");
    target.errorMessageText = finder.castView(view, 2131755260, "field 'errorMessageText'");
  }

  @Override public void unbind(T target) {
    target.mRecyclerView = null;
    target.errorMessageText = null;
  }
}
