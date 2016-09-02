package ekylibre.APICaller;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.util.Log;

import ekylibre.exceptions.HTTPException;
import ekylibre.exceptions.ServerErrorException;
import ekylibre.exceptions.UnauthorizedException;
import ekylibre.zero.Authenticator;
import ekylibre.zero.BuildConfig;

import java.io.BufferedReader; 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// All codes at:
// http://developer.android.com/reference/org/apache/http/HttpStatus.html
//
// SC_CONTINUE	100 Continue (HTTP/1.1 - RFC 2616) 
// SC_SWITCHING_PROTOCOLS	101 Switching Protocols (HTTP/1.1 - RFC 2616)
// SC_PROCESSING	102 Processing (WebDAV - RFC 2518) 
// SC_OK	200 OK (HTTP/1.0 - RFC 1945) 
// SC_CREATED	201 Created (HTTP/1.0 - RFC 1945) 
// SC_ACCEPTED	202 Accepted (HTTP/1.0 - RFC 1945) 
// SC_NON_AUTHORITATIVE_INFORMATION	203 Non Authoritative Information (HTTP/1.1 - RFC 2616) 
// SC_NO_CONTENT	204 No Content (HTTP/1.0 - RFC 1945) 
// SC_RESET_CONTENT	205 Reset Content (HTTP/1.1 - RFC 2616) 
// SC_PARTIAL_CONTENT	206 Partial Content (HTTP/1.1 - RFC 2616) 
// SC_MULTI_STATUS	207 Multi-Status (WebDAV - RFC 2518) or 207 Partial Update OK (HTTP/1.1 - draft-ietf-http-v11-spec-rev-01?) 
// SC_MULTIPLE_CHOICES	300 Mutliple Choices (HTTP/1.1 - RFC 2616) 
// SC_MOVED_PERMANENTLY	301 Moved Permanently (HTTP/1.0 - RFC 1945) 
// SC_MOVED_TEMPORARILY	302 Moved Temporarily (Sometimes Found) (HTTP/1.0 - RFC 1945) 
// SC_SEE_OTHER	303 See Other (HTTP/1.1 - RFC 2616) 
// SC_NOT_MODIFIED	304 Not Modified (HTTP/1.0 - RFC 1945) 
// SC_USE_PROXY	305 Use Proxy (HTTP/1.1 - RFC 2616) 
// SC_TEMPORARY_REDIRECT	307 Temporary Redirect (HTTP/1.1 - RFC 2616) 
// SC_BAD_REQUEST	400 Bad Request (HTTP/1.1 - RFC 2616) 
// SC_UNAUTHORIZED	401 Unauthorized (HTTP/1.0 - RFC 1945) 
// SC_PAYMENT_REQUIRED	402 Payment Required (HTTP/1.1 - RFC 2616) 
// SC_FORBIDDEN	403 Forbidden (HTTP/1.0 - RFC 1945) 
// SC_NOT_FOUND	404 Not Found (HTTP/1.0 - RFC 1945) 
// SC_METHOD_NOT_ALLOWED	405 Method Not Allowed (HTTP/1.1 - RFC 2616) 
// SC_NOT_ACCEPTABLE	406 Not Acceptable (HTTP/1.1 - RFC 2616) 
// SC_PROXY_AUTHENTICATION_REQUIRED	407 Proxy Authentication Required (HTTP/1.1 - RFC 2616)
// SC_REQUEST_TIMEOUT	408 Request Timeout (HTTP/1.1 - RFC 2616) 
// SC_CONFLICT	409 Conflict (HTTP/1.1 - RFC 2616) 
// SC_GONE	410 Gone (HTTP/1.1 - RFC 2616) 
// SC_LENGTH_REQUIRED	411 Length Required (HTTP/1.1 - RFC 2616) 
// SC_PRECONDITION_FAILED	412 Precondition Failed (HTTP/1.1 - RFC 2616) 
// SC_REQUEST_TOO_LONG	413 Request Entity Too Large (HTTP/1.1 - RFC 2616) 
// SC_REQUEST_URI_TOO_LONG	414 Request-URI Too Long (HTTP/1.1 - RFC 2616) 
// SC_UNSUPPORTED_MEDIA_TYPE	415 Unsupported Media Type (HTTP/1.1 - RFC 2616) 
// SC_REQUESTED_RANGE_NOT_SATISFIABLE	416 Requested Range Not Satisfiable (HTTP/1.1 - RFC 2616) 
// SC_EXPECTATION_FAILED	417 Expectation Failed (HTTP/1.1 - RFC 2616) 
// SC_INSUFFICIENT_SPACE_ON_RESOURCE	419 Static constant for a 419 error.
// SC_METHOD_FAILURE	420 Static constant for a 420 error.
// SC_UNPROCESSABLE_ENTITY	422 Unprocessable Entity (WebDAV - RFC 2518) 
// SC_LOCKED	423 Locked (WebDAV - RFC 2518) 
// SC_FAILED_DEPENDENCY	424 Failed Dependency (WebDAV - RFC 2518) 
// SC_INTERNAL_SERVER_ERROR	500 Server Error (HTTP/1.0 - RFC 1945) 
// SC_NOT_IMPLEMENTED	501 Not Implemented (HTTP/1.0 - RFC 1945) 
// SC_BAD_GATEWAY	502 Bad Gateway (HTTP/1.0 - RFC 1945) 
// SC_SERVICE_UNAVAILABLE	503 Service Unavailable (HTTP/1.0 - RFC 1945) 
// SC_GATEWAY_TIMEOUT	504 Gateway Timeout (HTTP/1.1 - RFC 2616) 
// SC_HTTP_VERSION_NOT_SUPPORTED	505 HTTP Version Not Supported (HTTP/1.1 - RFC 2616) 
// SC_INSUFFICIENT_STORAGE	507 Insufficient Storage (WebDAV - RFC 2518) 

