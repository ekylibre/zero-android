package ekylibre.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import ekylibre.zero.BuildConfig;


public final class ZeroContract {

    // Authority name for this provider
    // Same as defined in AndroidManifest in <provider> markup
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    // Content URI for this provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public interface InterventionsColumns extends BaseColumns {
        String TABLE_NAME = "intervention";
        String USER = "user";
        String EK_ID = "ek_id";
        String TYPE = "type";
        String PROCEDURE_NAME = "procedure_name";
        String NAME = "name";
        String NUMBER = "number";
        String STARTED_AT = "started_at";
        String STOPPED_AT = "stopped_at";
        String DESCRIPTION = "description";
        String STATE = "state";
        String REQUEST_COMPLIANT = "request_compliant";
        String GENERAL_CHRONO = "general_chrono";
        String PREPARATION_CHRONO = "preparation_chrono";
        String TRAVEL_CHRONO = "travel_chrono";
        String INTERVENTION_CHRONO = "intervention_chrono";
        String UUID = "uuid";
    }

    public interface InterventionParametersColumns extends BaseColumns
    {
        String TABLE_NAME = "intervention_parameters";
        String FK_INTERVENTION = "fk_intervention";
        String EK_ID = "ek_id";
        String ROLE = "role";
        String LABEL = "label";
        String NAME = "name";
        String PRODUCT_NAME = "product_name";
        String PRODUCT_ID = "product_id";
        String SHAPE = "shape";
    }

    public interface WorkingPeriodsColumns extends BaseColumns {
        String TABLE_NAME = "working_periods";
        String FK_INTERVENTION = "fk_intervention";
        String NATURE = "nature";
        String STARTED_AT = "started_at";
        String STOPPED_AT = "stopped_at";
    }

    public interface CrumbsColumns extends BaseColumns {
        String TABLE_NAME = "crumbs";
        String TYPE = "type";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String READ_AT = "read_at";
        String ACCURACY = "accuracy";
        String METADATA = "metadata";
        String SYNCED = "synced";
        String USER = "user";
        String FK_INTERVENTION = "fk_intervention";
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
        String USER = "user";
    }

    public interface PlantCountingsColumns extends BaseColumns {
        String TABLE_NAME = "plant_countings";
        String OBSERVED_AT = "observed_at";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String OBSERVATION = "description";
        String SYNCED_AT = "synced_at";
        String PLANT_DENSITY_ABACUS_ITEM_ID = "plant_density_abacus_item_id";
        String PLANT_DENSITY_ABACUS_ID = "plant_density_abacus_id";
        String PLANT_ID = "plant_id";
        String SYNCED = "synced";
        String AVERAGE_VALUE = "average_value";
        String USER = "user";
        String NATURE = "nature";
    }

    public interface PlantCountingItemsColumns extends BaseColumns {
        String TABLE_NAME = "plant_counting_items";
        String VALUE = "value";
        String PLANT_COUNTING_ID = "plant_counting_id";
        String USER = "user";
    }

    public interface PlantDensityAbaciColumns extends BaseColumns {
        String TABLE_NAME = "plant_density_abaci";
        String EK_ID = "ek_id";
        String NAME = "name";
        String VARIETY = "variety";
        String GERMINATION_PERCENTAGE = "germination_percentage";
        String SEEDING_DENSITY_UNIT = "seeding_density_unit";
        String SAMPLING_LENGTH_UNIT = "sampling_length_unit";
        String USER = "user";
        String ACTIVITY_ID = "activity_id";
    }

    public interface PlantDensityAbacusItemsColumns extends BaseColumns {
        String TABLE_NAME = "plant_density_abacus_items";
        String EK_ID = "ek_id";
        String SEEDING_DENSITY_VALUE = "seeding_density_value";
        String PLANTS_COUNT = "plants_count";
        String USER = "user";
        String FK_ID = "fk_id";
    }

    public interface PlantsColumns extends BaseColumns {
        String TABLE_NAME = "plants";
        String EK_ID = "ek_id";
        String NAME = "name";
        String SHAPE = "shape";
        String VARIETY = "variety";
        String ACTIVE = "active";
        String USER = "user";
        String ACTIVITY_ID = "activity_id";
        String ACTIVITY_NAME = "activity_name";
    }

    public interface ContactsColumns extends BaseColumns {
        String TABLE_NAME = "contacts";
        String EK_ID = "ek_id";
        String TYPE = "type";
        String LAST_NAME = "last_name";
        String FIRST_NAME = "first_name";
        String USER = "user";
        String PICTURE = "picture";
        String PICTURE_ID = "picture_id";
        String ORGANIZATION_NAME = "organization_name";
        String ORGANIZATION_POST = "organization_post";
    }

    public interface ContactParamsColumns extends BaseColumns {
        String TABLE_NAME = "contact_params";
        String FK_CONTACT = "fk_contact";
        String TYPE = "type";
        String EMAIL = "email";
        String PHONE = "phone";
        String MOBILE = "mobile";
        String WEBSITE = "website";
        String MAIL_LINES = "mail_lines";
        String POSTAL_CODE = "postal_code";
        String CITY = "city";
        String COUNTRY = "country";
    }

    public interface LastSyncsColumns extends BaseColumns {
        String TABLE_NAME   = "last_syncs";
        String USER         = "user";
        String TYPE         = "type";
        String DATE         = "date";
    }

    public interface ObservationsColumns extends BaseColumns {
        String TABLE_NAME = "observations";
        String ACTIVITY_ID = "activity_id";
        String OBSERVED_ON = "observed_on";
        String SCALE_ID = "scale_id";
        String DESCRIPTION = "description";
        String PICTURES = "pictures";
        String PLANTS = "plants";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String SYNCED = "synced";
        String USER = "user";
    }

    public interface ObservationPlantColumns extends BaseColumns {
        String TABLE_NAME = "observation_plants";
        String FK_OBSERVATION = "fk_observation";
        String FK_PLANT = "fk_plant";
        String EKY_ID_PLANT = "plant_eky_id";
    }

    public interface ObservationIssueColumns extends BaseColumns {
        String TABLE_NAME = "observation_issues";
        String FK_OBSERVATION = "fk_observation";
        String FK_ISSUE = "fk_issue";
        String EKY_ID_ISSUE = "issue_eky_id";
    }

    public interface IssueNatureColumns extends BaseColumns {
        String TABLE_NAME = "issue_natures";
        String CATEGORY = "category";
        String LABEL = "label";
        String NATURE = "nature";
    }

    public interface VegetalScaleColumns extends BaseColumns {
        String TABLE_NAME = "vegetal_scale";
        String REFERENCE = "reference";
        String LABEL = "label";
        String VARIETY = "variety";
        String POSITION = "position";
    }

    public interface EquipmentColumns {
        String TABLE_NAME = "equipments";
        String EK_ID = "ek_id";
        String NAME = "name";
        String NUMBER = "number";
        String VARIETY = "variety";
        String ABILITIES = "abilities";
        String USER = "user";
    }

    public interface ArticleColumns extends BaseColumns {
        String TABLE_NAME = "articles";
        String EK_ID = "ek_id";
        String NAME = "name";
        String NUMBER = "number";
        String TYPE = "type";
        String UNIT = "unit";
        String USER = "user";
    }

    public interface WorkerColumns {
        String TABLE_NAME = "workers";
        String EK_ID = "ek_id";
        String NAME = "name";
        String NUMBER = "number";
        String QUALIFICATION = "qualification";
        String ABILITIES = "abilities";
        String USER = "user";
    }

    public interface LandParcelColumns {
        String TABLE_NAME = "land_parcels";
        String EK_ID = "ek_id";
        String NAME = "name";
        String NET_SURFACE_AREA = "net_surface_area";
        String USER = "user";
    }

    public interface InputColumns {
        String TABLE_NAME = "inputs";
        String EK_ID = "ek_id";
        String NAME = "name";
        String REFERENCE_NAME = "reference_name";
        String QUANTITY_UNIT_NAME = "quantity_unit_name";
        String VARIETY = "variety";
        String ABILITIES = "abilities";
        String USER = "user";
    }

    public interface OutputColumns extends BaseColumns {
        String TABLE_NAME = "outputs";
        String EK_ID = "ek_id";
        String NAME = "name";
        String REFERENCE_NAME = "reference_name";
        String QUANTITY_UNIT_NAME = "quantity_unit_name";
        String USER = "user";
    }

    public static final class Crumbs implements CrumbsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "crumbs");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.crumbs";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.crumb";

        public static final String[] PROJECTION_ALL = {_ID, TYPE, LATITUDE, LONGITUDE, READ_AT, ACCURACY, METADATA, SYNCED, FK_INTERVENTION};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static final class Interventions implements InterventionsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "interventions");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.interventions";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.intervention";

        public static final String[] PROJECTION_ALL = {_ID};
        public static final String[] PROJECTION_BASIC = {_ID, NAME, DESCRIPTION, STARTED_AT,
                STOPPED_AT, STATE};
        public static final String[] PROJECTION_PAUSED = {STATE, REQUEST_COMPLIANT,
                GENERAL_CHRONO, PREPARATION_CHRONO, TRAVEL_CHRONO, INTERVENTION_CHRONO, NAME};
        public static final String[] PROJECTION_POST = {_ID, EK_ID, PROCEDURE_NAME,
                REQUEST_COMPLIANT, STATE, UUID};
        public static final String[] PROJECTION_NONE = {_ID};
        public static final String[] PROJECTION_NUMBER = {_ID, NUMBER};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
        public static final String SORT_ORDER_LAST = _ID + " DESC LIMIT 1";

    }

    public static final class InterventionParameters implements InterventionParametersColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "intervention_parameters");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.intervention_parameters";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.intervention_parameter";

        public static final String[] PROJECTION_ALL = {_ID};
        public static final String[] PROJECTION_NONE = {_ID};
        public static final String[] PROJECTION_SHAPE = {SHAPE};
        public static final String[] PROJECTION_TARGET = {_ID, PRODUCT_NAME};
        public static final String[] PROJECTION_TARGET_FULL = {_ID, PRODUCT_NAME, LABEL, NAME};
        public static final String[] PROJECTION_INPUT_FULL = {_ID, PRODUCT_NAME, LABEL, NAME};
        public static final String[] PROJECTION_TOOL_FULL = {_ID, PRODUCT_NAME, LABEL, NAME};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static final class WorkingPeriods implements WorkingPeriodsColumns {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI, "working_periods");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.ekylibre.zero.working_periods";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.ekylibre.zero.working_periods";

        public static final String[] PROJECTION_ALL = {_ID};
        public static final String[] PROJECTION_POST = {_ID, STARTED_AT, STOPPED_AT, NATURE};
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

        public static final String[] PROJECTION_ALL = {_ID, OBSERVED_AT, LATITUDE, LONGITUDE,
                OBSERVATION,  PLANT_DENSITY_ABACUS_ITEM_ID, SYNCED_AT, PLANT_DENSITY_ABACUS_ID,
                PLANT_ID, AVERAGE_VALUE, NATURE};
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

        public static final String[] PROJECTION_ALL = {_ID, NAME, VARIETY, GERMINATION_PERCENTAGE, SEEDING_DENSITY_UNIT, SAMPLING_LENGTH_UNIT, ACTIVITY_ID};
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

        public static final String[] PROJECTION_ALL = {_ID, NAME, SHAPE, VARIETY, ACTIVE, ACTIVITY_ID};
        public static final String[] PROJECTION_OBS = {_ID, EK_ID, NAME, VARIETY, ACTIVITY_ID, ACTIVITY_NAME};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class Contacts implements ContactsColumns
    {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI,
                "contacts");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd" +
                ".ekylibre.zero.contacts";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/vnd.ekylibre.zero.contact";

        public static final String[] PROJECTION_ALL = {_ID, LAST_NAME, FIRST_NAME, USER, PICTURE};
        public static final String[] PROJECTION_NAME = {FIRST_NAME, LAST_NAME};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static final class ContactParams implements ContactParamsColumns
    {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI,
                "contact_params");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd" +
                ".ekylibre.zero.contact_params";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/vnd.ekylibre.zero.contact_param";

        public static final String[] PROJECTION_ALL = {_ID, FK_CONTACT, TYPE, EMAIL,
                PHONE, MOBILE, WEBSITE, MAIL_LINES, POSTAL_CODE, CITY, COUNTRY};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static final class LastSyncs implements LastSyncsColumns
    {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ZeroContract.CONTENT_URI,
                "last_syncs");
        // MIME type for lists of records.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd" +
                ".ekylibre.zero.last_syncs";
        // MIME type for individual record.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/vnd.ekylibre.zero.last_syncs";

        public static final String[] PROJECTION_ALL = {_ID, USER, TYPE, DATE};
        public static final String[] PROJECTION_DATE = {DATE};
        public static final String[] PROJECTION_NONE = {_ID};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static final class Observations implements ObservationsColumns
    {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"observations");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.observations";

        public static final String[] PROJECTION_ALL = {_ID, ACTIVITY_ID, OBSERVED_ON, PLANTS,
                SCALE_ID, PICTURES, DESCRIPTION, LONGITUDE, LATITUDE, USER};


        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";

    }

    public static final class ObservationPlants implements ObservationPlantColumns
    {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"observation_plants");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.observation_plants";

        public static final String[] PROJECTION_ALL = {_ID, FK_OBSERVATION, FK_PLANT, EKY_ID_PLANT};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class ObservationIssues implements ObservationIssueColumns
    {
        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"observation_issues");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.observation_issues";

        public static final String[] PROJECTION_ALL = {_ID, FK_OBSERVATION, FK_ISSUE, EKY_ID_ISSUE};

        public static final String SORT_ORDER_DEFAULT = _ID + " ASC";
    }

    public static final class IssueNatures implements IssueNatureColumns {

        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"issue_natures");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.issue_natures";

        public static final String[] PROJECTION_ALL = {_ID, CATEGORY, LABEL, NATURE};

        public static final String SORT_ORDER_DEFAULT = LABEL + " ASC";
    }

    public static final class VegetalScale implements VegetalScaleColumns {

        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"vegetal_scale");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.vegetal_scale";

        public static final String[] PROJECTION_ALL = {_ID, REFERENCE, LABEL, VARIETY, POSITION};

        public static final String SORT_ORDER_DEFAULT = POSITION + " ASC";
    }

    public static final class Equipments implements EquipmentColumns {

        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"equipments");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.equipments";

        public static final String[] PROJECTION_ALL = {EK_ID, NAME, NUMBER, VARIETY, ABILITIES, USER};

        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
    }

    public static final class Articles implements ArticleColumns {

        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"articles");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.articles";

        public static final String[] PROJECTION_ALL = {_ID, EK_ID, NAME, NUMBER, TYPE, UNIT, USER};

        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
    }

    public static final class Workers implements WorkerColumns {

        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"workers");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.workers";

        public static final String[] PROJECTION_ALL = {EK_ID, NAME, NUMBER, QUALIFICATION, ABILITIES, USER};

        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
    }

    public static final class LandParcels implements LandParcelColumns {

        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"land_parcels");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.land_parcels";

        public static final String[] PROJECTION_ALL = {EK_ID, NAME, NET_SURFACE_AREA, USER};

        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
    }

    public static final class Inputs implements InputColumns {

        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"inputs");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.inputs";

        public static final String[] PROJECTION_ALL = {EK_ID, NAME, REFERENCE_NAME, QUANTITY_UNIT_NAME, VARIETY, ABILITIES, USER};

        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
    }

    public static final class Outputs implements OutputColumns {

        // Content URI for this table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ZeroContract.CONTENT_URI,"outputs");
        // MIME type for list and individual record.
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd.ekylibre.zero.outputs";

        public static final String[] PROJECTION_ALL = {_ID, EK_ID, NAME, REFERENCE_NAME , QUANTITY_UNIT_NAME, USER};

        public static final String SORT_ORDER_DEFAULT = NAME + " ASC";
    }
}
