/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import android.view.KeyEvent;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;
import com.jakewharton.rxbinding.widget.TextViewEditorActionEvent;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ItemButtonEditorActionEventOnSubscribe
    implements Observable.OnSubscribe<TextViewEditorActionEvent> {
  final ItemButton view;
  final Func1<? super TextViewEditorActionEvent, Boolean> handled;

  ItemButtonEditorActionEventOnSubscribe(ItemButton view,
                                         Func1<? super TextViewEditorActionEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super TextViewEditorActionEvent> subscriber) {
    checkUiThread();

    TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
        TextViewEditorActionEvent event = TextViewEditorActionEvent.create(v, actionId, keyEvent);
        if (handled.call(event)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(event);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnEditorActionListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnEditorActionListener(null);
      }
    });
  }
}