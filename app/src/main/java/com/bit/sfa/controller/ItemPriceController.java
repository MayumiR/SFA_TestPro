package com.bit.sfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.bit.sfa.model.ItemPri;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

public class ItemPriceController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "ItemPriceController";

	public ItemPriceController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	@SuppressWarnings("static-access")
	public int createOrUpdateItemPri(ArrayList<ItemPri> list) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {

			dB.beginTransaction();

			String sql = "Insert or Replace into " + dbHelper.TABLE_ITEMPRI + " (" + dbHelper.ITEMPRI_ITEM_CODE + ", " +
					 dbHelper.ITEMPRI_PRICE +
				  ") values(?,?)";

			SQLiteStatement insert = dB.compileStatement(sql);

			for (ItemPri pri : list) {

				insert.bindString(1, pri.getITEMPRI_ITEM_CODE());
				insert.bindString(2, pri.getITEMPRI_PRICE());


				insert.execute();

				count = 1;
			}

			dB.setTransactionSuccessful();
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.endTransaction();
			dB.close();
		}
		return count;

	}

	public int deleteAllItemPri() {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {

			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_ITEMPRI, null);
			count = cursor.getCount();
			if (count > 0) {
				int success = dB.delete(dbHelper.TABLE_ITEMPRI, null, null);
				Log.v("Success", success + "");
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
 



}
