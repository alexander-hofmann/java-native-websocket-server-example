package technikum.wien.at;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MeasurementValue {
    private static final TimeZone tz = TimeZone.getTimeZone("UTC");
    private Date date;
    private double value;

    public MeasurementValue(Date date, double value) {
        this.date = date;
        this.value = value;
    }

    public MeasurementValue(double value) {
        this.date = new Date();
        this.value = value;
    }

    public MeasurementValue() {
        this.date = new Date();
        this.value = 0.0;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
