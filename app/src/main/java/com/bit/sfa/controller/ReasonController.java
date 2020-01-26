package com.bit.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bit.sfa.model.Reason;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

public class ReasonController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "ReasonController";

	public ReasonController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	/*
	 * insert code
	 */
	@SuppressWarnings("static-access")
	public int createOrUpdateReason(ArrayList<Reason> list) {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		Cursor cursor_ini = null;
		try {

			cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_REASON, null);

			for (Reason reason : list) {
				ContentValues values = new ContentValues();

				values.put(dbHelper.REASON_NAME, reason.getReasonName());
				values.put(dbHelper.REASON_CODE, reason.getReasonCode());
				values.put(dbHelper.REASON_TYPE, reason.getReasonType());

				if (cursor_ini.moveToFirst()) {
					String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON + " WHERE " + dbHelper.REASON_CODE + "='" + reason.getReasonCode() + "'";
					cursor = dB.rawQuery(selectQuery, null);

					if (cursor.moveToFirst()) {
						count = (int) dB.update(dbHelper.TABLE_REASON, values, dbHelper.REASON_CODE + "='" + reason.getReasonCode() + "'", null);
					} else {
						count = (int) dB.insert(dbHelper.TABLE_REASON, null, values);
					}

				} else {
					count = (int) dB.insert(dbHelper.TABLE_REASON, null, values);
				}

			}
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			
			if (cursor_ini != null) {
				cursor_ini.close();
			}
			dB.close();
		}
		return count;

	}

	/*
	 * delete code
	 */
	@SuppressWarnings("static-access")
	public int deleteAll() {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {

			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_REASON, null);
			count = cursor.getCount();
			if (count > 0) {
				int success = dB.delete(dbHelper.TABLE_REASON, null, null);
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

	public ArrayList<String> getReasonName() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<String> reason = new ArrayList<String>();

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON + " WHERE " + dbHelper.REASON_CODE + "='RT01' OR reatcode='RT02'";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {

			reason.add(cursor.getString(cursor.getColumnIndex(dbHelper.REASON_NAME)));

		}

		return reason;
	}

	public String getReaCodeByName(String name) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON + " WHERE " + dbHelper.REASON_NAME + "='" + name + "'";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(dbHelper.REASON_CODE));
		}

		return "";
	}

	public ArrayList<Reason> getAllExpense(String excode) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> list = new ArrayList<Reason>();
		String selectQuery = null;
		if(excode.equals(""))
			selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON + " WHERE " + dbHelper.REASON_TYPE + " = 'ex'";
		else
			selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON + " WHERE " + dbHelper.REASON_CODE + "='" + excode + "' and "+ dbHelper.REASON_TYPE + " = 'ex'";;

		Cursor cursor = dB.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {

			Reason expense = new Reason();

			expense.setReasonCode(cursor.getString(cursor.getColumnIndex(dbHelper.REASON_CODE)));
			expense.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.REASON_NAME)));

			list.add(expense);

		}

		return list;
	}


	public String getReasonByReaCode(String reaCode) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON + " WHERE " + dbHelper.REASON_CODE + "='" + reaCode + "'";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex(dbHelper.REASON_NAME));
		}

		return "";
	}

	public ArrayList<Reason> getAllNonPrdReasons() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> list = new ArrayList<Reason>();

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON + " WHERE " + dbHelper.REASON_TYPE + "='np'";

		Cursor cursor = dB.rawQuery(selectQuery, null);
		while (cursor.moveToNext()) {

			Reason reason = new Reason();

			reason.setReasonCode(cursor.getString(cursor.getColumnIndex(dbHelper.REASON_CODE)));
			reason.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.REASON_NAME)));

			list.add(reason);

		}

		return list;
	}

	public ArrayList<Reason> getDebDetails(String searchword) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reason> Itemname = new ArrayList<Reason>();

		String selectQuery = "select * from fReason where ReaTcode='RT02' AND ReaCode LIKE '%" + searchword + "%' OR ReaName LIKE '%" + searchword + "%' ";

		Cursor cursor = null;
		cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {
			Reason items = new Reason();

			items.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.REASON_NAME)));
			items.setReasonName(cursor.getString(cursor.getColumnIndex(dbHelper.REASON_CODE)));
			Itemname.add(items);
		}

		return Itemname;
	}

	@SuppressWarnings("static-access")
	public String getReaNameByCode(String code) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_REASON + " WHERE " + dbHelper.REASON_CODE + "='" + code + "'";

		Cursor cursor = dB.rawQuery(selectQuery, null);

		while (cursor.moveToNext()) {

			return cursor.getString(cursor.getColumnIndex(dbHelper.REASON_NAME));

		}

		return "";
	}

}
