package com.traidev.masterCoder;

import android.app.Application;

import com.onesignal.OneSignal;

public class NotificationsOneSignal extends Application {

    private static NotificationsOneSignal mInstance;


    public NotificationsOneSignal()
    {
        mInstance=this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance=this;
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    }

    public static synchronized NotificationsOneSignal getInstance()
    {
        return mInstance;
    }
}
