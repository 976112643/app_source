package com.wq.support.util.badger.impl;

import java.util.Arrays;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.wq.support.util.badger.Badger;
import com.wq.support.util.badger.ShortcutBadgeException;
import com.wq.support.util.badger.util.BroadcastHelper;

/**
 * @author Leo Lin
 * Deprecated, LG devices will use DefaultBadger
 */
@Deprecated
public class LGHomeBadger implements Badger {

    private static final String INTENT_ACTION = "android.intent.action.BADGE_COUNT_UPDATE";
    private static final String INTENT_EXTRA_BADGE_COUNT = "badge_count";
    private static final String INTENT_EXTRA_PACKAGENAME = "badge_count_package_name";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "badge_count_class_name";

    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) throws ShortcutBadgeException {
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_EXTRA_BADGE_COUNT, badgeCount);
        intent.putExtra(INTENT_EXTRA_PACKAGENAME, componentName.getPackageName());
        intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, componentName.getClassName());
        if(BroadcastHelper.canResolveBroadcast(context, intent)) {
            context.sendBroadcast(intent);
        } else {
            throw new ShortcutBadgeException("unable to resolve intent: " + intent.toString());
        }
    }

    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "com.lge.launcher",
                "com.lge.launcher2"
        );
    }
}
