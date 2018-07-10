// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.main;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.tama.chat.ui.activities.main.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755270, "field 'navigation'");
    target.navigation = finder.castView(view, 2131755270, "field 'navigation'");
    view = finder.findRequiredView(source, 2131755213, "field 'toolbarView'");
    target.toolbarView = finder.castView(view, 2131755213, "field 'toolbarView'");
    view = finder.findRequiredView(source, 2131755218, "field 'toolbarTamaView'");
    target.toolbarTamaView = finder.castView(view, 2131755218, "field 'toolbarTamaView'");
    view = finder.findRequiredView(source, 2131755747, "field 'tamaToolbarLogo'");
    target.tamaToolbarLogo = finder.castView(view, 2131755747, "field 'tamaToolbarLogo'");
    view = finder.findRequiredView(source, 2131755749, "field 'tamaToolbarUserIcon'");
    target.tamaToolbarUserIcon = finder.castView(view, 2131755749, "field 'tamaToolbarUserIcon'");
    view = finder.findRequiredView(source, 2131755748, "field 'tamaToolbarTitle'");
    target.tamaToolbarTitle = finder.castView(view, 2131755748, "field 'tamaToolbarTitle'");
    view = finder.findRequiredView(source, 2131755750, "field 'tamaToolbarSubtitle'");
    target.tamaToolbarSubtitle = finder.castView(view, 2131755750, "field 'tamaToolbarSubtitle'");
  }

  @Override public void unbind(T target) {
    target.navigation = null;
    target.toolbarView = null;
    target.toolbarTamaView = null;
    target.tamaToolbarLogo = null;
    target.tamaToolbarUserIcon = null;
    target.tamaToolbarTitle = null;
    target.tamaToolbarSubtitle = null;
  }
}
