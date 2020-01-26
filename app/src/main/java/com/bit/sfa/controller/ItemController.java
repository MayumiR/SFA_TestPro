package com.bit.sfa.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.bit.sfa.model.Item;
import com.bit.sfa.model.tempOrderDet;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Rashmi on 12/20/2018.
 */

public class ItemController {

    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    Context context;
    private String TAG = "ItemController";

    public static SharedPreferences localSP;

    public ItemController(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public long InsertItems(ArrayList<Item> Items) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            dB.beginTransaction();
            String sql = "Insert or Replace into " + dbHelper.TABLE_ITEMS + " (" + dbHelper.ITEM_CODE + ", "
                    + dbHelper.ITEM_NAME + ", "
                    + dbHelper.UOM + ", "
                    + dbHelper.STATUS +") values(?,?,?,?)";

            SQLiteStatement insert = dB.compileStatement(sql);

            for (Item item : Items) {

                insert.bindString(1, item.getITEM_CODE());
                insert.bindString(2, item.getITEM_NAME());
                insert.bindString(3, item.getITEM_UOM());
                insert.bindString(4, item.getITEM_STATUS());

                insert.execute();

                count = 1;
            }

            dB.setTransactionSuccessful();
            Log.w(TAG, "Done");
        } catch (Exception e) {

            Log.v(TAG + " FmItemDS", e.toString());

        } finally {
            dB.endTransaction();

            dB.close();
        }
        return count;


    }
    /*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-**-*/

    public String getItemNameByCode(String code) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ITEMS + " WHERE " + DatabaseHelper.ITEM_CODE + "='" + code + "'";

        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {

                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_NAME));

            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return "";
    }

    /*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-**-*-*-*/
//---------------------------------------------------get Items for promo order----rashmi 2018-08-20------------------------------------------------

    public ArrayList<tempOrderDet> getAllItemForPreSales(String newText, String refno) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<tempOrderDet> list = new ArrayList<tempOrderDet>();
        String selectQuery;
        selectQuery = "SELECT itm.ItemName, itm.ItemCode, itm.UOM, itm.Status, pri.Price FROM Items itm," +
                " ItemPri pri WHERE itm.ItemName || itm.ItemCode LIKE '%" + newText + "%' AND  itm.ItemCode not in" +
                " (SELECT DISTINCT ItemCode FROM OrderDetail WHERE RefNo ='" + refno + "')";
        Cursor cursor = dB.rawQuery(selectQuery, null);
        try {
            while (cursor.moveToNext()) {


                tempOrderDet preProduct=new tempOrderDet();
                preProduct.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_CODE)));
                preProduct.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_NAME)));
                preProduct.setPREPRODUCT_UOM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.UOM)));
                preProduct.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEMPRI_PRICE)));
                 preProduct.setPREPRODUCT_QTY("0");

                list.add(preProduct);
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return list;
    }
    public ArrayList<Item> getAllItems() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Item> list = new ArrayList<Item>();
        try {


            String searchsql = "";
            searchsql = "SELECT * FROM " + DatabaseHelper.TABLE_ITEMS;
            cursor = dB.rawQuery(searchsql, null);


            while (cursor.moveToNext()) {

                Item item = new Item();
                item.setITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_CODE)));
                item.setITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_NAME)));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }
    public ArrayList<Item> findAllItems(String key) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<Item> list = new ArrayList<Item>();
        try {


            String searchsql = "";
            searchsql = "SELECT * FROM " + DatabaseHelper.TABLE_ITEMS + " WHERE ItemName LIKE '" + key + "%'";
            cursor = dB.rawQuery(searchsql, null);


            while (cursor.moveToNext()) {

                Item item = new Item();
                item.setITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_CODE)));
                item.setITEM_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM_NAME)));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }
}
