package ekylibre.rei.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rei";

		// Constructor
		public DatabaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
        onUpgrade(database, 0, DATABASE_VERSION);
		}
		
		// newVersion is ignored because always the same
		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
				switch(oldVersion) {
				case 0:
						database.execSQL("CREATE TABLE IF NOT EXISTS crumbs (" + TrackingContract.CrumbsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TrackingContract.CrumbsColumns.TYPE + " VARCHAR(32), " + TrackingContract.CrumbsColumns.LATITUDE + " BIGINT, " + TrackingContract.CrumbsColumns.LONGITUDE + " BIGINT, " + TrackingContract.CrumbsColumns.READ_AT + " DATE, " + TrackingContract.CrumbsColumns.ACCURACY + " FLOAT, " + TrackingContract.CrumbsColumns.PROCEDURE_NATURE + " VARCHAR(64))"); //, code TEXT, quantity NUMERIC, unit TEXT
        // case 1:
				//  		database.execSQL("ALTER TABLE crumbs SET"); 
				}
		}


}
