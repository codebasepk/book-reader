package com.joanzapata;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class Helpers {

    public static SharedPreferences getPreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(AppGlobals.getContext());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void savePreviousOpenedFile(String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(AppGlobals.LAST_FILE_KEY, value).apply();
    }

    public static String getPreviousSavedFile() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(AppGlobals.LAST_FILE_KEY, "");
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void saveCurrentPage(String key, int value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putInt(key, value).apply();
        System.out.println("pages saved");
    }

    public static int getLastLoadedPage(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getInt(key, 0);
    }


}
