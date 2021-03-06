/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.widget.rxwidget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.baidaojuhe.library.baidaolibrary.widget.ItemButton;
import com.jakewharton.rxbinding.internal.Functions;
import com.jakewharton.rxbinding.widget.TextViewEditorActionEvent;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link TextView}.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class RxItemButton {
    /**
     * Create an observable of editor actions on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
     * observe actions. Only one observable can be used for a view at a time.
     */
    @CheckResult
    @NonNull
    public static Observable<Integer> editorActions(@NonNull ItemButton view) {
        checkNotNull(view, "view == null");
        return editorActions(view, Functions.FUNC1_ALWAYS_TRUE);
    }

    /**
     * Create an observable of editor actions on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
     * observe actions. Only one observable can be used for a view at a time.
     *
     * @param handled Function invoked each occurrence to determine the return value of the
     *                underlying {@link TextView.OnEditorActionListener}.
     */
    @CheckResult
    @NonNull
    public static Observable<Integer> editorActions(@NonNull ItemButton view,
                                                    @NonNull Func1<? super Integer, Boolean> handled) {
        checkNotNull(view, "view == null");
        checkNotNull(handled, "handled == null");
        return Observable.create(new ItemButtonEditorActionOnSubscribe(view, handled));
    }

    /**
     * Create an observable of editor action events on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
     * observe actions. Only one observable can be used for a view at a time.
     */
    @CheckResult
    @NonNull
    public static Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull ItemButton view) {
        checkNotNull(view, "view == null");
        return editorActionEvents(view, Functions.FUNC1_ALWAYS_TRUE);
    }

    /**
     * Create an observable of editor action events on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
     * observe actions. Only one observable can be used for a view at a time.
     *
     * @param handled Function invoked each occurrence to determine the return value of the
     *                underlying {@link TextView.OnEditorActionListener}.
     */
    @CheckResult
    @NonNull
    public static Observable<TextViewEditorActionEvent> editorActionEvents(@NonNull ItemButton view,
                                                                           @NonNull Func1<? super TextViewEditorActionEvent, Boolean> handled) {
        checkNotNull(view, "view == null");
        checkNotNull(handled, "handled == null");
        return Observable.create(new ItemButtonEditorActionEventOnSubscribe(view, handled));
    }

    /**
     * Create an observable of character sequences for text changes on {@code view}.
     * <p>
     * <em>Warning:</em> Values emitted by this observable are <b>mutable</b> and owned by the host
     * {@code TextView} and thus are <b>not safe</b> to cache or delay reading (such as by observing
     * on a different thread). If you want to cache or delay reading the items emitted then you must
     * map values through a function which calls {@link String#valueOf} or
     * {@link CharSequence#toString() .toString()} to create a copy.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Note:</em> A value will be emitted immediately on subscribe.
     */
    @CheckResult
    @NonNull
    public static Observable<CharSequence> textChanges(@NonNull ItemButton view) {
        checkNotNull(view, "view == null");
        return Observable.create(new ItemButtonTextOnSubscribe(view));
    }

    /**
     * Create an observable of text change events for {@code view}.
     * <p>
     * <em>Warning:</em> Values emitted by this observable contain a <b>mutable</b>
     * {@link CharSequence} owned by the host {@code TextView} and thus are <b>not safe</b> to cache
     * or delay reading (such as by observing on a different thread). If you want to cache or delay
     * reading the items emitted then you must map values through a function which calls
     * {@link String#valueOf} or {@link CharSequence#toString() .toString()} to create a copy.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Note:</em> A value will be emitted immediately on subscribe.
     */
    @CheckResult
    @NonNull
    public static Observable<ItemButtonTextChangeEvent> textChangeEvents(@NonNull ItemButton view) {
        checkNotNull(view, "view == null");
        return Observable.create(new ItemButtonTextChangeEventOnSubscribe(view));
    }

    /**
     * Create an observable of before text change events for {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Note:</em> A value will be emitted immediately on subscribe.
     */
    @CheckResult
    @NonNull
    public static Observable<ItemButtonBeforeTextChangeEvent> beforeTextChangeEvents(@NonNull ItemButton view) {
        checkNotNull(view, "view == null");
        return Observable.create(new ItemButtonBeforeTextChangeEventOnSubscribe(view));
    }

    /**
     * Create an observable of after text change events for {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Note:</em> A value will be emitted immediately on subscribe.
     */
    @CheckResult
    @NonNull
    public static Observable<ItemButtonAfterTextChangeEvent> afterTextChangeEvents(@NonNull ItemButton view) {
        checkNotNull(view, "view == null");
        return Observable.create(new ItemButtonAfterTextChangeEventOnSubscribe(view));
    }

    /**
     * An action which sets the text property of {@code view} with character sequences.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super CharSequence> promptText(@NonNull final ItemButton view) {
        checkNotNull(view, "view == null");
        return (Action1<CharSequence>) view::setPromptText;
    }

    /**
     * An action which sets the text property of {@code view} string resource IDs.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Integer> promptTextRes(@NonNull final ItemButton view) {
        checkNotNull(view, "view == null");
        return (Action1<Integer>) view::setPromptText;
    }

    /**
     * An action which sets the text property of {@code view} with character sequences.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super CharSequence> valueText(@NonNull final ItemButton view) {
        checkNotNull(view, "view == null");
        return (Action1<CharSequence>) view::setValueText;
    }

    /**
     * An action which sets the text property of {@code view} string resource IDs.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Integer> valueTextRes(@NonNull final ItemButton view) {
        checkNotNull(view, "view == null");
        return (Action1<Integer>) view::setValueText;
    }

    /**
     * An action which sets the hint property of {@code view} with character sequences.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super CharSequence> hint(@NonNull final ItemButton view) {
        checkNotNull(view, "view == null");
        return (Action1<CharSequence>) view::setHint;
    }

    /**
     * An action which sets the hint property of {@code view} string resource IDs.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Integer> hintRes(@NonNull final ItemButton view) {
        checkNotNull(view, "view == null");
        return (Action1<Integer>) view::setHint;
    }

    /**
     * An action which sets the color property of {@code view} with color integer.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Integer> promptColor(@NonNull final ItemButton view) {
        checkNotNull(view, "view == null");
        return (Action1<Integer>) view::setPromptTextColor;
    }

    /**
     * An action which sets the color property of {@code view} with color integer.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     */
    @CheckResult
    @NonNull
    public static Action1<? super Integer> valueColor(@NonNull final ItemButton view) {
        checkNotNull(view, "view == null");
        return (Action1<Integer>) view::setValueTextColor;
    }

    private RxItemButton() {
        throw new AssertionError("No instances.");
    }
}