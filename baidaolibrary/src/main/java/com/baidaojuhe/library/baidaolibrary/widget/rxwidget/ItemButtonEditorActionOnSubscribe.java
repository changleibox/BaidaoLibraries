/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import android.view.KeyEvent;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

final class ItemButtonEditorActionOnSubscribe implements Observable.OnSubscribe<Integer> {
  final ItemButton view;
  final Func1<? super Integer, Boolean> handled;

  ItemButtonEditorActionOnSubscribe(ItemButton view, Func1<? super Integer, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (handled.call(actionId)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(actionId);
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