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
                    database.execSQL("CREATE TABLE IF NOT EXISTS plant_counting ("
                        + ZeroContract.PlantCountingColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantCountingColumns.OBSERVED_AT + " DATE"
                        + ", " + ZeroContract.PlantCountingColumns.LATITUDE + " REAL"
                        + ", " + ZeroContract.PlantCountingColumns.LONGITUDE + " REAL"
                        + ", " + ZeroContract.PlantCountingColumns.ADVOCATED_DENSITY + " REAL"
                        + ", " + ZeroContract.PlantCountingColumns.OBSERVATION + " TEXT"
                        + ")");
                    database.execSQL("CREATE TABLE IF NOT EXISTS plant_counting_items ("
                        + ZeroContract.PlantCountingItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantCountingItemsColumns.VALUE + " INTEGER"
                        + ", " + ZeroContract.PlantCountingItemsColumns.PLANT_COUNTING_ID + " INTEGER"
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingItemsColumns.PLANT_COUNTING_ID +") REFERENCES " + ZeroContract.PlantCountingColumns.TABLE_NAME + "(" + ZeroContract.PlantCountingColumns._ID + ") ON DELETE CASCADE"
                        + ")");
            case 3:
                    database.execSQL("CREATE TABLE IF NOT EXISTS plant_density_abaci ("
                        + ZeroContract.PlantDensityAbaciColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.NAME + " VARCHAR(192)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.VARIETY + " VARCHAR(192)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.GERMINATION_PERCENTAGE + " INTEGER"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.SAMPLING_LENGTH_UNIT + " VARCHAR(32)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.SEEDING_DENSITY_UNIT + " VARCHAR(32)"
                        + ")");
            case 4:
                    database.execSQL("CREATE TABLE IF NOT EXISTS plant_density_abacus_items ("
                        + ZeroContract.PlantDensityAbacusItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.SEEDING_DENSITY_VALUE + " INTEGER"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.PLANTS_COUNTS + " INTEGER"
                        + ")");
            case 5:
                    database.execSQL("CREATE TABLE IF NOT EXISTS plants ("
                        + ZeroContract.PlantsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.Plants.NAME + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.SHAPE+ " VARCHAR(32)"
                        + ", " + ZeroContract.Plants.VARIETY + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.EKYLIBRE_ID + " INTEGER"
                        + ", " + ZeroContract.Plants.ACTIVE + " BOOLEAN"
                        + ")");
        }
    }
}
