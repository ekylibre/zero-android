package ekylibre.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**************************************
 * Created by pierre on 9/1/16.      *
 * ekylibre.util for zero-android    *
 *************************************/
public final class DateConstant
{
    public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /*
    ** @return : Current date formatted in ISO_8601
    */
    static public String getCurrentDateFormatted()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DateConstant.ISO_8601);

        return (dateFormatter.format(cal.getTime()));
    }
}
