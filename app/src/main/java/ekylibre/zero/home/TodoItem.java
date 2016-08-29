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
    private Calendar day;

    public TodoItem(boolean headerState, Calendar day)
    {
        this.headerState = headerState;
        this.day = Calendar.getInstance();
        this.day.setTimeInMillis(day.getTimeInMillis());
        this.startDate = null;
        this.endDate = null;
        this.event = null;
        this.desc = null;
    }

    public TodoItem(String startdate, String endDate, String event, String desc)
    {
        this.startDate = startdate;
        this.endDate = endDate;
        this.event = event;
        this.desc = desc;
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
        String date = DateFormat.getDateInstance(DateFormat.LONG,
                Locale.getDefault()).format(day.getTime());
        return (date);
    }
}
