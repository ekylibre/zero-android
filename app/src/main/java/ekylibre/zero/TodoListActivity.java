package ekylibre.zero;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**************************************
 * Created by pierre on 7/8/16.     *
 * ekylibre.zero for zero-android*
 *************************************/

public class TodoListActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);

        affEvents();
    }

    public Calendar getDateOfDay()
    {
        Calendar    calendar = Calendar.getInstance();
        int         year;
        int         month;
        int         day;
        int         hour;
        int         minute;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = 0;
        minute = 0;

        calendar.set(year, month, day, hour, minute);
        System.out.println(calendar.getTime());
        return (calendar);
    }

    public Calendar getDateOfTomorrow()
    {
        Calendar    calendar = Calendar.getInstance();
        int         year;
        int         month;
        int         day;
        int         hour;
        int         minute;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = 0;
        minute = 0;

        calendar.set(year, month, day, hour, minute);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println(calendar.getTime());
        return (calendar);
    }

    private String[]    setProjection()
    {
        return (new String[]
                {
                        CalendarContract.Events.CALENDAR_ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DESCRIPTION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                        CalendarContract.Events.ALL_DAY,
                        CalendarContract.Events.EVENT_LOCATION
                });
    }

    private String  setSelection(Calendar startTime, Calendar endTime)
    {
        return ("(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() +
                " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))");
    }

    private Calendar    setTime(Date date)
    {
        Calendar        time;

        time = Calendar.getInstance();
        time.setTime(date);
        return (time);
    }

    public Cursor getEvents()
    {
        Context context;
        ContentResolver contentResolver;
        Calendar        startTime;
        Calendar        endTime;
        String[]        projection = setProjection();
        String          selection;
        Cursor          returnCurs;

        startTime = getDateOfDay();
        endTime = getDateOfTomorrow();
        selection = setSelection(startTime, endTime);

        context = this.getBaseContext();
        contentResolver = context.getContentResolver();
        returnCurs = contentResolver.query(CalendarContract.Events.CONTENT_URI,
                projection, selection, null, null);
        return (returnCurs);
    }

    public void         affEvents()
    {
        Cursor          curs;

        curs = getEvents();
        if (curs.moveToFirst())
        {
            do
            {
                Toast.makeText(this.getApplicationContext(),
                        "Title: " + curs.getString(1),
                        Toast.LENGTH_LONG).show();
            } while (curs.moveToNext());
        }
    }
}

