package de.forro_apps.ggvertretungsplan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The database in which settings are stored.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "settings.db";

    /**
     * The data of the table.
     */
    static class Settings {
        static final String TABLE = "login";
        static final String COLUMN_USERNAME = "username";
        static final String COLUMN_PASSWORD = "password";
        static final String COLUMNN_TYPE = "type";
    }


    /**
     * @param context The application {@link Context}
     * @param factory Can be null
     */
    Database(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table
        String query = "CREATE TABLE " + Settings.TABLE + " (" +
                Settings.COLUMN_USERNAME + " VARCHAR(32), " +
                Settings.COLUMN_PASSWORD + " VARCHAR(32), " +
                Settings.COLUMNN_TYPE + " VARCHAR(2)" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete the Table
        String query = "DROP TABLE IF EXISTS " + Settings.TABLE;
        db.execSQL(query);
        onCreate(db);
    }


    /**
     * This class contains the methods handling with the database. The methods execute queries to edit or get data.
     */
    public static class SQLMethods extends Database {

        /**
         * @param context The application {@link Context}
         * @param factory Can be null
         */
        public SQLMethods(Context context, SQLiteDatabase.CursorFactory factory) {
            super(context, factory);
        }


        /**
         * This methods stores the given parameters into the database.
         * @param username The username to authenticate to the webserver
         * @param password The password to authenticate to the webserver
         * @param type Is the user a student (0) or teacher (1)
         */
        public void setLoginCredentials(String username, String password, String type) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Settings.COLUMN_USERNAME, username);
            values.put(Settings.COLUMN_PASSWORD, password);
            values.put(Settings.COLUMNN_TYPE, type);
            db.insert(Settings.TABLE, null, values);
            db.update(Settings.TABLE, values, null, null);
            db.close();
        }


        /**
         * @return The stored login credentials to be used to authenticate to the webserver.<br>
         *     <p>#getLoginCredentials()[0] - username</p>
         *     <p>#getLoginCredentials()[1] - password</p>
         *     <p>#getLoginCredentials()[2] - type</p>
         */
        public String[] getLoginCredentials() {
            String[] res = new String[3];

            SQLiteDatabase db = getWritableDatabase();

            Cursor c = db.rawQuery("SELECT " + Settings.COLUMN_USERNAME + " FROM " + Settings.TABLE, null);
            if(c.moveToFirst()) {
                res[0] = c.getString(c.getColumnIndex(Settings.COLUMN_USERNAME));
            } else {
                res[0] = "";
            }

            c = db.rawQuery("SELECT " + Settings.COLUMN_PASSWORD + " FROM " + Settings.TABLE, null);
            if(c.moveToFirst()) {
                res[1] = c.getString(c.getColumnIndex(Settings.COLUMN_PASSWORD));
            } else {
                res[1] = "";
            }

            c = db.rawQuery("SELECT " + Settings.COLUMNN_TYPE + " FROM " + Settings.TABLE, null);
            if(c.moveToFirst()) {
                res[2] = c.getString(c.getColumnIndex(Settings.COLUMNN_TYPE));
            } else {
                res[2] = "0";
            }

            db.close();
            c.close();
            return res;
        }
    }
}
