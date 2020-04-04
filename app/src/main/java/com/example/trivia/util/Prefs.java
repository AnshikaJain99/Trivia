package com.example.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity )
    {
        this.preferences=activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighScore(int score)
    {
//        preferences.getInt("HS",0);
        preferences.edit().putInt("HS",score).apply();
    }
    public int getHighScore()
    {
        return preferences.getInt("HS",0);
    }

    public void saveCurr(int cur)
    {
        preferences.edit().putInt("current",cur).apply();
    }
    public int getCurr()
    {
        return preferences.getInt("current",0);
    }
}
