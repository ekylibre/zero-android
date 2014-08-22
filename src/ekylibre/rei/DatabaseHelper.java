package ekylibre.rei;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import ekylibre.rei.Crumb.CrumbColumns;

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
						database.execSQL("CREATE TABLE IF NOT EXISTS crumbs (" + CrumbColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CrumbColumns.COLUMN_NAME_TYPE + " VARCHAR(32), " + CrumbColumns.COLUMN_NAME_LATITUDE + " BIGINT, " + CrumbColumns.COLUMN_NAME_LONGITUDE + " BIGINT, " + CrumbColumns.COLUMN_NAME_READ_AT + " DATE, " + CrumbColumns.COLUMN_NAME_ACCURACY + " FLOAT, " + CrumbColumns.COLUMN_NAME_PROCEDURE_NATURE + " VARCHAR(64))"); //, code TEXT, quantity NUMERIC, unit TEXT
        // case 1:
				//  		database.execSQL("ALTER TABLE crumbs SET"); 
				}
		}


}
