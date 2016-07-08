package ekylibre.zero;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**************************************
 * Created by pierre on 7/7/16.       *
 * ekylibre.zero for zero-android     *
 *************************************/

public class CalendarActivity extends Activity
{
    public long date;
    private Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        CalendarView agenda;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        agenda = (CalendarView) findViewById(R.id.calendarView);
        this.calendar = Calendar.getInstance();
        date = calendar.getTimeInMillis();
        affEvents();

    }

    public Date getDateOfDay()
    {
        Date    date;

        date = calendar.getTime();
        return (date);
    }

    public Date getDateOfTomorrow()
    {
        Calendar    calendar = Calendar.getInstance();
        Date        date;

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        date = calendar.getTime();
        return (date);
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

    public Cursor       getEvents()
    {
        Context         context;
        ContentResolver contentResolver;
        Calendar        startTime;
        Calendar        endTime;
        String[]        projection = setProjection();
        String          selection;
        Cursor          returnCurs;

        startTime = setTime(getDateOfDay());
        endTime = setTime(getDateOfTomorrow());
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
