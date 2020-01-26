package com.bit.sfa.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemPri {

	private String ITEMPRI_ITEM_CODE;
	private String ITEMPRI_PRICE;


	public String getITEMPRI_ITEM_CODE() {
		return ITEMPRI_ITEM_CODE;
	}

	public void setITEMPRI_ITEM_CODE(String ITEMPRI_ITEM_CODE) {
		this.ITEMPRI_ITEM_CODE = ITEMPRI_ITEM_CODE;
	}

	public String getITEMPRI_PRICE() {
		return ITEMPRI_PRICE;
	}

	public void setITEMPRI_PRICE(String ITEMPRI_PRICE) {
		this.ITEMPRI_PRICE = ITEMPRI_PRICE;
	}


	public static ItemPri parsePrices(JSONObject instance) throws JSONException {

		if (instance != null) {
			ItemPri item = new ItemPri();
			item.setITEMPRI_ITEM_CODE(instance.getString("ItemCode"));
			item.setITEMPRI_PRICE(instance.getString("Price"));
			//item.setITEMPRI_PRIL_CODE(instance.getString("UOM"));
			return item;
		}

		return null;
	}
}
