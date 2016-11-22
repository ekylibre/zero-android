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
    private String      picture;
    private String      organizationName;
    private String      organizationPost;
    private JSONArray   paramEmail;
    private JSONArray   paramPhone;
    private JSONArray   paramMobile;
    private JSONArray   paramWebsite;
    private JSONArray   paramMails;
    private int         iteratorEmail;
    private int         iteratorPhone;
    private int         iteratorMobile;
    private int         iteratorWebsite;

    public ContactCaller(JSONObject json)
    {
        iteratorEmail   = 0;
        iteratorPhone   = 0;
        iteratorMobile  = 0;
        iteratorWebsite = 0;
        try
        {
            if (!json.isNull("last_name"))
                lastName         = json.getString("last_name");
            if (!json.isNull("first_name"))
                firstName        = json.getString("first_name");
            if (!json.isNull("picture"))
                pictureId        = json.getInt("picture");
            if (!json.isNull("organization"))
            {
                organizationName = json.getJSONObject("organization").getString("name");
                organizationPost = json.getJSONObject("organization").getString("post");
            }
            if (!json.isNull("email"))
                paramEmail       = json.getJSONArray("email");
            if (!json.isNull("phone"))
                paramPhone       = json.getJSONArray("phone");
            if (!json.isNull("mobile"))
                paramMobile      = json.getJSONArray("mobile");
            if (!json.isNull("website"))
                paramWebsite     = json.getJSONArray("website");
            if (!json.isNull("mails"))
                paramMails       = json.getJSONArray("mails");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public static List<ContactCaller> all(Instance instance, String attributes)
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d(TAG, "Get JSONArray => /api/v1/contacts || params = " +
                params);
        JSONArray json = null;
        try
        {
            json = instance.getJSONArray("/api/v1/contacts", params);

            List<ContactCaller> array = new ArrayList<>();

            for(int i = 0; i < json.length(); i++)
            {
                array.add(new ContactCaller(json.getJSONObject(i)));
            }
            return (array);
        } catch (JSONException | IOException | HTTPException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getPicture(Instance instance, int pictureId)
    {
        JSONObject json = null;
        try
        {
            json = instance.getJSONObject("/api/v1/contacts/" + pictureId + "/picture", "");
            return (json.getString("picture"));

        } catch (JSONException | IOException | HTTPException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public String getLastName()
    {
        if (lastName != null)
            return (lastName);
        else
            return ("");
    }

    public String getFirstName()
    {
        if (firstName != null)
            return (firstName);
        else
            return ("");
    }

    public int getPictureId()
    {
        return (pictureId);
    }

    public String getNextMobile()
    {
        if (paramMobile == null || iteratorMobile >= paramMobile.length())
            return (null);
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
        if (paramPhone == null || iteratorPhone >= paramPhone.length())
            return (null);
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
        if (paramEmail == null || iteratorEmail >= paramEmail.length())
            return (null);
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
        if (paramWebsite == null || iteratorWebsite >= paramWebsite.length())
            return (null);
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
        if (paramMails == null || index >= paramMails.length())
            return (null);
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
        if (paramMails == null || index >= paramMails.length())
            return (null);
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
        if (paramMails == null || index >= paramMails.length())
            return (null);
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
        if (paramMails == null || index >= paramMails.length())
            return (null);
        try
        {
            return (paramMails.getJSONObject(index).getString("country"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    public int getParamSize()
    {
        return (paramEmail.length() + paramWebsite.length() + paramPhone.length() + paramMobile
                .length() + paramMails.length());
    }

    public String getOrganizationName()
    {
        return (organizationName);
    }

    public String getOrganizationPost()
    {
        return (organizationPost);
    }
}
