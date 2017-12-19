package ekylibre.zero.reception;

import android.content.Context;
import android.database.ContentObserver;
import android.database.DatabaseUtils;

import java.util.ArrayList;

import ekylibre.database.ZeroProvider;

/**
 * Created by Asus on 18/12/2017.
 */

public class ReceptionActivity {
    ArrayList<ReceptionDataModel> receptionDataModels;

    public long getTaskCount(long tasklist_id) {
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }


    public void loadata(Context context) {
        receptionDataModels = new ArrayList<ReceptionDataModel>();
        ContentObserver receptionContentResolverObserver = null;
        getContentResolver().delete(ZeroProvider.Contract.CONTENT_URI, null, null);
        for (int i = 0; i <; i++) {


        }


    }
}