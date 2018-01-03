
package cz.zcu.fav.agrogps;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**********************************
 * Main application activity
 * @author SAR team
 *********************************/
public class MainActivity extends AppCompatActivity implements IOnTaskCompleted {
    SharedPreferences appSettings;
    /**
     * Application settings
     */
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
        if (serverAdr.equals("notSetYet")) {
            setContentView(R.layout.init_screen);
        }
        /* If server address is set, launch application */
        else {
            setContentView(R.layout.activity_main);
        }
    }

    /**********************************
     * Add items into the action bar
     * @param menu - added menu
     * @return return created menu
     *********************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*****************************************************
     * Set actions after click on menu items
     * @param item - clicked menu item
     * @return true if action was successfully completed
     ****************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /* Show settings after click in action bar */
        switch (id) {
            case R.id.action_settings:
                showSettings();
                break;

            default:
                return false;
        }
        return true;
    }

    /****************************************************************
     * Shows dialog with current settings and allows to change them
     ***************************************************************/
    public void showSettings() {
        final AlertDialog changeSettings = new AlertDialog.Builder(this).create(); //AlertDialog for change settings
        changeSettings.setTitle(R.string.settingsTitle);

        LinearLayout layout = new LinearLayout(this); //layout for inputs
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(15, 0, 15, 30);

        //input for server address
        final EditText serverAddr = new EditText(this);
        serverAddr.setInputType(InputType.TYPE_CLASS_TEXT);
        serverAddr.setHint("Zadejte adresu serveru");
        serverAddr.setText(serverAdr); //current server address
        serverAddr.setPadding(20, 25, 20, 25);
        layout.addView(serverAddr);

        changeSettings.setView(layout);

        //save changes button
        changeSettings.setButton(AlertDialog.BUTTON_POSITIVE, "Uložit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* On success */
                        if (saveSettings(serverAddr.getText().toString())) {
                            serverAdr = serverAddr.getText().toString();
                        } else {
                            showSettings();
                        }
                    }
                });

        //hide AlertDialog
        changeSettings.setButton(AlertDialog.BUTTON_NEUTRAL, "Zrušit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        changeSettings.show(); //show alertDialog
    }

    /****************************************
     * Save server address from init screen
     * @param v current view
     ***************************************/
    public void saveInitSettings(View v) {
        EditText serverAdrInput = (EditText) findViewById(R.id.serverAdr);

        /* On success */
        if (saveSettings(serverAdrInput.getText().toString())) {
            serverAdr = serverAdrInput.getText().toString();
            setContentView(R.layout.activity_main);
        }
    }

    /**********************************************
     * Save server address into SharedPreferences
     * @param serverAdrText - server address
     * @return true on success, false otherwise
     *********************************************/
    public boolean saveSettings(String serverAdrText) {
         /* If address is in valid URL format, save it into SharedPreferences setting */
        if (URLUtil.isValidUrl(serverAdrText)) {
            SharedPreferences.Editor editor = appSettings.edit(); //Editor for creating new SharedPreferences setting
            editor.putString("serverAdr", serverAdrText);
            editor.commit(); //commit and save

            Toast.makeText(this, R.string.serverAddrOk, Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, R.string.serverAddrErr, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /******************************************************
     * If GPS localization is enabled, start activity for
     * tracing user's position. Otherwise show err dialog
     * @param v current view
     *****************************************************/
    public void startTracing(View v) {
        checkAlive();
    }

    public void checkAlive() {
        /* check if GPS localization is enabled - NECESSARY */
        CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

             /* Check internet connection, if ok, synchronize with server */
        if (communicationHandler.checkInternetConnection(this) && LocationHandler.checkGPS(this)) {
            communicationHandler.setAppSettings(appSettings);
            try {
                communicationHandler.checkAlive(this);
            } catch (Exception e) {
                Log.e("agro_err_server_excptn", "Connection to server resulted in an error.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTaskCompleted(JSONObject result) {
        if (result == null) {
            Toast.makeText(this, "Nepodařilo se spojit se serverem.", Toast.LENGTH_SHORT).show();
        } else {
            Intent TracingActivity = new Intent(this, Tracing.class);
            startActivity(TracingActivity);
        }
    }
}