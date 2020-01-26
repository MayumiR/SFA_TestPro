package com.bit.sfa.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // database information
    public static final String DATABASE_NAME = "sfa_database.db";
    public static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**
     * ############################ Customer table Details
     * ################################
     */

    public static final String TABLE_CUSTOMER = "Customer";
    // table attributes
    public static final String CUSTOMER_CODE = "CustomerCode";
    public static final String CUSTOMER_NAME = "CustomerName";
    public static final String CUSTOMER_ADDRESS = "CustomerAddress";
    public static final String CUSTOMER_STATUS = "CustomerStatus";//0-active , 1- new
    public static final String CUSTOMER_ROUTE = "CustomerRoute";
    public static final String CUSTOMER_MOB = "CustomerMob";
    public static final String CUSTOMER_EMAIL = "CustomerEmail";

    // table

    // create String
    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMER + " (" + CUSTOMER_CODE + " TEXT, "
            + CUSTOMER_NAME +
            " TEXT, " + CUSTOMER_ADDRESS + " TEXT, " + CUSTOMER_STATUS + " TEXT, " +
            CUSTOMER_ROUTE + " TEXT, "+ CUSTOMER_MOB + " TEXT, " + CUSTOMER_EMAIL + " TEXT); ";


    /**
     * ############################ ReferenceSetting table Details
     * ################################
     */

    // table
    public static final String TABLE_REFERENCE_SETTING = "ReferenceSetting";
    // table attributes
    public static final String REFSETTING_ID = "Setting_id";// ok
    public static final String REFSETTING_SETTINGS_CODE = "SettingsCode";// ok
    public static final String REFSETTING_CHAR_VAL = "CharVal";// ok
    public static final String REFSETTING_REMARKS = "Remarks";// ok

    // create String
    private static final String CREATE_REFSETTING_TABLE = "CREATE  TABLE IF NOT EXISTS " +
            TABLE_REFERENCE_SETTING + " (" + REFSETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            REFSETTING_SETTINGS_CODE + " TEXT, " +
            REFSETTING_CHAR_VAL +  " TEXT, " +
            REFSETTING_REMARKS + " TEXT); ";

    // table
    public static final String TABLE_REFERENCE = "Reference";
    // table attributes
    public static final String REFERENCE_REPCODE = "RepCode";
    public static final String REFERENCE_RECORD_ID = "RecordId";
    public static final String REFERENCE_SETTINGS_CODE = "SettingsCode";
    public static final String REFERENCE_NNUM_VAL = "nNumVal";
    public static final String REFERENCE_NYEAR_VAL = "nYear";
    public static final String REFERENCE_NMONTH_VAL = "nMonth";

    // create String
    private static final String CREATE_REFERENCE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_REFERENCE + " (" + REFERENCE_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REFERENCE_REPCODE +  " TEXT, " + REFERENCE_SETTINGS_CODE + " TEXT, " + REFERENCE_NNUM_VAL + " TEXT, " + REFERENCE_NYEAR_VAL + " TEXT, " + REFERENCE_NMONTH_VAL + " TEXT); ";

    // table
    public static final String TABLE_ROUTE = "Route";
    public static final String ROUTE_ID = "RouteID";
    // table attributes

    public static final String ROUTE_NAME = "RouteName";
    public static final String ROUTE_CODE = "RouteCode";

    // create String
    private static final String CREATE_ROUTE_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_ROUTE + " (" + ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ROUTE_CODE + " TEXT, " + ROUTE_NAME + " TEXT) ";

    public static final String TABLE_NONPRDHED = "DaynPrdHed";
    // table attributes
    public static final String NONPRDHED_ID = "NonprdHed_id";
    public static final String NONPRDHED_REFNO = "RefNo";
    public static final String NONPRDHED_REPCODE = "RepCode";
    public static final String NONPRDHED_TXNDATE = "TxnDate";
    public static final String NONPRDHED_REMARK = "Remarks";
    public static final String NONPRDHED_ADDDATE = "AddDate";
    public static final String NONPRDHED_IS_SYNCED = "ISsync";
    public static final String NONPRDHED_DEBCODE = "DebCode";
    public static final String NONPRDHED_LONGITUDE = "Longitude";
    public static final String NONPRDHED_LATITUDE = "Latitude";
    public static final String NONPRDHED_IS_ACTIVE = "ISActive";
    // create String
    private static final String CREATE_TABLE_NONPRDHED = "CREATE  TABLE IF NOT EXISTS " + TABLE_NONPRDHED +
            " (" + NONPRDHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NONPRDHED_REFNO
            + " TEXT, " + NONPRDHED_TXNDATE + " TEXT, "  + NONPRDHED_REPCODE + " TEXT, " + NONPRDHED_REMARK +
            " TEXT, "  + NONPRDHED_ADDDATE + " TEXT,"  + NONPRDHED_IS_SYNCED + " TEXT," + NONPRDHED_DEBCODE +
            " TEXT," + NONPRDHED_LATITUDE + " TEXT," + NONPRDHED_LONGITUDE + " TEXT,"  + NONPRDHED_IS_ACTIVE + " TEXT); ";
    /**
     * ############################ FDaynonprdDet
     * ################################
     */
    public static final String TABLE_NONPRDDET = "DaynPrdDet";
    // table attributes

    public static final String NONPRDDET_ID = "NonprdDet_id";
    public static final String NONPRDDET_REFNO = "RefNo";
    public static final String NONPRDDET_REASON = "Reason";
    public static final String NONPRDDET_REASON_CODE = "ReasonCode";



    private static final String CREATE_TABLE_NONPRDDET = "CREATE  TABLE IF NOT EXISTS "
            + TABLE_NONPRDDET + " (" + NONPRDDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NONPRDDET_REFNO + " TEXT, "
            + NONPRDDET_REASON_CODE + " TEXT, "
            + NONPRDDET_REASON + " TEXT); ";


	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-ATTENDANCE-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public static final String TABLE_ATTENDANCE = "Attendance";
    public static final String ATTENDANCE_ID = "Id";
    public static final String ATTENDANCE_DATE = "tDate";
    public static final String ATTENDANCE_S_TIME = "StartTime";
    public static final String ATTENDANCE_F_TIME = "EndTime";
    public static final String ATTENDANCE_VEHICLE = "Vehicle";
    public static final String ATTENDANCE_S_KM = "StartKm";
    public static final String ATTENDANCE_F_KM = "EndKm";
    public static final String ATTENDANCE_ROUTE = "Route";

    public static final String ATTENDANCE_DRIVER = "Driver";
    public static final String ATTENDANCE_ASSIST = "Assist";

    public static final String ATTENDANCE_DISTANCE = "Distance";
    public static final String ATTENDANCE_IS_SYNCED = "IsSynced";

    public static final String ATTENDANCE_REPCODE = "RepCode";
    public static final String ATTENDANCE_MAC = "MacAdd";

    public static final String CREATE_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_ATTENDANCE + " (" + ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ATTENDANCE_DATE + " TEXT, " + ATTENDANCE_S_TIME + " TEXT, " + ATTENDANCE_F_TIME +
            " TEXT, " + ATTENDANCE_VEHICLE + " TEXT, " + ATTENDANCE_S_KM + " TEXT, " +
            ATTENDANCE_F_KM + " TEXT, " + ATTENDANCE_DISTANCE + " TEXT, " +
            ATTENDANCE_IS_SYNCED + " TEXT, " + ATTENDANCE_REPCODE + " TEXT, " +
            ATTENDANCE_DRIVER + " TEXT, " + ATTENDANCE_ASSIST + " TEXT, " +
            ATTENDANCE_MAC + " TEXT, " + ATTENDANCE_ROUTE + " TEXT ); ";


    /**
     * ############################ Reason table Details
     * ################################
     */

    public static final String TABLE_REASON = "Reason";
    // table attributes
    public static final String REASON_ID = "reason_id";
    public static final String REASON_CODE = "ReaCode";
    public static final String REASON_NAME = "ReaName";
    public static final String REASON_TYPE = "Type";

    // create String
    private static final String CREATE_REASON_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_REASON + " (" + REASON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REASON_CODE + " TEXT, " + REASON_NAME + " TEXT, "  + REASON_TYPE + " TEXT); ";


    //table items
    public static final String TABLE_ITEMS = "Items";
    public static final String ITEM_ID = "ItemId";
    public static final String ITEM_CODE = "ItemCode";
    public static final String ITEM_NAME = "ItemName";
    public static final String UOM = "UOM";
    public static final String STATUS = "Status";


    private static final String CREATE_TABLE_ITEMS = "CREATE  TABLE IF NOT EXISTS " + TABLE_ITEMS + " (" +
            ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ITEM_CODE + " TEXT, " + ITEM_NAME + " TEXT, " + UOM +
            " TEXT, " + STATUS +
            " TEXT) ";
    //--

    // table ORDER
    public static final String TABLE_ORDER = "OrderHeader";
    // table attributes
    public static final String ORDER_ID = "OrderId";
    public static final String ORDER_REFNO = "RefNo";
    public static final String ORDER_CUSCODE = "CustomerCode";
    public static final String ORDER_START_TIME = "StartTime";
    public static final String ORDER_END_TIME = "EndTime";
    public static final String ORDER_LONGITUDE = "Longitude";
    public static final String ORDER_LATITUDE = "Latitude";
    public static final String ORDER_MANU_REF = "ManuRef";
    public static final String ORDER_REMARKS = "Remarks";
    public static final String ORDER_REPCODE = "RepCode";
    public static final String ORDER_TOTAL_AMT = "TotalAmt";
    public static final String ORDER_ADDDATE = "AddDate";
    public static final String ORDER_TXN_DATE = "TxnDate";
    public static final String ORDER_IS_SYNCED = "isSynced";
    public static final String ORDER_IS_ACTIVE = "isActive";
    public static final String ORDER_ROUTE_CODE = "RouteCode";
    public static final String ORDER_DELIV_DATE = "DeliverDate";

    // create String

    private static final String CREATE_TABLE_ORDER = " CREATE  TABLE IF NOT EXISTS " + TABLE_ORDER + " (" +
            ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ORDER_ADDDATE + " TEXT, " +
            ORDER_CUSCODE + " TEXT, " +
            ORDER_MANU_REF + " TEXT, " +
            ORDER_REFNO + " TEXT, " +
            ORDER_REMARKS + " TEXT, " +
            ORDER_REPCODE + " TEXT, " +
            ORDER_TOTAL_AMT + " TEXT, " +
            ORDER_TXN_DATE + " TEXT, " +
            ORDER_LONGITUDE + " TEXT, " +
            ORDER_LATITUDE + " TEXT, " +
            ORDER_START_TIME + " TEXT, " +
            ORDER_IS_SYNCED + " TEXT, " +
            ORDER_IS_ACTIVE + " TEXT, " +
            ORDER_ROUTE_CODE + " TEXT, " +
            ORDER_DELIV_DATE + " TEXT, " +
            ORDER_END_TIME + " TEXT) ";
    //--
