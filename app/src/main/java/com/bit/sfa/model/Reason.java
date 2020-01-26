package com.bit.sfa.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Reason {

	private String ReasonCode;
	private String ReasonName;
	private String ReasonType;

	public String getReasonCode() {
		return ReasonCode;
	}

	public void setReasonCode(String reasonCode) {
		ReasonCode = reasonCode;
	}

	public String getReasonName() {
		return ReasonName;
	}

	public void setReasonName(String reasonName) {
		ReasonName = reasonName;
	}

	public String getReasonType() {
		return ReasonType;
	}

	public void setReasonType(String reasonType) {
		ReasonType = reasonType;
	}

	public static Reason parseReason(JSONObject instance) throws JSONException {

		if (instance != null) {
			Reason reason = new Reason();
			reason.setReasonCode(instance.getString("code"));
			reason.setReasonName(instance.getString("name"));
			reason.setReasonType(instance.getString("type"));
			return reason;
		}

		return null;
	}
}
