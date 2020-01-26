package com.bit.sfa.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DayNPrdHed implements Serializable {
    private String NONPRDHED_REFNO;
    private String NONPRDHED_TXNDATE;
    private String NONPRDHED_DEALCODE;
    private String NONPRDHED_REPCODE;
    private String NONPRDHED_REMARK;
    private String NONPRDHED_COSTCODE;
    private String NONPRDHED_ADDUSER;
    private String NONPRDHED_ADDDATE;
    private String NONPRDHED_ADDMACH;
    private String NONPRDHED_TRANSBATCH;
    private String NONPRDHED_IS_SYNCED;
    private String NONPRDHED_ADDRESS;
    private String NONPRDHED_LONGITUDE;
    private String NONPRDHED_LATITUDE;
    private String NONPRDHED_DEBCODE;
    private String NONPRDHED_IS_ACTIVE;
    private String nextNumVal;
    private ArrayList<DayNPrdDet> nonprdDets;

    public String getNextNumVal() {
        return nextNumVal;
    }

    public void setNextNumVal(String nextNumVal) {
        this.nextNumVal = nextNumVal;
    }

    public ArrayList<DayNPrdDet> getNonprdDets() {
        return nonprdDets;
    }

    public void setNonprdDets(ArrayList<DayNPrdDet> nonprdDets) {
        this.nonprdDets = nonprdDets;
    }

    public String getNONPRDHED_LONGITUDE() {
        return NONPRDHED_LONGITUDE;
    }

    public void setNONPRDHED_LONGITUDE(String NONPRDHED_LONGITUDE) {
        this.NONPRDHED_LONGITUDE = NONPRDHED_LONGITUDE;
    }

    public String getNONPRDHED_LATITUDE() {
        return NONPRDHED_LATITUDE;
    }

    public void setNONPRDHED_LATITUDE(String NONPRDHED_LATITUDE) {
        this.NONPRDHED_LATITUDE = NONPRDHED_LATITUDE;
    }

    public String getNONPRDHED_DEBCODE() {
        return NONPRDHED_DEBCODE;
    }

    public void setNONPRDHED_DEBCODE(String NONPRDHED_DEBCODE) {
        this.NONPRDHED_DEBCODE = NONPRDHED_DEBCODE;
    }

    public String getNONPRDHED_IS_ACTIVE() {
        return NONPRDHED_IS_ACTIVE;
    }

    public void setNONPRDHED_IS_ACTIVE(String NONPRDHED_IS_ACTIVE) {
        this.NONPRDHED_IS_ACTIVE = NONPRDHED_IS_ACTIVE;
    }

    public String getNONPRDHED_ADDRESS() {
        return NONPRDHED_ADDRESS;
    }

    public void setNONPRDHED_ADDRESS(String nONPRDHED_ADDRESS) {
        NONPRDHED_ADDRESS = nONPRDHED_ADDRESS;
    }

    public String getNONPRDHED_REFNO() {
        return NONPRDHED_REFNO;
    }

    public void setNONPRDHED_REFNO(String nONPRDHED_REFNO) {
        NONPRDHED_REFNO = nONPRDHED_REFNO;
    }

    public String getNONPRDHED_TXNDATE() {
        return NONPRDHED_TXNDATE;
    }

    public void setNONPRDHED_TXNDATE(String nONPRDHED_TXNDAET) {
        NONPRDHED_TXNDATE = nONPRDHED_TXNDAET;
    }

    public String getNONPRDHED_DEALCODE() {
        return NONPRDHED_DEALCODE;
    }

    public void setNONPRDHED_DEALCODE(String nONPRDHED_DEALCODE) {
        NONPRDHED_DEALCODE = nONPRDHED_DEALCODE;
    }

    public String getNONPRDHED_REPCODE() {
        return NONPRDHED_REPCODE;
    }

    public void setNONPRDHED_REPCODE(String nONPRDHED_REPCODE) {
        NONPRDHED_REPCODE = nONPRDHED_REPCODE;
    }

    public String getNONPRDHED_REMARK() {
        return NONPRDHED_REMARK;
    }

    public void setNONPRDHED_REMARK(String nONPRDHED_REMARK) {
        NONPRDHED_REMARK = nONPRDHED_REMARK;
    }

    public String getNONPRDHED_COSTCODE() {
        return NONPRDHED_COSTCODE;
    }

    public void setNONPRDHED_COSTCODE(String nONPRDHED_COSTCODE) {
        NONPRDHED_COSTCODE = nONPRDHED_COSTCODE;
    }

    public String getNONPRDHED_ADDUSER() {
        return NONPRDHED_ADDUSER;
    }

    public void setNONPRDHED_ADDUSER(String nONPRDHED_ADDUSER) {
        NONPRDHED_ADDUSER = nONPRDHED_ADDUSER;
    }

    public String getNONPRDHED_ADDDATE() {
        return NONPRDHED_ADDDATE;
    }

    public void setNONPRDHED_ADDDATE(String nONPRDHED_ADDDATE) {
        NONPRDHED_ADDDATE = nONPRDHED_ADDDATE;
    }

    public String getNONPRDHED_ADDMACH() {
        return NONPRDHED_ADDMACH;
    }

    public void setNONPRDHED_ADDMACH(String nONPRDHED_ADDMACH) {
        NONPRDHED_ADDMACH = nONPRDHED_ADDMACH;
    }

    public String getNONPRDHED_TRANSBATCH() {
        return NONPRDHED_TRANSBATCH;
    }

    public void setNONPRDHED_TRANSBATCH(String nONPRDHED_TRANSBATCH) {
        NONPRDHED_TRANSBATCH = nONPRDHED_TRANSBATCH;
    }

    public String getNONPRDHED_IS_SYNCED() {
        return NONPRDHED_IS_SYNCED;
    }

    public void setNONPRDHED_IS_SYNCED(String nONPRDHED_IS_SYNCED) {
        NONPRDHED_IS_SYNCED = nONPRDHED_IS_SYNCED;
    }
    public JSONObject getNonprdAsJSON() throws JSONException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        HashMap<String, Object> finalJSONParams = new HashMap<>();
        HashMap<String, Object> nonprdParams = new HashMap<>();
        nonprdParams.put("refno", NONPRDHED_REFNO);
        nonprdParams.put("latitude", NONPRDHED_LATITUDE);
        nonprdParams.put("longitude", NONPRDHED_LONGITUDE);
        nonprdParams.put("cusCode", NONPRDHED_DEBCODE);
        nonprdParams.put("remark", NONPRDHED_REMARK);
        nonprdParams.put("repCode", NONPRDHED_REPCODE);
        nonprdParams.put("txnDate", NONPRDHED_TXNDATE);
        nonprdParams.put("addDate", NONPRDHED_ADDDATE);
        nonprdParams.put("nextNumVal", nextNumVal);
        // finalJSONParams.put("posm", new JSONArray());
        JSONArray detArray = new JSONArray();
        if (nonprdDets != null) {
            for (int i = 0; i < nonprdDets.size(); i++) {
                JSONObject tmpNonpJSON = nonprdDets.get(i).getNonpDetailAsJSON(this);
                if (tmpNonpJSON != null) {
                    detArray.put(tmpNonpJSON);
                }
            }
        }
        nonprdParams.put("NonProductiveDets", detArray);
        JSONObject nonprdJSON = new JSONObject(nonprdParams);
        finalJSONParams.put("NonProductives", nonprdJSON);
        JSONObject finalObject = new JSONObject(finalJSONParams);
        Log.wtf("NonProductive CLASS ", "NonProductive JSON\n" + finalObject.toString());
        return new JSONObject(finalJSONParams);
    }
}