//    private static final String CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER +
//            " (" + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            ORDER_ADDDATE + " TEXT, " +
//            ORDER_CUSCODE + " TEXT, " +
//            ORDER_MANU_REF + " TEXT, " +
//            ORDER_REFNO + " TEXT, " +
//            ORDER_REMARKS + " TEXT, " +
//            ORDER_REPCODE + " TEXT, " +
//            ORDER_TOTAL_AMT + " TEXT, " +
//            ORDER_TXN_DATE + " TEXT, " +
//            ORDER_LONGITUDE + " TEXT, " +
//            ORDER_LATITUDE + " TEXT, " +
//            ORDER_START_TIME + " TEXT, " +
//            ORDER_IS_SYNCED + " TEXT, " +
//            ORDER_IS_ACTIVE + " TEXT, " +
//            ORDER_ROUTE_CODE + " TEXT, " +
//            ORDER_END_TIME + " TEXT) ";

    //------------------ temp table crate for PreSales detail data saved------------------------------------
    public static final String TABLE_PRODUCT = "Products";
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_ITEMCODE = "itemcode";
    public static final String PRODUCT_ITEMNAME = "itemname";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_REFNO = "refno";
    public static final String PRODUCT_PRILCODE = "prilcode";
    public static final String PRODUCT_QTY = "qty";   //------------------ temp table crate for PreSales detail data saved------------------------------------


    private static final String CREATE_PRODUCT_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_PRODUCT + " ("
            + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PRODUCT_ITEMCODE + " TEXT, "
            + PRODUCT_ITEMNAME + " TEXT, "
            + PRODUCT_PRICE + " TEXT, "
            + PRODUCT_REFNO + " TEXT, "
            + PRODUCT_PRILCODE + " TEXT, "
            + PRODUCT_QTY + " TEXT); ";
    private static final String INDEX_PRODUCTS = "CREATE UNIQUE INDEX IF NOT EXISTS Products_pre ON " +
            TABLE_PRODUCT + " (itemcode,itemname);";

    /**
     * ############################ Orderdetail table Details
     * ################################
     */

    // table
    public static final String TABLE_ORDER_DETAIL = "OrderDetail";
    // table attributes
    public static final String ORDDET_ID = "OrderDetId";
    public static final String ORDDET_AMT = "Amt";
    public static final String ORDDET_ITEM_CODE = "Itemcode";
    public static final String ORDDET_QTY = "Qty";
    public static final String ORDDET_REFNO = "RefNo";
    public static final String ORDDET_PRICE = "Price";
    public static final String ORDDET_IS_ACTIVE = "isActive";
    public static final String ORDDET_ITEMNAME = "ItemName";
    //----------------------------------------------------------------------------------------------------------------------------

    // create String
    private static final String CREATE_ORDDET_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_DETAIL +
            " (" + ORDDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ORDDET_AMT + " TEXT, " +
            ORDDET_ITEM_CODE + " TEXT, " +
            ORDDET_QTY + " TEXT, " +
            ORDDET_REFNO + " TEXT, " +
            ORDDET_PRICE + " TEXT, " +
            ORDDET_ITEMNAME + " TEXT, " +
            ORDDET_IS_ACTIVE  + " TEXT); ";

    private static final String ORDDET_IDX = "CREATE UNIQUE INDEX IF NOT EXISTS idxordet_duplicate ON " +
            TABLE_ORDER_DETAIL + " (" + ORDDET_REFNO + "," + ORDDET_ITEM_CODE +  ")";



    // table
    public static final String TABLE_ITEMPRI = "ItemPri";
    // table attributes
    public static final String ITEMPRI_ID = "ItemPri_id";
    public static final String ITEMPRI_ITEM_CODE = "ItemCode";
    public static final String ITEMPRI_PRICE = "Price";

    private static final String CREATE_TABLE_ITEMPRI = "CREATE  TABLE IF NOT EXISTS " + TABLE_ITEMPRI + " (" +
            ITEMPRI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ITEMPRI_ITEM_CODE + " TEXT, " + ITEMPRI_PRICE + " TEXT) ";

    /**
     * ############################ FDayExpHed ################################
     */

    public static final String TABLE_DAYEXPHED = "DayExpHed";
    // table attributes
    public static final String FDAYEXPHED_ID = "FDayExpHed_id";
    public static final String FDAYEXPHED_REFNO = "RefNo";
    public static final String FDAYEXPHED_TXNDATE = "TxnDate";
    public static final String FDAYEXPHED_REPNAME = "RepName";
    public static final String FDAYEXPHED_DEALCODE = "DealCode";
    public static final String FDAYEXPHED_COSTCODE = "CostCode";
    public static final String FDAYEXPHED_REPCODE = "RepCode";
    public static final String FDAYEXPHED_REMARKS = "Remarks";
    public static final String FDAYEXPHED_AREACODE = "AreaCode";
    public static final String FDAYEXPHED_ADDUSER = "AddUser";
    public static final String FDAYEXPHED_ADDDATE = "AddDate";
    public static final String FDAYEXPHED_ADDMATCH = "AddMach";
    public static final String FDAYEXPHED_LONGITUDE = "Longitude";
    public static final String FDAYEXPHED_LATITUDE = "Latitude";
    public static final String FDAYEXPHED_ISSYNC = "issync";
    public static final String FDAYEXPHED_ACTIVESTATE = "ActiveState";
    public static final String FDAYEXPHED_TOTAMT = "TotAmt";
    public static final String FDAYEXPHED_ADDRESS = "Address";

    // create String
    private static final String CREATE_DAYEXPHED_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_DAYEXPHED + " (" + FDAYEXPHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FDAYEXPHED_REFNO + " TEXT, " + FDAYEXPHED_TXNDATE + " TEXT, " + FDAYEXPHED_REPNAME + " TEXT, " + FDAYEXPHED_DEALCODE + " TEXT, " + FDAYEXPHED_COSTCODE + " TEXT, " + FDAYEXPHED_REPCODE + " TEXT, " + FDAYEXPHED_REMARKS + " TEXT, " + FDAYEXPHED_AREACODE + " TEXT, " + FDAYEXPHED_ADDUSER + " TEXT, " + FDAYEXPHED_ADDDATE + " TEXT, " + FDAYEXPHED_ADDMATCH + " TEXT, " + FDAYEXPHED_LONGITUDE + " TEXT," + FDAYEXPHED_LATITUDE + " TEXT," + FDAYEXPHED_ISSYNC + " TEXT," + FDAYEXPHED_ACTIVESTATE + " TEXT," + FDAYEXPHED_TOTAMT + " TEXT," + FDAYEXPHED_ADDRESS + " TEXT); ";

    /**
     * ############################ FDayExpDet ################################
     */
    public static final String TABLE_DAYEXPDET = "DayExpDet";
    // table attributes
    public static final String DAYEXPDET_ID = "DayExpDet_id";
    public static final String DAYEXPDET_REFNO = "RefNo";
    public static final String DAYEXPDET_EXPCODE = "ExpCode";
    public static final String DAYEXPDET_AMT = "Amt";

    // create String
    private static final String CREATE_DAYEXPDET_TABLE = "CREATE  TABLE IF NOT EXISTS " + TABLE_DAYEXPDET + " (" + DAYEXPDET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DAYEXPDET_REFNO +  " TEXT, " + DAYEXPDET_EXPCODE + " TEXT, " + DAYEXPDET_AMT + " TEXT); ";




    private static final String INDEX_CUSTOMER = "CREATE UNIQUE INDEX IF NOT EXISTS ui_debtor ON " + TABLE_CUSTOMER +
            " (CustomerCode);";

    //NEW CUSTOMER REGISTRATION
    public static final String TABLE_NEW_CUSTOMER = "NewCustomer";
    public static final String TABLE_REC_ID = "recID";
    public static final String CUSTOMER_ID = "customerID";
    public static final String NAME = "Name";
    public static final String ADDRESS = "Address";
    public static final String MOBILE = "Mobile";
    public static final String E_MAIL = "Email";
    public static final String C_LONGITUDE = "lng";
    public static final String C_LATITUDE = "lat";
    public static final String C_IS_SYNCED = "isSynced";

    private static final String CREATE_NEW_CUSTOMER = "CREATE  TABLE IF NOT EXISTS " + TABLE_NEW_CUSTOMER + " ("
            + TABLE_REC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CUSTOMER_ID + " TEXT, "
            + NAME + " TEXT, "
            + ROUTE_ID + " TEXT, "
            + ADDRESS + " TEXT, "
            + MOBILE + " TEXT, "
            + E_MAIL + " TEXT, "
            + C_LONGITUDE + " TEXT, "
            + C_LATITUDE + " TEXT, "
            + C_IS_SYNCED + " TEXT); ";

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(CREATE_CUSTOMER_TABLE);
        arg0.execSQL(CREATE_REFSETTING_TABLE);
        arg0.execSQL(CREATE_ROUTE_TABLE);
        arg0.execSQL(CREATE_REASON_TABLE);
        arg0.execSQL(CREATE_REFERENCE_TABLE);
        arg0.execSQL(CREATE_ATTENDANCE_TABLE);
        arg0.execSQL(CREATE_DAYEXPHED_TABLE);
        arg0.execSQL(CREATE_DAYEXPDET_TABLE);
        arg0.execSQL(CREATE_TABLE_NONPRDDET);
        arg0.execSQL(CREATE_TABLE_NONPRDHED);
        arg0.execSQL(CREATE_TABLE_ITEMS);
        arg0.execSQL(CREATE_TABLE_ITEMPRI);

        arg0.execSQL(CREATE_NEW_CUSTOMER);

        arg0.execSQL(CREATE_TABLE_ORDER);
        arg0.execSQL(CREATE_ORDDET_TABLE);
        arg0.execSQL(CREATE_PRODUCT_TABLE);
        arg0.execSQL(ORDDET_IDX);
        arg0.execSQL(INDEX_CUSTOMER);
        arg0.execSQL(INDEX_PRODUCTS);
    }

    // --------------------------------------------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


        try {

            arg0.execSQL(CREATE_CUSTOMER_TABLE);
            arg0.execSQL(CREATE_REFSETTING_TABLE);
            arg0.execSQL(CREATE_ROUTE_TABLE);
            arg0.execSQL(CREATE_REASON_TABLE);
            arg0.execSQL(CREATE_REFERENCE_TABLE);
            arg0.execSQL(CREATE_ATTENDANCE_TABLE);
            arg0.execSQL(CREATE_DAYEXPHED_TABLE);
            arg0.execSQL(CREATE_DAYEXPDET_TABLE);
            arg0.execSQL(CREATE_TABLE_NONPRDDET);
            arg0.execSQL(CREATE_TABLE_NONPRDHED);
            arg0.execSQL(CREATE_TABLE_ITEMS);
            arg0.execSQL(CREATE_TABLE_ITEMPRI);
            arg0.execSQL(INDEX_CUSTOMER);
            arg0.execSQL(CREATE_NEW_CUSTOMER);

            arg0.execSQL(ORDDET_IDX);
            arg0.execSQL(CREATE_TABLE_ORDER);
            arg0.execSQL(CREATE_ORDDET_TABLE);
            arg0.execSQL(CREATE_PRODUCT_TABLE);
            arg0.execSQL(INDEX_PRODUCTS);

        } catch (SQLiteException e) {
            Log.v("SQLite" , e.toString());
        }
//        try {
//            arg0.execSQL("ALTER TABLE " + TABLE_customer + " ADD COLUMN " + ROUTE_CODE + " TEXT");
//
//        } catch (SQLiteException e) {
//            Log.v("SQLite - " , e.toString());
//        }


        this.onCreate(arg0);

    }

}