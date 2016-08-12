package ekylibre.api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ekylibre.api.ZeroContract;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "zero";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        onUpgrade(database, 0, DATABASE_VERSION);
    }

    // newVersion is ignored because always the same
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        switch (oldVersion)
        {
            case 0:
                database.execSQL("CREATE TABLE IF NOT EXISTS crumbs ("
                        + ZeroContract.CrumbsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.CrumbsColumns.TYPE + " VARCHAR(32) NOT NULL"
                        + ", " + ZeroContract.CrumbsColumns.LATITUDE + " DOUBLE NOT NULL"
                        + ", " + ZeroContract.CrumbsColumns.LONGITUDE + " DOUBLE NOT NULL"
                        + ", " + ZeroContract.CrumbsColumns.READ_AT + " BIGINT NOT NULL"
                        + ", " + ZeroContract.CrumbsColumns.ACCURACY + " FLOAT"
                        + ", " + ZeroContract.CrumbsColumns.SYNCED + " INTEGER NOT NULL DEFAULT 0"
                        + ", " + ZeroContract.CrumbsColumns.METADATA + " TEXT"
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
                database.execSQL("CREATE TABLE IF NOT EXISTS plant_countings ("
                        + ZeroContract.PlantCountingsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantCountingsColumns.OBSERVED_AT + " DATE"
                        + ", " + ZeroContract.PlantCountingsColumns.LATITUDE + " REAL"
                        + ", " + ZeroContract.PlantCountingsColumns.LONGITUDE + " REAL"
                        + ", " + ZeroContract.PlantCountingsColumns.OBSERVATION + " TEXT"
                        + ", " + ZeroContract.PlantCountingsColumns.SYNCED_AT + " DATE"
                        + ", " + ZeroContract.PlantCountingsColumns.PLANT_DENSITY_ABACUS_ITEM_ID + " INTEGER"
                        + ", " + ZeroContract.PlantCountingsColumns.PLANT_DENSITY_ABACUS_ID + " INTEGER"
                        + ", " + ZeroContract.PlantCountingsColumns.PLANT_ID + " INTEGER"
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingsColumns.PLANT_DENSITY_ABACUS_ITEM_ID +") REFERENCES " + ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME + "(" + ZeroContract.PlantDensityAbacusItemsColumns._ID + ") ON DELETE CASCADE"
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingsColumns.PLANT_DENSITY_ABACUS_ID +") REFERENCES " + ZeroContract.PlantDensityAbaciColumns.TABLE_NAME + "(" + ZeroContract.PlantDensityAbaciColumns._ID + ") ON DELETE CASCADE"
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingsColumns.PLANT_ID +") REFERENCES " + ZeroContract.PlantsColumns.TABLE_NAME + "(" + ZeroContract.PlantsColumns._ID + ") ON DELETE CASCADE"
                        + ")");

                database.execSQL("CREATE TABLE IF NOT EXISTS plant_counting_items ("
                        + ZeroContract.PlantCountingItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantCountingItemsColumns.VALUE + " INTEGER"
                        + ", " + ZeroContract.PlantCountingItemsColumns.PLANT_COUNTING_ID + " INTEGER"
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingItemsColumns.PLANT_COUNTING_ID +") REFERENCES " + ZeroContract.PlantCountingsColumns.TABLE_NAME + "(" + ZeroContract.PlantCountingsColumns._ID + ") ON DELETE CASCADE"
                        + ")");
            case 3:
                database.execSQL("CREATE TABLE IF NOT EXISTS plant_density_abaci ("
                        + ZeroContract.PlantDensityAbaciColumns._ID + " INTEGER PRIMARY KEY"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.NAME + " VARCHAR(255)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.VARIETY + " VARCHAR(255)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.GERMINATION_PERCENTAGE + " REAL"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.SAMPLING_LENGTH_UNIT + " VARCHAR(255)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.SEEDING_DENSITY_UNIT + " VARCHAR(255)"
                        + ")");
                database.execSQL("CREATE TABLE IF NOT EXISTS plant_density_abacus_items ("
                        + ZeroContract.PlantDensityAbacusItemsColumns._ID + " INTEGER PRIMARY KEY"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.SEEDING_DENSITY_VALUE + " INTEGER"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.PLANTS_COUNT + " INTEGER"
                        + ")");
            case 4:
                database.execSQL("CREATE TABLE IF NOT EXISTS plants ("
                        + ZeroContract.PlantsColumns._ID + " INTEGER PRIMARY KEY"
                        + ", " + ZeroContract.Plants.NAME + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.SHAPE+ " TEXT"
                        + ", " + ZeroContract.Plants.VARIETY + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.ACTIVE + " BOOLEAN NOT NULL"
                        + ")");
            case 5:
                database.execSQL("ALTER TABLE crumbs ADD user VARCHAR(255)");
                database.execSQL("ALTER TABLE issues ADD user VARCHAR(255)");
                database.execSQL("ALTER TABLE plant_countings ADD user VARCHAR(255)");
                database.execSQL("ALTER TABLE plant_counting_items ADD user VARCHAR(255)");
            case 6:
                database.execSQL("ALTER TABLE plant_countings ADD average_value FLOAT");
                database.execSQL("ALTER TABLE plant_countings ADD synced BOOLEAN NOT NULL DEFAULT 0");
            case 7:
                /*
                ** Sorry for this part of code,
                ** SQLITE doesn't implements ALTER COLUMN and I had to change ID to be in
                ** autoincrement mode and add the ekylibre instance ID
                ** to deal with multi account problems on IDs.
                */
                database.execSQL("DROP TABLE IF EXISTS plant_density_abaci");
                database.execSQL("DROP TABLE IF EXISTS plant_density_abacus_items");
                database.execSQL("DROP TABLE IF EXISTS plants");
                database.execSQL("CREATE TABLE IF NOT EXISTS plant_density_abaci ("
                        + ZeroContract.PlantDensityAbaciColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.EK_ID  + " INTEGER"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.NAME + " VARCHAR(255)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.VARIETY + " VARCHAR(255)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.GERMINATION_PERCENTAGE + " REAL"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.SAMPLING_LENGTH_UNIT + " VARCHAR(255)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.SEEDING_DENSITY_UNIT + " VARCHAR(255)"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.USER + " VARCHAR(255)"
                        + ")");
                database.execSQL("CREATE TABLE IF NOT EXISTS plant_density_abacus_items ("
                        + ZeroContract.PlantDensityAbacusItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.EK_ID + " INTEGER"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.SEEDING_DENSITY_VALUE + " INTEGER"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.PLANTS_COUNT + " INTEGER"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.USER + " VARCHAR(255)"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.FK_ID + " INTEGER"
                        + ")");
                database.execSQL("CREATE TABLE IF NOT EXISTS plants ("
                        + ZeroContract.PlantsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.Plants.EK_ID + " INTEGER"
                        + ", " + ZeroContract.Plants.NAME + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.SHAPE+ " TEXT"
                        + ", " + ZeroContract.Plants.VARIETY + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.ACTIVE + " BOOLEAN NOT NULL"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.USER + " VARCHAR(255)"
                        + ")");
            case 8:
                database.execSQL("ALTER TABLE plants ADD activity_ID INTEGER");
                database.execSQL("ALTER TABLE plant_density_abaci ADD activity_ID INTEGER");
        }
    }
}
