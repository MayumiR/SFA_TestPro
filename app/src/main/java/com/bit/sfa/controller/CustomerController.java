package com.bit.sfa.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.bit.sfa.model.Customer;
import com.bit.sfa.helpers.DatabaseHelper;

import java.util.ArrayList;

public class CustomerController {

	private SQLiteDatabase dB;
	private DatabaseHelper dbHelper;
	Context context;
	private String TAG = "CustomerController";

	public CustomerController(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		dB = dbHelper.getWritableDatabase();
	}

	@SuppressWarnings("static-access")
	public int createOrUpdateDebtor(ArrayList<Customer> customers) {
		int serverdbID = 0;
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		try {

			dB.beginTransaction();

			String sql = "Insert or Replace into " + dbHelper.TABLE_CUSTOMER + " (" + dbHelper.CUSTOMER_CODE + ", "
					+ dbHelper.CUSTOMER_NAME + ", " + dbHelper.CUSTOMER_ADDRESS + ", "
					+ dbHelper.CUSTOMER_STATUS + ", " + dbHelper.CUSTOMER_ROUTE + ", " + dbHelper.CUSTOMER_MOB + ", "
					+ dbHelper.CUSTOMER_EMAIL +") values(?,?,?,?,?,?,?)";

			SQLiteStatement insert = dB.compileStatement(sql);

			for (Customer customer : customers) {

				insert.bindString(1, customer.getCusCode());
				insert.bindString(2, customer.getCusName());
				insert.bindString(3, customer.getCusAddress());
				insert.bindString(4, customer.getCusStatus());
				insert.bindString(5, customer.getCusRoute());
				insert.bindString(6, customer.getCusMob());
				insert.bindString(7, customer.getCusEmail());

				insert.execute();

				serverdbID = 1;
			}

			dB.setTransactionSuccessful();
			Log.w(TAG, "Done");
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			dB.endTransaction();
			dB.close();
		}
		return serverdbID;

	}

//	public int createOrUpdateTempDebtor(ArrayList<Customer> debtors) {
//		int serverdbID = 0;
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		try {
//
//			dB.beginTransaction();
//
//			String sql = "Insert or Replace into " + dbHelper.TABLE_TEMP_FDEBTOR + " (" + dbHelper.FDEBTOR_CODE + ", "
//					+ dbHelper.FDEBTOR_NAME + ") values(?,?)";
//
//			SQLiteStatement insert = dB.compileStatement(sql);
//
//			for (Customer debtor : debtors) {
//
//				insert.bindString(1, debtor.getFDEBTOR_CODE());
//				insert.bindString(2, debtor.getFDEBTOR_NAME());
//
//				insert.execute();
//
//				serverdbID = 1;
//			}
//
//			dB.setTransactionSuccessful();
//			Log.w(TAG, "Done");
//		} catch (Exception e) {
//
//			Log.v(TAG + " Exception", e.toString());
//
//		} finally {
//			dB.endTransaction();
//			dB.close();
//		}
//		return serverdbID;
//
//	}
//
//	public int deleteAll() {
//
//		int count = 0;
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//		Cursor cursor = null;
//		try {
//
//			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_CUSTOMER, null);
//			count = cursor.getCount();
//			if (count > 0) {
//				int success = dB.delete(dbHelper.TABLE_CUSTOMER, null, null);
//				Log.v("Success", success + "");
//			}
//		} catch (Exception e) {
//
//			Log.v(TAG + " Exception", e.toString());
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//
//		return count;
//
//	}
//
//	public int deleteAllTemp() {
//
//		int count = 0;
//
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//		Cursor cursor = null;
//		try {
//
//			cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_TEMP_FDEBTOR, null);
//			count = cursor.getCount();
//			if (count > 0) {
//				int success = dB.delete(dbHelper.TABLE_TEMP_FDEBTOR, null, null);
//				Log.v("Success", success + "");
//			}
//		} catch (Exception e) {
//
//			Log.v(TAG + " Exception", e.toString());
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//
//		return count;
//
//	}
//
	public ArrayList<Customer> getAllCustomers() {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		ArrayList<Customer> list = new ArrayList<Customer>();
		Cursor cursor = null;
		try {
			String selectQuery = "select * from " + dbHelper.TABLE_CUSTOMER;

			cursor = dB.rawQuery(selectQuery, null);
			while (cursor.moveToNext()) {

				Customer customer = new Customer();

				customer.setCusCode(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_CODE)));
				customer.setCusName(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_NAME)));
				customer.setCusAddress(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_ADDRESS)));
				customer.setCusMob(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_EMAIL)));
				customer.setCusRoute(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_ROUTE)));
				customer.setCusEmail(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_EMAIL)));
				customer.setCusStatus(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_STATUS)));


				list.add(customer);

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
//
//
//	public ArrayList<Customer> getAllSelectedCustomers() {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//		Cursor cursor = null;
//		try {
//			String selectQuery = "select * from " + dbHelper.TABLE_TEMP_FDEBTOR + " GROUP BY " + dbHelper.FDEBTOR_CODE;
//
//			cursor = dB.rawQuery(selectQuery, null);
//			while (cursor.moveToNext()) {
//
//				Customer aDebtor = new Customer();
//
//				aDebtor.setFDEBTOR_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ID)));
//				aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//				aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//
//				list.add(aDebtor);
//
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//		return list;
//	}
//
//	public ArrayList<Customer> getCustomerByCodeAndName(String newText) {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//		Cursor cursor = null;
//		try {
//			String selectQuery = "select * from " + dbHelper.TABLE_CUSTOMER + " where " + dbHelper.FDEBTOR_NAME
//					+ " like '" + newText + "%'";
//			// Original Menaka 25-05-2016 String selectQuery = "select * from "
//			// +
//			// dbHelper.TABLE_CUSTOMER + " where " + dbHelper.FDEBTOR_CODE + " ||
//			// " +
//			// dbHelper.FDEBTOR_NAME + " like '%" + newText + "%'";
//			cursor = dB.rawQuery(selectQuery, null);
//			while (cursor.moveToNext()) {
//
//				Customer aDebtor = new Customer();
//
//				aDebtor.setFDEBTOR_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ID)));
//				aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//				aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//				aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
//				aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
//				aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD3)));
//				aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TELE)));
//				aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
//				aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
//				aDebtor.setFDEBTOR_CREATEDATE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CREATEDATE)));
//				aDebtor.setFDEBTOR_TOWN_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TOWN_CODE)));
//				aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_AREA_CODE)));
//				// aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_DBGR_CODE)));
//				aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
//				// aDebtor.setFDEBTOR_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_USER)));
//				// aDebtor.setFDEBTOR_ADD_DATE_DEB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_DATE_DEB)));
//				// aDebtor.setFDEBTOR_ADD_MACH(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_MACH)));
//				aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
//				aDebtor.setFDEBTOR_CHK_CRD_PRD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_PRD)));
//				aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
//				aDebtor.setFDEBTOR_CHK_CRD_LIMIT(
//						cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
//				aDebtor.setFDEBTOR_REP_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_REP_CODE)));
//				// aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_RANK_CODE)));
//				aDebtor.setFDEBTOR_SUMMARY(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_SUMMARY)));
//
//				list.add(aDebtor);
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//		return list;
//	}
//
//
//
//	public ArrayList<Customer> getRouteCustomers(String RouteCode) {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//
//		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FROUTEDET + " RD, " + dbHelper.TABLE_FMDEBTOR
//				+ " D WHERE RD." + dbHelper.FROUTEDET_DEB_CODE + "=D." + dbHelper.FDEBTOR_CODEM + " AND RD."
//				+ dbHelper.FROUTEDET_ROUTE_CODE + "='" + RouteCode + "'";
//
//		Cursor cursor = dB.rawQuery(selectQuery, null);
//		while (cursor.moveToNext()) {
//
//			Customer aDebtor = new Customer();
//
//			aDebtor.setFDEBTOR_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ID)));
//			aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//			aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//			aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
//			aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
//			aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD3)));
//			aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TELE)));
//			aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
//			aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
//			aDebtor.setFDEBTOR_CREATEDATE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CREATEDATE)));
//			aDebtor.setFDEBTOR_TOWN_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TOWN_CODE)));
//			aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_AREA_CODE)));
//			aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_DBGR_CODE)));
//			aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
//			aDebtor.setFDEBTOR_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_USER)));
//			aDebtor.setFDEBTOR_ADD_DATE_DEB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_DATE_DEB)));
//			aDebtor.setFDEBTOR_ADD_MACH(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_MACH)));
//			aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
//			aDebtor.setFDEBTOR_CHK_CRD_PRD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_PRD)));
//			aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
//			aDebtor.setFDEBTOR_CHK_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
//			aDebtor.setFDEBTOR_REP_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_REP_CODE)));
//			aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_RANK_CODE)));
//			aDebtor.setFDEBTOR_SUMMARY(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_SUMMARY)));
//
//			list.add(aDebtor);
//
//		}
//
//		return list;
//	}
//
//	public ArrayList<Customer> getRouteCustomersByCodeAndName(String RouteCode, String newText) {
//		if (dB == null) {
//			open();
//		} else if (!dB.isOpen()) {
//			open();
//		}
//
//		ArrayList<Customer> list = new ArrayList<Customer>();
//		Cursor cursor = null;
//		try {
//			String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FROUTEDET + " RD, " + dbHelper.TABLE_CUSTOMER
//					+ " D WHERE RD." + dbHelper.FROUTEDET_DEB_CODE + "=D." + dbHelper.FDEBTOR_CODE + " AND RD."
//					+ dbHelper.FROUTEDET_ROUTE_CODE + "='" + RouteCode + "' AND D." + dbHelper.FDEBTOR_CODE + " || D."
//					+ dbHelper.FDEBTOR_NAME + " like '%" + newText + "%'";
//			;
//
//			cursor = dB.rawQuery(selectQuery, null);
//
//			while (cursor.moveToNext()) {
//
//				Customer aDebtor = new Customer();
//
//				aDebtor.setFDEBTOR_ID(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ID)));
//				aDebtor.setFDEBTOR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CODE)));
//				aDebtor.setFDEBTOR_NAME(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_NAME)));
//				aDebtor.setFDEBTOR_ADD1(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD1)));
//				aDebtor.setFDEBTOR_ADD2(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD2)));
//				aDebtor.setFDEBTOR_ADD3(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD3)));
//				aDebtor.setFDEBTOR_TELE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TELE)));
//				aDebtor.setFDEBTOR_MOB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_MOB)));
//				aDebtor.setFDEBTOR_EMAIL(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_EMAIL)));
//				aDebtor.setFDEBTOR_CREATEDATE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CREATEDATE)));
//				aDebtor.setFDEBTOR_TOWN_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_TOWN_CODE)));
//				aDebtor.setFDEBTOR_AREA_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_AREA_CODE)));
//				aDebtor.setFDEBTOR_DBGR_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_DBGR_CODE)));
//				aDebtor.setFDEBTOR_STATUS(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_STATUS)));
//				aDebtor.setFDEBTOR_ADD_USER(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_USER)));
//				aDebtor.setFDEBTOR_ADD_DATE_DEB(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_DATE_DEB)));
//				aDebtor.setFDEBTOR_ADD_MACH(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_ADD_MACH)));
//				aDebtor.setFDEBTOR_CRD_PERIOD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_PERIOD)));
//				aDebtor.setFDEBTOR_CHK_CRD_PRD(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_PRD)));
//				aDebtor.setFDEBTOR_CRD_LIMIT(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CRD_LIMIT)));
//				aDebtor.setFDEBTOR_CHK_CRD_LIMIT(
//						cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_CHK_CRD_LIMIT)));
//				aDebtor.setFDEBTOR_REP_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_REP_CODE)));
//				aDebtor.setFDEBTOR_RANK_CODE(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_RANK_CODE)));
//				aDebtor.setFDEBTOR_SUMMARY(cursor.getString(cursor.getColumnIndex(dbHelper.FDEBTOR_SUMMARY)));
//
//				list.add(aDebtor);
//
//			}
//		} catch (Exception e) {
//
//			Log.v(TAG + " Exception", e.toString());
//
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//			dB.close();
//		}
//
//		return list;
//	}
//
	public Customer getSelectedCustomerByCode(String code) {
		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		// ArrayList<Debtor> list = new ArrayList<Debtor>();
		Cursor cursor = null;
		try {
			String selectQuery = "select * from " + dbHelper.TABLE_CUSTOMER + " Where " + dbHelper.CUSTOMER_CODE + "='"
					+ code + "'";

			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				Customer customer = new Customer();

				customer.setCusName(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_NAME)));
				customer.setCusCode(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_CODE)));
				customer.setCusRoute(cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_ROUTE)));


				return customer;

			}
		} catch (Exception e) {

			Log.v(TAG + " Exception", e.toString());

		} finally {
			if (cursor != null) {
				cursor.close();
			}
			dB.close();
		}

		return null;

	}


	public String getCNameByCode(String refno) {

		if (dB == null) {
			open();
		} else if (!dB.isOpen()) {
			open();
		}

		String selectQuery = "SELECT * FROM " + dbHelper.TABLE_CUSTOMER + " WHERE " + dbHelper.CUSTOMER_CODE  +
				" in (select CustomerCode from OrderHeader where RefNo = '"
				+ refno + "') ";

		Cursor cursor = null;
		try {
			cursor = dB.rawQuery(selectQuery, null);

			while (cursor.moveToNext()) {

				return cursor.getString(cursor.getColumnIndex(dbHelper.CUSTOMER_NAME));

			}
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
				dB.close();
			}
		}
		return "";

	}



}
