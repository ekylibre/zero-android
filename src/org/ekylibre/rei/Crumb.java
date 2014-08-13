package org.ekylibre.rei;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.BaseColumns;

public class Crumb {

    // This coeff permits to store lat/lon as integer with an under-millimetric precision
    public static final long COORDINATE_COEFF = 1000000;
    

		private long id;
		private String type;
		private long latitude;
		private long longitude;
		private long readAt;
		private Float accuracy;

    private Boolean saved;
    private Boolean newRecord;
		// private String name;
		// private String code;
		// private BigDecimal quantity;
		// private String unit;

    public static abstract class CrumbColumns implements BaseColumns {
        public static final String TABLE_NAME = "crumbs";
        // public static final String COLUMN_NAME_ID        = "id";
        public static final String COLUMN_NAME_TYPE      = "type";
        public static final String COLUMN_NAME_LATITUDE  = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_READ_AT   = "read_at";
        public static final String COLUMN_NAME_ACCURACY  = "accuracy";
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
				this.type      = "crumb";
				this.latitude  = Math.round(COORDINATE_COEFF * location.getLatitude());
				this.longitude = Math.round(COORDINATE_COEFF * location.getLongitude());
				this.readAt    = location.getTime();
				this.accuracy  = location.getAccuracy();
        this.newRecord = true;
        this.saved     = false;
				// this.name      = name;
				// this.code      = code;
				// this.quantity  = quantity;
				// this.unit      = unit;
		}


		// private int id;
		// private String name;
		// private BigDecimal latitude;
		// private BigDecimal longitude;
		// private Date readAt;
		// private BigDecimal accuracy;
		// private String type;
		// private String code;
		// private BigDecimal quantity;
		// private String unit;

		// public Crumb(Database db, String name, BigDecimal latitude, BigDecimal longitude, Date readAt, BigDecimal accuracy, String type, String code, BigDecimal quantity, String unit) {
		// 		this.name      = name;
		// 		this.latitude  = latitude;
		// 		this.longitude = longitude;
		// 		this.readAt    = readAt;
		// 		this.accuracy  = accuracy;
		// 		this.type      = type;
		// 		this.code      = code;
		// 		this.quantity  = quantity;
		// 		this.unit      = unit;
		// }
		
		// public Crumb(String name, BigDecimal latitude, BigDecimal longitude, Date readAt, BigDecimal accuracy, String type, String code, BigDecimal quantity, String unit) {
		// 		this.name      = name;
		// 		this.latitude  = latitude;
		// 		this.longitude = longitude;
		// 		this.readAt    = readAt;
		// 		this.accuracy  = accuracy;
		// 		this.type      = type;
		// 		this.code      = code;
		// 		this.quantity  = quantity;
		// 		this.unit      = unit;
		// }

		// public Crumb(String name, BigDecimal latitude, BigDecimal longitude, Date readAt, BigDecimal accuracy, String type) {
		// 		this.name      = name;
		// 		this.latitude  = latitude;
		// 		this.longitude = longitude;
		// 		this.readAt    = readAt;
		// 		this.accuracy  = accuracy;
		// 		this.type      = type;
		// 		this.code      = null;
		// 		this.quantity  = null;
		// 		this.unit      = null;
		// }

		// public Crumb(String name, BigDecimal latitude, BigDecimal longitude, Date readAt, BigDecimal accuracy) {
		// 		this.name      = name;
		// 		this.latitude  = latitude;
		// 		this.longitude = longitude;
		// 		this.readAt    = readAt;
		// 		this.accuracy  = accuracy;
		// 		this.type      = "crumb";
		// 		this.code      = null;
		// 		this.quantity  = null;
		// 		this.unit      = null;
		// }


		// public void save() {
				
		// }

}
