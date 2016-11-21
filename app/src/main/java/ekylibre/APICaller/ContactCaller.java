package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 11/21/16.     *
 * ekylibre.APICaller for zero-android*
 *************************************/

public class ContactCaller
{
    private static final String TAG = "ContactCaller";
    private String      lastName;
    private String      firstName;
    private int         pictureId;
    String      picture;
    JSONArray   paramEmail;
    JSONArray   paramPhone;
    JSONArray   paramMobile;
    JSONArray   paramWebsite;
    JSONArray   paramMails;
    int         iteratorEmail;
    int         iteratorPhone;
    int         iteratorMobile;
    int         iteratorWebsite;
    int         iteratorMails;

    public ContactCaller(JSONObject json)
    {
        iteratorEmail   = 0;
        iteratorPhone   = 0;
        iteratorMobile  = 0;
        iteratorWebsite = 0;
        iteratorMails   = 0;
        try
        {
            lastName     = json.getString("last_name");
            firstName    = json.getString("first_name");
            pictureId    = json.getInt("picture");
            paramEmail   = json.getJSONArray("email");
            paramPhone   = json.getJSONArray("phone");
            paramMobile  = json.getJSONArray("mobile");
            paramWebsite = json.getJSONArray("website");
            paramMails   = json.getJSONArray("mails");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public static List<ContactCaller> all(Instance instance, String attributes) throws JSONException,
            IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d(TAG, "Get JSONArray => /api/v1/interventions || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/interventions", params);
        List<ContactCaller> array = new ArrayList<>();

        for(int i = 0; i < json.length(); i++)
        {
            array.add(new ContactCaller(json.getJSONObject(i)));
        }
        return (array);
    }

    public String getLastName()
    {
        return (lastName);
    }

    public String getFirstName()
    {
        return (firstName);
    }

    public int getPictureId()
    {
        return (pictureId);
    }

    public String getNextMobile()
    {
        try
        {
            return (paramMobile.getString(iteratorMobile++));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getNextPhone()
    {
        try
        {
            return (paramPhone.getString(iteratorPhone++));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getNextEmail()
    {
        try
        {
            return (paramEmail.getString(iteratorEmail++));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getNextWebsite()
    {
        try
        {
            return (paramWebsite.getString(iteratorWebsite++));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getMailLines(int index)
    {
        try
        {
            return (paramMails.getJSONObject(index).getString("mail_lines"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getPostalCode(int index)
    {
        try
        {
            return (paramMails.getJSONObject(index).getString("postal_code"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getCity(int index)
    {
        try
        {
            return (paramMails.getJSONObject(index).getString("city"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getCountry(int index)
    {
        try
        {
            return (paramMails.getJSONObject(index).getString("country"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }
}
