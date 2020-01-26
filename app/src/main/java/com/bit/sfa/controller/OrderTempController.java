package com.bit.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import com.bit.sfa.model.tempOrderDet;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by rashmi
 */

public class OrderTempController {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;

    public OrderTempController(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void insertOrUpdateProducts(ArrayList<tempOrderDet> list) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {
            dB.beginTransactionNonExclusive();
            String sql = "INSERT OR REPLACE INTO " + DatabaseHelper.TABLE_PRODUCT + " (itemcode,itemname,qty,price,prilcode) VALUES (?,?,?,?,?)";

            SQLiteStatement stmt = dB.compileStatement(sql);

            for (tempOrderDet items : list) {

                stmt.bindString(1, items.getPREPRODUCT_ITEMCODE());
                stmt.bindString(2, items.getPREPRODUCT_ITEMNAME());
                stmt.bindString(3, items.getPREPRODUCT_QTY());
                stmt.bindString(4, items.getPREPRODUCT_PRICE());
                stmt.bindString(5, items.getPREPRODUCT_UOM());

                stmt.execute();
                stmt.clearBindings();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dB.setTransactionSuccessful();
            dB.endTransaction();
            dB.close();
        }

    }

//-------------------------------------------------------------------------------------------------------------------------------------------------



    public int createOrUpdateSODett(ArrayList<tempOrderDet> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (tempOrderDet preProduct : list) {

                ContentValues values = new ContentValues();

                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PRODUCT + " WHERE " +
                        DatabaseHelper.PRODUCT_ITEMCODE + " = '" + preProduct.getPREPRODUCT_ITEMCODE() + "'";

                cursor = dB.rawQuery(selectQuery, null);
                values.put(DatabaseHelper.PRODUCT_ITEMCODE, preProduct.getPREPRODUCT_ITEMCODE());
                values.put(DatabaseHelper.PRODUCT_ITEMNAME, preProduct.getPREPRODUCT_ITEMNAME());
                values.put(DatabaseHelper.PRODUCT_QTY, preProduct.getPREPRODUCT_QTY());



                int cn = cursor.getCount();
                if (cn > 0) {

                    count = dB.update(DatabaseHelper.TABLE_PRODUCT, values,
                            DatabaseHelper.PRODUCT_ITEMCODE + " =?", new String[]{String.valueOf(preProduct.getPREPRODUCT_ITEMCODE())});

                } else {
                    count = (int) dB.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
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
        return count;

    }



    public boolean tableHasRecords() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PRODUCT, null);

            if (cursor.getCount() > 0)
                result = true;
            else
                result = false;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();

        }

        return result;

    }

    public ArrayList<tempOrderDet> getAllItems(String newText) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<tempOrderDet> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PRODUCT + " WHERE itemcode || itemname LIKE '%" + newText + "%' group by itemcode", null);

            while (cursor.moveToNext()) {
                tempOrderDet product = new tempOrderDet();
                product.setPREPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ID)));
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ITEMNAME)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_QTY)));

                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }
//--------------getItems ItemCodevise--------------------------------------------------------------------------------------------------------

    public ArrayList<tempOrderDet> getItemsCodeVise(String newText, String ItemCode) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<tempOrderDet> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PRODUCT + " WHERE itemcode_pre || itemname_pre LIKE '%" + newText + "%'and itemcode_pre = '" + ItemCode + "'", null);

            while (cursor.moveToNext()) {
                tempOrderDet product = new tempOrderDet();
                product.setPREPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ID)));
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ITEMNAME)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_QTY)));

                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }


    //-----------------------------------------------------------------------------------------------------------------------------------------

    public void updateProductQty(String itemCode, String qty) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.PRODUCT_QTY, qty);
            dB.update(DatabaseHelper.TABLE_PRODUCT, values, DatabaseHelper.PRODUCT_ITEMCODE + " =?", new String[]{String.valueOf(itemCode)});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }


    //------------------rashmi --------------------------------------------------------------------------------------------------

    public int updateProductQtyFor(String itemCode, String qty) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        try {

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.PRODUCT_QTY, qty);
          count=(int)  dB.update(DatabaseHelper.TABLE_PRODUCT, values, DatabaseHelper.PRODUCT_ITEMCODE + " =?", new String[]{String.valueOf(itemCode)});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
        return count;
    }
//----------------------------------------------------------------------------------------------------------------

    public ArrayList<tempOrderDet> getSelectedItems() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<tempOrderDet> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PRODUCT + " WHERE  qty <>'0'", null);

            while (cursor.moveToNext()) {
                tempOrderDet product = new tempOrderDet();
                product.setPREPRODUCT_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ID)));
                product.setPREPRODUCT_ITEMCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ITEMCODE)));
                product.setPREPRODUCT_ITEMNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_ITEMNAME)));
                product.setPREPRODUCT_REFNO(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_REFNO)));
                product.setPREPRODUCT_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_QTY)));
                product.setPREPRODUCT_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_PRICE)));
               // product.setPREPRODUCT_UOM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRODUCT_UO)));

                list.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }


    /*public ArrayList<InvDet> getSelectedItemsForInvoice(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        ArrayList<InvDet> list = new ArrayList<>();
        try {
            cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_FPRODUCT + " WHERE  qty<>'0'", null);

            while (cursor.moveToNext()) {
                InvDet invDet = new InvDet();
                invDet.setFINVDET_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ID)));
                invDet.setFINVDET_ITEM_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_ITEMCODE)));
                invDet.setFINVDET_REFNO(Refno);
                invDet.setFINVDET_SELL_PRICE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_PRICE)));
                invDet.setFINVDET_QOH(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QOH)));
                invDet.setFINVDET_QTY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FPRODUCT_QTY)));
                list.add(invDet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            dB.close();
        }

        return list;
    }*/

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*/

    public void mClearTables() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            dB.delete(DatabaseHelper.TABLE_PRODUCT, null, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dB.close();
        }
    }

}
