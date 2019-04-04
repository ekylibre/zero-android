package ekylibre.zero.inter.model;

import java.util.Date;

import static org.apache.commons.lang3.time.DateUtils.addHours;

public class Period {

    public Date startDateTime;
    public Date stopDateTime;

    public Period() {
        stopDateTime = new Date();
        startDateTime = addHours(stopDateTime, -1);
    }

}
