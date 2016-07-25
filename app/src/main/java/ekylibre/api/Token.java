package ekylibre.api;

import ekylibre.exceptions.HTTPException;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {

    public static String create(String email, String password, String url) throws JSONException, ClientProtocolException, IOException, HTTPException
    {
        JSONObject params = new JSONObject();
        params.put("email", email);
        params.put("password", password);

        JSONObject json = Instance.post(url + "/api/v1/tokens", params, null);
        
        String token = json.getString("token");
        
        return token;
    }

}
