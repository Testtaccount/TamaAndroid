// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.adapters.search;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LocalSearchAdapter$ViewHolder$$ViewBinder<T extends com.tama.chat.ui.adapters.search.LocalSearchAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755246, "field 'avatarImageView'");
    target.avatarImageView = finder.castView(view, 2131755246, "field 'avatarImageView'");
    view = finder.findRequiredView(source, 2131755629, "field 'titleTextView'");
    target.titleTextView = finder.castView(view, 2131755629, "field 'titleTextView'");
    view = finder.findRequiredView(source, 2131755617, "field 'labelTextView'");
    target.labelTextView = finder.castView(view, 2131755617, "field 'labelTextView'");
  }

  @Override public void unbind(T target) {
    target.avatarImageView = null;
    target.titleTextView = null;
    target.labelTextView = null;
  }
}
