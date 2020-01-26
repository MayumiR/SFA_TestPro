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
import com.bit.sfa.model.DayNPrdHed;

import java.util.ArrayList;

public class DayNPrdHedController {
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "NONPROD DS";

    public DayNPrdHedController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateNonPrdHed(ArrayList<DayNPrdHed> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayNPrdHed nonhed : list) {
                ContentValues values = new ContentValues();

                values.put(dbHelper.NONPRDHED_REFNO, nonhed.getNONPRDHED_REFNO());
                values.put(dbHelper.NONPRDHED_TXNDATE, nonhed.getNONPRDHED_TXNDATE());
                values.put(dbHelper.NONPRDHED_REPCODE, nonhed.getNONPRDHED_REPCODE());
                values.put(dbHelper.NONPRDHED_REMARK, nonhed.getNONPRDHED_REMARK());
                values.put(dbHelper.NONPRDHED_ADDDATE, nonhed.getNONPRDHED_ADDDATE());
                values.put(dbHelper.NONPRDHED_IS_SYNCED, nonhed.getNONPRDHED_IS_SYNCED());
                values.put(dbHelper.NONPRDHED_LONGITUDE,nonhed.getNONPRDHED_LONGITUDE());
                values.put(dbHelper.NONPRDHED_LATITUDE,nonhed.getNONPRDHED_LATITUDE());
                values.put(dbHelper.NONPRDHED_DEBCODE,nonhed.getNONPRDHED_DEBCODE());
                values.put(dbHelper.NONPRDHED_IS_ACTIVE,nonhed.getNONPRDHED_IS_ACTIVE());

                count = (int) dB.insert(dbHelper.TABLE_NONPRDHED, null, values);

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
//    public boolean validateActiveNonPrd()
//    {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        boolean res = false;
//
//        String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        Cursor cursor = null;
//        try {
//            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDHED + " WHERE " + DatabaseHelper.NONPRDHED_IS_ACTIVE+ "='1'";
//            cursor = dB.rawQuery(selectQuery, null);
//
//            /*Active invoice available*/
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
//
//                String txndate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_TXNDATE));
//
//                /*txndate is equal to current date*/
//                if (txndate.equals(toDay))
//                    res = true;
//                /*if invoice is older, then reset data*/
//                else {
//                    String Refno = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTRANSOHED_REFNO));
//                    restData(Refno);
////                    new InvDetDS(context).restData(Refno);
////                    new OrderDiscDS(context).clearData(Refno);
////                    new OrdFreeIssueDS(context).ClearFreeIssues(Refno);
//                    UtilityContainer.ClearNonSharedPref(context);
//                }
//
//            } else
//                res = false;
//
//        } catch (Exception e) {
//            Log.v(TAG, e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//
//        return res;
//
//    }

	/*-*-*-*--*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*--*-*-*-*-*-*-*/

    /**
     * -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
     * *-*-*-*-*-*-*-*-*-*-*-*-
     */

    public boolean restData(String refno) {

        boolean Result = false;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDHED + " WHERE " + DatabaseHelper.NONPRDHED_REFNO + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_IS_SYNCED));
                /* if order already synced, can't delete */
                if (status.equals("1"))
                    Result = false;
                else {
                    int success = dB.delete(DatabaseHelper.TABLE_NONPRDHED, DatabaseHelper.NONPRDHED_REFNO + " ='" + refno + "'", null);
                    Log.v("Success", success + "");
                    Result = true;
                }
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return Result;

    }
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public ArrayList<DayNPrdHed> getAllnonprdHedDetails(String newText) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdHed> list = new ArrayList<DayNPrdHed>();

        String selectQuery = "select * from " + dbHelper.TABLE_NONPRDHED + " WHERE AddDate LIKE '%" + newText + "%'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            DayNPrdHed fnonset = new DayNPrdHed();
            fnonset.setNONPRDHED_REFNO(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDHED_REFNO)));
            fnonset.setNONPRDHED_ADDDATE(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDHED_ADDDATE)));
            fnonset.setNONPRDHED_IS_SYNCED(cursor.getString(cursor.getColumnIndex(dbHelper.NONPRDHED_IS_SYNCED)));

            list.add(fnonset);

        }

        return list;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    @SuppressWarnings("static-access")
    public int undoOrdHedByID(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_NONPRDHED + " WHERE " + dbHelper.NONPRDHED_REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_NONPRDHED, dbHelper.NONPRDHED_REFNO + "='" + RefNo + "'", null);

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

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public ArrayList<DayNPrdHed> getUnSyncedData() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdHed> list = new ArrayList<DayNPrdHed>();

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDHED + " WHERE " + DatabaseHelper.NONPRDHED_IS_SYNCED + "='0'";
            Cursor cursor = dB.rawQuery(selectQuery, null);
            localSP = context.getSharedPreferences(SETTINGS, 0);

            while (cursor.moveToNext()) {

                DayNPrdHed mapper = new DayNPrdHed();
                ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);
                mapper.setNextNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.ExpenseNumVal)));

                mapper.setNONPRDHED_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_ADDDATE)));
                mapper.setNONPRDHED_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_REFNO)));
                mapper.setNONPRDHED_REMARK(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_REMARK)));
                mapper.setNONPRDHED_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_REPCODE)));
                mapper.setNONPRDHED_TXNDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_TXNDATE)));
                mapper.setNONPRDHED_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_DEBCODE)));
                mapper.setNONPRDHED_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_LONGITUDE)));
                mapper.setNONPRDHED_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_LATITUDE)));
                mapper.setNonprdDets(new DayNPrdDetController(context).getAllnonprdDetails(mapper.getNONPRDHED_REFNO()));

                list.add(mapper);
            }

        } catch (Exception e) {
            Log.v(TAG + " Exception", e.toString());
        } finally {
            dB.close();
        }

        return list;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

//    public int updateIsSynced(NonProdMapper mapper) {
//
//        int count = 0;
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//        Cursor cursor = null;
//
//        try {
//            ContentValues values = new ContentValues();
//            values.put(DatabaseHelper.NONPRDHED_IS_SYNCED, "1");
//            if (mapper.isSynced()) {
//                count = dB.update(DatabaseHelper.TABLE_NONPRDHED, values, DatabaseHelper.NONPRDHED_REFNO + " =?", new String[]{String.valueOf(mapper.getNONPRDHED_REFNO())});
//            }
//
//        } catch (Exception e) {
//
//            Log.v(TAG + " Exception", e.toString());
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            dB.close();
//        }
//        return count;
//
//    }

    public boolean isEntrySynced(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select ISsync from FDaynPrdHed where refno ='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            String result = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDHED_IS_SYNCED));

            if (result.equals("1"))
                return true;

        }
        cursor.close();
        dB.close();
        return false;

    }

}
