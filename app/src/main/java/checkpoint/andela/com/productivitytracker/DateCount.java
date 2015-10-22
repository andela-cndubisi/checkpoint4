package checkpoint.andela.com.productivitytracker;

/**
 * Created by andela-cj on 21/10/2015.
 */
public  class DateCount {
    public long timespent;
    public String date;
    public String position;

    public DateCount(String date, int numberOfPosition, long timespent) {
        this.date = date;
        this.position = formatPosition(numberOfPosition);
        this.timespent = timespent;
    }
    public String formatPosition(int p){
        return String.format("%d records",p);
    }

}