package com.bit.sfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.bit.sfa.R;
import com.bit.sfa.controller.ReasonController;
import com.bit.sfa.model.DayExpDet;

import java.util.ArrayList;


public class SalesExpenseGridDetails extends ArrayAdapter<DayExpDet> {
    Context context;
    ArrayList<DayExpDet> list;

    public SalesExpenseGridDetails(Context context, ArrayList<DayExpDet> list) {
        super(context, R.layout.row_expense_det, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflater = null;
        View row = null;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.row_expense_det, parent, false);

        TextView Itemname = (TextView) row.findViewById(R.id.row_nonprd_refno);
        TextView Itemcode = (TextView) row.findViewById(R.id.row_nonprd_reason);
        //TextView _id = (TextView) row.findViewById(R.id._id);

        Itemname.setText(new ReasonController(getContext()).getReaNameByCode(list.get(position).getEXPDET_EXPCODE()));
        Itemcode.setText(String.format("%.2f", Double.parseDouble(list.get(position).getEXPDET_AMOUNT())));

        return row;
    }

}
