package ekylibre.rei;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Crumb {

    // This coeff permits to store lat/lon as integer with an under-millimetric precision
    public static final long COORDINATE_COEFF = 1000000;

    private long   mId;
    private String mType;
    private long   mLatitude;
    private long   mLongitude;
    private long   mReadAt;
    private Float  mAccuracy;
    private String mProcedureNature;
    // private String mScannedCode;
    // private BigDecimal mQuantity;
    // private String mUnit;

    private Boolean mSaved;
    private Boolean mNewRecord;

    // Base URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + GlobalContentProvider.AUTHORITY);
 
    // Path component for "crumb"-type resources..
    private static final String PATH_CRUMBS = "crumbs";


    public static abstract class CrumbColumns implements BaseColumns {
        // MIME type for lists of crumbs.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.rei.crumbs";

        // MIME type for individual crumbs.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.rei.crumb";

        // Fully qualified URI for "crumb" resources.
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CRUMBS).build();

        public static final String TABLE_NAME = "crumbs";
        // public static final String COLUMN_NAME_ID        = "id";
        public static final String COLUMN_NAME_TYPE      = "type";
        public static final String COLUMN_NAME_LATITUDE  = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_READ_AT   = "read_at";
        public static final String COLUMN_NAME_ACCURACY  = "accuracy";
        public static final String COLUMN_NAME_PROCEDURE_NATURE = "procedure_nature";
    }


    // public static Crumb find(long id) {
    // }

    public long insert(SQLiteDatabase db) {        
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
        // values.put(CrumbColumns.COLUMN_NAME_ID, id);
        values.put(CrumbColumns.COLUMN_NAME_TYPE, mType);
        values.put(CrumbColumns.COLUMN_NAME_LATITUDE, mLatitude);
        values.put(CrumbColumns.COLUMN_NAME_LONGITUDE, mLongitude);
        values.put(CrumbColumns.COLUMN_NAME_READ_AT, parser.format(new Date(mReadAt)));
        values.put(CrumbColumns.COLUMN_NAME_ACCURACY, mAccuracy);
        values.put(CrumbColumns.COLUMN_NAME_PROCEDURE_NATURE, mProcedureNature);
        
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(CrumbColumns.TABLE_NAME, null, values);

        if (newRowId > 0) {
            mId = newRowId;
            mNewRecord = false;
        } else {
            mSaved = false;
        }

        return newRowId;
    }

        
    public Crumb(Location location) {
        this(location, "point");
    }

    public Crumb(Location location, String type) {
        this(location, type, null);
    }

    public Crumb(Location location, String type, Bundle options) {
        mType      = type;
        mLatitude  = Math.round(COORDINATE_COEFF * location.getLatitude());
        mLongitude = Math.round(COORDINATE_COEFF * location.getLongitude());
        mReadAt    = location.getTime();
        mAccuracy  = location.getAccuracy();
        mNewRecord = true;
        mSaved     = false;
        if (options != null) {
            mProcedureNature = options.getString("procedureNature");
            // mScannedCode     = options.getString("scannedCode");
            // mQuantity        = options.getString("quantity");
            // mUnit            = options.getString("unit");
        }
    }


}
