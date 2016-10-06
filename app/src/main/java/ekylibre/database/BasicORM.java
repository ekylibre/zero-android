package ekylibre.database;

import org.json.JSONException;
import org.json.JSONObject;

/**************************************
 * Created by pierre on 10/5/16.      *
 * ekylibre.database for zero-android    *
 *************************************/

public interface BasicORM
{
    void reset();

    void saveInDataBase();

    void setFromBase();

    void setFromJson(JSONObject object) throws JSONException;
}
