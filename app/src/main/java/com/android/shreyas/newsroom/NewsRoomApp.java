package com.android.shreyas.newsroom;

import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;
import com.facebook.FacebookSdk;

/**
 * Created by SHREYAS on 3/22/2016.
 */
public class NewsRoomApp extends android.app.Application {

    private boolean loggedIn = false;

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //Batch.Push.setGCMSenderId("629774082029");

        // TODO : switch to live Batch Api Key before shipping
        //Batch.setConfig(new Config("DEV56F0E64A4468E4985AA38F44025")); // devloppement
        // Batch.setConfig(new Config('56F0E64A432CB1A795149075253BF6')); // live
    }
}
