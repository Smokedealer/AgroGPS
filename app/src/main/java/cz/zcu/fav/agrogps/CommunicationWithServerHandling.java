package cz.zcu.fav.agrogps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by Srami92 on 11.11.2017.
 */

public class CommunicationWithServerHandling {

    /****************************************
     * Check if Internet connection is ok
     * @param activity  current activity
     ***************************************/
    public static boolean checkInternetConnection(final Activity activity) {
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
    public static void dialogForEnablingInternet(final Activity activity) {
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
    public static void loadSensors() {
        //TODO načtení ze serveru a uložení do lokální db místo současných
    }
}
