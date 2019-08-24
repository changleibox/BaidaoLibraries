/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;
import com.jakewharton.rxbinding.view.ViewEvent;

/**
 * A before text-change event on a view.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link android.content.Context}.
 */
public final class ItemButtonBeforeTextChangeEvent extends ViewEvent<ItemButton> {
  @CheckResult
  @NonNull
  public static ItemButtonBeforeTextChangeEvent create(@NonNull ItemButton view,
                                                       @NonNull CharSequence text, int start, int count, int after) {
    return new ItemButtonBeforeTextChangeEvent(view, text, start, count, after);
  }

  private final CharSequence text;
  private final int start;
  private final int count;
  private final int after;

  private ItemButtonBeforeTextChangeEvent(@NonNull ItemButton view, @NonNull CharSequence text,
                                          int start, int count, int after) {
    super(view);
    this.text = text;
    this.start = start;
    this.count = count;
    this.after = after;
  }

  @NonNull
  public CharSequence text() {
    return text;
  }

  public int start() {
    return start;
  }

  public int count() {
    return count;
  }

  public int after() {
    return after;
  }

  @Override public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof ItemButtonAfterTextChangeEvent)) return false;
    ItemButtonBeforeTextChangeEvent other = (ItemButtonBeforeTextChangeEvent) o;
    return other.view() == view()
        && text.equals(other.text)
        && start == other.start
        && count == other.count
        && after == other.after;
  }

  @Override public int hashCode() {
    int result = 17;
    result = result * 37 + view().hashCode();
    result = result * 37 + text.hashCode();
    result = result * 37 + start;
    result = result * 37 + count;
    result = result * 37 + after;
    return result;
  }

  @Override public String toString() {
    return "ItemButtonBeforeTextChangeEvent{text="
        + text
        + ", start="
        + start
        + ", count="
        + count
        + ", after="
        + after
        + ", view="
        + view()
        + '}';
  }

}