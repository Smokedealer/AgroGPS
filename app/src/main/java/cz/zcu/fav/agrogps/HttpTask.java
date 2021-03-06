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

public class HttpTask extends AsyncTask<String, Void, JSONObject> {

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final int READ_TIMEOUT = 2000;
    public static final int CONNECTION_TIMEOUT = 2000;



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        String endpointUrl = params[0];
        String method = params[1];
        String messageToBeSent = (params.length == 3 ? params[2] : "");
        URL url;
        JSONObject json;
        String result;
        String inputLine;


        Log.d("agro_endpointUrl",endpointUrl);
        Log.d("agro_method",method);
        Log.d("agro_messageToBeSent",messageToBeSent);

        try {
            url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection(); /* Create a connection */

            /* Set methods and timeouts */
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("Content-length", messageToBeSent.length() + "");
            if(method == METHOD_POST)  connection.setRequestProperty("Content-type", "application/json");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setDoOutput(method == METHOD_POST);

            if(method == METHOD_POST) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(messageToBeSent);
                outputStreamWriter.close();
            }

            connection.connect(); //Connect to our url

            int responseCode = connection.getResponseCode();

            if(responseCode != 200) {
                Log.d("agro_not_success", "Message sent to server resulted in unsuccessful response code.");
                return null;
            }

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream()); //Create a new InputStreamReader

            BufferedReader reader = new BufferedReader(streamReader); //Create a new buffered reader and String Builder
            StringBuilder stringBuilder = new StringBuilder();

            while((inputLine = reader.readLine()) != null){ //Check if the line we are reading is not null
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            result = stringBuilder.toString();

            Log.d("agro_result",result);
            json = new JSONObject(result);

        } catch(IOException e){
            Log.d("agro_ioexception",e.getMessage());
            Log.d("agro_ioexp", e.getStackTrace().toString());
            json = null;
        } catch (JSONException e) {
            Log.d("agro_endpointUrl",e.getMessage());
            Log.d("agro_jsonexp", e.getStackTrace().toString());
            e.printStackTrace();
            json = null;
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
