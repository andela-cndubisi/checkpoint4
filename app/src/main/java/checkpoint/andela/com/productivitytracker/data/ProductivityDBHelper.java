package checkpoint.andela.com.productivitytracker.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Map;

import checkpoint.andela.com.productivitytracker.DateCount;

import static checkpoint.andela.com.productivitytracker.data.ProductivityContract.LocationEntry;

/**
 * Created by andela-cj on 18/10/2015.
 */
public class ProductivityDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "productivity.db";
    public static final int DATABASE_VERSION = 1;

    public ProductivityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " +
                LocationEntry.TABLE_NAME + " ("+
                LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL,"+
                LocationEntry.COLUMN_LATITUDE +" REAL NOT NULL," +
                LocationEntry.COLUMN_LONGITUDE + " REAL NOT NULL, "+
                LocationEntry.COLUMN_DATE_TEXT + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public ArrayList<String> getUniqueLocationDateQueryArg() {
        ArrayList<String> dates = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(true,
                    LocationEntry.TABLE_NAME
                    , new String[]{LocationEntry.COLUMN_DATE_TEXT}
                    , null
                    , null
                    , LocationEntry.COLUMN_DATE_TEXT
                    , null
                    , null
                    , null );
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String date = cursor.getString(cursor.getColumnIndex(LocationEntry.COLUMN_DATE_TEXT));
                    dates.add(date);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }



    public ArrayList<DateCount> getDateWithCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> a = getUniqueLocationDateQueryArg();
        ArrayList<DateCount> list = new ArrayList<>();
        Cursor cursor =null;
        for (int i =0; i< a.size(); i++){
             cursor = db.query(false
                    , LocationEntry.TABLE_NAME
                    , null
                    , LocationEntry.COLUMN_DATE_TEXT + " =?"
                    , new String[]{a.get(i)}
                    , null
                    , null
                    , null
                    , null /* limit */);

                DateCount history = null;
                if (cursor.moveToNext()) {
                    history = getHistoryFromCursor(cursor);
                    list.add(history);
                }
        }
        if (cursor!=null) cursor.close();
        db.close();
        return list;
    }

    private DateCount getHistoryFromCursor(Cursor cursor) {
        int dateIdx = cursor.getColumnIndex(LocationEntry.COLUMN_DATE_TEXT);
        DateCount history = new DateCount(
                cursor.getString(dateIdx),cursor.getCount());
        return history;
    }

}
