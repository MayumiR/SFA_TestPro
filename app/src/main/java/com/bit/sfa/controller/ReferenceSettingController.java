package com.bit.sfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.bit.sfa.model.RefSetting;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

public class ReferenceSettingController {
	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "SettingsCode";
	
	public ReferenceSettingController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	@SuppressWarnings("static-access")
	public int createOrUpdateReferenceSetting(ArrayList<RefSetting> list) {
		int count = 0;
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
	
		
		try {

			dB.beginTransaction();
			
			String sql = "Insert or Replace into " + dbHelper.TABLE_REFERENCE_SETTING + " (" + dbHelper.REFSETTING_SETTINGS_CODE + ", "
		 + dbHelper.REFSETTING_CHAR_VAL + ", "
					+ dbHelper.REFSETTING_REMARKS
					+ ") values(?,?,?)";
			
			SQLiteStatement insert = dB.compileStatement(sql);
			
			for (int i = 0; i < list.size(); i++) {

				RefSetting item = list.get(i);
				
				insert.bindString(1, item.getREF_SETTINGS_CODE());
				insert.bindString(2, item.getREFSETTING_CHAR_VAL());
				insert.bindString(3, item.getREFSETTING_REMARKS());

				insert.execute();
				
				
			}
			dB.setTransactionSuccessful();
			Log.w(TAG, "Done");
			
		} catch (Exception e) {
			Log.w("XML:", e);
		}

		finally {
			dB.endTransaction();

			dB.close();
		}
		return count;

	}

	public int deleteAll() {

		int count = 0;

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}
		Cursor cursor = null;
		try {

			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_REFERENCE_SETTING, null);
			count = cursor.getCount();
			if (count > 0) {
				int success = dB.delete(dbHelper.TABLE_REFERENCE_SETTING, null, null);
				Log.v("Success", success + "");
			}
		} catch (Exception e) {

			Log.v(" Exception", e.toString());

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			dB.close();
		}

		return count;

	}
}
