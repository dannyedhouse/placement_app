package com.example.placementapplication;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Session session;

    // Fields for Placement Table
    private Context context;
    private static String DATABASE_NAME = "placements.db";
    private static String PLACEMENT_TABLE = "placementTable";
    private static String PLACEMENT_ID = "PlacementID"; //PrimaryKey
    private static String PLACEMENT_NAME = "PlacementName";
    private static String COMPANY_NAME = "Company";
    private static String CLOSING_DATE = "Deadline";
    private static String PLACEMENT_TYPE = "Type";
    private static String SALARY = "Salary";
    private static String LOCATION = "Location";
    private static String DESCRIPTION = "Description";
    private static String SUBJECT = "Subject";
    private static String MILES = "Miles";
    private static String PAID = "Paid";
    private static String URL = "URL";
    private static String SYNC = "Sync";
    public static String keyword;

    //Fields for Profile table
    private static String PROFILE_TABLE = "profileTable";
    private static String PROFILE_ID = "UserID"; //PrimaryKey
    private static String USERNAME = "Username";
    private static String NAME = "Name";
    private static String EMAIL = "Email";
    private static String PHONE = "Phone";
    private static String DOB = "DOB";
    private static String ADDRESS = "Address";
    private static String ABOUT = "About";
    private static String EXPERIENCE = "Experience";
    private static String UNIVERSITY = "University";

    // Query to create the Placement Table
    private static final String QUERY = "CREATE TABLE " + PLACEMENT_TABLE + "(" + PLACEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PLACEMENT_NAME + " TEXT, " + COMPANY_NAME + " TEXT, " + CLOSING_DATE + " TEXT, " + PLACEMENT_TYPE
            + " TEXT, " + SALARY + " TEXT, " + LOCATION + " TEXT, " + DESCRIPTION + " TEXT, " + SUBJECT + " TEXT, " +
            MILES + " INTEGER, " + PAID + " TEXT, " + URL + " TEXT, " + SYNC + " INTEGER)";

    // Create Profile Table
    private static final String profileQuery = "CREATE TABLE " + PROFILE_TABLE + " (" + PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT," + NAME + "  TEXT, " + EMAIL + " TEXT, " + PHONE + " INTEGER, " + DOB + " DATE, " + ADDRESS + " TEXT, " + ABOUT + " TEXT, " + EXPERIENCE + " TEXT, " + UNIVERSITY + " TEXT, " +
            SYNC + " INTEGER)";

    // Create Favorites Table
    private static String FAVORITES_TABLES = "userFavTable";
    private static final String favoriteQuery = "CREATE TABLE " + FAVORITES_TABLES + " (FavID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "UserID INTEGER, PlacementID INTEGER, Sync INTEGER, FOREIGN KEY(UserID) REFERENCES ProfileTable(UserID), FOREIGN KEY(PlacementID) " +
            "REFERENCES PlacementTable(PlacementID))";

    // Create Preferences Table
    private static String PREFERENCES_TABLE = "preferencesTable";
    private static final String preferencesQuery = "CREATE TABLE " + PREFERENCES_TABLE + " (PrefID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "UserID INTEGER UNIQUE, Paid TEXT, Type TEXT, Subject TEXT, Miles INTEGER, Relocate BOOLEAN, Sync INTEGER, FOREIGN KEY(UserID) REFERENCES ProfileTable(UserID))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY);
        db.execSQL(favoriteQuery);
        db.execSQL(profileQuery);
        db.execSQL(preferencesQuery);
        addPlacement(this.context, db);
    }

    @TargetApi(16)
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    public void addPreferences(String paid, String type, String subject, int miles, boolean relocate) {
        /**
         * Adds/updates the user preferences
         */
        SQLiteDatabase db = this.getWritableDatabase();
        int relocateInt = 0;

        if (relocate == true) {
            relocateInt = 1;
        }

        int userID = this.getUserID();

        String query = "UPDATE " + PREFERENCES_TABLE + " SET UserID = " + userID + ", Paid = '" + paid + "', Type ='" + type +
                "', Subject='" + subject + "', Miles=" + miles + ", Relocate=" + relocateInt + ", Sync=0 WHERE UserID = " + userID + ";";
        String update = "INSERT OR IGNORE INTO " + PREFERENCES_TABLE + " (UserID, Paid, Type, Subject, Miles, Relocate, Sync) VALUES (" + userID + ", '"
                + paid + "', '" + type + "', '" + subject + "', " + miles + ", " + relocateInt + ", 0)";

        db.execSQL(query);
        db.execSQL(update);
    }

    public void addPlacement(Context context, SQLiteDatabase db) {
        /**
         * Add each placement stored in the CSV file to the local SQLite database
         */
        String csvFile = "placements.csv"; // Read the placements from this CSV file (Saved in... app>src>main>assets)
        AssetManager manager = context.getAssets();
        InputStream stream = null;

        try {
            stream = manager.open(csvFile);
            Reader csv = new BufferedReader(new InputStreamReader(stream));

            CSVReader reader = new CSVReader(csv);
            String[] line; // Store each line of csv file in String array

            db.beginTransaction();
            try {
                while ((line = reader.readNext()) != null) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PLACEMENT_NAME, line[0].trim());
                    contentValues.put(COMPANY_NAME, line[1].trim());
                    contentValues.put(CLOSING_DATE, line[2].trim());
                    contentValues.put(PLACEMENT_TYPE, line[3].trim());
                    contentValues.put(SALARY, line[4].trim());
                    contentValues.put(LOCATION, line[5].trim());
                    contentValues.put(DESCRIPTION, line[6].trim());
                    contentValues.put(SUBJECT, line[7].trim());
                    contentValues.put(MILES, line[8].trim());
                    contentValues.put(PAID, line[9].trim());
                    contentValues.put(URL, line[10].trim());
                    contentValues.put(SYNC, 0);
                    db.insert(PLACEMENT_TABLE, null, contentValues);
                }

            } catch (IOException e) {
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }

            db.setTransactionSuccessful();
            db.endTransaction();

        } catch (IOException e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * Do not create table if already made
          */
        db.execSQL("DROP TABLE IF EXISTS " + PLACEMENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
        onCreate(db);
    }

    public void onConfig(SQLiteDatabase database) {
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public Cursor getPreferences() {
        int userID = this.getUserID();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT *, PrefID as _id FROM " + PREFERENCES_TABLE + " WHERE UserID = " + userID + ";";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getData(String keyword) {
        /**
         * Returns the placements using Cursor (returns all if no user preferences are set)
          */
        this.keyword = keyword;
        SQLiteDatabase db = getWritableDatabase();

        Cursor data;
        Cursor preferences = getPreferences();

        if (preferences.getCount() == 0) {
            String query = "SELECT *, PlacementID as _id FROM " + PLACEMENT_TABLE + " WHERE " + PLACEMENT_NAME + " LIKE '%" + keyword + "%' OR " +
                    COMPANY_NAME + " LIKE '%" + keyword + "%'";
            data = db.rawQuery(query, null);
            Log.d("App", query);
        } else {
            data = getDataPreferences(preferences);
        }

        return data;
    }

    public Cursor getFav() {
        /**
         * Returns placements without preference query
          */
        SQLiteDatabase db = getWritableDatabase();
        int userID = this.getUserID();
        String query = "SELECT *, PlacementID as _id FROM " + PLACEMENT_TABLE + " t1 WHERE EXISTS (SELECT PlacementID FROM userFavTable t2 " +
                "WHERE t1.PlacementID = t2.PlacementID AND UserID = " + userID + ")";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getDataPreferences(Cursor preferences) {
        /**
         * Returns placements based on user preferences set
         */
        SQLiteDatabase db = getWritableDatabase();
        String query = "";
        if (preferences.moveToFirst()) {
            do {
                String paid = preferences.getString(2);
                String type = preferences.getString(3);
                String subject = preferences.getString(4);
                int miles = preferences.getInt(5);
                boolean value = preferences.getInt(6) > 0;

                query = "SELECT *, PlacementID as _id FROM " + PLACEMENT_TABLE + " WHERE (" + PLACEMENT_NAME + " LIKE '%" + keyword + "%' OR " +
                        COMPANY_NAME + " LIKE '%" + keyword + "%') AND";

                if (!paid.equals("null") && (!paid.equals("")) && (!paid.equals("Both"))) {
                    query = query + " Paid = '" + paid + "' AND ";
                }
                if (!type.equals("null") && (!type.equals("")) && (!type.equals("Both"))) {
                    query = query + " Type = '" + type + "' AND";
                }
                if (!subject.equals("null")) {
                    query = query + " Subject = '" + subject + "' AND ";
                }
                if (value == false) {
                    if (miles >20) {
                        miles = 20; // Set maximum miles of 20 miles commute if they do not want to commute
                    }
                }

                query = query + " Miles <=" + miles + ";";
                Log.d("App", query);

            } while (preferences.moveToNext());
        }
        Cursor data = db.rawQuery(query, null);
        preferences.close();
        return data;
    }

    public boolean checkFav(int PlacementID) {
        /**
         * Checks if placement is in favorites table
         */
        SQLiteDatabase db = getWritableDatabase();
        int userID = this.getUserID();
        String query = "SELECT * FROM userFavTable WHERE UserID ="+userID + " AND PlacementID = "+PlacementID;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount()>0) {
            return true;
        } else {
            return false;
        }
    }

    public void addFav(int PlacementID) {
        /**
         * Updates favorite when heart icon clicked
         */
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int userID = this.getUserID();

        values.put("UserID", userID);
        values.put("PlacementID", PlacementID);
        values.put("Sync", 0);
        db.insert("userFavTable", null, values);
        db.close();
    }

    public void removeFav(int PlacementID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int userID = this.getUserID();

        String query = "DELETE FROM userFavTable WHERE UserID=" + userID + " AND PlacementID = " + PlacementID;
        db.execSQL(query);
    }

    public boolean addUser(String username) {
        /**
         * Adds new user to the database (Username only - more details added via profile activity)
         */
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", username);
        contentValues.put("SYNC", 0);
        long res = db.insert("profileTable", null, contentValues);
        db.close();

        if (res == -1) {
            return false;
        } else return true;
    }

    public Boolean checkUsername(String username){
        /**
         * Returns true if username already exists in database
         */
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM profileTable WHERE username =?", new String[]{username});
        if (cursor.getCount()>0) return true;
        else return false;
    }

    public int getUserID() {
        /**
         * Gets the correct UserID for user logged in
          */
        session = new Session(context);
        String username = session.getUsername();
        int userID = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM profileTable WHERE username ='" + username +"';";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                userID = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userID;
    }

    public void addProfile(String name, String email, int phone, String dob, String address, String about, String experience, String university) {
        /**
         * Updates existing user/profile details for existing user in database
         */
        SQLiteDatabase db = this.getWritableDatabase();

        int userID = this.getUserID();
        session = new Session(context);
        String username = session.getUsername();

        String query = "UPDATE " + PROFILE_TABLE + " SET Username = '" + username + "', Name ='" + name +
                "', Email='" + email + "', Phone=" + phone + ", DOB='" + dob + "', Address='" + address +"', About='" + about + "', Experience='" +
                experience + "', University ='" + university + "', Sync=0 WHERE UserID = " + userID + ";";

        String update = "INSERT OR IGNORE INTO " + PROFILE_TABLE + " (UserID, Username, Name, Email, Phone, DOB, Address, About, Experience, University, Sync) " +
                "VALUES ('" + userID + "','" + username + "', '" + name + "', '" + email + "', " + phone + ", '" + dob + "', '" + address + "', '" + about + "', '"
                + experience + "', '" + university + "', 0)";

        db.execSQL(query);
        db.execSQL(update);
    }
}