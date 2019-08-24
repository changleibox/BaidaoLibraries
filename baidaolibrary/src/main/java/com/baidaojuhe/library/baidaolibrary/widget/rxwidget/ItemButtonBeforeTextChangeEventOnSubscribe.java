/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import android.text.Editable;
import android.text.TextWatcher;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

final class ItemButtonBeforeTextChangeEventOnSubscribe
    implements Observable.OnSubscribe<ItemButtonBeforeTextChangeEvent> {
  final ItemButton view;

  ItemButtonBeforeTextChangeEventOnSubscribe(ItemButton view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ItemButtonBeforeTextChangeEvent> subscriber) {
    final TextWatcher watcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(ItemButtonBeforeTextChangeEvent.create(view, s, start, count, after));
        }
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(Editable s) {
      }
    };
    view.addTextChangedListener(watcher);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeTextChangedListener(watcher);
      }
    });

    // Emit initial value.
    subscriber.onNext(ItemButtonBeforeTextChangeEvent.create(view, view.getValue(), 0, 0, 0));
  }
}