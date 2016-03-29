package ekylibre.zero.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by antoine on 23/03/16.
 */
public class IssueContract {
    // Authority name for this provider
    // Same as defined in AndroidManifest in <provider> markup
    public static final String AUTHORITY = "ekylibre.zero.issue.provider";

    // Content URI for this provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final class Issues implements BaseColumns, IssueColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(IssueContract.CONTENT_URI, "issues");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.issues";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.issue";

        public static final String[] PROJECTION_ALL = {_ID, NATURE, SEVERITY, EMERGENCY, DESCRIPTION, PINNED, SYNCED_AT};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static interface IssueColumns extends BaseColumns {
        public static final String TABLE_NAME = "issues";
        public static final String NATURE = "nature";
        public static final String SEVERITY = "severity";
        public static final String EMERGENCY = "emergency";
        public static final String DESCRIPTION = "description";
        public static final String PINNED = "pinned";
        public static final String SYNCED_AT = "synced_at";
    }
}
