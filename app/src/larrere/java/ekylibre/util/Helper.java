package ekylibre.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ekylibre.zero.R;
import ekylibre.zero.home.Zero;

public class Helper {

    public static final SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRENCH);
    public static final SimpleDateFormat simpleISO8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter iso8601Parser = ISODateTimeFormat.dateTimeParser();
    public static final DateTimeFormatter iso8601Print = ISODateTimeFormat.dateTimeNoMillis();

    public static int getStringId(String key) {
        int resId =  Zero.getContext().getResources().getIdentifier(key, "string", Zero.getPkgName());
        if (resId != -1)
            return resId;
        else
            return R.string.translation_missing;
    }

    public static String getTranslation(String key) {
        int resId = Zero.getContext().getResources().getIdentifier(key, "string", Zero.getPkgName());
        return Zero.getContext().getString(resId != -1 ? resId : R.string.translation_missing);
    }
}
