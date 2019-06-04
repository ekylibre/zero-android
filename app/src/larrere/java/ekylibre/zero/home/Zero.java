package ekylibre.zero.home;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**************************************
 * Created by pierre on 9/19/16.      *
 * ekylibre.zero.home for zero-android    *
 *************************************/
public class Zero extends Application {

    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // Configure Realm database instance for storing and querying ontology
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .assetFile("ontology.realm")
                .readOnly()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static Context getContext() {
        return app.getApplicationContext();
    }

    public static String getPkgName() {
        return getContext().getPackageName();
    }
}
