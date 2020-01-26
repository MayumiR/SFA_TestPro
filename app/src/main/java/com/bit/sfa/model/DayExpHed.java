package com.bit.sfa.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DayExpHed {
    private String EXPHED_REFNO;
    private String EXPHED_TXNDATE;
    private String EXPHED_REPCODE;
    private String EXPHED_REMARK;
    private String EXPHED_ADDDATE;
    private String EXPHED_LONGITUDE;
    private String EXPHED_LATITUDE;
    private String EXPHED_IS_SYNCED;
    private String EXPHED_TOTAMT;
    private String EXPHED_ACTIVESTATE;
    private String nextNumVal;
    private ArrayList<DayExpDet> expenseDets;
    public String getNextNumVal() {
        return nextNumVal;
    }

    public ArrayList<DayExpDet> getExpenseDets() {
        return expenseDets;
    }

    public void setExpenseDets(ArrayList<DayExpDet> expenseDets) {
        this.expenseDets = expenseDets;
    }

    public void setNextNumVal(String nextNumVal) {
        this.nextNumVal = nextNumVal;
    }

    public String getEXPHED_ADDDATE() {
        return EXPHED_ADDDATE;
    }

    public void setEXPHED_ADDDATE(String EXPHED_ADDDATE) {
        this.EXPHED_ADDDATE = EXPHED_ADDDATE;
    }

    public String getEXPHED_REFNO() {
        return EXPHED_REFNO;
    }

    public void setEXPHED_REFNO(String eXPHED_REFNO) {
        EXPHED_REFNO = eXPHED_REFNO;
    }

    public String getEXPHED_REPCODE() {
        return EXPHED_REPCODE;
    }

    public void setEXPHED_REPCODE(String eXPHED_REPCODE) {
        EXPHED_REPCODE = eXPHED_REPCODE;
    }

    public String getEXPHED_REMARK() {
        return EXPHED_REMARK;
    }

    public void setEXPHED_REMARK(String eXPHED_REMARK) {
        EXPHED_REMARK = eXPHED_REMARK;
    }

    public String getEXPHED_IS_SYNCED() {
        return EXPHED_IS_SYNCED;
    }

    public void setEXPHED_IS_SYNCED(String eXPHED_IS_SYNCED) {
        EXPHED_IS_SYNCED = eXPHED_IS_SYNCED;
    }


    public String getEXPHED_TXNDATE() {
        return EXPHED_TXNDATE;
    }

    public void setEXPHED_TXNDATE(String eXPHED_TXNDATE) {
        EXPHED_TXNDATE = eXPHED_TXNDATE;
    }

    public String getEXPHED_TOTAMT() {
        return EXPHED_TOTAMT;
    }

    public void setEXPHED_TOTAMT(String eXPHED_TOTAMT) {
        EXPHED_TOTAMT = eXPHED_TOTAMT;
    }

    public String getEXPHED_ACTIVESTATE() {
        return EXPHED_ACTIVESTATE;
    }

    public void setEXPHED_ACTIVESTATE(String eXPHED_ACTIVESTATE) {
        EXPHED_ACTIVESTATE = eXPHED_ACTIVESTATE;
    }
    public String getEXPHED_LATITUDE() {
        return EXPHED_LATITUDE;
    }

    public void setEXPHED_LATITUDE(String eXPHED_LATITUDE) {
        EXPHED_LATITUDE = eXPHED_LATITUDE;
    }

    public String getEXPHED_LONGITUDE() {
        return EXPHED_LONGITUDE;
    }

    public void setEXPHED_LONGITUDE(String eXPHED_LONGITUDE) {
        EXPHED_LONGITUDE = eXPHED_LONGITUDE;
    }

    public JSONObject getExpenseAsJSON() throws JSONException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        HashMap<String, Object> finalJSONParams = new HashMap<>();
        HashMap<String, Object> expenseParams = new HashMap<>();
        expenseParams.put("refno", EXPHED_REFNO);
        expenseParams.put("latitude", EXPHED_LATITUDE);
        expenseParams.put("longitude", EXPHED_LONGITUDE);
        expenseParams.put("total_amount", EXPHED_TOTAMT);
        expenseParams.put("remark", EXPHED_REMARK);
        expenseParams.put("repCode", EXPHED_REPCODE);
        expenseParams.put("txnDate", EXPHED_TXNDATE);
        expenseParams.put("nextNumVal", nextNumVal);
        // finalJSONParams.put("posm", new JSONArray());
        JSONArray detArray = new JSONArray();
        if (expenseDets != null) {
            for (int i = 0; i < expenseDets.size(); i++) {
                JSONObject tmpExpJSON = expenseDets.get(i).getExpDetailAsJSON(this);
                if (tmpExpJSON != null) {
                    detArray.put(tmpExpJSON);
                }
            }
        }
        expenseParams.put("ExpenceDets", detArray);
        JSONObject expenseJSON = new JSONObject(expenseParams);
        finalJSONParams.put("Expences", expenseJSON);
        JSONObject finalObject = new JSONObject(finalJSONParams);
        Log.wtf("Expense CLASS ", "Expense JSON\n" + finalObject.toString());
        return new JSONObject(finalJSONParams);
    }
}
