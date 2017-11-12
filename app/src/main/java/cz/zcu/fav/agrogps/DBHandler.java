package cz.zcu.fav.agrogps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/***********************************************
 * Creates SQLite DB and handles all operations
 * @author SAR team
 **********************************************/
public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db"; //DB name
    private static final int DATABASE_VERSION = 1; //DB version

    /* Names of all tables in DB */
    private static final String TABLE_POSITIONS = "Positions"; //storing user's positions
    private static final String TABLE_SENSORS = "Sensors"; //storing info about all sensors positions
    private static final String TABLE_LOGS = "Logs"; //storing application's logs

    /* Columns names */
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private static final String TIME = "time";
    private static final String DISTANCE = "distance";
    private static final String EVENT = "event";


    /***********************************
     * DBHandler class constructor
     * @param context activity context
     **********************************/
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /***********************************
     * Creates database @DATABASE_NAME
     * @param db    created DB
     **********************************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        /* create table @TABLE_POSITIONS
         * LAT, LNG - user's positions
         * TIME - capturing time in milliseconds
        */
        db.execSQL(
                "CREATE TABLE " + TABLE_POSITIONS +
                        "(" + LAT + " DOUBLE, " +
                        LNG + " DOUBLE, " +
                        TIME + " LONG)"
        );

        /* create table @TABLE_SENSORS
         * LAT, LNG - sensor's positions
         * DISTANCE - distance from sensor for alert user (in meters)
        */
        db.execSQL(
                "CREATE TABLE " + TABLE_SENSORS +
                        "(" + LAT + " DOUBLE, " +
                        LNG + " DOUBLE, " +
                        DISTANCE + " INTEGER)"
        );

        /* create table @TABLE_LOGS
         * TIME - log's time in milliseconds
         * EVENT - log's type
        */
        db.execSQL(
                "CREATE TABLE " + TABLE_LOGS +
                        "(" + TIME + " LONG, " +
                        EVENT + " INTEGER(1))"
        );
    }

    /************************************
     * Upgrade database to new version
     * @param db            upgraded DB
     * @param oldVersion    old version
     * @param newVersion    new version
     ************************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop all existing tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        onCreate(db);
    }

    /*********************************************
     * Save new user's position into DB
     * @param lat   lat coordinate
     * @param lng   lng coordinate
     * @param time  time in milliseconds
     * @return  True on success, false otherwise
     *********************************************/
    public boolean addPosition(double lat, double lng, long time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LAT, lat);
        contentValues.put(LNG, lng);
        contentValues.put(TIME, time);

        /* Try to insert new position into DB */
        try {
            db.insertOrThrow(TABLE_POSITIONS, null, contentValues);
            db.close();
            return true;
        }
        catch(SQLException e)
        {
            Log.e("Exception","SQLException"+String.valueOf(e.getMessage()));
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    /*****************************************
     * Returns all positions ordered by time
     * @return all positions
     ****************************************/
    public Cursor getPositions() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + TABLE_POSITIONS + " ORDER BY "+ TIME, null );
        return res;
    }
}