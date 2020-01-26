package com.bit.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bit.sfa.model.ReferenceDetail;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sathiyaraja on 6/20/2018.
 */

public class ReferenceDetailDownloader {
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG="ReferenceDetailDownloader";


    public ReferenceDetailDownloader(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateFCompanyBranch(ArrayList<ReferenceDetail> list) {
        int count =0;
        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;
        Cursor cursor_ini = null;
        try{

            cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_REFERENCE, null);

            for (ReferenceDetail branch : list) {

                ContentValues values = new ContentValues();
                values.put(dbHelper.REFERENCE_REPCODE,  branch.getREFERENCE_REP_CODE());
                values.put(dbHelper.REFERENCE_SETTINGS_CODE,  branch.getREFERENCE_SETTING_CODE());
                values.put(dbHelper.REFERENCE_NNUM_VAL,  branch.getREFERENCE_NNUM_VAL());
                values.put(dbHelper.REFERENCE_NYEAR_VAL,  branch.getREFERENCE_NYEAR_VAL());
                values.put(dbHelper.REFERENCE_NMONTH_VAL,  branch.getREFERENCE_NMONTH_VAL());

                if (cursor_ini.moveToFirst()) {
                    String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REFERENCE + " WHERE " + dbHelper.REFERENCE_SETTINGS_CODE + "='" + branch.getREFERENCE_SETTING_CODE() + "' AND " + dbHelper.REFERENCE_REPCODE + "='" + branch.getREFERENCE_REP_CODE() + "' AND " + dbHelper.REFERENCE_NYEAR_VAL + "='" + branch.getREFERENCE_NYEAR_VAL() + "' AND " + dbHelper.REFERENCE_NMONTH_VAL + "='" + branch.getREFERENCE_NMONTH_VAL() + "'";
                    cursor = dB.rawQuery(selectQuery, null);

                    if (cursor.moveToFirst()) {
                        count = (int) dB.update(dbHelper.TABLE_REFERENCE, values, dbHelper.REFERENCE_SETTINGS_CODE + "='" + branch.getREFERENCE_SETTING_CODE() + "' AND " + dbHelper.REFERENCE_REPCODE + "='" + branch.getREFERENCE_REP_CODE() + "' AND " + dbHelper.REFERENCE_NYEAR_VAL + "='" + branch.getREFERENCE_NYEAR_VAL() + "' AND " + dbHelper.REFERENCE_NMONTH_VAL + "='" + branch.getREFERENCE_NMONTH_VAL() + "'", null);
                    } else {
                        count = (int) dB.insert(dbHelper.TABLE_REFERENCE, null, values);
                    }

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_REFERENCE, null, values);
                }

            }
        }finally {
            if (cursor !=null) {
                cursor.close();
            }

            if (cursor_ini !=null) {
                cursor_ini.close();
            }
            dB.close();
        }
        return count;

    }

    @SuppressWarnings("static-access")
    public String getCurrentNextNumVal(String cSettingsCode ){

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Calendar c = Calendar.getInstance();

        String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REFERENCE +" WHERE "+dbHelper.REFERENCE_SETTINGS_CODE +" ='"+cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){

            return cursor.getString(cursor.getColumnIndex(dbHelper.REFERENCE_NNUM_VAL));

        }

        return "0";
    }

    @SuppressWarnings("static-access")
    public int deleteAll(){

        int count =0;

        if(dB == null){
            open();
        }else if(!dB.isOpen()){
            open();
        }
        Cursor cursor = null;
        try{

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_REFERENCE, null);
            count =cursor.getCount();
            if(count>0){
                int success = dB.delete(dbHelper.TABLE_REFERENCE, null, null);
                Log.v("Success", success+"");
            }
        }catch (Exception e){

            Log.v(TAG+" Exception", e.toString());

        }finally{
            if (cursor !=null) {
                cursor.close();
            }
            dB.close();
        }

        return count;

    }




}
