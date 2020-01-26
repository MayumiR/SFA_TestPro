package com.bit.sfa.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {

	private String ITEM_CODE;
	private String ITEM_NAME;
	private String ITEM_STATUS;
	private String ITEM_UOM;

	public String getITEM_CODE() {
		return ITEM_CODE;
	}

	public void setITEM_CODE(String ITEM_CODE) {
		this.ITEM_CODE = ITEM_CODE;
	}

	public String getITEM_NAME() {
		return ITEM_NAME;
	}

	public void setITEM_NAME(String ITEM_NAME) {
		this.ITEM_NAME = ITEM_NAME;
	}

	public String getITEM_STATUS() {
		return ITEM_STATUS;
	}

	public void setITEM_STATUS(String ITEM_STATUS) {
		this.ITEM_STATUS = ITEM_STATUS;
	}

	public String getITEM_UOM() {
		return ITEM_UOM;
	}

	public void setITEM_UOM(String ITEM_UOM) {
		this.ITEM_UOM = ITEM_UOM;
	}

	public static Item parseItem(JSONObject instance) throws JSONException {

		if (instance != null) {
			Item item = new Item();
			item.setITEM_CODE(instance.getString("ItemCode"));
			item.setITEM_NAME(instance.getString("ItemName"));
			item.setITEM_UOM(instance.getString("UOM"));
			item.setITEM_STATUS(instance.getString("Status"));
			return item;
		}

		return null;
	}
}
