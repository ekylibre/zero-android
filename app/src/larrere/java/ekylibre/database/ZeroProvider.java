package ekylibre.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import ekylibre.util.SelectionBuilder;

public class ZeroProvider extends ContentProvider {
    // Routes codes


    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using URI_MATCHER.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through URI_MATCHER, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    public static final int ROUTE_CRUMB_LIST = 100;
    public static final int ROUTE_CRUMB_ITEM = 101;
    public static final int ROUTE_ISSUE_LIST = 200;
    public static final int ROUTE_ISSUE_ITEM = 201;
    public static final int ROUTE_PLANT_COUNTING_LIST = 300;
    public static final int ROUTE_PLANT_COUNTING_ITEM = 301;
    public static final int ROUTE_PLANT_COUNTING_ITEM_LIST = 400;
    public static final int ROUTE_PLANT_COUNTING_ITEM_ITEM = 401;
    public static final int ROUTE_PLANT_DENSITY_ABACUS_LIST = 500;
    public static final int ROUTE_PLANT_DENSITY_ABACUS_ITEM = 501;
    public static final int ROUTE_PLANT_DENSITY_ABACUS_ITEM_LIST = 600;
    public static final int ROUTE_PLANT_DENSITY_ABACUS_ITEM_ITEM = 601;
    public static final int ROUTE_PLANT_LIST = 700;
    public static final int ROUTE_PLANT_ITEM = 701;
    public static final int ROUTE_INTERVENTION_LIST = 800;
    public static final int ROUTE_INTERVENTION_ITEM = 801;
    public static final int ROUTE_INTERVENTION_PARAMETERS_LIST = 900;
    public static final int ROUTE_INTERVENTION_PARAMETERS_ITEM = 901;
    public static final int ROUTE_WORKING_PERIODS_LIST = 1000;
    public static final int ROUTE_WORKING_PERIODS_ITEM = 1001;
    public static final int ROUTE_CONTACTS_LIST = 1100;
    public static final int ROUTE_CONTACTS_ITEM = 1101;
    public static final int ROUTE_CONTACT_PARAMS_LIST = 1200;
    public static final int ROUTE_CONTACT_PARAMS_ITEM = 1201;
    public static final int ROUTE_LAST_SYNCS_LIST = 1300;
    public static final int ROUTE_LAST_SYNCS_ITEM = 1301;

    // Observation feature
    public static final int ROUTE_OBSERVATIONS_LIST = 1400;
    public static final int ROUTE_OBSERVATIONS_ITEM = 1401;
    public static final int ROUTE_OBSERVATION_PLANTS_LIST = 1402;
    public static final int ROUTE_OBSERVATION_PLANTS_ITEM = 1403;
    public static final int ROUTE_OBSERVATION_ISSUES_LIST = 1404;
    public static final int ROUTE_OBSERVATION_ISSUES_ITEM = 1405;
    public static final int ROUTE_ISSUE_NATURES_LIST = 1406;
    public static final int ROUTE_ISSUE_NATURES_ITEM = 1407;
    public static final int ROUTE_VEGETAL_SCALE_LIST = 1408;
    public static final int ROUTE_VEGETAL_SCALE_ITEM = 1409;

    // Intervention with params feature
    public static final int ROUTE_EQUIPMENTS_LIST = 1500;
    public static final int ROUTE_EQUIPMENTS_ITEM = 1501;
    public static final int ROUTE_WORKING_PERIOD_ATTRIBUTES_LIST = 1502;
    public static final int ROUTE_WORKING_PERIOD_ATTRIBUTES_ITEM = 1503;
    public static final int ROUTE_WORKERS_LIST = 1504;
    public static final int ROUTE_WORKERS_ITEM = 1505;
    public static final int ROUTE_LAND_PARCELS_LIST = 1506;
    public static final int ROUTE_LAND_PARCELS_ITEM = 1507;
    public static final int ROUTE_INPUTS_LIST = 1508;
    public static final int ROUTE_INPUTS_ITEM = 1509;
    public static final int ROUTE_OUTPUTS_LIST = 1510;
    public static final int ROUTE_OUTPUTS_ITEM = 1511;
    public static final int ROUTE_DETAILED_INTERVENTIONS_LIST = 1512;
    public static final int ROUTE_DETAILED_INTERVENTIONS_ITEM = 1513;
    public static final int ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_LIST = 1514;
    public static final int ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_ITEM = 1515;
    public static final int ROUTE_BUILDING_DIVISIONS_LIST = 1516;
    public static final int ROUTE_BUILDING_DIVISIONS_ITEM = 1517;

