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

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ItemButtonAfterTextChangeEventOnSubscribe
    implements Observable.OnSubscribe<ItemButtonAfterTextChangeEvent> {
  final ItemButton view;

  ItemButtonAfterTextChangeEventOnSubscribe(ItemButton view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ItemButtonAfterTextChangeEvent> subscriber) {
    checkUiThread();

    final TextWatcher watcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(Editable s) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(ItemButtonAfterTextChangeEvent.create(view, s));
        }
      }
    };
    view.addTextChangedListener(watcher);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeTextChangedListener(watcher);
      }
    });

    // Emit initial value.
    subscriber.onNext(ItemButtonAfterTextChangeEvent.create(view, view.getEditableText()));
  }
}