/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hope.nfagents;

import com.vaadin.ui.Notification;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 source code: http://en.proft.me/2013/12/5/how-parse-json-java/
*/
public abstract class MyJSONUtils {
    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException {
        // String s = URLEncoder.encode(url, "UTF-8");
        // URL url = new URL(s);
        InputStream is = new URL(url).openStream();
        JSONArray json = null;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            json = new JSONArray(jsonText);
        } catch (JSONException e) {
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
        } finally {
            is.close();
        }
        return json;
    }    
    
    public static String getValue(JSONObject obj, String key) {
        String val = "";
        try {
            val = obj.getString(key);
        } catch (Exception e) {
            val = "";
        }
        return val;
    }

}