public class Instance
{
    private String mUrl;
    private String mEmail;
    private String mToken;
    private final static String TAG = "Instance";

    public Instance(String url, String email, String token)
    {
        mUrl   = url;
        mEmail = email;
        mToken = token;
    }

    public Instance(Account account, AccountManager manager) throws AccountsException, IOException
    {
        mUrl   = manager.getUserData(account, Authenticator.KEY_INSTANCE_URL);
        mEmail = manager.getUserData(account, Authenticator.KEY_ACCOUNT_NAME);
        mToken = manager.blockingGetAuthToken(account, Authenticator.AUTH_TOKEN_TYPE_GLOBAL, true);
        if (BuildConfig.DEBUG) Log.d("Instance URL", mUrl);
        if (BuildConfig.DEBUG) Log.d("Instance Token", mToken);
    }

    // Send POST call to given instance
    public JSONObject post(String path, JSONObject params) throws JSONException, ClientProtocolException, IOException, HTTPException
    {
        List<Header> headersList = new ArrayList<Header>();
        headersList.add(new BasicHeader("Authorization", "simple-token " + mEmail + " " + mToken));
        Header[] headers = new Header[headersList.size()];
        headersList.toArray(headers);

        if (BuildConfig.DEBUG) Log.d("POST Instance URL", mUrl);
        if (BuildConfig.DEBUG) Log.d("POST parameters", params.toString());
        
        return Instance.post(mUrl + path, params, headers);
    }



    // Send POST call to given URL with given params
    public static JSONObject post(String url, JSONObject params, Header[] headers) throws JSONException, ClientProtocolException, IOException, HTTPException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "POST " + url);

        // Create a new HttpClient and Post Header
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        if (headers != null)
        {
            httpPost.setHeaders(headers);
        }
        httpPost.setHeader("Content-type", "application/json");
        
        InputStream inputStream = null;
        String result = null;
        // try {
        if (params != null)
        {
            httpPost.setEntity(new StringEntity(params.toString(), "UTF-8"));
        }
            
        // // Execute HTTP Post Request
        HttpResponse response = httpClient.execute(httpPost);

        // Check status
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        switch(statusLine.getStatusCode()) {
        case HttpStatus.SC_BAD_REQUEST:
        case HttpStatus.SC_UNAUTHORIZED:
        case HttpStatus.SC_PAYMENT_REQUIRED:
        case HttpStatus.SC_FORBIDDEN:
        case HttpStatus.SC_NOT_FOUND:
        case HttpStatus.SC_METHOD_NOT_ALLOWED:
        case HttpStatus.SC_NOT_ACCEPTABLE:
        case HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED:
        case HttpStatus.SC_REQUEST_TIMEOUT:
        case HttpStatus.SC_CONFLICT:
        case HttpStatus.SC_GONE:
        case HttpStatus.SC_LENGTH_REQUIRED:
        case HttpStatus.SC_PRECONDITION_FAILED:
        case HttpStatus.SC_REQUEST_TOO_LONG:
        case HttpStatus.SC_REQUEST_URI_TOO_LONG:
        case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE:
        case HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE:
        case HttpStatus.SC_EXPECTATION_FAILED:
        case HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE:
        case HttpStatus.SC_METHOD_FAILURE:
        case HttpStatus.SC_UNPROCESSABLE_ENTITY:
        case HttpStatus.SC_LOCKED:
        case HttpStatus.SC_FAILED_DEPENDENCY:
            throw new UnauthorizedException();
        case HttpStatus.SC_INTERNAL_SERVER_ERROR:
        case HttpStatus.SC_NOT_IMPLEMENTED:
        case HttpStatus.SC_BAD_GATEWAY:
        case HttpStatus.SC_SERVICE_UNAVAILABLE:
        case HttpStatus.SC_GATEWAY_TIMEOUT:
        case HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED:
        case HttpStatus.SC_INSUFFICIENT_STORAGE:
            throw new ServerErrorException();
        }

        HttpEntity entity = response.getEntity();
        inputStream = entity.getContent();
        // // json is UTF-8 by simple
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
        StringBuilder stringBuilder = new StringBuilder();
            
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        result = stringBuilder.toString();
        // } catch (ClientProtocolException e) {
        //     e.printStackTrace();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // } finally {
        try
        {
            if (inputStream != null)
                inputStream.close();
        }
        catch(Exception squish)
        {
        }
        // }

        return new JSONObject(result);
    }


