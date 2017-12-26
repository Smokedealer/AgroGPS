package cz.zcu.fav.agrogps;

import org.json.JSONObject;

/**
 * Created by Matěj Kareš on 26.12.2017.
 */

public interface IOnTaskCompleted {
    void onTaskCompleted(JSONObject result);
}
