package checkpoint.andela.com.productivitytracker.data;

import android.provider.BaseColumns;

/**
 * Created by andela-cj on 18/10/2015.
 */
public class ProductivityContract {

    public static final class ProductivityEntry implements BaseColumns{
        public static final String TABLE_NAME = "productivity";
        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_DATE_TEXT = "date";
        public static final String COLUMN_INTERVAL = "interval";
    }
}
