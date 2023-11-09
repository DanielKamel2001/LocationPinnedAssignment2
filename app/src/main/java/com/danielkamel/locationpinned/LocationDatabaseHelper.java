package com.danielkamel.locationpinned;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import kotlin.text.UStringsKt;

public class LocationDatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Location_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Locations_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String csvPath = "outCordsOnly50.csv";
    Context context;

    public LocationDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ADDRESS + " TEXT, " + COLUMN_LATITUDE + " TEXT, " + COLUMN_LONGITUDE + " TEXT );";
        db.execSQL(createQuery);

//        Load Data from CSV file
        try {
            loadAddresses(db);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    Void loadAddresses(SQLiteDatabase db) throws IOException {

        GeoCodingHelper geoCodingHelper = new GeoCodingHelper();

//        read the csv and insert the coords
//        FileReader file = new FileReader(context.getAssets().open(csvPath));
//        BufferedReader buffer = new BufferedReader(file);

        InputStreamReader is = new InputStreamReader(context.getAssets()
                .open(csvPath));

        BufferedReader buffer = new BufferedReader(is);
//        reader.readLine();

        String line = "";
        String columns = COLUMN_LATITUDE + ", " + COLUMN_LONGITUDE + ", " + COLUMN_ADDRESS; //"_id, name, dt1, dt2, dt3";
        System.out.println(columns);
        String str1 = "INSERT INTO " + TABLE_NAME + " (" + columns + ") values(";
        String str2 = ");";

        db.beginTransaction();
        while ((line = buffer.readLine()) != null) {
            StringBuilder sb = new StringBuilder(str1);
            String[] str = line.split(",");
            sb.append("'" + str[0] + "',");
            sb.append("'" + str[1] + "',");

            sb.append("'" + geoCodingHelper.getGeoCodes(this.context, Double.parseDouble(str[0]), Double.parseDouble(str[1])) + "'");
            sb.append(str2);
            db.execSQL(sb.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return null;
    }

    Cursor readAllData() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + ";";
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor pointer = null;

        if (database != null) {
            pointer = database.rawQuery(selectQuery, null);
        }

        return pointer;
    }


    void addAddress(String address, String latitude, String longitude) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        long result = database.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Address Added", Toast.LENGTH_SHORT).show();
        }

    }

    public void delete(Context context, String valueOf) {
        SQLiteDatabase database = this.getWritableDatabase();
        long result = database.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{valueOf});
        if (result == -1) {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }
    }
    void update(Context context,String id, String address, String latitude, String longitude){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        long result = database.update(TABLE_NAME, values,COLUMN_ID+"=?",new String[]{id} );
        if (result == -1) {
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
        }


    }

}
