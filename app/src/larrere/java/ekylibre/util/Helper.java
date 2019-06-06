package ekylibre.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ekylibre.zero.R;
import ekylibre.zero.home.Zero;

public class Helper {

    public static final SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRENCH);

    public static int getStringId(String key) {
        int resId =  Zero.getContext().getResources().getIdentifier(key, "string", Zero.getPkgName());
        if (resId != -1)
            return resId;
        else
            return R.string.translation_missing;
    }
}
