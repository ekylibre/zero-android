package ekylibre.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

import ekylibre.zero.R;
import ekylibre.zero.home.Zero;

public class Helper {

    public static final SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    public static final SimpleDateFormat iso8601date = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat simpleISO8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    public static Date parseISO8601toDate(String string) {

        DateTimeFormatter parser = ISODateTimeFormat.dateTime();
        return parser.parseDateTime(string).toDate();
    }
}
