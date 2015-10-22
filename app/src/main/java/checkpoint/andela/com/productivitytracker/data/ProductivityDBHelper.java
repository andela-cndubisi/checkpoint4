package checkpoint.andela.com.productivitytracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PointF;
import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
                LocationEntry.COLUMN_INTERVAL + " INTEGER NOT NULL, "+
                LocationEntry.COLUMN_DATE_TEXT + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<DateCount> getDateWithCount(){
        final String COLUMN_SELECTION = "Count("+LocationEntry.COLUMN_DATE_TEXT+")";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DateCount> list = new ArrayList<>();
        Cursor cursor = db.query(true
                , LocationEntry.TABLE_NAME
                , new String[]{COLUMN_SELECTION, LocationEntry.COLUMN_DATE_TEXT}
                , null
                , null
                , LocationEntry.COLUMN_DATE_TEXT
                , null
                , null
                , null);

        for (int i =0; i< cursor.getCount(); i++){
             if (cursor.moveToNext()) {
                 int countIdx = cursor.getColumnIndex(COLUMN_SELECTION);
                 int dateIdx = cursor.getColumnIndex(LocationEntry.COLUMN_DATE_TEXT);
                 DateCount history = new DateCount(cursor.getString(dateIdx),cursor.getInt(countIdx),0);
                list.add(history);
            }
        }
        cursor.close();
        db.close();
        return list;
    }


    public ArrayList<DateCount> getSumLocationWithInterval(){
        final String newColumnName = "count";
        final String COLUMN_SELECTION = "sum("+LocationEntry.COLUMN_INTERVAL+")";
        final String newColumn = "(select count("+LocationEntry.COLUMN_CITY_NAME+") " +
                " from "+LocationEntry.TABLE_NAME+" group by "+
                LocationEntry.COLUMN_CITY_NAME+") ";


        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DateCount> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select " + COLUMN_SELECTION + ", " +
                        LocationEntry.COLUMN_CITY_NAME + "," + newColumn +
                        " as " + newColumnName +
                        " from " + LocationEntry.TABLE_NAME + " group by " + LocationEntry.COLUMN_CITY_NAME, null);

        for (int i =0; i< cursor.getCount(); i++){
            if (cursor.moveToNext()) {
                int sumIdx = cursor.getColumnIndex(COLUMN_SELECTION);
                int cityIdx = cursor.getColumnIndex(LocationEntry.COLUMN_CITY_NAME);
                int countIdx = cursor.getColumnIndex(newColumnName);
                DateCount history = new DateCount(cursor.getString(cityIdx),cursor.getInt(countIdx),cursor.getLong(sumIdx));
                list.add(history);
            }
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<PointF> getDateLocations(String date) {
        ArrayList<PointF> locations = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+LocationEntry.TABLE_NAME+" where TRIM(date) = '" + date.trim() + "'", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                float lon = cursor.getFloat(cursor.getColumnIndex(LocationEntry.COLUMN_LONGITUDE));
                float lat = cursor.getFloat(cursor.getColumnIndex(LocationEntry.COLUMN_LATITUDE));
                cursor.moveToNext();
                locations.add(new PointF(lat,lon));
            }
            cursor.close();
        }
        return locations;
    }


    public long saveLocationAndAddress(Location currentLocation, String address, int interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date);
        ContentValues values = ProductivityContract.LocationEntry.createContentFromLocation(
                currentLocation.getLongitude()
                , currentLocation.getLatitude()
                , address
                , today, interval);
        long rowId = db.insert(ProductivityContract.LocationEntry.TABLE_NAME, null, values);
        return rowId;
    }
}
