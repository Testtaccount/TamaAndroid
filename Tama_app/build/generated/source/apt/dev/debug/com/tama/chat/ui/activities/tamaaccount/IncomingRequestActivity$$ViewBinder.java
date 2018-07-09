// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class IncomingRequestActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.IncomingRequestActivity> extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755256, "field 'requestItemShowLayout'");
    target.requestItemShowLayout = finder.castView(view, 2131755256, "field 'requestItemShowLayout'");
    view = finder.findRequiredView(source, 2131755250, "field 'titleLayout'");
    target.titleLayout = finder.castView(view, 2131755250, "field 'titleLayout'");
    view = finder.findRequiredView(source, 2131755255, "field 'incomingRequestList'");
    target.incomingRequestList = finder.castView(view, 2131755255, "field 'incomingRequestList'");
    view = finder.findRequiredView(source, 2131755257, "field 'itemShowText'");
    target.itemShowText = finder.castView(view, 2131755257, "field 'itemShowText'");
    view = finder.findRequiredView(source, 2131755260, "field 'errorMessageText'");
    target.errorMessageText = finder.castView(view, 2131755260, "field 'errorMessageText'");
    view = finder.findRequiredView(source, 2131755251, "field 'allTextBtn' and method 'onClickAllTextBtn'");
    target.allTextBtn = finder.castView(view, 2131755251, "field 'allTextBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickAllTextBtn();
        }
      });
    view = finder.findRequiredView(source, 2131755252, "field 'myTamaTextBtn' and method 'onClickMyTamaTextBtn'");
    target.myTamaTextBtn = finder.castView(view, 2131755252, "field 'myTamaTextBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickMyTamaTextBtn();
        }
      });
    view = finder.findRequiredView(source, 2131755253, "field 'tamaExpressTextBtn' and method 'onClickTamaExpressTextBtn'");
    target.tamaExpressTextBtn = finder.castView(view, 2131755253, "field 'tamaExpressTextBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickTamaExpressTextBtn();
        }
      });
    view = finder.findRequiredView(source, 2131755254, "field 'tamaTopupTextBtn' and method 'onClickTamaTopupTextBtn'");
    target.tamaTopupTextBtn = finder.castView(view, 2131755254, "field 'tamaTopupTextBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickTamaTopupTextBtn();
        }
      });
    view = finder.findRequiredView(source, 2131755258, "field 'btnItemOk'");
    target.btnItemOk = finder.castView(view, 2131755258, "field 'btnItemOk'");
    view = finder.findRequiredView(source, 2131755259, "field 'btnItemDenied'");
    target.btnItemDenied = finder.castView(view, 2131755259, "field 'btnItemDenied'");
    view = finder.findRequiredView(source, 2131755248, "field 'btnIncomingRequest' and method 'onClickIncomingBtn'");
    target.btnIncomingRequest = finder.castView(view, 2131755248, "field 'btnIncomingRequest'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickIncomingBtn();
        }
      });
    view = finder.findRequiredView(source, 2131755249, "field 'btnOutgoingRequest' and method 'onClickOutGoingBtn'");
    target.btnOutgoingRequest = finder.castView(view, 2131755249, "field 'btnOutgoingRequest'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickOutGoingBtn();
        }
      });
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.requestItemShowLayout = null;
    target.titleLayout = null;
    target.incomingRequestList = null;
    target.itemShowText = null;
    target.errorMessageText = null;
    target.allTextBtn = null;
    target.myTamaTextBtn = null;
    target.tamaExpressTextBtn = null;
    target.tamaTopupTextBtn = null;
    target.btnItemOk = null;
    target.btnItemDenied = null;
    target.btnIncomingRequest = null;
    target.btnOutgoingRequest = null;
  }
}
