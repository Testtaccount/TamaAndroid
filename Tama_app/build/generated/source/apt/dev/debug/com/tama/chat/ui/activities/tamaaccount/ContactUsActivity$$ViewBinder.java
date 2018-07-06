// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ContactUsActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.ContactUsActivity> extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755224, "field 'franceNumber'");
    target.franceNumber = finder.castView(view, 2131755224, "field 'franceNumber'");
    view = finder.findRequiredView(source, 2131755226, "field 'germanyNumber'");
    target.germanyNumber = finder.castView(view, 2131755226, "field 'germanyNumber'");
    view = finder.findRequiredView(source, 2131755228, "field 'emailText'");
    target.emailText = finder.castView(view, 2131755228, "field 'emailText'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.franceNumber = null;
    target.germanyNumber = null;
    target.emailText = null;
  }
}
