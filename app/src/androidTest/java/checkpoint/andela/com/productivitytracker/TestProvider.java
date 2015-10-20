package checkpoint.andela.com.productivitytracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import checkpoint.andela.com.productivitytracker.data.ProductivityContract;
import checkpoint.andela.com.productivitytracker.data.ProductivityDBHelper;

public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(ProductivityDBHelper.DATABASE_NAME);
    }


    public void testInsertReadProvider(){

        ProductivityDBHelper dbHelper = new ProductivityDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues PValues = TestDb.getLocationContentValues();

        long productivityRowId = db.insert(ProductivityContract.LocationEntry.TABLE_NAME, null, PValues);
        // Verify we got a row back.
        assertTrue(productivityRowId != -1);
        Log.d(LOG_TAG, "New row id: " + productivityRowId);


        Cursor PCursor = mContext.getContentResolver().query(
                ProductivityContract.LocationEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(PValues,PCursor);
        dbHelper.close();

    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(ProductivityContract.LocationEntry.CONTENT_URI);
        assertEquals(ProductivityContract.LocationEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ProductivityContract.LocationEntry.buildLocationUri(1L));
        assertEquals(ProductivityContract.LocationEntry.CONTENT_ITEM_TYPE, type);



    }
}