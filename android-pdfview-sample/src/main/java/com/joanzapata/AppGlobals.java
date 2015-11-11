package com.joanzapata;

import android.app.Application;
import android.content.Context;


public class AppGlobals extends Application {

    private static Context sContext;
    public static final String LAST_FILE_KEY = "last_file_key";
    public static String fileName;
    public static boolean listFilesOpen = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
