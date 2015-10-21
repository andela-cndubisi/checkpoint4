package checkpoint.andela.com.productivitytracker.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by andela-cj on 18/10/2015.
 */
public class ProductivityContract {

    public static final String CONTENT_AUTHORITY = "checkpoint.andela.com.productivitytracker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_LOCATION = "location";


    public static final class LocationEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String TABLE_NAME = "location";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_DATE_TEXT = "date";
        public static final String COLUMN_INTERVAL = "interval";



        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static ContentValues createContentFromLocation(double longitude, double latitude, String name, String date, int duration){
            ContentValues values = new ContentValues();
            values.put(COLUMN_LATITUDE, latitude);
            values.put(COLUMN_LONGITUDE, longitude);
            values.put(COLUMN_CITY_NAME, name);
            values.put(COLUMN_DATE_TEXT, date);
            values.put(COLUMN_INTERVAL, duration);
            return values;
        }
    }
}
