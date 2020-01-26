package com.bit.sfa.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Customer {

	private String cusCode;
	private String cusName;
	private String cusAddress;
	private String cusMob;
	private String cusRoute;
	private String cusStatus;
	private String cusEmail;
	private boolean selectedOnList;

	public String getCusCode() {
		return cusCode;
	}

	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusMob() {
		return cusMob;
	}

	public void setCusMob(String cusMob) {
		this.cusMob = cusMob;
	}

	public String getCusRoute() {
		return cusRoute;
	}

	public void setCusRoute(String cusRoute) {
		this.cusRoute = cusRoute;
	}

	public String getCusStatus() {
		return cusStatus;
	}

	public void setCusStatus(String cusStatus) {
		this.cusStatus = cusStatus;
	}

	public String getCusEmail() {
		return cusEmail;
	}

	public void setCusEmail(String cusEmail) {
		this.cusEmail = cusEmail;
	}

	public boolean isSelectedOnList() {
		return selectedOnList;
	}
	
	public void setSelectedOnList(boolean selectedOnList) {
		this.selectedOnList = selectedOnList;
	}

	public static Customer parseOutlet(JSONObject instance) throws JSONException {

		if (instance != null) {
			Customer outlet = new Customer();
			String outletIdString;
			outlet.setCusCode(instance.getString("cuscode"));
			outlet.setCusName(instance.getString("cusname"));
			outlet.setCusRoute(instance.getString("routecode"));
			outlet.setCusAddress(instance.getString("address"));
			//outlet.setCusAdd2(instance.getString("addressline2"));
			outlet.setCusMob(instance.getString("mobile"));
			outlet.setCusEmail(instance.getString("email"));
			outlet.setCusStatus(instance.getString("status"));
			return outlet;
		}

		return null;
	}
		
}
