package ekylibre.util;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

/**************************************
 * Created by pierre on 9/15/16.      *
 * ekylibre.util for zero-android    *
 *************************************/
public class ExtendedChronometer extends Chronometer
{
    private boolean isRunning = false;
    private long    timeWhenStopped;

    public ExtendedChronometer(Context context)
    {
        super(context);
        timeWhenStopped = 0;
    }

    public ExtendedChronometer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        timeWhenStopped = 0;
    }

    public ExtendedChronometer(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        timeWhenStopped = 0;
    }

    private void refreshTimer()
    {
        if (!isRunning)
        {
            this.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
            this.start();
            this.stop();
            isRunning = false;
        }
    }

    /*
    ** This method is used in replacement of start method from Chronometer
    ** Use this if you want this class to be able to work.
    */
    public void startTimer()
    {
        if (!isRunning)
        {
            this.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
            this.start();
            isRunning = true;
        }
    }

    /*
    ** This method is used in replacement of stop method from Chronometer
    ** Use this if you want this class to be able to work.
    */
    public void stopTimer()
    {
        if (isRunning)
        {
            this.stop();
            timeWhenStopped = SystemClock.elapsedRealtime() - this.getBase();
            isRunning = false;
        }
    }

    public void resetTimer()
    {
        this.stop();
        this.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        isRunning = false;
    }

    public long getTime()
    {
        return (timeWhenStopped);
    }

    public void setTime(long time)
    {
        timeWhenStopped = time;
        refreshTimer();
    }

    public boolean isRunning()
    {
        return (isRunning);
    }
}
