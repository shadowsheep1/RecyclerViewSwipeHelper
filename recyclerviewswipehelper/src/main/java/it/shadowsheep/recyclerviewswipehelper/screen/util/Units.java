package it.shadowsheep.recyclerviewswipehelper.screen.util;

import android.content.Context;
import android.support.annotation.NonNull;

public final class Units {
    // https://developer.android.com/training/multiscreen/screendensities.html#TaskUseDP
    public static float px2dp(@NonNull final Context context, final float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return px / scale;
    }

    public static float dp2px(@NonNull final Context context, final float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale;
    }
}
