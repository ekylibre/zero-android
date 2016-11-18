package ekylibre.util;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.provider.ContactsContract;

import java.util.ArrayList;

import ekylibre.APICaller.SyncAdapter;

/**************************************
 * Created by pierre on 11/17/16.     *
 * ekylibre.util for zero-android     *
 *************************************/

public class Contact
{
    private String name;
    private String mobileNumber;
    private String homeNumber;
    private String workNumber;
    private String email;
    private String company;
    private String jobTitle;
    private String photo;
    private ArrayList<ContentProviderOperation> contactParameter;
    private Context mContext;

    public Contact(Context context)
    {
        contactParameter = new ArrayList<>();
        mContext = context;
    }

    public void setAccount()
    {
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, SyncAdapter.ACCOUNT_TYPE)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, AccountTool.getAccountName(
                        AccountTool.getCurrentAccount(mContext), mContext))
                .build());
    }

    public void setPhoto(String photo)
    {
        if (photo == null)
            return;
        this.photo = photo;
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo)
                .build()
        );

    }

    public void setName(String name)
    {
        if (name == null)
            return;
        this.name = name;
        contactParameter.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        name).build());

    }

    public void setMobileNumber(String mobileNumber)
    {
        if (mobileNumber == null)
            return;
        this.mobileNumber = mobileNumber;
        contactParameter.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
        }

    public void setHomeNumber(String homeNumber)
    {
        if (homeNumber == null)
            return;
        this.homeNumber = homeNumber;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
    }

    public void setWorkNumber(String workNumber)
    {
        if (workNumber == null)
            return;
        this.workNumber = workNumber;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());

    }

    public void setEmail(String email)
    {
        if (email == null)
            return;
        this.email = email;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());
    }

    public void setOrganization(String company, String jobTitle)
    {
        if (company == null || jobTitle == null)
            return;
        this.company = company;
        this.jobTitle = jobTitle;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                .build());
    }

    /*
    ** Alternative way to add organization without jobTitle
    */
    public void setOrganization(String company)
    {
        if (company == null)
            return;
        this.company = company;
        contactParameter.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                .build());
    }

    public void commit()
    {
        if (contactParameter == null)
            return;
        try
        {
            mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, contactParameter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    /* ****************
    **    Getters
    ******************/

    public String getName()
    {
        return (name);
    }

    public String getMobileNumber()
    {
        return (mobileNumber);
    }

    public String getHomeNumber()
    {
        return (homeNumber);
    }

    public String getWorkNumber()
    {
        return (workNumber);
    }

    public String getEmail()
    {
        return (email);
    }

    public String getPhoto()
    {
        return (photo);
    }

    public String getCompany()
    {
        return (company);
    }
}

