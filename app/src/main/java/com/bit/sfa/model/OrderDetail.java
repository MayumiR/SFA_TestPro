package com.bit.sfa.model;

import org.json.JSONObject;

import java.util.HashMap;

public class OrderDetail {
    private String ORDDET_ID;
    private String ORDDET_AMT;
    private String ORDDET_ITEMCODE;
    private String ORDDET_QTY;
    private String ORDDET_REFNO;
    private String ORDDET_PRICE;
    private String ORDDET_IS_ACTIVE;
    private String ORDDET_ITEMNAME;

    public String getORDDET_ID() {
        return ORDDET_ID;
    }

    public void setORDDET_ID(String ORDDET_ID) {
        this.ORDDET_ID = ORDDET_ID;
    }

    public String getORDDET_AMT() {
        return ORDDET_AMT;
    }

    public void setORDDET_AMT(String ORDDET_AMT) {
        this.ORDDET_AMT = ORDDET_AMT;
    }

    public String getORDDET_ITEMCODE() {
        return ORDDET_ITEMCODE;
    }

    public void setORDDET_ITEMCODE(String ORDDET_ITEMCODE) {
        this.ORDDET_ITEMCODE = ORDDET_ITEMCODE;
    }


    public String getORDDET_QTY() {
        return ORDDET_QTY;
    }

    public void setORDDET_QTY(String ORDDET_QTY) {
        this.ORDDET_QTY = ORDDET_QTY;
    }

    public String getORDDET_REFNO() {
        return ORDDET_REFNO;
    }

    public void setORDDET_REFNO(String ORDDET_REFNO) {
        this.ORDDET_REFNO = ORDDET_REFNO;
    }

    public String getORDDET_PRICE() {
        return ORDDET_PRICE;
    }

    public void setORDDET_PRICE(String ORDDET_PRICE) {
        this.ORDDET_PRICE = ORDDET_PRICE;
    }

    public String getORDDET_IS_ACTIVE() {
        return ORDDET_IS_ACTIVE;
    }

    public void setORDDET_IS_ACTIVE(String ORDDET_IS_ACTIVE) {
        this.ORDDET_IS_ACTIVE = ORDDET_IS_ACTIVE;
    }

    public String getORDDET_ITEMNAME() {
        return ORDDET_ITEMNAME;
    }

    public void setORDDET_ITEMNAME(String ORDDET_ITEMNAME) {
        this.ORDDET_ITEMNAME = ORDDET_ITEMNAME;
    }

    public JSONObject getOrderDetailAsJSON(Order order) {

        HashMap<String, Object> params = new HashMap<>();

        params.put("refno", ORDDET_REFNO);
        params.put("itemcode", ORDDET_ITEMCODE);
        params.put("itemname", ORDDET_ITEMNAME);
        params.put("qty", ORDDET_QTY);
        params.put("price", ORDDET_PRICE);
        params.put("amount", ORDDET_AMT);

               return new JSONObject(params);
    }

}
