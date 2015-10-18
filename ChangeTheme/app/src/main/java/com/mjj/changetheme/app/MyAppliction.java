package com.mjj.changetheme.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.mjj.changetheme.R;
import com.mjj.changetheme.data.Theme;

import java.util.Map;


public class MyAppliction extends Application {

    private static int MyTheme = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        getLocalData();
    }

    private void getLocalData() {
        SharedPreferences sharedPreferences = getSharedPreferences("theme",MODE_PRIVATE);
        MyTheme = sharedPreferences.getInt("theme",0);

    }
    public static int getThemeValue(){
        return MyTheme;
    }

    public static void setThemeValue(int themeValue){
        MyTheme = themeValue;
    }

    public static int getThemeResources(){
        switch (MyTheme){
            case Theme.DAYTHEME:
                return Theme.RESOURCES_DAYTHEME;
            case Theme.NIGHTTHEME:
                return Theme.RESOURCES_NIGHTTHEME;
            default:
                return Theme.RESOURCES_DAYTHEME;
        }
    }


}
