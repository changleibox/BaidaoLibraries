/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import android.text.Editable;
import android.text.TextWatcher;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;
import com.jakewharton.rxbinding.internal.Preconditions;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

public final class ItemButtonTextOnSubscribe implements Observable.OnSubscribe<CharSequence> {
    final ItemButton view;

    ItemButtonTextOnSubscribe(ItemButton view) {
        this.view = view;
    }

    @Override
    public void call(final Subscriber<? super CharSequence> subscriber) {
        Preconditions.checkUiThread();

        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        view.addTextChangedListener(watcher);

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                view.removeTextChangedListener(watcher);
            }
        });

        // Emit initial value.
        subscriber.onNext(view.getValue());
    }
}