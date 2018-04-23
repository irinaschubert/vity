package ch.ffhs.vity.vity.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityRegistry {
    private static List<Activity> _activities;

    public static void register(Activity activity) {
        if(_activities == null) {
            _activities = new ArrayList<Activity>();
        }
        _activities.add(activity);
    }

    public static void finishAll() {
        for (Activity activity : _activities) {
            activity.finish();
        }
    }
}