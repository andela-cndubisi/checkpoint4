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

        ContentValues PValues = TestDb.getProductivityContentValues();

        long productivityRowId = db.insert(ProductivityContract.ProductivityEntry.TABLE_NAME, null, PValues);
        // Verify we got a row back.
        assertTrue(productivityRowId != -1);
        Log.d(LOG_TAG, "New row id: " + productivityRowId);


        Cursor PCursor = mContext.getContentResolver().query(
                ProductivityContract.ProductivityEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(PValues,PCursor);

        PCursor = mContext.getContentResolver().query(
                ProductivityContract.LocationEntry.buildLocationUri(productivityRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(PValues, PCursor);

        ContentValues LValues = TestDb.getLocationContentValues(productivityRowId);

        long LocationRowId = db.insert(ProductivityContract.ProductivityEntry.TABLE_NAME,
                null,
                LValues);
        assertTrue(LocationRowId != -1);

        // A cursor is your primary interface to the query results.
        Cursor LCursor = mContext.getContentResolver().query(
                ProductivityContract.LocationEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestDb.validateCursor(LValues, LCursor);

        dbHelper.close();

    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(ProductivityContract.ProductivityEntry.CONTENT_URI);
        assertEquals(ProductivityContract.ProductivityEntry.CONTENT_TYPE, type);


        type = mContext.getContentResolver().getType(ProductivityContract.LocationEntry.CONTENT_URI);
        assertEquals(ProductivityContract.LocationEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(ProductivityContract.LocationEntry.buildLocationUri(1L));
        assertEquals(ProductivityContract.LocationEntry.CONTENT_ITEM_TYPE, type);

        String testDate = "20151017";
        type = mContext.getContentResolver().getType(
                ProductivityContract.ProductivityEntry.buildProductivityWithADate(testDate));
        assertEquals(ProductivityContract.ProductivityEntry.CONTENT_ITEM_TYPE, type);


    }
}