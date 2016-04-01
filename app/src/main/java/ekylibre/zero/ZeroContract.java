package ekylibre.zero;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ZeroContract {

    // Authority name for this provider
    // Same as defined in AndroidManifest in <provider> markup
    public static final String AUTHORITY = "ekylibre.zero";

    // Content URI for this provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public interface CrumbsColumns extends BaseColumns {
        String TABLE_NAME = "crumbs";
        String TYPE = "type";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String READ_AT = "read_at";
        String ACCURACY = "accuracy";
        String METADATA = "metadata";
        String SYNCED = "synced";
    }

    public interface IssuesColumns extends BaseColumns {
        String TABLE_NAME = "issues";
        String NATURE = "nature";
        String SEVERITY = "severity";
        String EMERGENCY = "emergency";
        String SYNCED = "synced";
        String DESCRIPTION = "description";
        String PINNED = "pinned";
        String SYNCED_AT = "synced_at";
        String OBSERVED_AT = "observed_at";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";

    }

    public static final class Crumbs implements CrumbsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "crumbs");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.crumbs";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.crumb";

        public static final String[] PROJECTION_ALL = {_ID, TYPE, LATITUDE, LONGITUDE, READ_AT, ACCURACY, METADATA, SYNCED};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static final class Issues implements IssuesColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "issues");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.issues";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.issue";

        public static final String[] PROJECTION_ALL = {_ID, NATURE, SEVERITY, EMERGENCY, SYNCED, DESCRIPTION, PINNED, SYNCED_AT, OBSERVED_AT, LATITUDE, LONGITUDE};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }
}
