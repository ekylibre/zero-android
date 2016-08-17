package ekylibre.zero.home;

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
}
