package ekylibre.APICaller;

import ekylibre.exceptions.HTTPException;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {

    public static String create(String email, String password, String url)
            throws JSONException, IOException, HTTPException
    {
        JSONObject params = new JSONObject();
        params.put("email", email);
        params.put("password", password);

        JSONObject json = Instance.post(url + "/api/v1/tokens", params, null);

        return json.getString("token");
    }

}
