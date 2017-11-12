package cz.zcu.fav.agrogps;

import android.location.Location;
import android.location.LocationManager;

/******************************
 * Class representing Sensor
 * @author SAR team
 *****************************/
public class Sensor {
    private double lat; /** Latitude coordinate */
    private double lng; /** Longitude coordinate */
    private int distance; /** Safe distance from sensor */

    /**********************************************************
     * Class constructor
     * @param lat lat coordinate
     * @param lng lng coordinate
     * @param distance Safe distance from sensor (in meters)
     *********************************************************/
    public Sensor(double lat, double lng, int distance) {
        super();
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
    }

    /*********************************
     * Get sensors's lat coordinate
     * @return  lat coordinate
     ********************************/
    public double getLat() {
        return lat;
    }

    /*********************************
     * Set sensors's lat coordinate
     * @param lat   lat coordinate
     ********************************/
    public void setLat(double lat) {
        this.lat = lat;
    }

    /*********************************
     * Get sensor's lng coordinate
     * @return  lng coordinate
     ********************************/
    public double getLng() {
        return lng;
    }

    /*********************************
     * Set sensors's lng coordinate
     * @param lng   lng coordinate
     ********************************/
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**************************************
     * Get sensors's position as Location
     * @return  sensors's location
     *************************************/
    public Location getLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(this.lat);
        location.setLongitude(this.lng);
        return location;
    }

    /*********************************
     * Get sensor's safe distance
     * @return  distance in meters
     ********************************/
    public int getDistance() {
        return distance;
    }

    /***************************************
     * Set sensor's safe distance
     * @param distance   distance in meters
     **************************************/
    public void setDistance(int distance) {
        this.distance = distance;
    }
}
