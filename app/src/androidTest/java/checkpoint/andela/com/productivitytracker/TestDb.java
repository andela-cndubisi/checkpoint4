package checkpoint.andela.com.productivitytracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import static checkpoint.andela.com.productivitytracker.db.ProductivityContract.LocationEntry;
import checkpoint.andela.com.productivitytracker.db.ProductivityDBHelper;

/**
 * Created by andela-cj on 18/10/2015.
 */
public class TestDb extends AndroidTestCase {
    public void testCreateDb() throws Throwable{
        mContext.deleteDatabase(ProductivityDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new ProductivityDBHelper(this.mContext)
                .getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();

    }

    public static ContentValues getLocationContentValues(){
        ContentValues values = new ContentValues();

        String testname = "Yaba, Lagos";
        double testlatitide = 3.03420;
        double testlongitude = 6.04533;
        values.put(LocationEntry.COLUMN_CITY_NAME, testname);
        values.put(LocationEntry.COLUMN_LATITUDE, testlatitide);
        values.put(LocationEntry.COLUMN_LONGITUDE, testlongitude);
        return  values;
    }

    public static void validateCursor(ContentValues expectedValues, Cursor valueCursor){
        assertTrue(valueCursor.moveToFirst());
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet){
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(-1 == idx);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
    }

    public void testInsertReadDB(){


        ProductivityDBHelper dbHelper =  new ProductivityDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = getLocationContentValues();
        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);

        assertTrue(locationRowId != -1);
        Log.i(TestDb.class.getSimpleName(), "New row id:" + locationRowId);

        // A cursor is your primary interface to query results
        Cursor cursor = db.query(LocationEntry.TABLE_NAME,
                null, // column names
                null, // columns for the 'where' clause
                null, // Values for the 'where' clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );


        if (cursor.moveToFirst()) {

            validateCursor(values, cursor);
        }else {
            fail("no values returned ");
        }
    }
}
