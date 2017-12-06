
package cz.zcu.fav.agrogps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Srami92 on 11.11.2017.
 */

public class CommunicationHandler {

    private SharedPreferences appSettings; /** Application settings */

    public static final String ENDPOINT_SENSORS = "/sensors";
    public static final String ENDPOINT_SETTINGS = "/config";
    public static final String ENDPOINT_TRACKING = "/tracking";


    private static CommunicationHandler instance;

    /****************************************
     * This class will only have one instance
     * Singleton design pattern
     ***************************************/
    public static CommunicationHandler getInstance() {
        if(instance == null){
            instance = new CommunicationHandler();
        }

        return instance;
    }

    /****************************************
     * Private constructor, since the class
     * is a singleton class
     ***************************************/
    private CommunicationHandler() {
    }



    /****************************************
     * Check if Internet connection is ok
     * @param activity  current activity
     ***************************************/
    public boolean checkInternetConnection(final Activity activity) {
        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected) {
            dialogForEnablingInternet(activity);
            return false;
        }
        return true;
    }

    /******************************************
     * Dialog for enabling internet connection
     * @param activity  current activity
     ******************************************/
    public void dialogForEnablingInternet(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_SETTINGS;
        final String message = "Není dostupné internetové připojení.\nKliknutím na Povolit přejdete do nastavení, kde můžete internetové připojení povolit.";

        builder.setMessage(message)
                .setPositiveButton(R.string.enable,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

    /************************************
     * Returns all sensors from server
     * @return  array of all sensors
     ***********************************/
    public void loadSensorsFromServer(Context context) throws ExecutionException, InterruptedException {
        ArrayList<Sensor> sensors;

        JSONObject json = readFromEndpoint(ENDPOINT_SENSORS);
        sensors = JsonParser.parseSensorsFromJson(json);

        DBHandler dbHandler = new DBHandler(context);
        dbHandler.truncateSensors();

        for(Sensor sensor : sensors){
            dbHandler.addSensor(sensor);
        }

        dbHandler.close();
    }

    public JSONObject readFromEndpoint(String endpoint) throws ExecutionException, InterruptedException {
        HttpTask task = new HttpTask();
        JSONObject result;

        task.execute(appSettings.getString("serverAdr", null) + endpoint, HttpTask.METHOD_GET);

        result = task.get();
        return result;
    }

    public JSONObject writeToEndpoint(String endpoint, String message) throws ExecutionException, InterruptedException {
        HttpTask task = new HttpTask();
        JSONObject result;

        task.execute(appSettings.getString("serverAdr", null) + endpoint, HttpTask.METHOD_POST, message);

        result = task.get();
        return result;
    }

    public void sendPositions(String message, Context context) throws ExecutionException, InterruptedException {
        SendPositionsTask task = new SendPositionsTask(context);
        task.execute(appSettings.getString("serverAdr", null) + ENDPOINT_TRACKING, HttpTask.METHOD_POST, message);
    }


    public HashMap<String, Integer> getSettings() throws ExecutionException, InterruptedException {
        HashMap<String, Integer> settings;

        JSONObject json = readFromEndpoint(ENDPOINT_SETTINGS);
        settings = JsonParser.parseSettingsFromJson(json);

        return settings;
    }

    public void setAppSettings(SharedPreferences appSettings) {
        this.appSettings = appSettings;
    }

}
