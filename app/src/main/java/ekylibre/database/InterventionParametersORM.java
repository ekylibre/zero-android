package ekylibre.database;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;



/**************************************
 * Created by pierre on 10/7/16.      *
 * ekylibre.database for zero-android *
 *************************************/

public class InterventionParametersORM extends BaseORM
{
    private int     id;
    private String  role;
    private String  name;
    private String  label;
    private int     product_id;
    private String  product_name;
    private int interventionID;

    public InterventionParametersORM(Account account, Context context, int interventionID)
    {
        super(account, context);
        this.interventionID = interventionID;
    }

    @Override
    public void reset()
    {
        id = 0;
        role = null;
        name = null;
        label = null;
        product_id = 0;
        product_name = null;
    }

    @Override
    public void saveInDataBase()
    {
        ContentValues cv = new ContentValues();

        cv.put(ZeroContract.InterventionParameters.EK_ID, id);
        cv.put(ZeroContract.InterventionParameters.NAME, name);
        cv.put(ZeroContract.InterventionParameters.FK_INTERVENTION, interventionID);
        cv.put(ZeroContract.InterventionParameters.ROLE, role);
        cv.put(ZeroContract.InterventionParameters.LABEL, label);
        cv.put(ZeroContract.InterventionParameters.PRODUCT_NAME, product_name);
        cv.put(ZeroContract.InterventionParameters.PRODUCT_ID, product_id);
        contentResolver.insert(ZeroContract.InterventionParameters.CONTENT_URI, cv);
    }

    @Override
    public void setFromBase()
    {

    }

    @Override
    public void setFromJson(JSONObject object) throws JSONException
    {
        id = object.getInt("id");
        role = object.getString("role");
        label = object.getString("label");
        product_name = object.getJSONObject("product").getString("name");
        product_id = object.getJSONObject("product").getInt("id");
    }



    /* ****************
    **     Getters
    ** ***************/

    public int getId()
    {
        return (id);
    }

    public String getRole()
    {
        return (role);
    }

    public String getName()
    {
        return (name);
    }

    public String getLabel(int index)
    {
        return (label);
    }

    public int getProductId(int index)
    {
        return (product_id);
    }

    public String getProductName(int index)
    {
        return (product_name);
    }
}
