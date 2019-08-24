/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.text.Editable;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;
import com.jakewharton.rxbinding.view.ViewEvent;

/**
 * An after text-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link android.content.Context}.
 */
public final class ItemButtonAfterTextChangeEvent extends ViewEvent<ItemButton> {
  @CheckResult
  @NonNull
  public static ItemButtonAfterTextChangeEvent create(@NonNull ItemButton view,
                                                      @NonNull Editable editable) {
    return new ItemButtonAfterTextChangeEvent(view, editable);
  }

  private final Editable editable;

  private ItemButtonAfterTextChangeEvent(@NonNull ItemButton view, @NonNull Editable editable) {
    super(view);
    this.editable = editable;
  }

  @NonNull
  public Editable editable() {
    return editable;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ItemButtonAfterTextChangeEvent)) return false;
    ItemButtonAfterTextChangeEvent other = (ItemButtonAfterTextChangeEvent) o;
    return other.view() == view()
        && editable.equals(other.editable);
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + editable.hashCode();
    return result;
  }

  @Override public String toString() {
    return "ItemButtonAfterTextChangeEvent{editable="
        + editable
        + ", view="
        + view()
        + '}';
  }

}