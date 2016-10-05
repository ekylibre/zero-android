package ekylibre.APICaller;

import android.support.annotation.CallSuper;

import org.json.JSONObject;

/**************************************
 * Created by pierre on 10/5/16.      *
 * ekylibre.APICaller for zero-android*
 *************************************/

abstract class BaseCaller implements BasicCaller
{
    String APIPath = "/api/v1/";

    @CallSuper
    @Override
    public void post(Instance instance, JSONObject attributes)
    {

    }

    @CallSuper
    @Override
    public void get(Instance instance, String attributes)
    {

    }

    abstract void setAPIPath();
}
