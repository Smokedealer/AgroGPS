package cz.zcu.fav.agrogps;

import org.json.JSONObject;

import static cz.zcu.fav.agrogps.Tracing.timeOfLastSendPosition;

/**
 * Created by Matěj Kareš on 04.12.2017.
 */

public class SendPositionsTask extends HttpTask {
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(JsonParser.parseSuccessFromJson(jsonObject)){
            if(timeOfLastSendPosition != 0) {
              DBHandler.getDbHandler().truncatePositions(timeOfLastSendPosition);
            }
        } else {

        }
    }
}
