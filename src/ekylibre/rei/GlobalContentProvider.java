package ekylibre.rei;
 
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import ekylibre.rei.util.SelectionBuilder;

public class GlobalContentProvider extends ContentProvider {
    DatabaseHelper mDatabaseHelper;
 
    // Content authority for this provider.
    public static final String AUTHORITY = "ekylibre.authority.basic";
 
    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.

    // Routes codes
    public static final int ROUTE_CRUMBS = 1;
    public static final int ROUTE_CRUMBS_ID = 2;
 
    // UriMatcher, used to decode incoming URIs.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "crumbs",   ROUTE_CRUMBS);
        sUriMatcher.addURI(AUTHORITY, "crumbs/#", ROUTE_CRUMBS_ID);
    }
 
    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }
 
    // Determine the mime type for records returned by a given URI.
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            // Adds here new data types
        case ROUTE_CRUMBS:
            return Crumb.CrumbColumns.CONTENT_TYPE;
        case ROUTE_CRUMBS_ID:
            return Crumb.CrumbColumns.CONTENT_ITEM_TYPE;
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
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
        case ROUTE_CRUMBS_ID:
            // Return a single crumb, by ID.
            String id = uri.getLastPathSegment();
            builder.where(Crumb.CrumbColumns._ID + "=?", id);
        case ROUTE_CRUMBS:
            // Return all known crumbs.
            builder.table(Crumb.CrumbColumns.TABLE_NAME)
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
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
        case ROUTE_CRUMBS:
            long id = database.insertOrThrow(Crumb.CrumbColumns.TABLE_NAME, null, values);
            result = Uri.parse(Crumb.CrumbColumns.CONTENT_URI + "/" + id);
            break;
        case ROUTE_CRUMBS_ID:
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
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
        case ROUTE_CRUMBS:
            count = builder.table(Crumb.CrumbColumns.TABLE_NAME)
                .where(selection, selectionArgs)
                .delete(database);
            break;
        case ROUTE_CRUMBS_ID:
            String id = uri.getLastPathSegment();
            count = builder.table(Crumb.CrumbColumns.TABLE_NAME)
                .where(Crumb.CrumbColumns._ID + "=?", id)
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
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
        case ROUTE_CRUMBS:
            count = builder.table(Crumb.CrumbColumns.TABLE_NAME)
                .where(selection, selectionArgs)
                .update(database, values);
            break;
        case ROUTE_CRUMBS_ID:
            String id = uri.getLastPathSegment();
            count = builder.table(Crumb.CrumbColumns.TABLE_NAME)
                .where(Crumb.CrumbColumns._ID + "=?", id)
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
 
    // /**
    //  * SQLite backend for @{link FeedProvider}.
    //  *
    //  * Provides access to an disk-backed, SQLite datastore which is utilized by FeedProvider. This
    //  * database should never be accessed by other parts of the application directly.
    //  */
    // static class DatabaseHelper extends SQLiteOpenHelper {
    //     /** Schema version. */
    //     public static final int DATABASE_VERSION = 1;
    //     /** Filename for SQLite file. */
    //     public static final String DATABASE_NAME = "feed.db";
 
    //     private static final String TYPE_TEXT = " TEXT";
    //     private static final String TYPE_INTEGER = " INTEGER";
    //     private static final String COMMA_SEP = ",";
    //     /** SQL statement to create "crumb" table. */
    //     private static final String SQL_CREATE_CRUMBS =
    //             "CREATE TABLE " + Crumb.CrumbColumns.TABLE_NAME + " (" +
    //                     Crumb.CrumbColumns._ID + " INTEGER PRIMARY KEY," +
    //                     Crumb.CrumbColumns.COLUMN_NAME_CRUMB_ID + TYPE_TEXT + COMMA_SEP +
    //                     Crumb.CrumbColumns.COLUMN_NAME_TITLE    + TYPE_TEXT + COMMA_SEP +
    //                     Crumb.CrumbColumns.COLUMN_NAME_LINK + TYPE_TEXT + COMMA_SEP +
    //                     Crumb.CrumbColumns.COLUMN_NAME_PUBLISHED + TYPE_INTEGER + ")";
 
    //     /** SQL statement to drop "crumb" table. */
    //     private static final String SQL_DELETE_CRUMBS =
    //             "DROP TABLE IF EXISTS " + Crumb.CrumbColumns.TABLE_NAME;
 
    //     public DatabaseHelper(Context context) {
    //         super(context, DATABASE_NAME, null, DATABASE_VERSION);
    //     }
 
    //     @Override
    //     public void onCreate(SQLiteDatabase database) {
    //         database.execSQL(SQL_CREATE_CRUMBS);
    //     }
 
    //     @Override
    //     public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    //         // This database is only a cache for online data, so its upgrade policy is
    //         // to simply to discard the data and start over
    //         database.execSQL(SQL_DELETE_CRUMBS);
    //         onCreate(database);
    //     }
    // }
}
