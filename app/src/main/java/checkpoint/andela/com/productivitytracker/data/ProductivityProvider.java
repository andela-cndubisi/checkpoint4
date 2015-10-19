package checkpoint.andela.com.productivitytracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class ProductivityProvider extends ContentProvider {

    private ProductivityDBHelper dbHelper;


    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int PRODUCTIVITY = 100;
    private static final int PRODUCTIVITY_ID = 101;
    private static final int PRODUCTIVITY_WITH_DATE = 102;
    private static final int LOCATION = 300;
    private static final int LOCATION_ID = 301;
    private static final int LOCATION_WITH_PRODUCTIVITY = 302;
    private static final int LOCATION_WITH_NAME = 303;


    private static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ProductivityContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, ProductivityContract.PATH_PRODUCTIVITY, PRODUCTIVITY);
        matcher.addURI(authority, ProductivityContract.PATH_PRODUCTIVITY + "/#", PRODUCTIVITY_ID);
        matcher.addURI(authority, ProductivityContract.PATH_PRODUCTIVITY + "/*=*", PRODUCTIVITY_WITH_DATE);

        matcher.addURI(authority, ProductivityContract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, ProductivityContract.PATH_LOCATION + "/#", LOCATION_ID);
        matcher.addURI(authority, ProductivityContract.PATH_PRODUCTIVITY + "/#", LOCATION_WITH_PRODUCTIVITY);
        matcher.addURI(authority, ProductivityContract.PATH_PRODUCTIVITY + "/*", LOCATION_WITH_NAME);



        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ProductivityDBHelper(getContext());
        return true;
    }


    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case PRODUCTIVITY:
                return ProductivityContract.ProductivityEntry.CONTENT_TYPE;
            case PRODUCTIVITY_ID:
                return ProductivityContract.ProductivityEntry.CONTENT_ITEM_TYPE;
            case PRODUCTIVITY_WITH_DATE:
                return ProductivityContract.ProductivityEntry.CONTENT_ITEM_TYPE;
            case LOCATION:
                return ProductivityContract.LocationEntry.CONTENT_TYPE;
            case LOCATION_WITH_PRODUCTIVITY:
                return ProductivityContract.ProductivityEntry.CONTENT_TYPE;
            case LOCATION_WITH_NAME:
                return ProductivityContract.ProductivityEntry.CONTENT_TYPE;
            case LOCATION_ID:
                return ProductivityContract.LocationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // all locations with foreign key

            // "productivity"
            case PRODUCTIVITY: {
                retCursor = dbHelper.getReadableDatabase().query(
                        ProductivityContract.ProductivityEntry.TABLE_NAME,
                        null,
                        null ,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            // "productivity"
            case PRODUCTIVITY_WITH_DATE: {
                retCursor = dbHelper.getReadableDatabase().query(
                        ProductivityContract.ProductivityEntry.TABLE_NAME,
                        projection,
                        selection ,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LOCATION: {
                retCursor = dbHelper.getReadableDatabase().query(
                        ProductivityContract.LocationEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            case LOCATION_ID: {
                retCursor = dbHelper.getReadableDatabase().query(
                        ProductivityContract.LocationEntry.TABLE_NAME,
                        projection,
                        ProductivityContract.LocationEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LOCATION_WITH_PRODUCTIVITY:
            {
                retCursor = dbHelper.getReadableDatabase().query(
                        ProductivityContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }
            case LOCATION_WITH_NAME:
            {
                retCursor = dbHelper.getReadableDatabase().query(
                        ProductivityContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
}