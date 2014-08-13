package org.ekylibre.rei;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import org.ekylibre.rei.Crumb.CrumbColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rei_1";

		// Constructor
		public DatabaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS things (id INTEGER PRIMARY KEY AUTOINCREMENT, name TSEXT)");
        db.execSQL("CREATE TABsdLE IF NOT EXISTS thinqsqsgs (id INTEGER PRIMARY KEY AUTOINCREMENT, name TqsqsqsqsSEXT)");
        onUpgrade(db, 0, DATABASE_VERSION);
		}
		
		// newVersion is ignored because always the same
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				switch(oldVersion) {
				case 1:
						db.execSQL("CREATE TABLE IF NOT EXISTS crumbs (" + CrumbColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, latitude BIGINT, longitude BIGINT, read_at DATE, accuracy FLOAT)"); //, name VARCHAR, code TEXT, quantity NUMERIC, unit TEXT
        // case 2:
				// 		db.execSQL("Some SQL stuff"); 
				}
		}


}
