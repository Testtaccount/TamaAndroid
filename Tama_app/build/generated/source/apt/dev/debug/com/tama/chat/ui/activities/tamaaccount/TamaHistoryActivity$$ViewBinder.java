// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class TamaHistoryActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.TamaHistoryActivity> extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755339, "field 'mProgressBar'");
    target.mProgressBar = finder.castView(view, 2131755339, "field 'mProgressBar'");
    view = finder.findRequiredView(source, 2131755612, "field 'mTabLayout'");
    target.mTabLayout = finder.castView(view, 2131755612, "field 'mTabLayout'");
    view = finder.findRequiredView(source, 2131755340, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131755340, "field 'mViewPager'");
    view = finder.findRequiredView(source, 2131755260, "field 'errorMessageText'");
    target.errorMessageText = finder.castView(view, 2131755260, "field 'errorMessageText'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.mProgressBar = null;
    target.mTabLayout = null;
    target.mViewPager = null;
    target.errorMessageText = null;
  }
}
