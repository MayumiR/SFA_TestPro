package com.bit.sfa.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class Order {
    private String ORDHED_ID;
    private String ORDHED_REFNO;
    private String ORDHED_ADD_DATE;
    private String ORDHED_CUS_CODE;
    private String ORDHED_START_TIME;
    private String ORDHED_END_TIME;
    private String ORDHED_LONGITUDE;
    private String ORDHED_LATITUDE;
    private String ORDHED_MANU_REF;
    private String ORDHED_REMARKS;
    private String ORDHED_REPCODE;
    private String ORDHED_TOTAL_AMT;
    private String ORDHED_TXN_DATE;
    private String ORDHED_IS_SYNCED;
    private String ORDHED_IS_ACTIVE;
    private String ORDHED_DELV_DATE;
    private String ORDHED_ROUTE_CODE;
    private String nextNumVal;

    public String getNextNumVal() {
        return nextNumVal;
    }

    public void setNextNumVal(String nextNumVal) {
        this.nextNumVal = nextNumVal;
    }

    private ArrayList<OrderDetail> soDetArrayList;

    public ArrayList<OrderDetail> getSoDetArrayList() {
        return soDetArrayList;
    }

    public void setSoDetArrayList(ArrayList<OrderDetail> soDetArrayList) {
        this.soDetArrayList = soDetArrayList;
    }

    public String getORDHED_ID() {
        return ORDHED_ID;
    }

    public void setORDHED_ID(String ORDHED_ID) {
        this.ORDHED_ID = ORDHED_ID;
    }

    public String getORDHED_REFNO() {
        return ORDHED_REFNO;
    }

    public void setORDHED_REFNO(String ORDHED_REFNO) {
        this.ORDHED_REFNO = ORDHED_REFNO;
    }

    public String getORDHED_ADD_DATE() {
        return ORDHED_ADD_DATE;
    }

    public void setORDHED_ADD_DATE(String ORDHED_ADD_DATE) {
        this.ORDHED_ADD_DATE = ORDHED_ADD_DATE;
    }

    public String getORDHED_CUS_CODE() {
        return ORDHED_CUS_CODE;
    }

    public void setORDHED_CUS_CODE(String ORDHED_CUS_CODE) {
        this.ORDHED_CUS_CODE = ORDHED_CUS_CODE;
    }

    public String getORDHED_START_TIME() {
        return ORDHED_START_TIME;
    }

    public void setORDHED_START_TIME(String ORDHED_START_TIME) {
        this.ORDHED_START_TIME = ORDHED_START_TIME;
    }

    public String getORDHED_END_TIME() {
        return ORDHED_END_TIME;
    }

    public void setORDHED_END_TIME(String ORDHED_END_TIME) {
        this.ORDHED_END_TIME = ORDHED_END_TIME;
    }

    public String getORDHED_LONGITUDE() {
        return ORDHED_LONGITUDE;
    }

    public void setORDHED_LONGITUDE(String ORDHED_LONGITUDE) {
        this.ORDHED_LONGITUDE = ORDHED_LONGITUDE;
    }

    public String getORDHED_LATITUDE() {
        return ORDHED_LATITUDE;
    }

    public void setORDHED_LATITUDE(String ORDHED_LATITUDE) {
        this.ORDHED_LATITUDE = ORDHED_LATITUDE;
    }

    public String getORDHED_MANU_REF() {
        return ORDHED_MANU_REF;
    }

    public void setORDHED_MANU_REF(String ORDHED_MANU_REF) {
        this.ORDHED_MANU_REF = ORDHED_MANU_REF;
    }

    public String getORDHED_REMARKS() {
        return ORDHED_REMARKS;
    }

    public void setORDHED_REMARKS(String ORDHED_REMARKS) {
        this.ORDHED_REMARKS = ORDHED_REMARKS;
    }

    public String getORDHED_REPCODE() {
        return ORDHED_REPCODE;
    }

    public void setORDHED_REPCODE(String ORDHED_REPCODE) {
        this.ORDHED_REPCODE = ORDHED_REPCODE;
    }

    public String getORDHED_TOTAL_AMT() {
        return ORDHED_TOTAL_AMT;
    }

    public void setORDHED_TOTAL_AMT(String ORDHED_TOTAL_AMT) {
        this.ORDHED_TOTAL_AMT = ORDHED_TOTAL_AMT;
    }

    public String getORDHED_TXN_DATE() {
        return ORDHED_TXN_DATE;
    }

    public void setORDHED_TXN_DATE(String ORDHED_TXN_DATE) {
        this.ORDHED_TXN_DATE = ORDHED_TXN_DATE;
    }

    public String getORDHED_IS_SYNCED() {
        return ORDHED_IS_SYNCED;
    }

    public void setORDHED_IS_SYNCED(String ORDHED_IS_SYNCED) {
        this.ORDHED_IS_SYNCED = ORDHED_IS_SYNCED;
    }

    public String getORDHED_IS_ACTIVE() {
        return ORDHED_IS_ACTIVE;
    }

    public void setORDHED_IS_ACTIVE(String ORDHED_IS_ACTIVE) {
        this.ORDHED_IS_ACTIVE = ORDHED_IS_ACTIVE;
    }

    public String getORDHED_DELV_DATE() {
        return ORDHED_DELV_DATE;
    }

    public void setORDHED_DELV_DATE(String ORDHED_DELV_DATE) {
        this.ORDHED_DELV_DATE = ORDHED_DELV_DATE;
    }

    public String getORDHED_ROUTE_CODE() {
        return ORDHED_ROUTE_CODE;
    }

    public void setORDHED_ROUTE_CODE(String ORDHED_ROUTE_CODE) {
        this.ORDHED_ROUTE_CODE = ORDHED_ROUTE_CODE;
    }

    public JSONObject getOrderAsJSON() throws JSONException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        HashMap<String, Object> finalJSONParams = new HashMap<>();
        HashMap<String, Object> invoiceParams = new HashMap<>();
        invoiceParams.put("refno", ORDHED_REFNO);
        invoiceParams.put("customer", ORDHED_CUS_CODE);
        invoiceParams.put("route", ORDHED_ROUTE_CODE);
        invoiceParams.put("latitude", ORDHED_LATITUDE);
        invoiceParams.put("longitude", ORDHED_LONGITUDE);
        invoiceParams.put("total_amount", ORDHED_TOTAL_AMT);
        invoiceParams.put("addDate", ORDHED_ADD_DATE);
        invoiceParams.put("deldate", ORDHED_DELV_DATE);
        invoiceParams.put("startTime", ORDHED_START_TIME);
        invoiceParams.put("endTime", ORDHED_END_TIME);
        invoiceParams.put("manualRef", ORDHED_MANU_REF);
        invoiceParams.put("remark", ORDHED_REMARKS);
        invoiceParams.put("repCode", ORDHED_REPCODE);
        invoiceParams.put("txnDate", ORDHED_TXN_DATE);
        invoiceParams.put("nextNumVal", nextNumVal);

       // finalJSONParams.put("posm", new JSONArray());
        JSONArray itemsArray = new JSONArray();
        if (soDetArrayList != null) {
            for (int i = 0; i < soDetArrayList.size(); i++) {
                JSONObject tmpItemJSON = soDetArrayList.get(i).getOrderDetailAsJSON(this);
                if (tmpItemJSON != null) {
                    itemsArray.put(tmpItemJSON);
                }
            }
        }
        invoiceParams.put("invitems", itemsArray);
        JSONObject invoiceJSON = new JSONObject(invoiceParams);
        finalJSONParams.put("Invoice", invoiceJSON);
        JSONObject finalObject = new JSONObject(finalJSONParams);
        Log.wtf("ORDER CLASS ", "ORDER JSON\n" + finalObject.toString());
        return new JSONObject(finalJSONParams);
    }
}
