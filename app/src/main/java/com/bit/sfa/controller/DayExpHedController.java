package com.bit.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.bit.sfa.R;
import com.bit.sfa.helpers.DatabaseHelper;
import com.bit.sfa.model.DayExpHed;

import java.util.ArrayList;

public class DayExpHedController {
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "Ebony";

    public DayExpHedController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/

    public int createOrUpdateExpHed(ArrayList<DayExpHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayExpHed exphed : list) {
                ContentValues values = new ContentValues();

                values.put(DatabaseHelper.FDAYEXPHED_REFNO, exphed.getEXPHED_REFNO());
                values.put(DatabaseHelper.FDAYEXPHED_TXNDATE, exphed.getEXPHED_TXNDATE());
                values.put(DatabaseHelper.FDAYEXPHED_REPCODE, exphed.getEXPHED_REPCODE());
                values.put(DatabaseHelper.FDAYEXPHED_REMARKS, exphed.getEXPHED_REMARK());
                values.put(DatabaseHelper.FDAYEXPHED_ADDDATE, exphed.getEXPHED_ADDDATE());
                values.put(DatabaseHelper.FDAYEXPHED_ISSYNC, exphed.getEXPHED_IS_SYNCED());
                values.put(DatabaseHelper.FDAYEXPHED_TOTAMT, exphed.getEXPHED_TOTAMT());
                values.put(DatabaseHelper.FDAYEXPHED_ACTIVESTATE, exphed.getEXPHED_ACTIVESTATE());
                values.put(DatabaseHelper.FDAYEXPHED_LATITUDE, exphed.getEXPHED_LATITUDE());
                values.put(DatabaseHelper.FDAYEXPHED_LONGITUDE, exphed.getEXPHED_LONGITUDE());


                count = (int) dB.insert(DatabaseHelper.TABLE_DAYEXPHED, null, values);

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

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/

    public ArrayList<DayExpHed> getAllExpHedDetails(String newTExt) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayExpHed> list = new ArrayList<DayExpHed>();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_DAYEXPHED + " WHERE TxnDate LIKE '%" + newTExt + "%'";
        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            DayExpHed fdayexpset = new DayExpHed();
            fdayexpset.setEXPHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_REFNO)));
            fdayexpset.setEXPHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_TXNDATE)));
            fdayexpset.setEXPHED_TOTAMT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_TOTAMT)));
            list.add(fdayexpset);
        }

        return list;
    }

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/

    public int undoExpHedByID(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_DAYEXPHED + " WHERE " + DatabaseHelper.FDAYEXPHED_REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_DAYEXPHED, DatabaseHelper.FDAYEXPHED_REFNO + "='" + RefNo + "'", null);

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }

        return count;

    }

	/*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*/



    public int restDataExp(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_DAYEXPHED + " WHERE " + DatabaseHelper.FDAYEXPHED_REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(DatabaseHelper.TABLE_DAYEXPHED, DatabaseHelper.FDAYEXPHED_REFNO + " ='" + refno + "'", null);
                Log.v("Success Stauts", count + "");
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


    public boolean isEntrySynced(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select issync from FDayExpHed where refno ='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            String result = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FDAYEXPHED_ISSYNC));

            if (result.equals("1"))
                return true;

        }
        cursor.close();
        dB.close();
        return false;

    }


    public int updateIsSynced(boolean status,String refNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();


            values.put(DatabaseHelper.FDAYEXPHED_ISSYNC, "1");

            if (status) {
                count = dB.update(DatabaseHelper.TABLE_DAYEXPHED, values, DatabaseHelper.FDAYEXPHED_REFNO + " =?",
                        new String[] { String.valueOf(refNo) });
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


    public ArrayList<DayExpHed> getAllUnSync() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayExpHed> list = new ArrayList<DayExpHed>();

        @SuppressWarnings("static-access")
        String selectQuery = "select * from " + dbHelper.TABLE_DAYEXPHED + " Where " + dbHelper.FDAYEXPHED_ACTIVESTATE
                + "='0' and " + dbHelper.FDAYEXPHED_ISSYNC + "='0'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        while (cursor.moveToNext()) {

            DayExpHed expHed = new DayExpHed();
            DayExpDetController detDS = new DayExpDetController(context);
            ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
            expHed.setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.ExpenseNumVal)));

            expHed.setEXPHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.FDAYEXPHED_REFNO)));
            expHed.setEXPHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.FDAYEXPHED_LONGITUDE)));
            expHed.setEXPHED_LATITUDE(cursor.getString(cursor.getColumnIndex(dbHelper.FDAYEXPHED_LATITUDE)));
            expHed.setEXPHED_REMARK(cursor.getString(cursor.getColumnIndex(dbHelper.FDAYEXPHED_REMARKS)));
            expHed.setEXPHED_REPCODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDAYEXPHED_REPCODE)));
            expHed.setEXPHED_TOTAMT(cursor.getString(cursor.getColumnIndex(dbHelper.FDAYEXPHED_TOTAMT)));
            expHed.setEXPHED_TXNDATE(cursor.getString(cursor.getColumnIndex(dbHelper.FDAYEXPHED_TXNDATE)));

            expHed.setExpenseDets(detDS.getAllUnSync(cursor.getString(cursor.getColumnIndex(dbHelper.FDAYEXPHED_REFNO))));

            list.add(expHed);

        }

        return list;
    }
}
