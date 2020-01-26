package com.bit.sfa.model;

import org.json.JSONObject;

import java.util.HashMap;

public class DayExpDet {


    private String EXPDET_REFNO;
    private String EXPDET_EXPCODE;
    private String EXPDET_AMOUNT;

    public String getEXPDET_REFNO() {
        return EXPDET_REFNO;
    }

    public void setEXPDET_REFNO(String eXPDET_REFNO) {
        EXPDET_REFNO = eXPDET_REFNO;
    }

    public String getEXPDET_EXPCODE() {
        return EXPDET_EXPCODE;
    }
    public void setEXPDET_EXPCODE(String eXPDET_EXPCODE) {
        EXPDET_EXPCODE = eXPDET_EXPCODE;
    }

    public String getEXPDET_AMOUNT() {
        return EXPDET_AMOUNT;
    }

    public void setEXPDET_AMOUNT(String eXPDET_AMOUNT) {
        EXPDET_AMOUNT = eXPDET_AMOUNT;
    }

    public JSONObject getExpDetailAsJSON(DayExpHed expense) {

        HashMap<String, Object> params = new HashMap<>();

        params.put("refno", EXPDET_REFNO);
        params.put("expcode", EXPDET_EXPCODE);
        params.put("amount", EXPDET_AMOUNT);

        return new JSONObject(params);
    }
}
