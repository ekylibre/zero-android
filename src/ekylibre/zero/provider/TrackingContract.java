package ekylibre.zero.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class TrackingContract {

    // Authority name for this provider
    // Same as defined in AndroidManifest in <provider> markup
    public static final String AUTHORITY = "ekylibre.zero.tracking.provider";

    // Content URI for this provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final class Crumbs implements BaseColumns, CrumbsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(TrackingContract.CONTENT_URI, "crumbs");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.crumbs";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.crumb";

        public static final String[] PROJECTION_ALL = {_ID, TYPE, LATITUDE, LONGITUDE, READ_AT, ACCURACY, METADATA, SYNCED};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
        
    }

    public static interface CrumbsColumns extends BaseColumns {
        public static final String TABLE_NAME = "crumbs";
        // public static final String ID        = "id";
        public static final String TYPE      = "type";
        public static final String LATITUDE  = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String READ_AT   = "read_at";
        public static final String ACCURACY  = "accuracy";
        public static final String METADATA  = "metadata";
        public static final String SYNCED    = "synced";
    }

}
