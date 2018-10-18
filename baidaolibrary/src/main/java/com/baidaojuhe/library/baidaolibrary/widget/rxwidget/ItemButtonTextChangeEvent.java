/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;
import com.jakewharton.rxbinding.view.ViewEvent;

/**
 * A text-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link android.content.Context}.
 */
public final class ItemButtonTextChangeEvent extends ViewEvent<ItemButton> {
  @CheckResult
  @NonNull
  public static ItemButtonTextChangeEvent create(@NonNull ItemButton view, @NonNull CharSequence text,
                                                 int start, int before, int count) {
    return new ItemButtonTextChangeEvent(view, text, start, before, count);
  }

  private final CharSequence text;
  private final int start;
  private final int before;
  private final int count;

  private ItemButtonTextChangeEvent(@NonNull ItemButton view, @NonNull CharSequence text, int start,
                                    int before, int count) {
    super(view);
    this.text = text;
    this.start = start;
    this.before = before;
    this.count = count;
  }

  @NonNull
  public CharSequence text() {
    return text;
  }

  public int start() {
    return start;
  }

  public int before() {
    return before;
  }

  public int count() {
    return count;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ItemButtonTextChangeEvent)) return false;
    ItemButtonTextChangeEvent other = (ItemButtonTextChangeEvent) o;
    return other.view() == view()
        && text.equals(other.text)
        && start == other.start
        && before == other.before
        && count == other.count;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + text.hashCode();
    result = result * 37 + start;
    result = result * 37 + before;
    result = result * 37 + count;
    return result;
  }

  @Override public String toString() {
    return "ItemButtonTextChangeEvent{text="
        + text
        + ", start="
        + start
        + ", before="
        + before
        + ", count="
        + count
        + ", view="
        + view()
        + '}';
  }
}