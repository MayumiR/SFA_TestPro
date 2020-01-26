package com.bit.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bit.sfa.model.Attendance;
import com.bit.sfa.model.TourMapper;
import com.bit.sfa.helpers.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rashmi on 12/9/2018.
 */

public class AttendanceController {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "AttendanceController";

    public AttendanceController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public long InsertUpdateTourData(Attendance tour) {

        long result = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ATTENDANCE + " WHERE " + DatabaseHelper.ATTENDANCE_DATE + " = '" + tour.getFTOUR_DATE() + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.ATTENDANCE_DATE, tour.getFTOUR_DATE());
            values.put(DatabaseHelper.ATTENDANCE_F_KM, tour.getFTOUR_F_KM());
            values.put(DatabaseHelper.ATTENDANCE_F_TIME, tour.getFTOUR_F_TIME());
            values.put(DatabaseHelper.ATTENDANCE_ROUTE, tour.getFTOUR_ROUTE());
            values.put(DatabaseHelper.ATTENDANCE_S_KM, tour.getFTOUR_S_KM());
            values.put(DatabaseHelper.ATTENDANCE_S_TIME, tour.getFTOUR_S_TIME());
            values.put(DatabaseHelper.ATTENDANCE_VEHICLE, tour.getFTOUR_VEHICLE());
            values.put(DatabaseHelper.ATTENDANCE_DISTANCE, tour.getFTOUR_DISTANCE());
            values.put(DatabaseHelper.ATTENDANCE_IS_SYNCED, tour.getFTOUR_IS_SYNCED());
            values.put(DatabaseHelper.ATTENDANCE_REPCODE, tour.getFTOUR_REPCODE());
            values.put(DatabaseHelper.ATTENDANCE_MAC, tour.getFTOUR_MAC());
            values.put(DatabaseHelper.ATTENDANCE_DRIVER, tour.getFTOUR_DRIVER());
            values.put(DatabaseHelper.ATTENDANCE_ASSIST, tour.getFTOUR_ASSIST());


            if (cursor.getCount() > 0) {
                result = dB.update(DatabaseHelper.TABLE_ATTENDANCE, values, DatabaseHelper.ATTENDANCE_DATE + " =?", new String[]{String.valueOf(tour.getFTOUR_DATE())});
            } else {
                result = dB.insert(DatabaseHelper.TABLE_ATTENDANCE, null, values);
            }

            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return result;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<Attendance> getIncompleteRecord() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ATTENDANCE + " WHERE " + DatabaseHelper.ATTENDANCE_F_TIME + " IS NULL AND " + DatabaseHelper.ATTENDANCE_F_KM + " IS NULL AND " + DatabaseHelper.ATTENDANCE_DATE + " IS NOT NULL";


        Cursor cursor = dB.rawQuery(selectQuery, null);

        ArrayList<Attendance> tours = new ArrayList<>();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Attendance tour = new Attendance();
                    tour.setFTOUR_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_DATE)));
                    tour.setFTOUR_F_KM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_F_KM)));
                    tour.setFTOUR_F_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_F_TIME)));
                    tour.setFTOUR_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_ID)));
                    tour.setFTOUR_ROUTE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_ROUTE)));
                    tour.setFTOUR_S_KM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_S_KM)));
                    tour.setFTOUR_S_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_S_TIME)));
                    tour.setFTOUR_VEHICLE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_VEHICLE)));
                    tour.setFTOUR_DISTANCE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_DISTANCE)));
                    tour.setFTOUR_DRIVER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_DRIVER)));
                    tour.setFTOUR_ASSIST(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_ASSIST)));
                    tours.add(tour);
                }

                return tours;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return tours;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public boolean hasTodayRecord() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ATTENDANCE + " WHERE " + DatabaseHelper.ATTENDANCE_F_TIME + " IS NOT NULL AND " + DatabaseHelper.ATTENDANCE_F_KM + " IS NOT NULL AND " + DatabaseHelper.ATTENDANCE_DATE + "='" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' AND " + DatabaseHelper.ATTENDANCE_ROUTE + " IS NOT NULL AND " + DatabaseHelper.ATTENDANCE_S_KM + " IS NOT NULL AND " + DatabaseHelper.ATTENDANCE_S_TIME + " IS NOT NULL AND " + DatabaseHelper.ATTENDANCE_VEHICLE + " IS NOT NULL";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                return true;

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return false;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int updateIsSynced(TourMapper mapper) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.ATTENDANCE_IS_SYNCED, "1");

            if (mapper.isSynced()) {
                count = dB.update(DatabaseHelper.TABLE_ATTENDANCE, values, DatabaseHelper.ATTENDANCE_DATE + " =?", new String[]{String.valueOf(mapper.getFTOUR_DATE())});
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<TourMapper> getUnsyncedTourData() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<TourMapper> list = new ArrayList<TourMapper>();
        try {
            String s = "SELECT * FROM " + DatabaseHelper.TABLE_ATTENDANCE + " WHERE " + DatabaseHelper.ATTENDANCE_IS_SYNCED + "='0'";

            Cursor cursor = dB.rawQuery(s, null);

            while (cursor.moveToNext()) {

                TourMapper mapper = new TourMapper();
               // mapper.setConsoleDB(localSP.getString("Console_DB", "").toString());
                mapper.setFTOUR_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_DATE)));
                mapper.setFTOUR_F_KM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_F_KM)));
                mapper.setFTOUR_F_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_F_TIME)));
                mapper.setFTOUR_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_ID)));
                mapper.setFTOUR_ROUTE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_ROUTE)));
                mapper.setFTOUR_S_KM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_S_KM)));
                mapper.setFTOUR_S_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_S_TIME)));
                mapper.setFTOUR_VEHICLE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_VEHICLE)));
                mapper.setFTOUR_DISTANCE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_DISTANCE)));
                mapper.setFTOUR_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_IS_SYNCED)));
                mapper.setFTOUR_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_REPCODE)));
                mapper.setFTOUR_MAC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_MAC)));
                mapper.setfTOUR_DRIVER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_DRIVER)));
                mapper.setFTOUR_ASSIST(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ATTENDANCE_ASSIST)));

                list.add(mapper);

            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return list;

    }

    public boolean isDayEnd(String Ydate) {


        String[] dates = Ydate.split("-");
        int day = Integer.parseInt(dates[2].toString());
        day = day - 1;
        String oldDate = dates[0] + "-" + dates[1] + "-" + day;


        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ATTENDANCE + " WHERE EndTime IS NOT NULL AND EndKm IS NOT NULL AND tDate=" + oldDate;
            Cursor cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                return true;

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return false;
    }
}
