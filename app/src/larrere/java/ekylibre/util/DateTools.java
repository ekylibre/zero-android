package ekylibre.util;

import android.annotation.SuppressLint;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ekylibre.zero.R;

@SuppressLint("SimpleDateFormat")
public class DateTools {

    private static SimpleDateFormat SIMPLE_DATE = new SimpleDateFormat("EEE d MMMM yyyy");
    private static SimpleDateFormat DATETIME = new SimpleDateFormat("d MMMM à HH:mm");
    private static SimpleDateFormat DATE = new SimpleDateFormat("d MMM");
    private static SimpleDateFormat TIME = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat STANDARD_DISPLAY = new SimpleDateFormat("dd/MM/yyyy");


    public static String display(Context context, Calendar date) {

        // Get today instance and compare date
        final Calendar today = Calendar.getInstance();
        if (isSameDay(today, date))
            return StringUtils.capitalize(context.getString(R.string.today));

        // Compare with yesterday
        today.add(Calendar.DATE, -1);
        if (isSameDay(today, date))
            return StringUtils.capitalize(context.getString(R.string.yesterday));

        // In all other cases, return regular date
        return StringUtils.capitalize(SIMPLE_DATE.format(date.getTimeInMillis()));
    }

    private static boolean isSameDay(Calendar today, Calendar date) {
        return date.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && date.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    public static String displayDateTime(Date date) {
        return DATETIME.format(date);
    }

    public static String displayDate(Date date) {
        return DATE.format(date);
    }

    public static String displayTime(Date date) {
        return TIME.format(date);
    }
}
