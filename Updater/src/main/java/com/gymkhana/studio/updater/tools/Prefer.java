package com.gymkhana.studio.updater.tools;

import android.content.SharedPreferences;
import android.content.Context;

public class Prefer {
    private static SharedPreferences preferences;
    private static String fileName;
    private static Context context;
    public static void init(Context context){
        Prefer.context = context;
        Prefer.fileName = context.getPackageName();
    }
    public static void init(Context context, String fileName){
        Prefer.context = context;
        Prefer.fileName = fileName;
    }
    public static synchronized SharedPreferences getSharedPreferences(){
        if(context == null){
            throw new NullPointerException();
        }
        if(preferences == null){
            preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        return preferences;
    }
    public static void set(String key, Object value){
        if(key==null || value==null){
            throw new NullPointerException();
        }
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        if(value instanceof Integer){
            editor.putInt(key, (int)value);
        }
        else if(value instanceof String){
            editor.putString(key, (String)value);
        }
        else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean)value);
        }
        else if(value instanceof Float){
            editor.putFloat(key, (Float)value);
        }
        else if(value instanceof Long){
            editor.putLong(key, (Long)value);
        }
        else{
        }
        editor.commit();
    }
    public static <T> T get(String key, T defaultValue){
        if (key == null){
            throw new NullPointerException();
        }
        SharedPreferences preference = getSharedPreferences();
        Class classType = defaultValue.getClass();
        Object returnValue = null;
        if(defaultValue instanceof Integer){
            returnValue = preference.getInt(key, ((Integer) defaultValue).intValue());
        }
        else if(defaultValue instanceof String){
            returnValue = preference.getString(key, ((String)defaultValue));
        }
        else if(defaultValue instanceof Boolean){
            returnValue = preference.getBoolean(key, (Boolean)defaultValue);
        }
        else if(defaultValue instanceof Float){
            returnValue = preference.getFloat(key, (Float)defaultValue);
        }
        else if(defaultValue instanceof Long){
            returnValue = preference.getLong(key, (Long)defaultValue);
        }
        else{
            returnValue = preference.getString(key, (String)defaultValue);
        }
        return (T)classType.cast(returnValue);
    }
}
