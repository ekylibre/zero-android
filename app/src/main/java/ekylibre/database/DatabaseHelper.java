package ekylibre.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.os.Build;
import android.os.Debug;
import android.support.design.BuildConfig;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 13;

    private static final String DATABASE_NAME = "zero";

    private static final int NO_VERSION = -1;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /*
    ** Use this to enable constraint on foreign key, but it's actually not working
    ** Crash on the onDelete dunno why => FOREIGN KEY constraint failed (code 787)
    */

/*
    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        if (!db.isReadOnly())
        {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }
*/


    /*
    ** Called on the first creation of the DB if there is no DB with the same DATABASE_NAME
    ** Parameters NO_VERSION is used to say there isn't any version at the creation
    */
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        onUpgrade(database, NO_VERSION, DATABASE_VERSION);
    }

    /*
    ** Called when the newVersion is > than oldVersion
    ** Execute migration from oldVersion + 1 to DATABASE_VERSION
    ** note : +1 is applied to oldVersion to start at the next migration that is needed by current device
    */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        if (BuildConfig.DEBUG) Log.d("DatabaseHelper", "OLD VERSION => " + oldVersion);
        oldVersion += 1;
        switch (oldVersion)
        {
            case 0:
            {
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
                if (newVersion == 0)
                    break;
            }
            case 1:
            {
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
                if (newVersion == 1)
                    break;
            }
            case 2:
            {
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
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingsColumns.PLANT_DENSITY_ABACUS_ITEM_ID + ") REFERENCES " + ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME + "(" + ZeroContract.PlantDensityAbacusItemsColumns._ID + ") ON DELETE CASCADE"
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingsColumns.PLANT_DENSITY_ABACUS_ID + ") REFERENCES " + ZeroContract.PlantDensityAbaciColumns.TABLE_NAME + "(" + ZeroContract.PlantDensityAbaciColumns._ID + ") ON DELETE CASCADE"
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingsColumns.PLANT_ID + ") REFERENCES " + ZeroContract.PlantsColumns.TABLE_NAME + "(" + ZeroContract.PlantsColumns._ID + ") ON DELETE CASCADE"
                        + ")");


                database.execSQL("CREATE TABLE IF NOT EXISTS plant_counting_items ("
                        + ZeroContract.PlantCountingItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantCountingItemsColumns.VALUE + " INTEGER"
                        + ", " + ZeroContract.PlantCountingItemsColumns.PLANT_COUNTING_ID + " INTEGER"
                        + ", FOREIGN KEY(" + ZeroContract.PlantCountingItemsColumns.PLANT_COUNTING_ID + ") REFERENCES " + ZeroContract.PlantCountingsColumns.TABLE_NAME + "(" + ZeroContract.PlantCountingsColumns._ID + ") ON DELETE CASCADE"
                        + ")");
                if (newVersion == 2)
                    break;
            }
            case 3:
            {
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
                if (newVersion == 3)
                    break;
            }
            case 4:
            {
                database.execSQL("CREATE TABLE IF NOT EXISTS plants ("
                        + ZeroContract.PlantsColumns._ID + " INTEGER PRIMARY KEY"
                        + ", " + ZeroContract.Plants.NAME + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.SHAPE + " TEXT"
                        + ", " + ZeroContract.Plants.VARIETY + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.ACTIVE + " BOOLEAN NOT NULL"
                        + ")");
                if (newVersion == 4)
                    break;
            }
            case 5:
            {
                database.execSQL("ALTER TABLE crumbs ADD user VARCHAR(255)");
                database.execSQL("ALTER TABLE issues ADD user VARCHAR(255)");
                database.execSQL("ALTER TABLE plant_countings ADD user VARCHAR(255)");
                database.execSQL("ALTER TABLE plant_counting_items ADD user VARCHAR(255)");
                if (newVersion == 5)
                    break;
            }
            case 6:
            {
                database.execSQL("ALTER TABLE plant_countings ADD average_value FLOAT");
                database.execSQL("ALTER TABLE plant_countings ADD synced BOOLEAN NOT NULL DEFAULT 0");
                if (newVersion == 6)
                    break;
            }
            case 7:
            {
                /*
                ** Sorry for this part of code,
                ** SQLITE doesn't implement ALTER COLUMN and I had to change ID to be in
                ** autoincrement mode and add the ekylibre instance ID
                ** to deal with multi account problems on IDs.
                */
                database.execSQL("DROP TABLE IF EXISTS plant_density_abaci");
                database.execSQL("DROP TABLE IF EXISTS plant_density_abacus_items");
                database.execSQL("DROP TABLE IF EXISTS plants");
                database.execSQL("CREATE TABLE IF NOT EXISTS plant_density_abaci ("
                        + ZeroContract.PlantDensityAbaciColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.PlantDensityAbaciColumns.EK_ID + " INTEGER"
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
                        + ", " + ZeroContract.Plants.SHAPE + " TEXT"
                        + ", " + ZeroContract.Plants.VARIETY + " VARCHAR(255)"
                        + ", " + ZeroContract.Plants.ACTIVE + " BOOLEAN NOT NULL"
                        + ", " + ZeroContract.PlantDensityAbacusItemsColumns.USER + " VARCHAR(255)"
                        + ")");
                if (newVersion == 7)
                    break;
            }
            case 8:
            {
                database.execSQL("ALTER TABLE plants ADD activity_ID INTEGER DEFAULT 0");
                database.execSQL("ALTER TABLE plant_density_abaci ADD activity_ID INTEGER DEFAULT 0");
                if (newVersion == 8)
                    break;
            }
            case 9:
            {
                database.execSQL("CREATE TABLE IF NOT EXISTS intervention ("
                        + ZeroContract.InterventionsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.InterventionsColumns.USER + " VARCHAR(255)"
                        + ")");
                database.execSQL("ALTER TABLE crumbs RENAME TO TMP_TABLE");
                database.execSQL("CREATE TABLE IF NOT EXISTS crumbs ("
                        + ZeroContract.CrumbsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.CrumbsColumns.FK_INTERVENTION + " INTEGER"
                        + ", " + ZeroContract.CrumbsColumns.TYPE + " VARCHAR(32) NOT NULL"
                        + ", " + ZeroContract.CrumbsColumns.LATITUDE + " DOUBLE NOT NULL"
                        + ", " + ZeroContract.CrumbsColumns.LONGITUDE + " DOUBLE NOT NULL"
                        + ", " + ZeroContract.CrumbsColumns.READ_AT + " BIGINT NOT NULL"
                        + ", " + ZeroContract.CrumbsColumns.ACCURACY + " FLOAT"
                        + ", " + ZeroContract.CrumbsColumns.SYNCED + " INTEGER NOT NULL DEFAULT 0"
                        + ", " + ZeroContract.CrumbsColumns.METADATA + " TEXT"
                        + ", " + ZeroContract.CrumbsColumns.USER + " VARCHAR(255)"
                        + ", " + "FOREIGN KEY (" + ZeroContract.CrumbsColumns.FK_INTERVENTION + ") REFERENCES intervention(_id)"
                        + ")");
                database.execSQL("INSERT INTO crumbs ("
                        + ZeroContract.CrumbsColumns._ID
                        + ", " + ZeroContract.CrumbsColumns.TYPE
                        + ", " + ZeroContract.CrumbsColumns.LATITUDE
                        + ", " + ZeroContract.CrumbsColumns.LONGITUDE
                        + ", " + ZeroContract.CrumbsColumns.READ_AT
                        + ", " + ZeroContract.CrumbsColumns.ACCURACY
                        + ", " + ZeroContract.CrumbsColumns.SYNCED
                        + ", " + ZeroContract.CrumbsColumns.METADATA
                        + ", " + ZeroContract.CrumbsColumns.USER
                        + ") SELECT "
                        + ZeroContract.CrumbsColumns._ID
                        + ", " + ZeroContract.CrumbsColumns.TYPE
                        + ", " + ZeroContract.CrumbsColumns.LATITUDE
                        + ", " + ZeroContract.CrumbsColumns.LONGITUDE
                        + ", " + ZeroContract.CrumbsColumns.READ_AT
                        + ", " + ZeroContract.CrumbsColumns.ACCURACY
                        + ", " + ZeroContract.CrumbsColumns.SYNCED
                        + ", " + ZeroContract.CrumbsColumns.METADATA
                        + ", " + ZeroContract.CrumbsColumns.USER
                        + " FROM TMP_TABLE");
                database.execSQL("DROP TABLE IF EXISTS TMP_TABLE");
                if (newVersion == 9)
                    break;
            }
            case 10:
            {
                database.execSQL("ALTER TABLE intervention ADD EK_ID INTEGER");
                database.execSQL("ALTER TABLE intervention ADD type VARCHAR(255) DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD procedure_name VARCHAR(255) DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD name VARCHAR(255) DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD number INTEGER DEFAULT 0");
                database.execSQL("ALTER TABLE intervention ADD started_at VARCHAR(255) DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD stopped_at VARCHAR(255) DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD description TEXT DEFAULT NULL");
                database.execSQL("CREATE TABLE IF NOT EXISTS intervention_parameters ("
                        + ZeroContract.InterventionParametersColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.InterventionParametersColumns.FK_INTERVENTION + " INTEGER"
                        + ", " + ZeroContract.InterventionParametersColumns.EK_ID + " INTEGER"
                        + ", " + ZeroContract.InterventionParametersColumns.ROLE + " VARCHAR(255)"
                        + ", " + ZeroContract.InterventionParametersColumns.LABEL + " VARCHAR(255)"
                        + ", " + ZeroContract.InterventionParametersColumns.NAME + " VARCHAR(255)"
                        + ", " + "FOREIGN KEY (" + ZeroContract.CrumbsColumns.FK_INTERVENTION + ") REFERENCES intervention(_id)"
                        + ")");
                if (newVersion == 10)
                    break;
            }
            case 11:
            {
                database.execSQL("ALTER TABLE intervention_parameters ADD product_name VARCHAR(255) DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention_parameters ADD product_id INTEGER DEFAULT 0");
                if (newVersion == 11)
                    break;
            }
            case 12:
            {
                database.execSQL("CREATE TABLE IF NOT EXISTS working_periods ("
                        + ZeroContract.WorkingPeriodsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ", " + ZeroContract.WorkingPeriodsColumns.FK_INTERVENTION + " INTEGER"
                        + ", " + ZeroContract.WorkingPeriodsColumns.NATURE + " VARCHAR(255)"
                        + ", " + ZeroContract.WorkingPeriodsColumns.STARTED_AT + " VARCHAR(255)"
                        + ", " + ZeroContract.WorkingPeriodsColumns.STOPPED_AT + " VARCHAR(255)"
                        + ", " + "FOREIGN KEY (" + ZeroContract.WorkingPeriodsColumns.FK_INTERVENTION + ") REFERENCES intervention(_id)"
                        + ")");
                if (newVersion == 12)
                    break;
            }
            case 13:
            {
                database.execSQL("ALTER TABLE intervention ADD state VARCHAR(255) DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD request_compliant BOOLEAN " +
                        "DEFAULT 1");
                database.execSQL("ALTER TABLE intervention ADD general_chrono BIGINT " +
                        "DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD preparation_chrono BIGINT " +
                        "DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD traveling_chrono BIGINT " +
                        "DEFAULT NULL");
                database.execSQL("ALTER TABLE intervention ADD intervention_chrono BIGINT " +
                        "DEFAULT NULL");
                if (newVersion == 13)
                    break;
            }
        }
    }
}