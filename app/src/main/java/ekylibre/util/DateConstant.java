package ekylibre.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**************************************
 * Created by pierre on 9/1/16.      *
 * ekylibre.util for zero-android    *
 *************************************/

/*
** This class contains ISO_8601 format which is the standard used in this project.
** You can also call methods that deal with date formatted
*/
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
