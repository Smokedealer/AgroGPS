package cz.zcu.fav.agrogps;

import android.content.Intent;
import android.location.Location;
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

/****************************************
 * Activity for tracing user's position
 * @author SAR team
 ***************************************/
public class Tracing extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = Tracing.class.getSimpleName(); /** Current class name */
    private static GoogleApiClient mGoogleApiClient; /** Entry point for Google Play services */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing);

        /* check if internet connection is ok - NOT NECESSARY */
        CommunicationWithServerHandling.checkInternetConnection(this);

        /* Create Google Api client */
        buildGoogleApiClient();

        /* Start tracing user's position */
        LocationHandler.startTracing(this, mGoogleApiClient);
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
     * Stop tracing and return to main activity
     * @param v current view
     ********************************************/
    public void stopTracing(View v) {
        Intent mainActivity = new Intent(this, MainActivity.class); //main activity
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //disable return to current activity when back button pressed
        startActivity(mainActivity); //start main activity

        mGoogleApiClient.disconnect(); //disconnect GoogleApiClient

        this.finish(); //end current activity
    }
}