    // UriMatcher, used to decode incoming URIs.
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "crumbs", ROUTE_CRUMB_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "crumbs/#", ROUTE_CRUMB_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "issues", ROUTE_ISSUE_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "issues/#", ROUTE_ISSUE_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_countings", ROUTE_PLANT_COUNTING_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_countings/#", ROUTE_PLANT_COUNTING_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_counting_items", ROUTE_PLANT_COUNTING_ITEM_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_counting_items/#", ROUTE_PLANT_COUNTING_ITEM_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_density_abaci", ROUTE_PLANT_DENSITY_ABACUS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_density_abaci/#", ROUTE_PLANT_DENSITY_ABACUS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_density_abacus_items", ROUTE_PLANT_DENSITY_ABACUS_ITEM_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plant_density_abacus_items/#", ROUTE_PLANT_DENSITY_ABACUS_ITEM_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plants", ROUTE_PLANT_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "plants/#", ROUTE_PLANT_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "interventions", ROUTE_INTERVENTION_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "interventions/#", ROUTE_INTERVENTION_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "intervention_parameters", ROUTE_INTERVENTION_PARAMETERS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "intervention_parameters/#", ROUTE_INTERVENTION_PARAMETERS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "working_periods", ROUTE_WORKING_PERIODS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "working_periods/#", ROUTE_WORKING_PERIODS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "contacts", ROUTE_CONTACTS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "contacts/#", ROUTE_CONTACTS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "contact_params", ROUTE_CONTACT_PARAMS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "contact_params/#", ROUTE_CONTACT_PARAMS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "last_syncs", ROUTE_LAST_SYNCS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "last_syncs/#", ROUTE_LAST_SYNCS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "observations", ROUTE_OBSERVATIONS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "observations/#", ROUTE_OBSERVATIONS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "observation_plants", ROUTE_OBSERVATION_PLANTS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "observation_plants/#", ROUTE_OBSERVATION_PLANTS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "observation_issues", ROUTE_OBSERVATION_ISSUES_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "observation_issues/#", ROUTE_OBSERVATION_ISSUES_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "issue_natures", ROUTE_ISSUE_NATURES_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "issue_natures/#", ROUTE_ISSUE_NATURES_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "vegetal_scale", ROUTE_VEGETAL_SCALE_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "vegetal_scale/#", ROUTE_VEGETAL_SCALE_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "equipments", ROUTE_EQUIPMENTS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "equipments/#", ROUTE_EQUIPMENTS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "workers", ROUTE_WORKERS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "workers/#", ROUTE_WORKERS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "land_parcels", ROUTE_LAND_PARCELS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "land_parcels/#", ROUTE_LAND_PARCELS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "inputs", ROUTE_INPUTS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "inputs/#", ROUTE_INPUTS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "outputs", ROUTE_OUTPUTS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "outputs/#", ROUTE_OUTPUTS_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "detailed_interventions", ROUTE_DETAILED_INTERVENTIONS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "detailed_interventions/#", ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "detailed_intervention_attributes", ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "detailed_intervention_attributes/#", ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "working_period_attributes", ROUTE_WORKING_PERIOD_ATTRIBUTES_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "working_period_attributes/#", ROUTE_WORKING_PERIOD_ATTRIBUTES_ITEM);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "building_divisions", ROUTE_BUILDING_DIVISIONS_LIST);
        URI_MATCHER.addURI(ZeroContract.AUTHORITY, "building_divisions/#", ROUTE_BUILDING_DIVISIONS_ITEM);
    }

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    // Determine the mime variety for records returned by a given URI.
    @Override
    public String getType(@NonNull Uri uri) {

        switch (URI_MATCHER.match(uri)) {
            case ROUTE_CRUMB_LIST:
                return ZeroContract.Crumbs.CONTENT_TYPE;
            case ROUTE_CRUMB_ITEM:
                return ZeroContract.Crumbs.CONTENT_ITEM_TYPE;
            case ROUTE_ISSUE_LIST:
                return ZeroContract.Issues.CONTENT_TYPE;
            case ROUTE_ISSUE_ITEM:
                return ZeroContract.Issues.CONTENT_ITEM_TYPE;
            case ROUTE_PLANT_COUNTING_LIST:
                return ZeroContract.PlantCountings.CONTENT_TYPE;
            case ROUTE_PLANT_COUNTING_ITEM:
                return ZeroContract.PlantCountings.CONTENT_ITEM_TYPE;
            case ROUTE_PLANT_COUNTING_ITEM_LIST:
                return ZeroContract.PlantCountingItems.CONTENT_TYPE;
            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                return ZeroContract.PlantCountingItems.CONTENT_TYPE;
            case ROUTE_PLANT_DENSITY_ABACUS_LIST:
                return ZeroContract.PlantDensityAbaci.CONTENT_TYPE;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM:
                return ZeroContract.PlantDensityAbaci.CONTENT_TYPE;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_LIST:
                return ZeroContract.PlantDensityAbacusItems.CONTENT_TYPE;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_ITEM:
                return ZeroContract.PlantDensityAbacusItems.CONTENT_TYPE;
            case ROUTE_PLANT_LIST:
                return ZeroContract.Plants.CONTENT_TYPE;
            case ROUTE_PLANT_ITEM:
                return ZeroContract.Plants.CONTENT_TYPE;
            case ROUTE_INTERVENTION_LIST:
                return ZeroContract.Interventions.CONTENT_TYPE;
            case ROUTE_INTERVENTION_ITEM:
                return ZeroContract.Interventions.CONTENT_TYPE;
            case ROUTE_INTERVENTION_PARAMETERS_LIST:
                return ZeroContract.InterventionParameters.CONTENT_TYPE;
            case ROUTE_INTERVENTION_PARAMETERS_ITEM:
                return ZeroContract.InterventionParameters.CONTENT_TYPE;
            case ROUTE_WORKING_PERIODS_LIST:
                return ZeroContract.WorkingPeriods.CONTENT_TYPE;
            case ROUTE_WORKING_PERIODS_ITEM:
                return ZeroContract.WorkingPeriods.CONTENT_TYPE;
            case ROUTE_CONTACTS_LIST:
                return ZeroContract.Contacts.CONTENT_TYPE;
            case ROUTE_CONTACTS_ITEM:
                return ZeroContract.Contacts.CONTENT_TYPE;
            case ROUTE_CONTACT_PARAMS_LIST:
                return ZeroContract.ContactParams.CONTENT_TYPE;
            case ROUTE_CONTACT_PARAMS_ITEM:
                return ZeroContract.ContactParams.CONTENT_TYPE;
            case ROUTE_LAST_SYNCS_LIST:
                return ZeroContract.LastSyncs.CONTENT_TYPE;
            case ROUTE_LAST_SYNCS_ITEM:
                return ZeroContract.LastSyncs.CONTENT_TYPE;

            case ROUTE_OBSERVATIONS_LIST:
            case ROUTE_OBSERVATIONS_ITEM:
                return ZeroContract.Observations.CONTENT_TYPE;

            case ROUTE_OBSERVATION_PLANTS_LIST:
            case ROUTE_OBSERVATION_PLANTS_ITEM:
                return ZeroContract.ObservationPlants.CONTENT_TYPE;

            case ROUTE_OBSERVATION_ISSUES_LIST:
            case ROUTE_OBSERVATION_ISSUES_ITEM:
                return ZeroContract.ObservationIssues.CONTENT_TYPE;

            case ROUTE_ISSUE_NATURES_LIST:
            case ROUTE_ISSUE_NATURES_ITEM:
                return ZeroContract.IssueNatures.CONTENT_TYPE;

            case ROUTE_VEGETAL_SCALE_LIST:
            case ROUTE_VEGETAL_SCALE_ITEM:
                return ZeroContract.VegetalScale.CONTENT_TYPE;

            case ROUTE_EQUIPMENTS_LIST:
            case ROUTE_EQUIPMENTS_ITEM:
                return ZeroContract.Equipments.CONTENT_TYPE;

            case ROUTE_WORKERS_LIST:
            case ROUTE_WORKERS_ITEM:
                return ZeroContract.Workers.CONTENT_TYPE;

            case ROUTE_LAND_PARCELS_LIST:
            case ROUTE_LAND_PARCELS_ITEM:
                return ZeroContract.LandParcels.CONTENT_TYPE;

            case ROUTE_BUILDING_DIVISIONS_LIST:
            case ROUTE_BUILDING_DIVISIONS_ITEM:
                return ZeroContract.BuildingDivisions.CONTENT_TYPE;

            case ROUTE_INPUTS_LIST:
            case ROUTE_INPUTS_ITEM:
                return ZeroContract.Inputs.CONTENT_TYPE;

            case ROUTE_OUTPUTS_LIST:
            case ROUTE_OUTPUTS_ITEM:
                return ZeroContract.Outputs.CONTENT_TYPE;

            case ROUTE_DETAILED_INTERVENTIONS_LIST:
            case ROUTE_DETAILED_INTERVENTIONS_ITEM:
                return ZeroContract.DetailedInterventions.CONTENT_TYPE;

            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_LIST:
            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_ITEM:
                return ZeroContract.DetailedInterventionAttributes.CONTENT_TYPE;

            case ROUTE_WORKING_PERIOD_ATTRIBUTES_LIST:
            case ROUTE_WORKING_PERIOD_ATTRIBUTES_ITEM:
                return ZeroContract.WorkingPeriodAttributes.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    /**
     * Perform a database query by URI.
     * <p/>
     * <p>Currently supports returning all records (/records) and individual records by ID
     * (/records/{ID}).
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        String id;
        Cursor cursor;
        Context context = getContext();
        assert context != null;
        ContentResolver contentResolver = context.getContentResolver();
        switch (URI_MATCHER.match(uri)) {
            case ROUTE_CRUMB_ITEM:
                // Return a single crumb, by ID.
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.CrumbsColumns._ID + "=?", id);
            case ROUTE_CRUMB_LIST:
                // Return all known crumbs.
                builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_ISSUE_ITEM:
                // Return a single ISSUE, by ID.
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.IssuesColumns._ID + "=?", id);
            case ROUTE_ISSUE_LIST:
                // Return all known Issue.
                builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_PLANT_COUNTING_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantCountingsColumns._ID + "=?", id);
            case ROUTE_PLANT_COUNTING_LIST:
                builder.table(ZeroContract.PlantCountingsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantCountingItemsColumns._ID + "=?", id);
            case ROUTE_PLANT_COUNTING_ITEM_LIST:                // Return all known Issue.
                builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_PLANT_DENSITY_ABACUS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantDensityAbaciColumns._ID + "=?", id);
            case ROUTE_PLANT_DENSITY_ABACUS_LIST:                // Return all known Issue.
                builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantDensityAbacusItemsColumns._ID + "=?", id);
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_LIST:                // Return all known Issue.
                builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_PLANT_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.PlantsColumns._ID + "=?", id);
            case ROUTE_PLANT_LIST:                // Return all known Issue.
                builder.table(ZeroContract.PlantsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_INTERVENTION_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.InterventionsColumns._ID + "=?", id);
            case ROUTE_INTERVENTION_LIST:
                builder.table(ZeroContract.InterventionsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_INTERVENTION_PARAMETERS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.InterventionParametersColumns._ID + "=?", id);
            case ROUTE_INTERVENTION_PARAMETERS_LIST:
                builder.table(ZeroContract.InterventionParametersColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_WORKING_PERIODS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.WorkingPeriodsColumns._ID + "=?", id);
            case ROUTE_WORKING_PERIODS_LIST:
                builder.table(ZeroContract.WorkingPeriodsColumns.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_CONTACTS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.Contacts._ID + "=?", id);
            case ROUTE_CONTACTS_LIST:
                builder.table(ZeroContract.Contacts.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_CONTACT_PARAMS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.ContactParams._ID + "=?", id);
            case ROUTE_CONTACT_PARAMS_LIST:
                builder.table(ZeroContract.ContactParams.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_LAST_SYNCS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.LastSyncs._ID + "=?", id);
            case ROUTE_LAST_SYNCS_LIST:
                builder.table(ZeroContract.LastSyncs.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_OBSERVATIONS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.Observations._ID + "=?", id);
            case ROUTE_OBSERVATIONS_LIST:
                builder.table(ZeroContract.Observations.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_OBSERVATION_PLANTS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.ObservationPlants._ID + "=?", id);
            case ROUTE_OBSERVATION_PLANTS_LIST:
                builder.table(ZeroContract.ObservationPlants.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_OBSERVATION_ISSUES_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.ObservationIssues._ID + "=?", id);
            case ROUTE_OBSERVATION_ISSUES_LIST:
                builder.table(ZeroContract.ObservationIssues.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_ISSUE_NATURES_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.IssueNatures._ID + "=?", id);
            case ROUTE_ISSUE_NATURES_LIST:
                builder.table(ZeroContract.IssueNatures.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_VEGETAL_SCALE_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.VegetalScale._ID + "=?", id);
            case ROUTE_VEGETAL_SCALE_LIST:
                builder.table(ZeroContract.VegetalScale.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_EQUIPMENTS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.Equipments.EK_ID + "=?", id);
            case ROUTE_EQUIPMENTS_LIST:
                builder.table(ZeroContract.Equipments.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_WORKERS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.Workers.EK_ID + "=?", id);
            case ROUTE_WORKERS_LIST:
                builder.table(ZeroContract.Workers.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_LAND_PARCELS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.LandParcels.EK_ID + "=?", id);
            case ROUTE_LAND_PARCELS_LIST:
                builder.table(ZeroContract.LandParcels.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_BUILDING_DIVISIONS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.BuildingDivisions.EK_ID + "=?", id);
            case ROUTE_BUILDING_DIVISIONS_LIST:
                builder.table(ZeroContract.BuildingDivisions.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_INPUTS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.Inputs.EK_ID + "=?", id);
            case ROUTE_INPUTS_LIST:
                builder.table(ZeroContract.Inputs.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_OUTPUTS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.Outputs.EK_ID + "=?", id);
            case ROUTE_OUTPUTS_LIST:
                builder.table(ZeroContract.Outputs.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_DETAILED_INTERVENTIONS_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.DetailedInterventions._ID + "=?", id);
            case ROUTE_DETAILED_INTERVENTIONS_LIST:
                builder.table(ZeroContract.DetailedInterventions.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.DetailedInterventionAttributes._ID + "=?", id);
            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_LIST:
                builder.table(ZeroContract.DetailedInterventionAttributes.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            case ROUTE_WORKING_PERIOD_ATTRIBUTES_ITEM:
                id = uri.getLastPathSegment();
                builder.where(ZeroContract.WorkingPeriodAttributes._ID + "=?", id);
            case ROUTE_WORKING_PERIOD_ATTRIBUTES_LIST:
                builder.table(ZeroContract.WorkingPeriodAttributes.TABLE_NAME)
                        .where(selection, selectionArgs);
                cursor = builder.query(database, projection, sortOrder);
                cursor.setNotificationUri(contentResolver, uri);
                return cursor;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
    }

    /**
     * Insert a new record into the database.
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        assert database != null;
        final int match = URI_MATCHER.match(uri);
        Uri result;
        long id;
        switch (match) {
            case ROUTE_CRUMB_LIST:
                id = database.insertOrThrow(ZeroContract.CrumbsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.Crumbs.CONTENT_URI + "/" + id);
                break;
            case ROUTE_CRUMB_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_ISSUE_LIST:
                id = database.insertOrThrow(ZeroContract.IssuesColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.Issues.CONTENT_URI + "/" + id);
                break;
            case ROUTE_ISSUE_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_COUNTING_LIST:
                id = database.insertOrThrow(ZeroContract.PlantCountingsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.PlantCountings.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_COUNTING_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_COUNTING_ITEM_LIST:
                id = database.insertOrThrow(ZeroContract.PlantCountingItemsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.PlantCountingItems.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_DENSITY_ABACUS_LIST:
                id = database.insertOrThrow(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.PlantDensityAbaci.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_LIST:
                id = database.insertOrThrow(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.PlantDensityAbacusItems.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_PLANT_LIST:
                id = database.insertWithOnConflict(ZeroContract.PlantsColumns.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result = Uri.parse(ZeroContract.Plants.CONTENT_URI + "/" + id);
                break;
            case ROUTE_PLANT_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_INTERVENTION_LIST:
                id = database.insertOrThrow(ZeroContract.InterventionsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.Interventions.CONTENT_URI + "/" + id);
                break;
            case ROUTE_INTERVENTION_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_INTERVENTION_PARAMETERS_LIST:
                id = database.insertOrThrow(ZeroContract.InterventionParametersColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.InterventionParameters.CONTENT_URI + "/" + id);
                break;
            case ROUTE_INTERVENTION_PARAMETERS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_WORKING_PERIODS_LIST:
                id = database.insertOrThrow(ZeroContract.WorkingPeriodsColumns.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.WorkingPeriods.CONTENT_URI + "/" + id);
                break;
            case ROUTE_WORKING_PERIODS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_CONTACTS_LIST:
                id = database.insertOrThrow(ZeroContract.Contacts.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.Contacts.CONTENT_URI + "/" + id);
                break;
            case ROUTE_CONTACTS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_CONTACT_PARAMS_LIST:
                id = database.insertOrThrow(ZeroContract.ContactParams.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.ContactParams.CONTENT_URI + "/" + id);
                break;
            case ROUTE_CONTACT_PARAMS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_LAST_SYNCS_LIST:
                id = database.insertOrThrow(ZeroContract.LastSyncs.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.LastSyncs.CONTENT_URI + "/" + id);
                break;
            case ROUTE_LAST_SYNCS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_OBSERVATIONS_LIST:
                id = database.insertOrThrow(ZeroContract.Observations.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.Observations.CONTENT_URI + "/" + id);
                break;
            case ROUTE_OBSERVATIONS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_OBSERVATION_PLANTS_LIST:
                id = database.insertOrThrow(ZeroContract.ObservationPlants.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.ObservationPlants.CONTENT_URI + "/" + id);
                break;
            case ROUTE_OBSERVATION_PLANTS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_OBSERVATION_ISSUES_LIST:
                id = database.insertOrThrow(ZeroContract.ObservationIssues.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.ObservationIssues.CONTENT_URI + "/" + id);
                break;
            case ROUTE_OBSERVATION_ISSUES_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_ISSUE_NATURES_LIST:
                id = database.insertOrThrow(ZeroContract.IssueNatures.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.IssueNatures.CONTENT_URI + "/" + id);
                break;
            case ROUTE_ISSUE_NATURES_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_VEGETAL_SCALE_LIST:
                id = database.insertOrThrow(ZeroContract.VegetalScale.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.VegetalScale.CONTENT_URI + "/" + id);
                break;
            case ROUTE_VEGETAL_SCALE_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_EQUIPMENTS_LIST:
                id = database.insertWithOnConflict(ZeroContract.Equipments.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result = Uri.parse(ZeroContract.Equipments.CONTENT_URI + "/" + id);
                break;
            case ROUTE_EQUIPMENTS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_WORKERS_LIST:
                id = database.insertWithOnConflict(ZeroContract.Workers.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result = Uri.parse(ZeroContract.Workers.CONTENT_URI + "/" + id);
                break;
            case ROUTE_WORKERS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_LAND_PARCELS_LIST:
                id = database.insertWithOnConflict(ZeroContract.LandParcels.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result = Uri.parse(ZeroContract.LandParcels.CONTENT_URI + "/" + id);
                break;
            case ROUTE_LAND_PARCELS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_BUILDING_DIVISIONS_LIST:
                id = database.insertWithOnConflict(ZeroContract.BuildingDivisions.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result = Uri.parse(ZeroContract.BuildingDivisions.CONTENT_URI + "/" + id);
                break;
            case ROUTE_BUILDING_DIVISIONS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_INPUTS_LIST:
                id = database.insertWithOnConflict(ZeroContract.Inputs.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result = Uri.parse(ZeroContract.Inputs.CONTENT_URI + "/" + id);
                break;
            case ROUTE_INPUTS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_OUTPUTS_LIST:
                id = database.insertWithOnConflict(ZeroContract.Outputs.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                result = Uri.parse(ZeroContract.Outputs.CONTENT_URI + "/" + id);
                break;
            case ROUTE_OUTPUTS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_DETAILED_INTERVENTIONS_LIST:
                id = database.insert(ZeroContract.DetailedInterventions.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.DetailedInterventions.CONTENT_URI + "/" + id);
                break;
            case ROUTE_DETAILED_INTERVENTIONS_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_LIST:
                id = database.insert(ZeroContract.DetailedInterventionAttributes.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.DetailedInterventionAttributes.CONTENT_URI + "/" + id);
                break;
            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

            case ROUTE_WORKING_PERIOD_ATTRIBUTES_LIST:
                id = database.insert(ZeroContract.WorkingPeriodAttributes.TABLE_NAME, null, values);
                result = Uri.parse(ZeroContract.WorkingPeriodAttributes.CONTENT_URI + "/" + id);
                break;
            case ROUTE_WORKING_PERIOD_ATTRIBUTES_ITEM:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);

                default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    /**
     * Delete a record by database by URI.
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int count;
        String id = uri.getLastPathSegment();
        switch (match) {
            case ROUTE_CRUMB_LIST:
                count = builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_CRUMB_ITEM:
                count = builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(ZeroContract.CrumbsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_ISSUE_LIST:
                count = builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_ISSUE_ITEM:
                count = builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(ZeroContract.IssuesColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_COUNTING_LIST:
                count = builder.table(ZeroContract.PlantCountingsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_COUNTING_ITEM:
                count = builder.table(ZeroContract.PlantCountingsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantCountingsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_COUNTING_ITEM_LIST:
                count = builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                count = builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantCountingItemsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_LIST:
                count = builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM:
                count = builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(ZeroContract.PlantDensityAbaciColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_LIST:
                count = builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_ITEM:
                count = builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantDensityAbacusItemsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_LIST:
                count = builder.table(ZeroContract.PlantsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_PLANT_ITEM:
                count = builder.table(ZeroContract.PlantsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_INTERVENTION_LIST:
                count = builder.table(ZeroContract.InterventionsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_INTERVENTION_ITEM:
                count = builder.table(ZeroContract.InterventionsColumns.TABLE_NAME)
                        .where(ZeroContract.InterventionsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_INTERVENTION_PARAMETERS_LIST:
                count = builder.table(ZeroContract.InterventionParametersColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_INTERVENTION_PARAMETERS_ITEM:
                count = builder.table(ZeroContract.InterventionParametersColumns.TABLE_NAME)
                        .where(ZeroContract.InterventionParametersColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_WORKING_PERIODS_LIST:
                count = builder.table(ZeroContract.WorkingPeriodsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_WORKING_PERIODS_ITEM:
                count = builder.table(ZeroContract.WorkingPeriodsColumns.TABLE_NAME)
                        .where(ZeroContract.WorkingPeriodsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_CONTACTS_LIST:
                count = builder.table(ZeroContract.Contacts.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_CONTACTS_ITEM:
                count = builder.table(ZeroContract.Contacts.TABLE_NAME)
                        .where(ZeroContract.Contacts._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_CONTACT_PARAMS_LIST:
                count = builder.table(ZeroContract.ContactParams.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_CONTACT_PARAMS_ITEM:
                count = builder.table(ZeroContract.ContactParams.TABLE_NAME)
                        .where(ZeroContract.ContactParams._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_LAST_SYNCS_LIST:
                count = builder.table(ZeroContract.LastSyncs.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_LAST_SYNCS_ITEM:
                count = builder.table(ZeroContract.LastSyncs.TABLE_NAME)
                        .where(ZeroContract.LastSyncs._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_OBSERVATIONS_LIST:
                count = builder.table(ZeroContract.LastSyncs.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_OBSERVATIONS_ITEM:
                count = builder.table(ZeroContract.Observations.TABLE_NAME)
                        .where(ZeroContract.Observations._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_OBSERVATION_PLANTS_LIST:
                count = builder.table(ZeroContract.ObservationPlants.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_OBSERVATION_PLANTS_ITEM:
                count = builder.table(ZeroContract.ObservationPlants.TABLE_NAME)
                        .where(ZeroContract.ObservationPlants._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_OBSERVATION_ISSUES_LIST:
                count = builder.table(ZeroContract.ObservationIssues.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_OBSERVATION_ISSUES_ITEM:
                count = builder.table(ZeroContract.ObservationIssues.TABLE_NAME)
                        .where(ZeroContract.ObservationIssues._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_ISSUE_NATURES_LIST:
                count = builder.table(ZeroContract.IssueNatures.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_ISSUE_NATURES_ITEM:
                count = builder.table(ZeroContract.IssueNatures.TABLE_NAME)
                        .where(ZeroContract.IssueNatures._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_VEGETAL_SCALE_LIST:
                count = builder.table(ZeroContract.VegetalScale.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_VEGETAL_SCALE_ITEM:
                count = builder.table(ZeroContract.VegetalScale.TABLE_NAME)
                        .where(ZeroContract.VegetalScale._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_EQUIPMENTS_LIST:
                count = builder.table(ZeroContract.Equipments.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_EQUIPMENTS_ITEM:
                count = builder.table(ZeroContract.Equipments.TABLE_NAME)
                        .where(ZeroContract.Equipments.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_WORKERS_LIST:
                count = builder.table(ZeroContract.Workers.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_WORKERS_ITEM:
                count = builder.table(ZeroContract.Workers.TABLE_NAME)
                        .where(ZeroContract.Workers.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_LAND_PARCELS_LIST:
                count = builder.table(ZeroContract.LandParcels.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_LAND_PARCELS_ITEM:
                count = builder.table(ZeroContract.LandParcels.TABLE_NAME)
                        .where(ZeroContract.LandParcels.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_BUILDING_DIVISIONS_LIST:
                count = builder.table(ZeroContract.BuildingDivisions.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_BUILDING_DIVISIONS_ITEM:
                count = builder.table(ZeroContract.BuildingDivisions.TABLE_NAME)
                        .where(ZeroContract.BuildingDivisions.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_INPUTS_LIST:
                count = builder.table(ZeroContract.Inputs.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_INPUTS_ITEM:
                count = builder.table(ZeroContract.Inputs.TABLE_NAME)
                        .where(ZeroContract.Inputs.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_OUTPUTS_LIST:
                count = builder.table(ZeroContract.Outputs.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_OUTPUTS_ITEM:
                count = builder.table(ZeroContract.Outputs.TABLE_NAME)
                        .where(ZeroContract.Outputs.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_DETAILED_INTERVENTIONS_LIST:
                count = builder.table(ZeroContract.DetailedInterventions.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_DETAILED_INTERVENTIONS_ITEM:
                count = builder.table(ZeroContract.DetailedInterventions.TABLE_NAME)
                        .where(ZeroContract.DetailedInterventions._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_LIST:
                count = builder.table(ZeroContract.DetailedInterventionAttributes.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_ITEM:
                count = builder.table(ZeroContract.DetailedInterventionAttributes.TABLE_NAME)
                        .where(ZeroContract.DetailedInterventionAttributes._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            case ROUTE_WORKING_PERIOD_ATTRIBUTES_LIST:
                count = builder.table(ZeroContract.WorkingPeriodAttributes.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;
            case ROUTE_WORKING_PERIOD_ATTRIBUTES_ITEM:
                count = builder.table(ZeroContract.WorkingPeriodAttributes.TABLE_NAME)
                        .where(ZeroContract.WorkingPeriodAttributes._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(database);
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * Update a record in the database by URI.
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int count;
        String id;
        switch (match) {
            case ROUTE_CRUMB_LIST:
                count = builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_CRUMB_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.CrumbsColumns.TABLE_NAME)
                        .where(ZeroContract.CrumbsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_ISSUE_LIST:
                count = builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_ISSUE_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.IssuesColumns.TABLE_NAME)
                        .where(ZeroContract.IssuesColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_PLANT_COUNTING_LIST:
                count = builder.table(ZeroContract.PlantCountingsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_COUNTING_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantCountingsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantCountingsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_PLANT_COUNTING_ITEM_LIST:
                count = builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_COUNTING_ITEM_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantCountingItemsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantCountingItemsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_LIST:
                count = builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantDensityAbaciColumns.TABLE_NAME)
                        .where(ZeroContract.PlantDensityAbaciColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_LIST:
                count = builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_DENSITY_ABACUS_ITEM_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantDensityAbacusItemsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantDensityAbacusItemsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_LIST:
                count = builder.table(ZeroContract.PlantsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_PLANT_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.PlantsColumns.TABLE_NAME)
                        .where(ZeroContract.PlantsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_INTERVENTION_LIST:
                count = builder.table(ZeroContract.InterventionsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_INTERVENTION_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.InterventionsColumns.TABLE_NAME)
                        .where(ZeroContract.InterventionsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_INTERVENTION_PARAMETERS_LIST:
                count = builder.table(ZeroContract.InterventionParametersColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_INTERVENTION_PARAMETERS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.InterventionParametersColumns.TABLE_NAME)
                        .where(ZeroContract.InterventionParametersColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_WORKING_PERIODS_LIST:
                count = builder.table(ZeroContract.WorkingPeriodsColumns.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_WORKING_PERIODS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.WorkingPeriodsColumns.TABLE_NAME)
                        .where(ZeroContract.WorkingPeriodsColumns._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_CONTACTS_LIST:
                count = builder.table(ZeroContract.Contacts.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_CONTACTS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.Contacts.TABLE_NAME)
                        .where(ZeroContract.Contacts._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_CONTACT_PARAMS_LIST:
                count = builder.table(ZeroContract.ContactParams.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_CONTACT_PARAMS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.ContactParams.TABLE_NAME)
                        .where(ZeroContract.ContactParams._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_LAST_SYNCS_LIST:
                count = builder.table(ZeroContract.LastSyncs.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_LAST_SYNCS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.LastSyncs.TABLE_NAME)
                        .where(ZeroContract.LastSyncs._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_OBSERVATIONS_LIST:
                count = builder.table(ZeroContract.Observations.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_OBSERVATIONS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.Observations.TABLE_NAME)
                        .where(ZeroContract.Observations._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_OBSERVATION_PLANTS_LIST:
                count = builder.table(ZeroContract.ObservationPlants.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_OBSERVATION_PLANTS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.ObservationPlants.TABLE_NAME)
                        .where(ZeroContract.ObservationPlants._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_OBSERVATION_ISSUES_LIST:
                count = builder.table(ZeroContract.ObservationIssues.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_OBSERVATION_ISSUES_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.ObservationIssues.TABLE_NAME)
                        .where(ZeroContract.ObservationIssues._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_ISSUE_NATURES_LIST:
                count = builder.table(ZeroContract.IssueNatures.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_ISSUE_NATURES_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.IssueNatures.TABLE_NAME)
                        .where(ZeroContract.IssueNatures._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_VEGETAL_SCALE_LIST:
                count = builder.table(ZeroContract.VegetalScale.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_VEGETAL_SCALE_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.VegetalScale.TABLE_NAME)
                        .where(ZeroContract.VegetalScale._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_EQUIPMENTS_LIST:
                count = builder.table(ZeroContract.Equipments.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_EQUIPMENTS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.Equipments.TABLE_NAME)
                        .where(ZeroContract.Equipments.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_WORKERS_LIST:
                count = builder.table(ZeroContract.Workers.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_WORKERS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.Workers.TABLE_NAME)
                        .where(ZeroContract.Workers.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_LAND_PARCELS_LIST:
                count = builder.table(ZeroContract.LandParcels.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_LAND_PARCELS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.LandParcels.TABLE_NAME)
                        .where(ZeroContract.LandParcels.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_BUILDING_DIVISIONS_LIST:
                count = builder.table(ZeroContract.BuildingDivisions.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_BUILDING_DIVISIONS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.BuildingDivisions.TABLE_NAME)
                        .where(ZeroContract.BuildingDivisions.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_INPUTS_LIST:
                count = builder.table(ZeroContract.Inputs.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_INPUTS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.Inputs.TABLE_NAME)
                        .where(ZeroContract.Inputs.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_OUTPUTS_LIST:
                count = builder.table(ZeroContract.Outputs.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_OUTPUTS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.Outputs.TABLE_NAME)
                        .where(ZeroContract.Outputs.EK_ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_DETAILED_INTERVENTIONS_LIST:
                count = builder.table(ZeroContract.DetailedInterventions.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_DETAILED_INTERVENTIONS_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.DetailedInterventions.TABLE_NAME)
                        .where(ZeroContract.DetailedInterventions._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_LIST:
                count = builder.table(ZeroContract.DetailedInterventionAttributes.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_DETAILED_INTERVENTION_ATTRIBUTES_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.DetailedInterventionAttributes.TABLE_NAME)
                        .where(ZeroContract.DetailedInterventionAttributes._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            case ROUTE_WORKING_PERIOD_ATTRIBUTES_LIST:
                count = builder.table(ZeroContract.WorkingPeriodAttributes.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;
            case ROUTE_WORKING_PERIOD_ATTRIBUTES_ITEM:
                id = uri.getLastPathSegment();
                count = builder.table(ZeroContract.WorkingPeriodAttributes.TABLE_NAME)
                        .where(ZeroContract.WorkingPeriodAttributes._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(database, values);
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        Context context = getContext();
        assert context != null;
        context.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

}
