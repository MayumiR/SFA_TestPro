package com.bit.sfa.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bit.sfa.helpers.SharedPref;
import com.bit.sfa.model.Reference;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class ReferenceController {
	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "ReferenceController";

	public ReferenceController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	public void isNewMonth(String cCode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {

			//String RepCode = new SalRepDS(context).getCurrentRepCode();

			Calendar c = Calendar.getInstance();
			Cursor cursor = dB.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_REFERENCE + " WHERE cSettingsCode='" + cCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'", null);

			if (cursor.getCount() == 0) {

				ContentValues values = new ContentValues();
			//	values.put(DatabaseHelper.REFERENCE_REPCODE, RepCode);
				values.put(DatabaseHelper.REFERENCE_RECORD_ID, "");
				values.put(DatabaseHelper.REFERENCE_SETTINGS_CODE, cCode);
				values.put(DatabaseHelper.REFERENCE_NNUM_VAL, "1");
				values.put(DatabaseHelper.REFERENCE_NYEAR_VAL, String.valueOf(c.get(Calendar.YEAR)));
				values.put(DatabaseHelper.REFERENCE_NMONTH_VAL, String.valueOf(c.get(Calendar.MONTH) + 1));

				dB.insert(DatabaseHelper.TABLE_REFERENCE, null, values);

			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public String getNextNumVal(String cSettingsCode,String repcode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String nextNumVal = "";

		Calendar c = Calendar.getInstance();

		try {
			String query = "SELECT " + DatabaseHelper.REFERENCE_NNUM_VAL +" from "+ DatabaseHelper.TABLE_REFERENCE + " WHERE " + DatabaseHelper.REFERENCE_REPCODE + " = '" + repcode + "'  AND SettingsCode = '" + cSettingsCode + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			Cursor cursor = dB.rawQuery(query, null);
			int count = cursor.getCount();
			if (count > 0) {
				while (cursor.moveToNext()) {
					nextNumVal = cursor.getString(cursor.getColumnIndex(dbHelper.REFERENCE_NNUM_VAL));
				}
			} else {
				nextNumVal = "1";
			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

		return nextNumVal;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public ArrayList<Reference> getCurrentPreFix(String cSettingsCode, String repPrefix) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Reference> list = new ArrayList<Reference>();

		try {
			String selectRep = "select CharVal from ReferenceSetting where SettingsCode='" + cSettingsCode + "'";

			Cursor cursor = null;
			cursor = dB.rawQuery(selectRep, null);

			while (cursor.moveToNext()) {

				Reference reference = new Reference();

				reference.setCharVal(cursor.getString(cursor.getColumnIndex(dbHelper.REFSETTING_CHAR_VAL)));
				reference.setRepPrefix(repPrefix);
				list.add(reference);

			}
		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}
		return list;
	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public int getCount(String cSettingsCode,String repcode) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		int count = 0;

		try {
			count = 0;

			String query = "SELECT " + dbHelper.REFERENCE_NNUM_VAL + " FROM " + dbHelper.TABLE_REFERENCE + " WHERE " + dbHelper.REFERENCE_REPCODE + " ='" + repcode + "' AND " + dbHelper.REFERENCE_SETTINGS_CODE + "='" + cSettingsCode + "'";
			Cursor cursor = dB.rawQuery(query, null);
			count = cursor.getCount();

		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}

		return count;

	}

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	public int InsetOrUpdate(String code, int nextNumVal) {
		int count = 0;
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {

			Calendar c = Calendar.getInstance();

		//	SalRepDS repDS = new SalRepDS(context);

			ContentValues values = new ContentValues();

			values.put(dbHelper.REFERENCE_NNUM_VAL, String.valueOf(nextNumVal));

			//String query = "SELECT " + dbHelper.REFERENCE_NNUM_VAL + " FROM " + dbHelper.TABLE_REFERENCE + " WHERE " + dbHelper.REFERENCE_REPCODE + "='" + repDS.getCurrentRepCode() + "' AND " + dbHelper.REFERENCE_SETTINGS_CODE + "='" + code + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			String query = "SELECT " + dbHelper.REFERENCE_NNUM_VAL + " FROM " + dbHelper.TABLE_REFERENCE + " WHERE "  + dbHelper.REFERENCE_SETTINGS_CODE + "='" + code + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'";
			Cursor cursor = dB.rawQuery(query, null);

			if (cursor.getCount() > 0) {
				count = (int) dB.update(dbHelper.TABLE_REFERENCE, values,  dbHelper.REFERENCE_SETTINGS_CODE + "='" + code + "' AND nYear='" + String.valueOf(c.get(Calendar.YEAR)) + "' AND nMonth='" + String.valueOf(c.get(Calendar.MONTH) + 1) + "'", null);
			} else {
				values.put(dbHelper.REFERENCE_REPCODE, SharedPref.getInstance(context).getLoginUser().getCode());
				values.put(dbHelper.REFERENCE_SETTINGS_CODE, code);
				values.put(dbHelper.REFERENCE_NYEAR_VAL, String.valueOf(c.get(Calendar.YEAR)));
				values.put(dbHelper.REFERENCE_NMONTH_VAL, String.valueOf(c.get(Calendar.MONTH) + 1));

				count = (int) dB.insert(dbHelper.TABLE_REFERENCE, null, values);
			}

		} catch (Exception e) {
			Log.v(TAG, e.toString());
		} finally {
			dB.close();
		}
		return count;

	}

}
