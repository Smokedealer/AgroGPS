package cz.zcu.fav.agrogps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/* Google Play dependency */
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static cz.zcu.fav.agrogps.Tracing.timeOfLastSendPosition;
import static cz.zcu.fav.agrogps.Tracing.traceId;

/**********************************
 * Handles all location services
 * @author SAR team
 *********************************/
public class LocationHandler {
    private static int updatesInterval = 5000; /** Average interval between location update (5s) */
    private static int fUpdatesInterval = 3000; /** Fastest interval between location update (3s) */
    private static final int MIN_DISTANCE = 15; /** Min distance (in meters) between position for save position */

    //private static CommunicationHandler communicationWithServerHandling; /** Server communication API */
    private static LocationRequest mLocationRequest; /** Fused Location Provider Api parameters */
    private static Activity currentActivity; /** current Activity */
    private static DBHandler db; /** Database handler class */
    private static Location mPreviousLocation = null; /** Last saved position */

    private static ArrayList<Sensor> sensors = new ArrayList<>();

    /*****************************************************************
     * Prepare application for receiving info about current location
     * @param activity  current Activity
     * @param mGoogleApiClient  Google Play entry point
     ****************************************************************/
    public static void startTracing(Activity activity, GoogleApiClient mGoogleApiClient) throws ExecutionException, InterruptedException {
        currentActivity = activity;

        Log.i("OKK", "Start tracing - started");
        
        db = new DBHandler(currentActivity);
        /*CommunicationHandler communicationHandler = CommunicationHandler.getInstance();
        communicationHandler.loadSensorsFromServer();

        sensors = db.getSensors();
        HashMap<String, Integer> settings = communicationHandler.getSettings();
        //updatesInterval = settings.get("interval_updates");
        //fUpdatesInterval = settings.get("interval_server_push");

        db.close();*/

        buildLocationRequest(); //create Location Request
        mGoogleApiClient.connect(); //connect to Google Api client - after connect start updating position

        Log.i("OKK", "Start tracing - end");
    }

    /*************************************************
     * Check if GPS localization is enabled
     * @param activity  current activity
     * @return  true if GPS enabled, false otherwise
     ************************************************/
    public static boolean checkGPS(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER)) {
            dialogForEnablingGPS(activity);
            return false;
        }
        return true;
    }

    /****************************************
     * Dialog for enabling GPS localization
     * @param activity  current activity
     ***************************************/
    private static void dialogForEnablingGPS(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Není povolená lokalizace pomocí GPS.\nKliknutím na Povolit přejdete do nastavní, kde je nutné GPS povolit.";

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

    /*************************************
     * Create and set up LocationRequest
     ************************************/
    protected static synchronized void buildLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) //accuracy of returned location
                .setInterval(updatesInterval)
                .setFastestInterval(fUpdatesInterval);
    }

    /*********************************************
     * Start requesting currrent user's location
     * from FusedLocationApi.
     ********************************************/
    protected static void startLocationUpdates(GoogleApiClient mGoogleApiClient, LocationListener listener) {
        //Check if all permissions are ok
        if (ActivityCompat.checkSelfPermission(currentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(currentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, listener); //request for getting user's current location
    }

    /**********************************************
     * Saves new position into database and check
     * distance from all sensors
     * @param location  current location
     *********************************************/
    public static void locationUpdate(Location location) {

        Log.d("agro_location",location.toString());

        /* Save only if change from previous position is > MIN_DISTANCE */
        if(mPreviousLocation != null) {
            if(location.distanceTo(mPreviousLocation) > MIN_DISTANCE) {
                checkSensorsDistance(location);
                savePosition(location);
            }
        }
        else {
            checkSensorsDistance(location);
            savePosition(location);
        }

        TextView t = (TextView) currentActivity.findViewById(R.id.textInfo);
        t.setText("Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
    }

    public static String prepareTracingForServer() {
        db = new DBHandler(currentActivity);
        String data = "{\"action\": \"push\", \"trackingId\": " + traceId + ", \"data\": ";

        JSONArray trackingData = new JSONArray();

        for(Position p : db.getPositions()) {

            JSONObject pos = new JSONObject();
            try {
                pos.put("y",String.valueOf(p.getLat()));
                pos.put("x",String.valueOf(p.getLng()));
                pos.put("time", String.valueOf(p.getTime() / 1000));

                timeOfLastSendPosition = p.getTime();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            trackingData.put(pos);
        }

        db.close();

        data += trackingData.toString();

        data += "}";

        return data;
    }

    /***************************************
     * Save current position into local DB
     * @param location  current location
     **************************************/
    private static void savePosition(Location location) {
        db = new DBHandler(currentActivity);
        db.addPosition(location.getLatitude(), location.getLongitude(), System.currentTimeMillis());
        db.close();
    }

    /*****************************************
     * Check sensors distance from @location
     * @param location  current location
     ****************************************/
    private static void checkSensorsDistance(Location location) {
        Button alertBtn = currentActivity.findViewById(R.id.alertButton);
        float distance;

        for(Sensor sensor : sensors) {
            distance = location.distanceTo(sensor.getLocation());
            if(distance < sensor.getDistance()) {
                alertBtn.setVisibility(View.VISIBLE);
                alertBtn.setText("POZOR SONDA (" + distance + "m)");
                alertBtn.setBackgroundResource(android.R.color.holo_red_light);
                break;
            }
            else {
                alertBtn.setVisibility(View.INVISIBLE);
                alertBtn.setBackgroundResource(android.R.color.transparent);
            }
        }
    }
}
