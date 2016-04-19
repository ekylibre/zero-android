package ekylibre.zero;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "zero";

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
        switch (oldVersion) {
            case 0:
                database.execSQL("CREATE TABLE IF NOT EXISTS crumbs ("
                        + ekylibre.zero.ZeroContract.CrumbsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ekylibre.zero.ZeroContract.CrumbsColumns.TYPE + " VARCHAR(32) NOT NULL"
                        + ", " + ekylibre.zero.ZeroContract.CrumbsColumns.LATITUDE + " DOUBLE NOT NULL"
                        + ", " + ekylibre.zero.ZeroContract.CrumbsColumns.LONGITUDE + " DOUBLE NOT NULL"
                        + ", " + ekylibre.zero.ZeroContract.CrumbsColumns.READ_AT + " BIGINT NOT NULL"
                        + ", " + ekylibre.zero.ZeroContract.CrumbsColumns.ACCURACY + " FLOAT"
                        + ", " + ekylibre.zero.ZeroContract.CrumbsColumns.SYNCED + " INTEGER NOT NULL DEFAULT 0"
                        + ", " + ekylibre.zero.ZeroContract.CrumbsColumns.METADATA + " TEXT"
                        + ")");
            case 1:
                database.execSQL("CREATE TABLE IF NOT EXISTS issues ("
                        + ZeroContract.IssuesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.IssuesColumns.NATURE + " VARCHAR(192) NOT NULL"
                        + ", " + ZeroContract.IssuesColumns.SEVERITY + " INTEGER NOT NULL DEFAULT 2"
                        + ", " + ZeroContract.IssuesColumns.EMERGENCY + " INTEGER NOT NULL DEFAULT 2"
                        + ", " + ZeroContract.IssuesColumns.DESCRIPTION + " TEXT"
                        + ", " + ZeroContract.IssuesColumns.SYNCED + " BOOLEAN NOT NULL DEFAULT 0"
                        + ", " + ZeroContract.IssuesColumns.PINNED + " BOOLEAN NOT NULL DEFAULT 0"
                        + ", " + ZeroContract.IssuesColumns.SYNCED_AT + " DATE"
                        + ", " + ZeroContract.IssuesColumns.OBSERVED_AT + " DATE"
                        + ", " + ZeroContract.IssuesColumns.LATITUDE + " REAL"
                        + ", " + ZeroContract.IssuesColumns.LONGITUDE + " REAL"
                        + ")");

            case 2:
                    database.execSQL("CREATE TABLE IF NOT EXISTS samplings ("
                        + ZeroContract.SamplingColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.SamplingColumns.OBSERVED_AT + " DATE"
                        + ", " + ZeroContract.SamplingColumns.LATITUDE + " REAL"
                        + ", " + ZeroContract.SamplingColumns.LONGITUDE + " REAL"
                        + ", " + ZeroContract.SamplingColumns.ADVOCATED_DENSITY + " REAL"
                        + ", " + ZeroContract.SamplingColumns.OBSERVATION + " TEXT"
                        + ")");
                database.execSQL("CREATE TABLE IF NOT EXISTS sampling_counts ("
                        + ZeroContract.SamplingCountsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.SamplingCountsColumns.VALUE + " INTEGER"
                        + ", " + ZeroContract.SamplingCountsColumns.SAMPLING_ID + " INTEGER"
                        +", FOREIGN KEY(" + ZeroContract.SamplingCountsColumns.SAMPLING_ID +") REFERENCES " + ZeroContract.SamplingColumns.TABLE_NAME + "(" + ZeroContract.SamplingColumns._ID + ") ON DELETE CASCADE"
                        +")");

        }
    }
}
