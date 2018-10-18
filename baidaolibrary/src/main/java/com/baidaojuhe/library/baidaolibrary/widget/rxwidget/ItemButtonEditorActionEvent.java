/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class ItemButtonEditorActionEvent extends ViewEvent<ItemButton> {
  @CheckResult
  @NonNull
  public static ItemButtonEditorActionEvent create(@NonNull ItemButton view, int actionId,
                                                   @NonNull KeyEvent keyEvent) {
    return new ItemButtonEditorActionEvent(view, actionId, keyEvent);
  }

  private final int actionId;
  private final KeyEvent keyEvent;

  private ItemButtonEditorActionEvent(@NonNull ItemButton view, int actionId,
                                      @NonNull KeyEvent keyEvent) {
    super(view);
    this.actionId = actionId;
    this.keyEvent = keyEvent;
  }

  public int actionId() {
    return actionId;
  }

  @NonNull
  public KeyEvent keyEvent() {
    return keyEvent;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ItemButtonEditorActionEvent)) return false;
    ItemButtonEditorActionEvent other = (ItemButtonEditorActionEvent) o;
    return other.view() == view() && other.actionId == actionId && other.keyEvent.equals(keyEvent);
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + actionId;
    result = result * 37 + keyEvent.hashCode();
    return result;
  }

  @Override public String toString() {
    return "ItemButtonEditorActionEvent{view="
        + view()
        + ", actionId="
        + actionId
        + ", keyEvent="
        + keyEvent
        + '}';
  }
}