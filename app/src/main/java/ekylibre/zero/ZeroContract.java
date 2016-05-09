package ekylibre.zero;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ZeroContract {

    // Authority name for this provider
    // Same as defined in AndroidManifest in <provider> markup
    public static final String AUTHORITY = "ekylibre.zero";

    // Content URI for this provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public interface CrumbsColumns extends BaseColumns {
        String TABLE_NAME = "crumbs";
        String TYPE = "type";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String READ_AT = "read_at";
        String ACCURACY = "accuracy";
        String METADATA = "metadata";
        String SYNCED = "synced";
    }

    public interface IssuesColumns extends BaseColumns {
        String TABLE_NAME = "issues";
        String NATURE = "nature";
        String SEVERITY = "severity";
        String EMERGENCY = "emergency";
        String SYNCED = "synced";
        String DESCRIPTION = "description";
        String PINNED = "pinned";
        String SYNCED_AT = "synced_at";
        String OBSERVED_AT = "observed_at";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
    }

    public interface PlantCountingsColumns extends BaseColumns {
        String TABLE_NAME = "plan_counting";
        String OBSERVED_AT = "observed_at";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String OBSERVATION = "observation";
        String SYNCED_AT = "synced_at";
        String PLANT_DENSITY_ABACUS_ITEM_ID = "plant_density_abacus_item_id";
        String PLANT_DENSITY_ABACUS_ID = "plant_density_abacus_id";
        String PLANT_ID = "plant_id";
    }

    public interface PlantCountingItemsColumns extends BaseColumns {
        String TABLE_NAME = "plant_counting_items";
        String VALUE = "value";
        String PLANT_COUNTING_ID = "plant_counting_id";
    }

    public interface PlantDensityAbaciColumns extends BaseColumns {
        String TABLE_NAME = "plant_density_abaci";
        String NAME = "name";
        String VARIETY = "variety";
        String GERMINATION_PERCENTAGE = "germination_percentage";
        String SEEDING_DENSITY_UNIT = "seeding_density_unit";
        String SAMPLING_LENGTH_UNIT = "sampling_length_unit";
    }

    public interface PlantDensityAbacusItemsColumns extends BaseColumns {
        String TABLE_NAME = "plant_density_abacus_items";
        String SEEDING_DENSITY_VALUE = "seeding_density_value";
        String PLANTS_COUNT = "plants_count";
    }

    public interface PlantsColumns extends BaseColumns {
        String TABLE_NAME = "plants";
        String NAME = "name";
        String SHAPE = "shape";
        String VARIETY = "variety";
        String ACTIVE = "active";
    }

    public static final class Crumbs implements CrumbsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "crumbs");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.crumbs";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.crumb";

        public static final String[] PROJECTION_ALL = {_ID, TYPE, LATITUDE, LONGITUDE, READ_AT, ACCURACY, METADATA, SYNCED};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static final class Issues implements IssuesColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "issues");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.issues";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.issue";

        public static final String[] PROJECTION_ALL = {_ID, NATURE, SEVERITY, EMERGENCY, SYNCED, DESCRIPTION, PINNED, SYNCED_AT, OBSERVED_AT, LATITUDE, LONGITUDE};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class PlantCountings implements PlantCountingsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "plant_countings");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.plant_countings";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.plant_counting";

        public static final String[] PROJECTION_ALL = {_ID, OBSERVED_AT, LATITUDE, LONGITUDE, OBSERVATION,  PLANT_DENSITY_ABACUS_ITEM_ID, SYNCED_AT, PLANT_DENSITY_ABACUS_ID, PLANT_ID};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class PlantCountingItems implements PlantCountingItemsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "plant_counting_items");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.plant_counting_items";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.plant_counting_item";

        public static final String[] PROJECTION_ALL = {_ID, VALUE, PLANT_COUNTING_ID};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class PlantDensityAbaci implements PlantDensityAbaciColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "plant_density_abaci");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.plant_density_abaci";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.plant_density_abacus";

        public static final String[] PROJECTION_ALL = {_ID, NAME, VARIETY, GERMINATION_PERCENTAGE, SEEDING_DENSITY_UNIT, SAMPLING_LENGTH_UNIT};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class PlantDensityAbacusItems implements PlantDensityAbacusItemsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "plant_density_abacus_items");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.plant_density_abacus_items";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.plant_density_abacus_item";

        public static final String[] PROJECTION_ALL = {_ID, SEEDING_DENSITY_VALUE, PLANTS_COUNT};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class Plants implements PlantsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "plants");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.plants";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.plant";

        public static final String[] PROJECTION_ALL = {_ID, NAME, SHAPE, VARIETY, ACTIVE};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }
}
