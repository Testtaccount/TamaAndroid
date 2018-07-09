// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TamaHistoryFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.tamaaccount.TamaHistoryFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755548, "field 'historyElementList'");
    target.historyElementList = finder.castView(view, 2131755548, "field 'historyElementList'");
    view = finder.findRequiredView(source, 2131755260, "field 'errorMessageText'");
    target.errorMessageText = finder.castView(view, 2131755260, "field 'errorMessageText'");
    view = finder.findRequiredView(source, 2131755543, "field 'historyAll' and method 'onClickHistoryAll'");
    target.historyAll = finder.castView(view, 2131755543, "field 'historyAll'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickHistoryAll();
        }
      });
    view = finder.findRequiredView(source, 2131755544, "field 'historyMyTama' and method 'onClickHistoryMyTama'");
    target.historyMyTama = finder.castView(view, 2131755544, "field 'historyMyTama'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickHistoryMyTama();
        }
      });
    view = finder.findRequiredView(source, 2131755545, "field 'historyTamaExpress' and method 'onClickHistoryTamaExpress'");
    target.historyTamaExpress = finder.castView(view, 2131755545, "field 'historyTamaExpress'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickHistoryTamaExpress();
        }
      });
    view = finder.findRequiredView(source, 2131755546, "field 'historyTamaTopup' and method 'onClickHistoryTamaTopup'");
    target.historyTamaTopup = finder.castView(view, 2131755546, "field 'historyTamaTopup'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickHistoryTamaTopup();
        }
      });
  }

  @Override public void unbind(T target) {
    target.historyElementList = null;
    target.errorMessageText = null;
    target.historyAll = null;
    target.historyMyTama = null;
    target.historyTamaExpress = null;
    target.historyTamaTopup = null;
  }
}
