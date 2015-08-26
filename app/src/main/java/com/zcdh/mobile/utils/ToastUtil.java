package com.zcdh.mobile.utils;

import com.zcdh.mobile.app.ZcdhApplication;

import android.widget.Toast;

public class ToastUtil {

    public static void show(String text) {
        Toast.makeText(ZcdhApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String text) {
        Toast.makeText(ZcdhApplication.getInstance(), text, Toast.LENGTH_LONG).show();
    }

    public static void show(int strId) {
        Toast.makeText(ZcdhApplication.getInstance(), strId, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int strId) {
        Toast.makeText(ZcdhApplication.getInstance(), strId, Toast.LENGTH_LONG).show();
    }
}

