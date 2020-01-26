package com.bit.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.bit.sfa.helpers.DatabaseHelper;
import com.bit.sfa.model.DayNPrdDet;

import java.util.ArrayList;

public class DayNPrdDetController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "Ebony";

    public DayNPrdDetController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateNonPrdDet(ArrayList<DayNPrdDet> list) {

        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (DayNPrdDet nondet : list) {
                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.NONPRDDET_REFNO + " = '" + nondet.getNONPRDDET_REFNO() + "'"
                        //;
                        + " AND " + DatabaseHelper.NONPRDDET_REASON_CODE + " = '" + nondet.getNONPRDDET_REASON_CODE() + "'";
                cursor = dB.rawQuery(selectQuery, null);

                values.put(DatabaseHelper.NONPRDDET_REFNO, nondet.getNONPRDDET_REFNO());
                values.put(DatabaseHelper.NONPRDDET_REASON, nondet.getNONPRDDET_REASON());
                values.put(DatabaseHelper.NONPRDDET_REASON_CODE, nondet.getNONPRDDET_REASON_CODE());


                int count = cursor.getCount();
                if (count > 0) {
                    serverdbID = (int) dB.update(DatabaseHelper.TABLE_NONPRDDET, values, DatabaseHelper.NONPRDDET_REASON_CODE + " =?", new String[]{String.valueOf(nondet.getNONPRDDET_REFNO())});

                } else {
                    serverdbID = (int) dB.insert(DatabaseHelper.TABLE_NONPRDDET, null, values);
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
        return serverdbID;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public ArrayList<DayNPrdDet> getAllnonprdDetails(String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<DayNPrdDet> list = new ArrayList<DayNPrdDet>();

        String selectQuery = "select * from " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.NONPRDDET_REFNO + "='" + refno + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {

            DayNPrdDet fnonset = new DayNPrdDet();

            // fnonset.setNONPRDDET_DEBCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_DEBCODE)));
            fnonset.setNONPRDDET_REASON(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_REASON)));
            fnonset.setNONPRDDET_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_REFNO)));
            fnonset.setNONPRDDET_REASON_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_REASON_CODE)));

            list.add(fnonset);
        }

        return list;
    }
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

//    public String getDuplicate(String code, String RefNo) {
//
//        if (dB == null) {
//            open();
//        } else if (!dB.isOpen()) {
//            open();
//        }
//
//        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.NONPRDDET_DEBCODE + "='" + code + "' AND " + DatabaseHelper.NONPRDDET_REFNO + "='" + RefNo + "'";
//
//        Cursor cursor = dB.rawQuery(selectQuery, null);
//
//        while (cursor.moveToNext()) {
//            return cursor.getString(cursor.getColumnIndex(DatabaseHelper.NONPRDDET_DEBCODE));
//        }
//
//        return "";
//    }
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public int deleteOrdDetByID(String id) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.NONPRDDET_REFNO + "='" + id + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_NONPRDDET, DatabaseHelper.NONPRDDET_REFNO + "='" + id + "'", null);
                Log.v("FtranDet Deleted ", success + "");
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
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*/

    public int OrdDetByRefno(String RefNo) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NONPRDDET + " WHERE " + DatabaseHelper.NONPRDDET_REFNO + "='" + RefNo + "'", null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(DatabaseHelper.TABLE_NONPRDDET, DatabaseHelper.NONPRDDET_REFNO + "='" + RefNo + "'", null);
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
    public int getNonProdCount(String refNo) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            String selectQuery = "SELECT count(RefNo) as RefNo FROM " + DatabaseHelper.TABLE_NONPRDDET +  " WHERE  " + DatabaseHelper.NONPRDDET_REFNO + "='" + refNo + "'";
            Cursor cursor = dB.rawQuery(selectQuery, null);

            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(cursor.getColumnIndex("RefNo")));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            dB.close();
        }
        return 0;

    }
}
