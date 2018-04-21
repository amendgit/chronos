package com.amendgit.chronos;

import android.content.*;
import android.graphics.*;
import android.util.*;

/**
 * Created by jash on 16/04/2018.
 * github: https://github.com/amendgit
 * email: shijian0912@163.com
 * homepage: http:www.amendgit.com
 */
public class ColorUtils {
    public static int setMinValue(int color, float newValue) {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = Math.max(hsv[2], newValue);
        return Color.HSVToColor(hsv);
    }
}
