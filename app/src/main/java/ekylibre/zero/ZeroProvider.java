package ekylibre.zero;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import ekylibre.zero.util.SelectionBuilder;

public class ZeroProvider extends ContentProvider {
    // Routes codes


    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using URI_MATCHER.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through URI_MATCHER, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    public static final int ROUTE_CRUMB_LIST = 100;
    public static final int ROUTE_CRUMB_ITEM = 101;
    public static final int ROUTE_ISSUE_LIST = 200;
    public static final int ROUTE_ISSUE_ITEM = 201;
    public static final int ROUTE_PLANT_COUNTING_LIST = 300;
    public static final int ROUTE_PLANT_COUNTING_ITEM = 301;
    public static final int ROUTE_PLANT_COUNTING_ITEM_LIST = 400;
    public static final int ROUTE_PLANT_COUNTING_ITEM_ITEM = 401;
    public static final int ROUTE_PLANT_DENSITY_ABACI_LIST = 500;
    public static final int ROUTE_PLANT_DENSITY_ABACI_ITEM = 501;
    public static final int ROUTE_PLANT_DENSITY_ABACUS_ITEMS_LIST = 600;
    public static final int ROUTE_PLANT_DENSITY_ABACUS_ITEMS_ITEM = 601;
    // UriMatcher, used to decode incoming URIs.
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "crumbs", ROUTE_CRUMB_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "crumbs/#", ROUTE_CRUMB_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "issues", ROUTE_ISSUE_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "issues/#", ROUTE_ISSUE_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_counting", ROUTE_PLANT_COUNTING_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_counting/#", ROUTE_PLANT_COUNTING_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_counting", ROUTE_PLANT_COUNTING_ITEM_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_counting/#", ROUTE_PLANT_COUNTING_ITEM_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_density_abaci", ROUTE_PLANT_DENSITY_ABACI_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_density_abaci/#", ROUTE_PLANT_DENSITY_ABACI_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_density_abacus_items", ROUTE_PLANT_DENSITY_ABACUS_ITEMS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_density_abacus_items/#", ROUTE_PLANT_DENSITY_ABACUS_ITEMS_ITEM);
    }

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    // Determine the mime type for records returned by a given URI.
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ROUTE_CRUMB_LIST:
                return ZeroContract.Crumbs.CONTENT_TYPE;
            case ROUTE_CRUMB_ITEM:
                return ZeroContract.Crumbs.CONTENT_ITEM_TYPE;
            case ROUTE_ISSUE_LIST:
                return ZeroContract.Issues.CONTENT_TYPE;
            case ROUTE_ISSUE_ITEM:
                return ZeroContract.Issues.CONTENT_ITEM_TYPE;
            case ROUTE_PLANT_COUNTING_LIST:
                return ZeroContract.PlantCounting.CONTENT_TYPE;
            case ROUTE_PLANT_COUNTING_ITEM:
                return ZeroContract.PlantCounting.CONTENT_ITEM_TYPE;
            case ROUTE_PLANT_COUNTING_ITEM_LIST:
                return ZeroContract.PlantCountingItems.CONTENT_TYPE;
            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                return ZeroContract.PlantCountingItems.CONTENT_TYPE;
            case ROUTE_PLANT_DENSITY_ABACI_LIST:
                return ZeroContract.PlantDensityAbaci.CONTENT_TYPE;
            case ROUTE_PLANT_DENSITY_ABACI_ITEM:
                return ZeroContract.PlantDensityAbaci.CONTENT_TYPE;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_LIST:
                return ZeroContract.PlantDensityAbacusItems.CONTENT_TYPE;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_ITEM:
                return ZeroContract.PlantDensityAbacusItems.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    /**
     * Perform a database query by URI.
     * <p/>
     * <p>Currently supports returning all records (/records) and individual records by ID
     * (/records/{ID}).
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        String id;
        Cursor cursor;
        Context context;
        switch (URI_MATCHER.match(uri)) {
            case ROUTE_CRUMB_ITEM:
                // Return a single crumb, by ID.
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.CrumbsColumns._ID + "=?", id);
            case ROUTE_CRUMB_LIST:
                // Return all known crumbs.
                builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                context = getContext();
                assert context != null;
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;

            case ROUTE_ISSUE_ITEM:
                // Return a single ISSUE, by ID.
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.IssuesColumns._ID + "=?", id);
            case ROUTE_ISSUE_LIST:
                // Return all known Issue.
                builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                context = getContext();
                assert context != null;
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;

            case ROUTE_PLANT_COUNTING_ITEM:
                // Return a single ISSUE, by ID.
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantCountingColumns._ID + "=?", id);
            case ROUTE_PLANT_COUNTING_LIST:
                // Return all known Issue.
                builder.table(ZeroContract.PlantCountingColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                context = getContext();
                assert context != null;
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;

            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                // Return a single ISSUE, by ID.
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantCountingItemsColumns._ID + "=?", id);
            case ROUTE_PLANT_COUNTING_ITEM_LIST:                // Return all known Issue.
                builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                context = getContext();
                assert context != null;
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;

            case ROUTE_PLANT_DENSITY_ABACI_ITEM:
                // Return a single ISSUE, by ID.
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantDensityAbaciColumns._ID + "=?", id);
            case ROUTE_PLANT_DENSITY_ABACI_LIST:                // Return all known Issue.
                builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                context = getContext();
                assert context != null;
                cursor.setNotificationUri(context.getContentResolver(), uri);
                return cursor;

            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_ITEM:
                // Return a single ISSUE, by ID.
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantDensityAbacusItemsColumns._ID + "=?", id);
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_LIST:                // Return all known Issue.
                builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                context = getContext();
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
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        assert database != null;
        final int match = URI_MATCHER.match(uri);
        Uri result;
        long id;
        switch (match) {
            case ROUTE_CRUMB_LIST:
                id = database.insertOrThrow(ZeroContract.CrumbsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.Crumbs.CONTENT_URI + "/" + id);
                break;
            case ROUTE_CRUMB_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_ISSUE_LIST:
                id = database.insertOrThrow(ZeroContract.IssuesColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.Issues.CONTENT_URI + "/" + id);
                break;
            case ROUTE_ISSUE_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_COUNTING_LIST:
                id = database.insertOrThrow(ZeroContract.PlantCountingColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.PlantCounting.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_COUNTING_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_COUNTING_ITEM_LIST:
                id = database.insertOrThrow(ZeroContract.PlantCountingItemsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.PlantCountingItems.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_DENSITY_ABACI_LIST:
                id = database.insertOrThrow(ZeroContract.PlantCountingItemsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.PlantCountingItems.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_DENSITY_ABACI_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_LIST:
                id = database.insertOrThrow(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.PlantDensityAbacusItems.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_ITEM:
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
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int count;
        String id;
        switch (match) {
            case ROUTE_CRUMB_LIST:
                count = builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_CRUMB_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(ZeroContract.CrumbsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_ISSUE_LIST:
                count = builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_ISSUE_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(ZeroContract.IssuesColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_COUNTING_LIST:
                count = builder.table(ZeroContract.PlantCountingColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_COUNTING_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantCountingColumns.TABLE_NAME)
                        .where(ZeroContract.PlantCountingColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_COUNTING_ITEM_LIST:
                count = builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantCountingItemsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_DENSITY_ABACI_LIST:
                count = builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_DENSITY_ABACI_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(ZeroContract.PlantDensityAbaciColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_LIST:
                count = builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantDensityAbacusItemsColumns._ID + "=?", id)
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
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int count;
        String id;
        switch (match) {
            case ROUTE_CRUMB_LIST:
                count = builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_CRUMB_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(ZeroContract.CrumbsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_ISSUE_LIST:
                count = builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_ISSUE_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(ZeroContract.IssuesColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_PLANT_COUNTING_LIST:
                count = builder.table(ZeroContract.PlantCountingColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_COUNTING_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantCountingColumns.TABLE_NAME)
                        .where(ZeroContract.PlantCountingColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_PLANT_COUNTING_ITEM_LIST:
                count = builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantCountingItemsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_DENSITY_ABACI_LIST:
                count = builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_DENSITY_ABACI_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(ZeroContract.PlantDensityAbaciColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_LIST:
                count = builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEMS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantDensityAbacusItemsColumns._ID + "=?", id)
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
