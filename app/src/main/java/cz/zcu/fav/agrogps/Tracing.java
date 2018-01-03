package cz.zcu.fav.agrogps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/****************************************
 * Activity for tracing user's position
 * @author SAR team
 ***************************************/
public class Tracing extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = Tracing.class.getSimpleName(); /** Current class name */
    private static GoogleApiClient mGoogleApiClient; /** Entry point for Google Play services */
    private static CountDownTimer sendToServerCounter; /** counter for sending tracing to server */
    private static int pushTimerIntervalMs = 60000;

    public static long timeOfLastSendPosition = 0;
    public static int traceId;
    private String traceIdRequestBody = "{\"action\": \"new\"}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing);

        pushTimerIntervalMs = 1000 * PreferenceManager.getDefaultSharedPreferences(this).getInt("interval_server_push", 60);

        Log.i("agro_interval", "Push to server interval set to: " + pushTimerIntervalMs);

        /* check if internet connection is ok - NOT NECESSARY */
        CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

        communicationHandler.checkInternetConnection(this);
        try {
            JSONObject json = communicationHandler.writeToEndpoint(CommunicationHandler.ENDPOINT_TRACKING, traceIdRequestBody);
            traceId = JsonParser.parseTraceIdFromJson(json);
        } catch (Exception e) {
            Log.e("Exception", "Exception: Couldn't get trace ID from server, setting to -1");
            traceId = -1;
        }

        DBHandler dbHandler = new DBHandler(this);
        dbHandler.truncatePositions(Long.MAX_VALUE);
        dbHandler.close();

        /* Create Google Api client */
        buildGoogleApiClient();

        Log.i("OKK", "before start tracing");

        /* Start tracing user's position */
        try {
            LocationHandler.startTracing(this, mGoogleApiClient);
            Log.i("OKK", "after start tracing");
            /* Create counter for 10 * pushTimerIntervalMs with tick by pushTimerIntervalMs */
            sendToServerCounter = new CountDownTimer(10 * pushTimerIntervalMs, pushTimerIntervalMs) {

                public void onTick(long millisUntilFinished) {
                    pushDataToServer();
                }

                //Restart counter on finish
                public void onFinish() {
                    sendToServerCounter.start(); //restart counter
                }
            };

            sendToServerCounter.start(); //start counter
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*******************************************************
     * Create GoogleApiClient, which calls addApi() for
     * sending requests to LocationServices API.
     ******************************************************/
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationHandler.startLocationUpdates(mGoogleApiClient, this);
    }

    /******************************************************
     * reconnection is handled automatically
     * When app is disconnected form Google Play services
     * @param i cause code
     *****************************************************/
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /************************************
     * When user's position was changed
     * @param location  new position
     ***********************************/
    @Override
    public void onLocationChanged(Location location) {
        LocationHandler.locationUpdate(location);
    }

    /*********************************************
     * Show AlertDialog for commit end of tracing
     ********************************************/
    private void commitStopTracing() {
        AlertDialog commitEnd = new AlertDialog.Builder(this).create();
        commitEnd.setTitle("Ukončení záznamu");
        commitEnd.setMessage("Opravdu chcete ukončit zaznamenávání polohy?");

        //submit
        commitEnd.setButton(AlertDialog.BUTTON_POSITIVE, "Ano",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stopTracing();
                    }
                });

        //cancel
        commitEnd.setButton(AlertDialog.BUTTON_NEGATIVE, "Ne",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        commitEnd.show(); //show alertDialog
    }

    /*******************************************
     * Stop tracing and return to main activity
     ******************************************/
    public void stopTracing() {
        sendToServerCounter.cancel(); //end counter

        pushDataToServer();

        String endMessage = "{\"action\":\"finish\", \"trackingId\": " + traceId + "}";

        try {
            CommunicationHandler.getInstance().writeToEndpoint(CommunicationHandler.ENDPOINT_TRACKING, endMessage);
        } catch (Exception e) {
           Log.e("agro_end", "Failed to send end message.");
           e.printStackTrace();
        }

        Intent mainActivity = new Intent(this, MainActivity.class); //main activity
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //disable return to current activity when back button pressed
        startActivity(mainActivity); //start main activity

        mGoogleApiClient.disconnect(); //disconnect GoogleApiClient

        this.finish(); //end current activity
    }

    private void pushDataToServer() {
        try {
            CommunicationHandler.getInstance().sendPositions(LocationHandler.prepareTracingForServer(), Tracing.this);
            Log.i("OKK", "counter tick = sending data to server");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*******************************************
     * On app button stop tracing pressed event
     * @param v current view
     ******************************************/
    public void stopPressed(View v) {
        commitStopTracing();
    }

    /***********************************
     * On hw back button pressed event
     **********************************/
    @Override
    public void onBackPressed() {
        commitStopTracing();
    }
}
