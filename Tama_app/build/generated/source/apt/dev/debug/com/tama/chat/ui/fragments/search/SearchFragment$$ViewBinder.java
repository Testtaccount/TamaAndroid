// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.search;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SearchFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.search.SearchFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755510, "field 'searchViewPager'");
    target.searchViewPager = finder.castView(view, 2131755510, "field 'searchViewPager'");
  }

  @Override public void unbind(T target) {
    target.searchViewPager = null;
  }
}
