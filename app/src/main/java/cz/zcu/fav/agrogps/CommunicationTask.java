package cz.zcu.fav.agrogps;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matěj Kareš on 02.12.2017.
 */

public class CommunicationTask extends AsyncTask<String, Void, JSONObject> {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;



    @Override
    protected JSONObject doInBackground(String... urls) {
        URL url;
        JSONObject json;
        String result;
        String inputLine;

        try {
            url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection(); /* Create a connection */

            /* Set methods and timeouts */
            connection.setRequestMethod(urls[1]);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect(); //Connect to our url

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream()); //Create a new InputStreamReader

            BufferedReader reader = new BufferedReader(streamReader); //Create a new buffered reader and String Builder
            StringBuilder stringBuilder = new StringBuilder();

            while((inputLine = reader.readLine()) != null){ //Check if the line we are reading is not null
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            result = stringBuilder.toString();
            json = new JSONObject(result);

        } catch(IOException e){
            e.printStackTrace();
            json = null;
        } catch (JSONException e) {
            e.printStackTrace();
            json = null;
        }

        return json;
    }
}
