package ekylibre.util;

import android.content.Context;

import ekylibre.zero.R;
import ekylibre.zero.home.Zero;

public class Translate {

    public static int getStringId(Context context, String key) {
        int resId =  context.getResources().getIdentifier(key, "string", Zero.getPkgName());
        if (resId != -1)
            return resId;
        else
            return R.string.translation_missing;
    }
}
