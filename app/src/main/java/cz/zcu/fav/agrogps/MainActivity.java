package cz.zcu.fav.agrogps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

/**********************************
 * Main application activity
 * @author SAR team
 *********************************/
public class MainActivity extends AppCompatActivity {
    SharedPreferences appSettings; /** Application settings */
    public String serverAdr; /** Server address */

    /*************************************************************
     * Called as first when activity is created
     * @param savedInstanceState    for saving application state
     ************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appSettings = PreferenceManager.getDefaultSharedPreferences(this); //application settings
        serverAdr = appSettings.getString("serverAdr", "notSetYet"); //server address - return notSetYet if isn't set up

        /* On first launch show init screen for set up server address */
        if(serverAdr.equals("notSetYet")) {
            setContentView(R.layout.init_screen);
        }
        /* If server address is set, launch application */
        else {
            setContentView(R.layout.activity_main);

            /* Check internet connection, if ok, synchronize with server */
            if(CommunicationWithServerHandling.checkInternetConnection(this)) {
                CommunicationWithServerHandling.loadSensors();
            }
        }
    }

    /**************************************************************
     * Save server address from init screen into SharedPreferences
     * @param v current view
     *************************************************************/
    public void saveInitSettings(View v) {
        EditText serverAdrInput = (EditText)findViewById(R.id.serverAdr);
        String serverAdrText = serverAdrInput.getText().toString();

        /* If address is in valid URL format, save it into SharedPreferences setting */
        if(URLUtil.isValidUrl(serverAdrText)) {
            SharedPreferences.Editor editor = appSettings.edit(); //Editor for creating new SharedPreferences setting
            editor.putString("serverAdr", serverAdrText);
            editor.commit(); //commit and save

            setContentView(R.layout.activity_main);
        }
        else {
            Toast.makeText(this, R.string.serverAddrErr, Toast.LENGTH_LONG).show();
        }
    }

    /******************************************************
     * If GPS localization is enabled, start activity for
     * tracing user's position. Otherwise show err dialog
     * @param v current view
     *****************************************************/
    public void startTracing(View v) {
        /* check if GPS localization is enabled - NECESSARY */
        if(LocationHandler.checkGPS(this)) {
            Intent TracingActivity = new Intent(this, Tracing.class);
            startActivity(TracingActivity);
        }
    }
}