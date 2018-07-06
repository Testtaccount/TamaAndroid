// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class TamaHistoryActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.TamaHistoryActivity> extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755262, "field 'errorMessageText'");
    target.errorMessageText = finder.castView(view, 2131755262, "field 'errorMessageText'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.errorMessageText = null;
  }
}
