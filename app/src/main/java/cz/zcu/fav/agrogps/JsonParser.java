package cz.zcu.fav.agrogps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matěj Kareš on 03.12.2017.
 */

public class JsonParser {
    public static ArrayList<Sensor> parseSensorsFromJson(JSONObject json){
        ArrayList<Sensor> sensors = new ArrayList<>();

        try {
            JSONArray data = json.getJSONArray("data");

            for (int i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                double latitude = item.getDouble("x");
                double longitude = item.getDouble("y");
                int distance = item.getInt("radius");

                Sensor sensor = new Sensor(latitude, longitude, distance);

                sensors.add(sensor);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sensors;
    }


    public static HashMap<String, Integer> parseSettingsFromJson(JSONObject json){
        HashMap<String, Integer> settings = new HashMap<>();

        try {
            JSONArray data = json.getJSONArray("data");

            for (int i = 0; i < data.length(); i++){
                JSONObject item = data.getJSONObject(i);

                int intervalTracking = item.getInt("interval_tracking");
                int intervalServerPush = item.getInt("interval_server_push");

                settings.put("interval_tracking", intervalTracking);
                settings.put("interval_server_push", intervalServerPush);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return settings;
    }

    public static boolean parseSuccessFromJson(JSONObject json){
        String success = "";

        try {
            success = json.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return success.equals("success");
    }

    public static int parseTraceIdFromJson(JSONObject json) {

        int id = -1;

        try {
            id = json.getInt("trackingId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return id;
    }
}
