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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ekylibre.APICaller.Intervention;
import ekylibre.database.ZeroContract;
import ekylibre.util.AccountTool;
import ekylibre.util.DateConstant;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
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


    public final static int NONE_CALENDAR = 0;
    public final static int LOCAL_CALENDAR = 1;
    public final static int EKYLIBRE_CALENDAR = 2;


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

    public List<TodoItem> createList()
    {
        Cursor          cursLocal, cursRequested;

        cursLocal = getEventsFromLocal();
        cursRequested = getEventsFromEK();

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(0);
        ArrayList<TodoItem> compiledList = getListCompact(cursLocal, cursRequested);
        int i = 0;
        compiledList.add(0, new TodoItem(true, getDateOfDay()));
        if (compiledList.get(1).getDate().getTimeInMillis() >= getDateOfTomorrow().getTimeInMillis())
        {
            i++;
            compiledList.add(1, new TodoItem(true, context.getResources().getString(R.string.no_event_today)));
        }


        while (++i < compiledList.size())
        {
            if (newDay(compiledList.get(i).getDate().getTimeInMillis(), currentDate))
            {
                currentDate.setTimeInMillis(compiledList.get(i).getDate().getTimeInMillis());
                compiledList.add(i, new TodoItem(true, currentDate));
            }
        }
        return (compiledList);
    }

    private ArrayList<TodoItem> getListCompact(Cursor cursLocal, Cursor cursRequested)
    {
        ArrayList<TodoItem> list = new ArrayList<>();
        DateFormat formatterSTART = new SimpleDateFormat("HH:mm");
        DateFormat formatterEND = new SimpleDateFormat("HH:mm");

        if (BuildConfig.DEBUG) Log.d(TAG, "===============");
        if (BuildConfig.DEBUG) Log.d(TAG, "===============");

        addLocalEvents(list, cursLocal, formatterSTART, formatterEND);
        addEkylibreEvents(list, cursRequested, formatterSTART, formatterEND);

        sortList(list);

        return (list);
    }

    private void sortList(ArrayList<TodoItem> list)
    {
        Collections.sort(list, new Comparator<TodoItem>()
        {
            @Override
            public int compare(TodoItem t1, TodoItem t2)
            {
                if (t1.getDate().getTimeInMillis() > t2.getDate().getTimeInMillis())
                    return (1);
                else if (t1.getDate().getTimeInMillis() < t2.getDate().getTimeInMillis())
                    return (-1);
                else
                    return (0);
            }
        });

        int i = -1;
        while (++i < list.size())
            list.get(i).setNumber(i);
    }

    private void addEkylibreEvents(ArrayList<TodoItem> list, Cursor cursRequested, DateFormat formatterSTART, DateFormat formatterEND)
    {
        Calendar dateOfDay = getDateOfDay();
        SimpleDateFormat dateParser = new SimpleDateFormat(DateConstant.ISO_8601);
        String startDateFormatted;
        String endDateFormatted;

        while (cursRequested != null && cursRequested.moveToNext())
        {
            try
            {
                if (BuildConfig.DEBUG) Log.d(TAG, "DATES => " + dateParser.toPattern() + "  "
                        + cursRequested.getString(DTSTART).replaceAll("Z$", "+00:00") + "   "
                        + cursRequested.getString(DTEND).replaceAll("Z$", "+00:00"));
                Date startDate = dateParser.parse(cursRequested.getString(DTSTART).replaceAll("Z$", "+00:00"));

                if (startDate.getTime() > dateOfDay.getTimeInMillis())
                {
                    Date endDate = dateParser.parse(cursRequested.getString(DTEND).replaceAll("Z$", "+00:00"));
                    startDateFormatted = formatterSTART.format(startDate);
                    endDateFormatted = formatterEND.format(endDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);
                    String desc = getDescription(cursRequested);
                    list.add(new TodoItem(startDateFormatted, endDateFormatted, cursRequested.getString(TITLE),
                            desc, cal, cursRequested.getInt(0), EKYLIBRE_CALENDAR));
                }
            }
            catch (ParseException e)
            {
                Log.i(TAG, "Error while parsing date from requested intervention  " + e.getMessage());
            }
        }

    }

    private void addLocalEvents(ArrayList<TodoItem> list, Cursor cursLocal, DateFormat formatterSTART, DateFormat formatterEND)
    {
        String startDateFormatted;
        String endDateFormatted;

        while (cursLocal != null && cursLocal.moveToNext())
        {
            Date startDate = new Date(cursLocal.getLong(DTSTART));
            Date endDate = new Date(cursLocal.getLong(DTEND));
            startDateFormatted = formatterSTART.format(startDate);
            if (cursLocal.getInt(ALL_DAY) == 0)
                endDateFormatted = formatterEND.format(endDate);
            else
                endDateFormatted = "00h00";
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            list.add(new TodoItem(startDateFormatted, endDateFormatted, cursLocal.getString(TITLE),
                    cursLocal.getString(DESCRIPTION), cal, LOCAL_CALENDAR));
        }
    }

    public static String getTimeZone()
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String   timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        return (timeZone.substring(0, 3) + ":"+ timeZone.substring(3, 5));
    }

    private String getDescription(Cursor cursRequested)
    {
        String str = "";
        int id = cursRequested.getInt(0);

        Cursor curs = getInfoForIntervention(id);
        int i = -1;
        if (curs == null || curs.getCount() == 0)
            return (str);
        while (curs.moveToNext())
        {
            str += "\u2022 ";
            str += curs.getString(1);
            if (!curs.isLast())
                str += "\n";
        }
        return (str);
    }

    private Cursor getInfoForIntervention(int id)
    {
        Cursor curs;
        ContentResolver contentResolver = context.getContentResolver();

        curs = contentResolver.query(ZeroContract.InterventionParameters.CONTENT_URI,
                ZeroContract.InterventionParameters.PROJECTION_TARGET,
                ZeroContract.InterventionParameters.ROLE + " LIKE " + "\"" + Intervention.TARGET + "\" AND "
                + ZeroContract.InterventionParameters.FK_INTERVENTION + " == " + id,
                null,
                null);

        return (curs);
    }

    private Cursor getEventsFromEK()
    {
        Cursor curs;
        ContentResolver contentResolver = context.getContentResolver();

        curs = contentResolver.query(ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_BASIC,
                ZeroContract.Interventions.TYPE + " LIKE " + "\"" + Intervention.REQUESTED + "\" AND "
                        + ZeroContract.Interventions.USER + " LIKE " + "\"" + AccountTool.getCurrentAccount(this.context).name + "\"",
                null,
                "datetime(started_at)");

        return (curs);
    }

    private boolean newDay(long date, Calendar currentDate)
    {
        if (date < getDateOfTomorrow().getTimeInMillis())
            return (false);
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
        //Log.d(TAG, "TODAY DATE = " + calendar.getTime());
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
        //Log.d(TAG, "TOMORROW DATE = " + calendar.getTime());
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
        if (BuildConfig.DEBUG) Log.d(TAG, "NEXT MONTH DATE = " + calendar.getTime().toString());
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
        if (BuildConfig.DEBUG) Log.d(TAG, "Getting events from local calendar");
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

        if (!BuildConfig.DEBUG)
            return;
        it = 0;
        if (curs == null)
            curs = getEventsFromLocal();
        if (curs != null && curs.moveToFirst())
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

