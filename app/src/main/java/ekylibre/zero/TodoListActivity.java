package ekylibre.zero;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**************************************
 * Created by pierre on 7/8/16.       *
 * ekylibre.zero for zero-android     *
 *************************************/

public class TodoListActivity
{
    private final int    CALENDAR_ID    = 0;
    private final int    TITLE          = 1;
    private final int    DESCRIPTION    = 2;
    private final int    DTSTART        = 3;
    private final int    DTEND          = 4;
    private final int    ALL_DAY        = 5;
    private final int    EVENT_LOCATION = 6;
    private final String TAG            = "TodoListAct";

    private ListView                todoListView;
    private ArrayAdapter<String>    adapter;
    private Context                 context;


    /*
    ** This activity display all the tasks of the day requesting the actual phone calendar
    ** Events are stock in ArrayAdapter between today 00h00 tomorrow 00h00
    ** CONSTANTS are use to get the part of data you want from adapter
    */

    public TodoListActivity(Context context, ListView foundListView)
    {
        this.todoListView = foundListView;
        this.context = context;
        List<TodoItem> todolist = createList();

        TodoAdapter adapter = new TodoAdapter(context, todolist);
        todoListView.setAdapter(adapter);
    }

    public void setListView(ListView listView)
    {
        todoListView = listView;
    }

    public List<TodoItem>          createList()
    {
        Cursor                      curs;
        String                      startDateFormatted;
        String                      endDateFormatted;

        curs = getEvents();
        affEvents(curs);

        List<TodoItem> todoList = new ArrayList<TodoItem>();

        DateFormat formatterSTART = new SimpleDateFormat("HH:mm - ");
        DateFormat formatterEND = new SimpleDateFormat("HH:mm");

        if (curs.moveToFirst())
        {
            do
            {
                Date startDate = new Date(curs.getLong(DTSTART));
                Date endDate = new Date(curs.getLong(DTEND));
                startDateFormatted = formatterSTART.format(startDate);
                if (curs.getInt(ALL_DAY) == 0)
                    endDateFormatted = formatterEND.format(endDate);
                else
                    endDateFormatted = "00h00";
                todoList.add(new TodoItem(startDateFormatted, endDateFormatted, curs.getString(TITLE), curs.getString(DESCRIPTION)));
            } while (curs.moveToNext());
        }
        return (todoList);
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


        context = this.context;
        contentResolver = context.getContentResolver();
        returnCurs = contentResolver.query(CalendarContract.Events.CONTENT_URI,
                projection, selection, null, CalendarContract.Events.DTSTART);
        return (returnCurs);
    }

    public void         affEvents(Cursor curs)
    {
        int             it;

        it = 0;
        if (curs == null)
            curs = getEvents();
        if (curs.moveToFirst())
        {
            do
            {
                ++it;
                Log.d(TAG, "==========");
                Log.d(TAG, "ENVENT NUM " + it);
                Log.d(TAG, "ID = " + curs.getString(CALENDAR_ID));
                Log.d(TAG, "DESC = " + curs.getString(DESCRIPTION));
                Log.d(TAG, "DATE START = " + curs.getString(DTSTART));
                Log.d(TAG, "DATE END = " + curs.getString(DTEND));
                Log.d(TAG, "ALL DAY = " + curs.getString(ALL_DAY));
                Log.d(TAG, "EVT LOCATION = " + curs.getString(EVENT_LOCATION));
                Log.d(TAG, "==========");
            } while (curs.moveToNext());
        }
    }
}