// Send GET call to given instance
    public JSONArray getJSONArray(String path, String params) throws JSONException, ClientProtocolException, IOException, HTTPException
    {
        if (params == null)
            params = "";
        List<Header> headersList = new ArrayList<Header>();
        headersList.add(new BasicHeader("Authorization", "simple-token " + mEmail + " " + mToken));
        Header[] headers = new Header[headersList.size()];
        headersList.toArray(headers);

        if (BuildConfig.DEBUG) Log.d("POST Instance URL", mUrl);
        if (BuildConfig.DEBUG) Log.d("POST parameters", params);

        return Instance.getJSONArray(mUrl + path + params, headers);
    }



    // Send POST call to given URL with given params
    public static JSONArray getJSONArray(String url, Header[] headers) throws JSONException, ClientProtocolException, IOException, HTTPException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "GET " + url);

        // Create a new HttpClient and Get Header
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        if (BuildConfig.DEBUG) Log.i(TAG, "Header => " + headers[0].toString());
        if (headers != null)
        {
            httpGet.setHeaders(headers);
        }
        httpGet.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        // try {
/*        if (params != null)
        {
            httpGet.setParams(new HTT);
        }*/

        // // Execute HTTP Get Request
        HttpResponse response = httpClient.execute(httpGet);

        // Check status
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        switch(statusLine.getStatusCode())
        {
            case HttpStatus.SC_BAD_REQUEST:
            case HttpStatus.SC_UNAUTHORIZED:
            case HttpStatus.SC_PAYMENT_REQUIRED:
            case HttpStatus.SC_FORBIDDEN:
            case HttpStatus.SC_NOT_FOUND:
            case HttpStatus.SC_METHOD_NOT_ALLOWED:
            case HttpStatus.SC_NOT_ACCEPTABLE:
            case HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED:
            case HttpStatus.SC_REQUEST_TIMEOUT:
            case HttpStatus.SC_CONFLICT:
            case HttpStatus.SC_GONE:
            case HttpStatus.SC_LENGTH_REQUIRED:
            case HttpStatus.SC_PRECONDITION_FAILED:
            case HttpStatus.SC_REQUEST_TOO_LONG:
            case HttpStatus.SC_REQUEST_URI_TOO_LONG:
            case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE:
            case HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE:
            case HttpStatus.SC_EXPECTATION_FAILED:
            case HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE:
            case HttpStatus.SC_METHOD_FAILURE:
            case HttpStatus.SC_UNPROCESSABLE_ENTITY:
            case HttpStatus.SC_LOCKED:
            case HttpStatus.SC_FAILED_DEPENDENCY:
                throw new UnauthorizedException();
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
            case HttpStatus.SC_NOT_IMPLEMENTED:
            case HttpStatus.SC_BAD_GATEWAY:
            case HttpStatus.SC_SERVICE_UNAVAILABLE:
            case HttpStatus.SC_GATEWAY_TIMEOUT:
            case HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED:
            case HttpStatus.SC_INSUFFICIENT_STORAGE:
                throw new ServerErrorException();
        }

        HttpEntity entity = response.getEntity();
        inputStream = entity.getContent();
        // // json is UTF-8 by simple
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
        StringBuilder stringBuilder = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            stringBuilder.append(line + "\n");
        }
        result = stringBuilder.toString();
        // } catch (ClientProtocolException e) {
        //     e.printStackTrace();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // } finally {
        try
        {
            if (inputStream != null)
                inputStream.close();
        }
        catch(Exception squish)
        {
        }
        // }

        return new JSONArray(result);
    }



}
