package checkpoint.andela.com.productivitytracker.data;

import android.provider.BaseColumns;

/**
 * Created by andela-cj on 18/10/2015.
 */
public class LocationContract {
    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_CITY_NAME = "city_name";
    }
}
