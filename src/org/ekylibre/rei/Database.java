package org.ekylibre.rei;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

		// Constructor
		public Database(Context context, String name, CursorFactory factory, int version) {
				super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
				// onUpgrade(db, 0, 1);
		}
		

		


		// newVersion is ignored because always the same
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				switch(oldVersion) {
				case 1:
						db.execSQL("CREATE TABLE IF NOT EXISTS crumbs (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, latitude FLOAT, longitude FLOAT, read_at DATE, accuracy FLOAT, type TEXT, code TEXT, quantity NUMERIC, unit TEXT)");
        // case 2:
				// 		db.execSQL("Some SQL stuff"); 
				}
		}

}
