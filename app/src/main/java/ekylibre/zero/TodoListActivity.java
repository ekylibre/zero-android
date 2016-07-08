package ekylibre.zero;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**************************************
 * Created by pierre on 7/8/16.       *
 * ekylibre.zero for zero-android     *
 *************************************/

public class TodoListActivity extends Activity
{
    private final int    CALENDAR_ID    = 0;
    private final int    TITLE          = 1;
    private final int    DESCRIPTION    = 2;
    private final int    DTSTART        = 3;
    private final int    DTEND          = 4;
    private final int    ALL_DAY        = 5;
    private final int    EVENT_LOCATION = 6;
    private final String TAG            = "TodoListAct";

    private ListView                todoList;
    private ArrayAdapter<String>    adapter;

    /**
    *** TODO ==> Get event at 00h00 do not work !
    ***
    **/

    /*
    ** This activity display all the tasks of the day requesting the actual phone calendar
    ** Events are stock in ArrayAdapter between today 00h00 tomorrow 00h00
    ** CONSTANTS are use to get the part of data you want from adapter
    */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);

        todoList = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(TodoListActivity.this, android.R.layout.simple_list_item_1);

        adapter = fillAdapter(adapter);
        todoList.setAdapter(adapter);
    }

    /*
    ** Return new instance of Calendar containing current date
    */
    public Calendar getDateOfDay()
    {
        Calendar    calendar = Calendar.getInstance();
        int         year;
        int         month;
        int         day;
        int         hour;
        int         minute;
        int         second;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = 0;
        minute = 0;
        second = 0;

        calendar.set(year, month, day, hour, minute, second);
        Log.d(TAG, "TODAY DATE = " + calendar.getTime());
        return (calendar);
    }

    /*
    ** Return new instance of Calendar containing tomorrow date
    */
    public Calendar getDateOfTomorrow()
    {
        Calendar    calendar = Calendar.getInstance();
        int         year;
        int         month;
        int         day;
        int         hour;
        int         minute;
        int         second;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = 0;
        minute = 0;
        second = 0;

        calendar.set(year, month, day, hour, minute, second);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Log.d(TAG, "TOMORROW DATE = " + calendar.getTime());
        return (calendar);
    }

    /*
    ** Fill adapter with Events got from getEvents
    */
    private ArrayAdapter<String>    fillAdapter(ArrayAdapter<String> adapter)
    {
        Cursor                      curs;

        curs = getEvents();
        if (curs.moveToFirst())
        {
            do
            {
                adapter.add(curs.getString(TITLE));
            } while (curs.moveToNext());
        }
        return (adapter);
    }

    /*
    ** Set fields we want to request on getEvents from phone calendar
    */
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

    /*
    ** Core of the request send to phone calendar
    */
    private String  setSelection(Calendar startTime, Calendar endTime)
    {
        return ("(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() +
                " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))");
    }

    /*
    ** Return Instance of Calendar with a Date passed by argument
    */
    private Calendar    setTime(Date date)
    {
        Calendar        time;

        time = Calendar.getInstance();
        time.setTime(date);
        return (time);
    }

    /*
    ** Get events from Phone calendar with value set as we want on methods call :
    ** setSelection (Core of the request)
    ** setProjection (Selection of fields to request)
    */
    public Cursor getEvents()
    {
        Context         context;
        ContentResolver contentResolver;
        Calendar        startTime;
        Calendar        endTime;
        String[]        projection;
        String          selection;
        Cursor          returnCurs;

        Log.d(TAG, "Getting events from local calendar");
        projection = setProjection();
        startTime = getDateOfDay();
        endTime = getDateOfTomorrow();
        startTime.setTimeInMillis(startTime.getTimeInMillis() - 1000);
        selection = setSelection(startTime, endTime);


        context = this.getBaseContext();
        contentResolver = context.getContentResolver();
        returnCurs = contentResolver.query(CalendarContract.Events.CONTENT_URI,
                projection, selection, null, CalendarContract.Events.DTSTART);
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

