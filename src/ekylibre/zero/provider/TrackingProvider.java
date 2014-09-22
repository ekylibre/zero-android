package ekylibre.zero.provider;
 
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import ekylibre.zero.util.SelectionBuilder;

public class TrackingProvider extends ContentProvider {
    private DatabaseHelper mDatabaseHelper;
 
    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using URI_MATCHER.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through URI_MATCHER, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.

    // Routes codes
    public static final int ROUTE_CRUMB_LIST = 100;
    public static final int ROUTE_CRUMB_ITEM = 101;
 
    // UriMatcher, used to decode incoming URIs.
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(TrackingContract.AUTHORITY, "crumbs",   ROUTE_CRUMB_LIST);
        URI_MATCHER.addURI(TrackingContract.AUTHORITY, "crumbs/#", ROUTE_CRUMB_ITEM);
    }
 
    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }
 
    // Determine the mime type for records returned by a given URI.
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
        case ROUTE_CRUMB_LIST:
            return TrackingContract.Crumbs.CONTENT_TYPE;
        case ROUTE_CRUMB_ITEM:
            return TrackingContract.Crumbs.CONTENT_ITEM_TYPE;
        default:
            throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }
 
    /**
     * Perform a database query by URI.
     *
     * <p>Currently supports returning all records (/records) and individual records by ID
     * (/records/{ID}).
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        switch (URI_MATCHER.match(uri)) {
        case ROUTE_CRUMB_ITEM:
            // Return a single crumb, by ID.
            String id = uri.getLastPathSegment();
            builder.where(TrackingContract.CrumbsColumns._ID + "=?", id);
        case ROUTE_CRUMB_LIST:
            // Return all known crumbs.
            builder.table(TrackingContract.CrumbsColumns.TABLE_NAME)
                .where(selection, selectionArgs);
            Cursor cursor = builder.query(database, projection, sortOrder);
            // Note: Notification URI must be manually set here for loaders to correctly
            // register ContentObservers.
            Context context = getContext();
            assert context != null;
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        default:
            throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }
 
    /**
     * Insert a new record into the database.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        assert database != null;
        final int match = URI_MATCHER.match(uri);
        Uri result;
        switch (match) {
        case ROUTE_CRUMB_LIST:
            long id = database.insertOrThrow(TrackingContract.CrumbsColumns.TABLE_NAME, null, values);
            result = Uri.parse(TrackingContract.Crumbs.CONTENT_URI + "/" + id);
            break;
        case ROUTE_CRUMB_ITEM:
            throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
        default:
            throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(uri, null, false);
        return result;
    }
 
    /**
     * Delete a record by database by URI.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int count;
        switch (match) {
        case ROUTE_CRUMB_LIST:
            count = builder.table(TrackingContract.CrumbsColumns.TABLE_NAME)
                .where(selection, selectionArgs)
                .delete(database);
            break;
        case ROUTE_CRUMB_ITEM:
            String id = uri.getLastPathSegment();
            count = builder.table(TrackingContract.CrumbsColumns.TABLE_NAME)
                .where(TrackingContract.CrumbsColumns._ID + "=?", id)
                .where(selection, selectionArgs)
                .delete(database);
            break;
        default:
            throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(uri, null, false);
        return count;
    }
 
    /**
     * Update a record in the database by URI.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int count;
        switch (match) {
        case ROUTE_CRUMB_LIST:
            count = builder.table(TrackingContract.CrumbsColumns.TABLE_NAME)
                .where(selection, selectionArgs)
                .update(database, values);
            break;
        case ROUTE_CRUMB_ITEM:
            String id = uri.getLastPathSegment();
            count = builder.table(TrackingContract.CrumbsColumns.TABLE_NAME)
                .where(TrackingContract.CrumbsColumns._ID + "=?", id)
                .where(selection, selectionArgs)
                .update(database, values);
            break;
        default:
            throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(uri, null, false);
        return count;
    }
 
}
