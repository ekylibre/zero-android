package ekylibre.zero.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ekylibre.zero.SettingsActivity;
import ekylibre.util.PermissionManager;

/**************************************
 * Created by pierre on 7/8/16.       *
 * ekylibre.zero for zero-android     *
 *************************************/

public class TodoListActivity {
    private final int CALENDAR_ID = 0;
    private final int TITLE = 1;
    private final int DESCRIPTION = 2;
    private final int DTSTART = 3;
    private final int DTEND = 4;
    private final int ALL_DAY = 5;
    private final int EVENT_LOCATION = 6;
    private final String TAG = "TodoListAct";
    private final int REQUEST_CALENDAR = 42;

    private ListView todoListView;
    private ArrayAdapter<String> adapter;
    private Context context;
    private Activity activity;

    /*
    ** This activity display all the tasks of the current day requesting the actual phone calendar
    ** Events are stock in ArrayAdapter between today 00h00 tomorrow 00h00
    ** CONSTANTS are use to get the part of data you want from adapter
    */

    public TodoListActivity(Context context, Activity activity, ListView foundListView) {
        this.todoListView = foundListView;
        this.context = context;
        this.activity = activity;

        List<TodoItem> todolist = createList();
        if (todolist == null)
            return;

        TodoAdapter adapter = new TodoAdapter(context, todolist);

        todoListView.setAdapter(adapter);

    }

    public void setListView(ListView listView) {
        todoListView = listView;
    }

    public List<TodoItem> createList() {
        Cursor curs;
        String startDateFormatted;
        String endDateFormatted;

        curs = getEventsFromLocal();
        if (curs == null)
            return (null);
        affEvents(curs);

        List<TodoItem> todoList = new ArrayList<TodoItem>();

        DateFormat formatterSTART = new SimpleDateFormat("HH:mm - ");
        DateFormat formatterEND = new SimpleDateFormat("HH:mm");

        
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(0);
        if (curs.moveToFirst())
        {
            do
            {
                if (newDay(curs.getLong(DTSTART), currentDate))
                {
                    currentDate.setTimeInMillis(curs.getLong(DTSTART));
                    todoList.add(new TodoItem(true, currentDate));
                }
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
        else
            todoList.add(new TodoItem(true, getDateOfDay()));
        return (todoList);
    }

    private boolean newDay(long date, Calendar currentDate)
    {
        if (currentDate.getTimeInMillis() == 0
                || getDayFromMillis(date) != currentDate.get(Calendar.DAY_OF_YEAR))
            return (true);
        else
            return (false);
    }

    /*
    ** Return new instance of Calendar containing current date
    */
    public Calendar getDateOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

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
    public Calendar getDateOfTomorrow() {
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

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

    public Calendar getDateOfNextMonth() {
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = 0;
        minute = 0;
        second = 0;

        calendar.set(year, month, day, hour, minute, second);
        calendar.add(Calendar.MONTH, 1);
        Log.d(TAG, "NEXT MONTH DATE = " + calendar.getTime().toString());
        return (calendar);
    }

    /*
    ** Set fields we want to request on getEvents from phone calendar
    */
    private String[] setProjection() {
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
    private String setSelection(Calendar startTime, Calendar endTime) {
        return ("(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() +
                " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))");
    }

    /*
    ** Return Instance of Calendar with a Date passed by argument
    */
    private Calendar setTime(Date date) {
        Calendar time;

        time = Calendar.getInstance();
        time.setTime(date);
        return (time);
    }

    /*
    ** Get events from Phone calendar with value set as we want on methods call :
    ** setSelection (Core of the request)
    ** setProjection (Selection of fields to request)
    */
    @TargetApi(23)
    public Cursor getEventsFromLocal() {
        Context context;
        ContentResolver contentResolver;
        Calendar startTime;
        Calendar endTime;
        String[] projection;
        String selection;
        Cursor returnCurs = null;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.context);
        if (!pref.getBoolean(SettingsActivity.PREF_SYNC_CALENDAR, false))
            return (null);
        Log.d(TAG, "Getting events from local calendar");
        projection = setProjection();
        startTime = getDateOfDay();
        endTime = getDateOfNextMonth();
        startTime.setTimeInMillis(startTime.getTimeInMillis() - 1000);
        selection = setSelection(startTime, endTime);

        context = this.context;
        contentResolver = context.getContentResolver();
        if (!PermissionManager.calendarPermissions(context, this.activity))
            return (null);

        returnCurs = contentResolver.query(CalendarContract.Events.CONTENT_URI,
                projection,
                selection,
                null,
                CalendarContract.Events.DTSTART);
        return (returnCurs);
    }

    public void         affEvents(Cursor curs)
    {
        int             it;

        it = 0;
        if (curs == null)
            curs = getEventsFromLocal();
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

    public int getDayFromMillis(long time)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return (cal.get(Calendar.DAY_OF_YEAR));
    }
}

