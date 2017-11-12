package cz.zcu.fav.agrogps;

/**************************************
 * Class representing user's Position
 * @author SAR team
 *************************************/
public class Position {
    private double lat; /** Latitude coordinate */
    private double lng; /** Longitude coordinate */
    private long time; /** Position's capturing time */

    /*****************************
     * Class constructor
     * @param lat lat coordinate
     * @param lng lng coordinate
     * @param time capturing time
     *****************************/
    public Position(double lat, double lng, long time) {
        super();
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    /*********************************
     * Get position's lat coordinate
     * @return  lat coordinate
     ********************************/
    public double getLat() {
        return lat;
    }

    /*********************************
     * Set position's lat coordinate
     * @param lat   lat coordinate
     ********************************/
    public void setLat(double lat) {
        this.lat = lat;
    }

    /*********************************
     * Get position's lng coordinate
     * @return  lng coordinate
     ********************************/
    public double getLng() {
        return lng;
    }

    /*********************************
     * Set position's lng coordinate
     * @param lng   lng coordinate
     ********************************/
    public void setLng(double lng) {
        this.lng = lng;
    }

    /*********************************
     * Get position's capturing time
     * @return  capturing time
     ********************************/
    public long getTime() {
        return time;
    }

    /*********************************
     * Set position's capturing time
     * @param time   capturing time
     ********************************/
    public void setTime(long time) {
        this.time = time;
    }
}
