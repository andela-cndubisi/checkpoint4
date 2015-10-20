package checkpoint.andela.com.productivitytracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static checkpoint.andela.com.productivitytracker.data.ProductivityContract.*;
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
}
