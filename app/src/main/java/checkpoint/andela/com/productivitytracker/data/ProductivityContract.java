package checkpoint.andela.com.productivitytracker.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by andela-cj on 18/10/2015.
 */
public class ProductivityContract {

    public static final String CONTENT_AUTHORITY = "checkpoint.andela.com.productivitytracker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_PRODUCTIVITY = "productivity";
    public static final String PATH_LOCATION = "location";

    public static final class ProductivityEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRODUCTIVITY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTIVITY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTIVITY;



        public static final String TABLE_NAME = "productivity";
        public static final String COLUMN_DATE_TEXT = "date";
        public static final String COLUMN_INTERVAL = "interval";

        public static Uri buildProductivityUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildProductivityWithADate(String aDate) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_DATE_TEXT, aDate).build();
        }
   }

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
        public static final String COLUMN_PRODUCTIVITY_ID = "productivity_id";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
