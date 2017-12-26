package cz.zcu.fav.agrogps;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import static cz.zcu.fav.agrogps.Tracing.timeOfLastSendPosition;

/**
 * Created by Matěj Kareš on 04.12.2017.
 */

public class SendPositionsTask extends HttpTask {

    private Context context;

    public SendPositionsTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(JsonParser.parseSuccessFromJson(jsonObject)){
            if(timeOfLastSendPosition != 0) {
                DBHandler dbHandler = new DBHandler(context);
                dbHandler.truncatePositions(timeOfLastSendPosition);
                dbHandler.close();
            }
        } else {
            Log.i("agro_send_data", "Failed to send data, waiting for next period.");
        }
    }
}
