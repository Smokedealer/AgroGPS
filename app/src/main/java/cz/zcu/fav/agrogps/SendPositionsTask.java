package cz.zcu.fav.agrogps;

import org.json.JSONObject;

/**
 * Created by Matěj Kareš on 04.12.2017.
 */

public class SendPositionsTask extends HttpTask {
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(JsonParser.parseSuccessFromJson(jsonObject)){
            //TODO data odeslána v pořádku můžeme je smazat lokálně.
            // V podstatě jde jen o to že někde bude ukazatel na poslední
            // odeslanej údaj a když se to podaří tak ukazatel přesuneme.
        } else {
           //TODO data se nepodařilo odeslat, přístě je potřeba je znovu zahrnout do odeslání
        }
    }
}
