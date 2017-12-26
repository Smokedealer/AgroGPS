package cz.zcu.fav.agrogps;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matěj Kareš on 02.12.2017.
 */

public class CheckAliveTask extends HttpTask {

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final int READ_TIMEOUT = 2000;
    public static final int CONNECTION_TIMEOUT = 2000;

    ProgressDialog progressBar;
    boolean showDialog;
    IOnTaskCompleted callBack;

    public CheckAliveTask(Context context, IOnTaskCompleted callBack, boolean showDialog) {
        this.showDialog = showDialog;
        this.callBack = callBack;

        if(showDialog) {
            this.progressBar = new ProgressDialog(context);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(showDialog){
            progressBar.setMessage("Připojuji k serveru");
            progressBar.setIndeterminate(true);
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }
    }


    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        if(showDialog){
            progressBar.dismiss();
        }

        callBack.onTaskCompleted(jsonObject);
    }
}
