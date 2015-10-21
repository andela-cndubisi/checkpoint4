package checkpoint.andela.com.productivitytracker;

import android.graphics.PointF;

/**
 * Created by andela-cj on 21/10/2015.
 */
public  class DateCount {
    public String date;
    public String position;

    public DateCount(String date, int numberOfPosition) {
        this.date = date;
        this.position = formatPosition(numberOfPosition);


    }
    public String formatPosition(int p){
        return String.format("%d records",p);
    }

}