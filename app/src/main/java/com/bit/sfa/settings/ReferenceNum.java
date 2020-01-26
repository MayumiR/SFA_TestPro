package com.bit.sfa.settings;

import android.content.Context;
import android.util.Log;

import com.bit.sfa.controller.ReferenceController;
import com.bit.sfa.helpers.SharedPref;
import com.bit.sfa.model.Reference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReferenceNum {
    Context context;
    SharedPref pref;

    public ReferenceNum(Context context) {
        this.context = context;
        this.pref = SharedPref.getInstance(context);
    }

    public String getCurrentRefNo(String cSettingsCode) {

        String preFix = "";
        ReferenceController referenceDS = new ReferenceController(context);
        DecimalFormat dFormat = new DecimalFormat("0000");

        Calendar c = Calendar.getInstance();
		/* Check if its new month. if so update fCompanyBranch */
        //referenceDS.isNewMonth(cSettingsCode);

        String sDate = String.valueOf(c.get(Calendar.YEAR)).substring(2) + String.format("%02d", c.get(Calendar.MONTH) + 1);

        String nextNumVal = referenceDS.getNextNumVal(cSettingsCode,pref.getLoginUser().getCode());
        ArrayList<Reference> list = referenceDS.getCurrentPreFix(cSettingsCode,pref.getLoginUser().getPrefix());

        if (!nextNumVal.equals("")) {

            for (Reference reference : list) {
                preFix = reference.getCharVal() + reference.getRepPrefix() + sDate;
            }
            Log.v("next num val", "NEXT :" + preFix + "/" + dFormat.format(Integer.valueOf(nextNumVal)));

        } else {
            for (Reference reference : list) {
                preFix = reference.getCharVal() + reference.getRepPrefix() + sDate;
            }

            Log.v("next num val", "NEXT :" + preFix);
        }

        return preFix + "/" + dFormat.format(Integer.valueOf(nextNumVal));
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int nNumValueInsertOrUpdate(String cSettingsCode) {

        ReferenceController referenceDS = new ReferenceController(context);
        int nextNumVal = 0;

        nextNumVal = Integer.parseInt(referenceDS.getNextNumVal(cSettingsCode,pref.getLoginUser().getCode())) + 1;

        int count = referenceDS.InsetOrUpdate(cSettingsCode, nextNumVal);

        if (count > 0) {
            Log.v("InsertOrUpdate", "success");
        } else {
            Log.v("InsertOrUpdate", "Failed");
        }

        return count;

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Item or value based ref no update-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int NumValueUpdate(String cSettingsCode) {

        ReferenceController referenceDS = new ReferenceController(context);
        int nextNumVal = 0;

        nextNumVal = Integer.parseInt(referenceDS.getNextNumVal(cSettingsCode,pref.getLoginUser().getCode())) + 1;

        int count = referenceDS.InsetOrUpdate(cSettingsCode, nextNumVal);

        if (count > 0) {
            Log.v("InsertOrUpdate", "success");
        } else {
            Log.v("InsertOrUpdate", "Failed");
        }

        return 0;

    }

}
