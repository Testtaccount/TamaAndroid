// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class TamaTopUpActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.TamaTopUpActivity> extends com.tama.chat.countryCode.BaseFlagActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755342, "field 'tamaTopupFirstPage'");
    target.tamaTopupFirstPage = finder.castView(view, 2131755342, "field 'tamaTopupFirstPage'");
    view = finder.findRequiredView(source, 2131755349, "field 'tamaTopupSecondPage'");
    target.tamaTopupSecondPage = finder.castView(view, 2131755349, "field 'tamaTopupSecondPage'");
    view = finder.findRequiredView(source, 2131755341, "field 'topupTitle'");
    target.topupTitle = finder.castView(view, 2131755341, "field 'topupTitle'");
    view = finder.findRequiredView(source, 2131755343, "field 'topupErrorText'");
    target.topupErrorText = finder.castView(view, 2131755343, "field 'topupErrorText'");
    view = finder.findRequiredView(source, 2131755351, "field 'topupInfoText'");
    target.topupInfoText = finder.castView(view, 2131755351, "field 'topupInfoText'");
    view = finder.findRequiredView(source, 2131755350, "field 'gridView'");
    target.gridView = finder.castView(view, 2131755350, "field 'gridView'");
    view = finder.findRequiredView(source, 2131755344, "field 'topupButtonFirst' and method 'onClickTopUpFirstButton'");
    target.topupButtonFirst = finder.castView(view, 2131755344, "field 'topupButtonFirst'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickTopUpFirstButton();
        }
      });
    view = finder.findRequiredView(source, 2131755353, "field 'topupButtonSeconds' and method 'onClickTopUpSecondsButton'");
    target.topupButtonSeconds = finder.castView(view, 2131755353, "field 'topupButtonSeconds'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickTopUpSecondsButton();
        }
      });
    view = finder.findRequiredView(source, 2131755325, "field 'openContactsList' and method 'clickOpenContactsList'");
    target.openContactsList = finder.castView(view, 2131755325, "field 'openContactsList'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOpenContactsList();
        }
      });
    view = finder.findRequiredView(source, 2131755352, "field 'promoCheckBox' and method 'usePromoCheckBoxCheck'");
    target.promoCheckBox = finder.castView(view, 2131755352, "field 'promoCheckBox'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.usePromoCheckBoxCheck(p0, p1);
        }
      });
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.tamaTopupFirstPage = null;
    target.tamaTopupSecondPage = null;
    target.topupTitle = null;
    target.topupErrorText = null;
    target.topupInfoText = null;
    target.gridView = null;
    target.topupButtonFirst = null;
    target.topupButtonSeconds = null;
    target.openContactsList = null;
    target.promoCheckBox = null;
  }
}
