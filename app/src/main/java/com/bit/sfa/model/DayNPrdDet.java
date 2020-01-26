package com.bit.sfa.model;

import org.json.JSONObject;

import java.util.HashMap;

public class DayNPrdDet {

    private String NONPRDDET_ID;
    private String NONPRDDET_REFNO;
    private String NONPRDDET_TXNDATE;
    private String NONPRDDET_REASON_CODE;
    private String NONPRDDET_REASON;
    private String NONPRDDET_REPCODE;
    private String NONPRDDET_IS_SYNCED;

    public String getNONPRDDET_REASON_CODE() {
        return NONPRDDET_REASON_CODE;
    }

    public void setNONPRDDET_REASON_CODE(String NONPRDDET_REASON_CODE) {
        this.NONPRDDET_REASON_CODE = NONPRDDET_REASON_CODE;
    }

    public String getNONPRDDET_ID() {
        return NONPRDDET_ID;
    }

    public void setNONPRDDET_ID(String nONPRDDET_ID) {
        NONPRDDET_ID = nONPRDDET_ID;
    }

    public String getNONPRDDET_REFNO() {
        return NONPRDDET_REFNO;
    }

    public void setNONPRDDET_REFNO(String nONPRDDET_REFNO) {
        NONPRDDET_REFNO = nONPRDDET_REFNO;
    }

    public String getNONPRDDET_TXNDATE() {
        return NONPRDDET_TXNDATE;
    }

    public void setNONPRDDET_TXNDATE(String nONPRDDET_TXNDATE) {
        NONPRDDET_TXNDATE = nONPRDDET_TXNDATE;
    }

//    public String getNONPRDDET_DEBCODE() {
//        return NONPRDDET_DEBCODE;
//    }
//
//    public void setNONPRDDET_DEBCODE(String nONPRDDET_DEBCODE) {
//        NONPRDDET_DEBCODE = nONPRDDET_DEBCODE;
//    }

    public String getNONPRDDET_REASON() {
        return NONPRDDET_REASON;
    }

    public void setNONPRDDET_REASON(String nONPRDDET_REASON) {
        NONPRDDET_REASON = nONPRDDET_REASON;
    }

//    public String getNONPRDDET_LONGITUDE() {
//        return NONPRDDET_LONGITUDE;
//    }
//
//    public void setNONPRDDET_LONGITUDE(String nONPRDDET_LONGITUDE) {
//        NONPRDDET_LONGITUDE = nONPRDDET_LONGITUDE;
//    }
//
//    public String getNONPRDDET_LATITUDE() {
//        return NONPRDDET_LATITUDE;
//    }
//
//    public void setNONPRDDET_LATITUDE(String nONPRDDET_LATITUDE) {
//        NONPRDDET_LATITUDE = nONPRDDET_LATITUDE;
//    }

    public String getNONPRDDET_REPCODE() {
        return NONPRDDET_REPCODE;
    }

    public void setNONPRDDET_REPCODE(String nONPRDDET_REPCODE) {
        NONPRDDET_REPCODE = nONPRDDET_REPCODE;
    }

    public String getNONPRDDET_IS_SYNCED() {
        return NONPRDDET_IS_SYNCED;
    }

    public void setNONPRDDET_IS_SYNCED(String nONPRDDET_IS_SYNCED) {
        NONPRDDET_IS_SYNCED = nONPRDDET_IS_SYNCED;
    }
    public JSONObject getNonpDetailAsJSON(DayNPrdHed nonprd) {

        HashMap<String, Object> params = new HashMap<>();

        params.put("refno", NONPRDDET_REFNO);
        params.put("reasonCode", NONPRDDET_REASON_CODE);
        params.put("reason", NONPRDDET_REASON);

        return new JSONObject(params);
    }

}
