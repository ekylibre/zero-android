package ekylibre;

import java.io.BufferedReader; 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {

    public static String create(String email, String password, String url) throws JSONException {
        // Create a new HttpClient and Post Header
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url + "/api/v1/tokens.json");
        httpPost.setHeader("Content-type", "application/json");       
        
        InputStream inputStream = null;
        String result = null;
        try {
            // Add your data
            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("password", password);
            httpPost.setEntity(new StringEntity(params.toString()));
            
            // Execute HTTP Post Request
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            result = stringBuilder.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch(Exception squish) {
            }
        }


        JSONObject json = new JSONObject(result);
        
        String token = json.getString("token");
        
        return token;
    }

}
