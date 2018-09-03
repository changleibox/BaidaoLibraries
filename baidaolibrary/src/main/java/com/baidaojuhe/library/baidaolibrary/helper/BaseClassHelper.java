/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.helper;

import android.content.Intent;

import com.annimon.stream.Stream;
import com.baidaojuhe.library.baidaolibrary.impl.ActivityListener;

import net.box.app.library.IContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by box on 2017/3/24.
 * <p>
 * BaseActivity辅助类
 */

@SuppressWarnings("WeakerAccess")
public class BaseClassHelper {

    private static final Map<IContext, List<ActivityListener>> BASE_CLASSES = Collections.synchronizedMap(new HashMap<>());

    public static void addActivityListener(IContext aClass, ActivityListener listener) {
        List<ActivityListener> activityListeners = BASE_CLASSES.get(aClass);
        if (activityListeners == null) {
            activityListeners = new ArrayList<>();
        }
        if (!activityListeners.contains(listener)) {
            activityListeners.add(listener);
        }
        BASE_CLASSES.put(aClass, activityListeners);
    }

    public static void removeActivityListener(IContext aClass, ActivityListener listener) {
        List<ActivityListener> activityListeners = BASE_CLASSES.get(aClass);
        if (activityListeners != null) {
            activityListeners.remove(listener);
            if (activityListeners.isEmpty()) {
                removeActivityListener(aClass);
            } else {
                BASE_CLASSES.put(aClass, activityListeners);
            }
        }
    }

    public static void removeActivityListener(IContext aClass) {
        BASE_CLASSES.remove(aClass);
    }

    public static void onActivityResult(IContext activity, int requestCode, int resultCode, Intent data) {
        List<ActivityListener> activityListeners = BASE_CLASSES.get(activity);
        if (activityListeners != null) {
            Stream.of(activityListeners).forEach(value -> value.onActivityResult(requestCode, resultCode, data));
        }
    }

    public static void onActivityReenter(IContext activity, int resultCode, Intent data) {
        List<ActivityListener> activityListeners = BASE_CLASSES.get(activity);
        if (activityListeners != null) {
            Stream.of(activityListeners).forEach(value -> value.onActivityReenter(resultCode, data));
        }
    }

}
