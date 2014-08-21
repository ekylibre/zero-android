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

    private long id;
    private String type;
    private long latitude;
    private long longitude;
    private long readAt;
    private Float accuracy;
    private String procedureNature;

    private Boolean saved;
    private Boolean newRecord;
    // private String code;
    // private BigDecimal quantity;
    // private String unit;

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
        values.put(CrumbColumns.COLUMN_NAME_TYPE, this.type);
        values.put(CrumbColumns.COLUMN_NAME_LATITUDE, this.latitude);
        values.put(CrumbColumns.COLUMN_NAME_LONGITUDE, this.longitude);
        values.put(CrumbColumns.COLUMN_NAME_READ_AT, parser.format(new Date(this.readAt)));
        values.put(CrumbColumns.COLUMN_NAME_ACCURACY, this.accuracy);
        values.put(CrumbColumns.COLUMN_NAME_PROCEDURE_NATURE, this.procedureNature);
        
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(CrumbColumns.TABLE_NAME, null, values);

        if (newRowId > 0) {
            this.id = newRowId;
            this.newRecord = false;
        } else {
            this.saved = false;
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
        this.type      = type;
        this.latitude  = Math.round(COORDINATE_COEFF * location.getLatitude());
        this.longitude = Math.round(COORDINATE_COEFF * location.getLongitude());
        this.readAt    = location.getTime();
        this.accuracy  = location.getAccuracy();
        this.newRecord = true;
        this.saved     = false;
        if (options != null) {
            this.procedureNature = options.getString("procedureNature");
            // this.name      = name;
            // this.code      = code;
            // this.quantity  = quantity;
            // this.unit      = unit;
        }
    }


}
