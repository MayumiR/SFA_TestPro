package com.bit.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.bit.sfa.R;
import com.bit.sfa.helpers.SharedPref;
import com.bit.sfa.model.NewCustomer;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Rashmi on 25/12/2018.
 */

public class NewCustomerController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "NewCustomerController";
    public static final String SETTINGS = "SETTINGS";

    public NewCustomerController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateCustomer(ArrayList<NewCustomer> list) {

        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }


        Cursor cursor = null;
        try {

            for (NewCustomer fN : list) {

                ContentValues contentValues = new ContentValues();
                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_NEW_CUSTOMER + " WHERE " + dbHelper.CUSTOMER_ID + " = '" + fN.getCUSTOMER_ID() + "'";
                cursor = dB.rawQuery(selectQuery, null);
                contentValues.put(dbHelper.CUSTOMER_ID, fN.getCUSTOMER_ID());
                contentValues.put(dbHelper.NAME, fN.getNAME());
                contentValues.put(dbHelper.ADDRESS, fN.getADDRESS1());
                contentValues.put(dbHelper.MOBILE, fN.getMOBILE());
                contentValues.put(dbHelper.E_MAIL, fN.getE_MAIL());
                contentValues.put(dbHelper.ROUTE_ID, fN.getROUTE_ID());
                contentValues.put(dbHelper.C_LONGITUDE, fN.getC_LONGITUDE());
                contentValues.put(dbHelper.C_LATITUDE, fN.getC_LATITUDE());
                contentValues.put(dbHelper.C_IS_SYNCED, fN.getC_SYNCSTATE());

                int cn = cursor.getCount();
                if (cn > 0) {
                    serverdbID = dB.update(dbHelper.TABLE_NEW_CUSTOMER, contentValues, dbHelper.CUSTOMER_ID + " = '" + fN.getCUSTOMER_ID() + "'", null);
                } else {
                    serverdbID = (int) dB.insert(dbHelper.TABLE_NEW_CUSTOMER, null, contentValues);
                }

            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return serverdbID;


    }
//----------------------------------------------------------------------------------------------------------------

    public ArrayList<NewCustomer> getAllNewCusDetails(String newTExt, String uploaded) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<NewCustomer> list = new ArrayList<NewCustomer>();
        String selectQuery;

        selectQuery = "select * from " + DatabaseHelper.TABLE_NEW_CUSTOMER ;

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            NewCustomer fCustomer = new NewCustomer();
            fCustomer.setCUSTOMER_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_ID)));
            fCustomer.setNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));
            fCustomer.setC_SYNCSTATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IS_SYNCED)));
            list.add(fCustomer);
        }

        return list;
    }

//---------------------get all Customer for edit------------------------------------------------------------------------

    public ArrayList<NewCustomer> getAllNewCusDetailsForEdit(String newTExt) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<NewCustomer> list = new ArrayList<NewCustomer>();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_NEW_CUSTOMER + " WHERE Name LIKE '%" + newTExt + "%' and isSynced=0";
        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            NewCustomer fCustomer = new NewCustomer();
            fCustomer.setCUSTOMER_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_ID)));
            fCustomer.setNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));
            fCustomer.setADDRESS1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS)));
            fCustomer.setMOBILE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOBILE)));
            fCustomer.setE_MAIL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.E_MAIL)));
            fCustomer.setROUTE_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_ID)));
            fCustomer.setC_SYNCSTATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IS_SYNCED)));

            list.add(fCustomer);
        }

        return list;
    }

    public int updateIsSynced(String res, String customerID) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.C_IS_SYNCED, "1");

            if (res.equalsIgnoreCase("1")) {
                count = dB.update(DatabaseHelper.TABLE_NEW_CUSTOMER, values, DatabaseHelper.CUSTOMER_ID + " =?", new String[]{String.valueOf(customerID)});
            }

        } catch (Exception e) {

            e.printStackTrace();

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

        Cursor cursor = dB.rawQuery("select isSynced from NewCustomer where customerID ='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            String result = cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IS_SYNCED));

            if (result.equals("1"))
                return true;

        }
        cursor.close();
        dB.close();
        return false;

    }

    public int deleteRecord(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NEW_CUSTOMER + " WHERE " + DatabaseHelper.CUSTOMER_ID + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(DatabaseHelper.TABLE_NEW_CUSTOMER, DatabaseHelper.CUSTOMER_ID + " ='" + refno + "'", null);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }


    public ArrayList<NewCustomer> getUnsyncRecord() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        ReferenceDetailDownloader branchDS = new ReferenceDetailDownloader(context);

        ArrayList<NewCustomer> list = new ArrayList<NewCustomer>();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_NEW_CUSTOMER + " WHERE  isSynced='N'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            NewCustomer fCustomer = new NewCustomer();
            fCustomer.setCUSTOMER_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_ID)));

            fCustomer.setNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));
            fCustomer.setC_SYNCSTATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IS_SYNCED)));
            fCustomer.setnNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.newCusVal)));
            fCustomer.setC_REPCODE(SharedPref.getInstance(context).getLoginUser().getCode());

            fCustomer.setROUTE_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_ID)));

            fCustomer.setADDRESS1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS)));

            fCustomer.setMOBILE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOBILE)));
            fCustomer.setE_MAIL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.E_MAIL)));

            fCustomer.setC_LATITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_LATITUDE)));
            fCustomer.setC_LONGITUDE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_LONGITUDE)));



            list.add(fCustomer);
        }
        return list;
    }
}
