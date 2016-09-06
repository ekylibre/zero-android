package ekylibre.zero.home;

import android.util.Log;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**************************************
 * Created by pierre on 7/11/16.      *
 * ekylibre.zero for zero-android     *
 *************************************/

public class TodoItem
{
    private String  startDate;
    private String  endDate;
    private String  event;
    private String  desc;
    private boolean headerState = false;
    private boolean messageState = false;
    private String messageString = "";
    private Calendar day;
    private int     id;
    private Calendar date;
    private int number;
    private int source;

    public TodoItem(boolean headerState, Calendar day)
    {
        this.headerState = headerState;
        this.day = Calendar.getInstance();
        this.day.setTimeInMillis(day.getTimeInMillis());
        this.startDate = null;
        this.endDate = null;
        this.event = null;
        this.desc = null;
        this.source = TodoListActivity.NONE_CALENDAR;
    }

    public TodoItem(boolean msg, String message)
    {
        this.messageState = msg;
        this.day = null;
        this.startDate = null;
        this.endDate = null;
        this.event = null;
        this.desc = null;
        this.source = TodoListActivity.NONE_CALENDAR;
        this.messageString = message;
    }

    public TodoItem(String startdate, String endDate, String event, String desc, Calendar calendar, int source)
    {
        this.startDate = startdate;
        this.endDate = endDate;
        this.event = event;
        this.desc = desc;
        this.date = calendar;
        this.id = -1;
        this.source = source;
    }

    public TodoItem(String startdate, String endDate, String event, String desc, Calendar calendar, int _id, int source)
    {
        this.startDate = startdate;
        this.endDate = endDate;
        this.event = event;
        this.desc = desc;
        this.date = calendar;
        this.id = _id;
        this.source = source;
    }

    public void setStartDate(String newDate)
    {
        this.startDate = newDate;
    }

    public void setEvent(String newEvent)
    {
        this.event = newEvent;
    }

    public void setDesc(String newDesc)
    {
        this.desc = newDesc;
    }

    public void setEndDate(String newDate)
    {
        this.endDate = newDate;
    }

    public void setHeaderState(boolean state)
    {
        this.headerState = state;
    }

    public void setNumber(int nb)
    {
        this.number = nb;
    }

    public String   getStartDate()
    {
        return (this.startDate);
    }

    public String   getEndDate()
    {
        return (this.endDate);
    }

    public String   getEvent()
    {
        return (this.event);
    }

    public String   getDesc()
    {
        return (this.desc);
    }

    public boolean   getHeaderState()
    {
        return (this.headerState);
    }

    public String getDay()
    {
        String date = DateFormat.getDateInstance(DateFormat.FULL,
                Locale.getDefault()).format(day.getTime());
        return (date);
    }
    public Calendar getDate()
    {
        return (date);
    }

    public int getNumber()
    {
        return (number);
    }

    public int getSource()
    {
        return (source);
    }

    public boolean getMessageState()
    {
        return (messageState);
    }

    public String getMessageString()
    {
        return (this.messageString);
    }
}
