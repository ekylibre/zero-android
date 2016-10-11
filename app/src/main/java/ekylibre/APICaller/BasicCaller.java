package ekylibre.APICaller;

import org.json.JSONObject;

/**************************************
 * Created by pierre on 10/5/16.      *
 * ekylibre.APICaller for zero-android    *
 *************************************/

public interface BasicCaller
{
    void post(Instance instance, JSONObject attributes);

    void get(String attributes);
}
