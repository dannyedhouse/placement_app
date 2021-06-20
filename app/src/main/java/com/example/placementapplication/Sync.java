package com.example.placementapplication;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.*;

/**
 * Class to synchronise the local SQLite database and remote MySQL database.
 */
public class Sync extends AppCompatActivity {
    public static final String URL_SAVE_NAME = "http://127.0.0.1/syncDB.php"; //Replace with local IP address (e.g., 192...).
    private DatabaseHelper db;
    private List<Placement> placements;
    private List<Favourites> favourites;
    private List<Preferences> preferences;
    private List<Profile> profiles;
    private String newData;
    private Context context;
    private String favouriteData;
    private String newProfile;
    private String newPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<Placement> loadPlacements() {
        String query = "SELECT  * FROM PlacementTable WHERE Sync = 0";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(query, null);
        placements = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Placement data = new Placement(cursor.getInt(0),cursor.getString(1),cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getInt(9), cursor.getString(10), cursor.getString(11));

                placements.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return placements;
    }

    private List<Favourites> loadFavourites(){
        String query = "SELECT  * FROM userFavTable WHERE Sync = 0";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(query, null);
        favourites = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Favourites data = new Favourites(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2));

                favourites.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favourites;

    }

    private List<Preferences> loadPreferences(){
        Log.d("App", "test");
        String query = "SELECT  * FROM preferencesTable WHERE Sync = 0";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(query, null);
        preferences = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Preferences data = new Preferences(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getInt(5), cursor.getInt(6));

                preferences.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return preferences;
    }

    private List<Profile> loadProfile() {
        String query = "SELECT  * FROM profileTable WHERE Sync = 0";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(query, null);
        profiles = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Profile user = new Profile(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9));

                profiles.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return profiles;
    }

    public void updatePlacementData() { // Sets Sync to 1 after being synced to server
        String query = "UPDATE placementTable SET Sync = 1";
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL(query);
    }

    public void updateProfileData() {
        String query = "UPDATE profileTable SET Sync = 1";
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL(query);
    }

    public void updatePreferenceData() {
        String query = "UPDATE preferencesTable SET Sync = 1";
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL(query);
    }

    public void updateFavData() {
        String query = "UPDATE userFavTable SET Sync = 1";
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL(query);
    }

    public void NewPlacementData(JSONArray data) {
        /**
         * Inserts existing placement data from remote MySQL database.
         */

        for (int i = 0; i<data.length(); i++) {
            JSONObject data_obj;
            try {
                data_obj = data.getJSONObject(i);
                int id = data_obj.getInt("PlacementID");
                String name = data_obj.getString("PlacementName");
                String company = data_obj.getString("Company");
                String deadline = data_obj.getString("Deadline");
                String type = data_obj.getString("Type");
                String salary = data_obj.getString("Salary");
                String location = data_obj.getString("Location");
                String desc = data_obj.getString("Description");
                desc = desc.replace("'", "''");
                String subject = data_obj.getString("Subject");
                int miles = data_obj.getInt("Miles");
                String paid = data_obj.getString("Paid");
                String url = data_obj.getString("URL");

                SQLiteDatabase database = db.getWritableDatabase();
                String update = "UPDATE placementTable SET PlacementName ='" + name + "', Company='" + company + "', Deadline='" + deadline + "', Type='" + type + "', " +
                        "Salary ='" + salary + "', Location ='" + location + "', Description='" + desc + "', Subject='" + subject + "', Miles="
                        + miles + ", Paid='" + paid + "', URL='" + url + "' WHERE PlacementID='" + id + "'";
                String query = "INSERT OR IGNORE INTO placementTable (PlacementID, PlacementName, Company, Deadline, Type, Salary, Location, Description," +
                        " Subject, Miles, Paid, URL) VALUES (" + id + ",'" + name + "','" + company + "','" + deadline + "','" + type + "','" + salary +
                        "','" + location + "','" + desc + "','" + subject + "'," + miles + ",'" + paid + "','" + url + "')";
                database.execSQL(update);
                database.execSQL(query);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveToServer(DatabaseHelper db, Context context) {
        /**
         * Saves local placement data to MySQL remote database.
         * If placement data already exists in remote database, update that locally.
         */
        this.db = db;
        this.context = context;
        Gson gson = new Gson();
        this.newData = gson.toJson(loadPlacements());
        this.newPreferences = gson.toJson(loadPreferences());
        this.favouriteData = gson.toJson(loadFavourites());
        this.newProfile = gson.toJson(loadProfile());

        if (checkNetwork()) {
            StringRequest strRequest = new StringRequest(Method.POST, URL_SAVE_NAME,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject msg = new JSONObject(response);
                                Log.d("App", response);
                                if (msg.get("placementData").equals("success")) {
                                    updatePlacementData();
                                } else if (msg.get("placementData").equals("success_new")) { // Placements exist already in
                                    JSONArray obj = new JSONArray(msg.getString("new_data"));
                                    NewPlacementData(obj);
                                    updatePlacementData();
                                }
                                if (msg.get("preferenceData").equals("success")){ // No existing placement data in remote database
                                    updatePreferenceData();
                                }
                                if (msg.get("profileData").equals("success")){
                                    updateProfileData();
                                }
                                if (msg.get("favData").equals("success")){
                                    updateFavData();
                                }
                            } catch (JSONException e) {
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("array", newData);
                    param.put("user_array", newProfile);
                    param.put("pref_array", newPreferences);
                    param.put("favourite_array", favouriteData);

                    return param;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
        }
    }

    public boolean checkNetwork() {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
}