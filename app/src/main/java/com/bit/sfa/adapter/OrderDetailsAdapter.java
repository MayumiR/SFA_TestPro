package com.bit.sfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bit.sfa.controller.ItemController;
import com.bit.sfa.model.OrderDetail;
import com.bit.sfa.R;

import java.util.ArrayList;


public class OrderDetailsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    ArrayList<OrderDetail> list;
    Context context;

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetail> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;

    }
    @Override
    public int getCount() {
        if (list != null) return list.size();
        return 0;
    }
    @Override
    public OrderDetail getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView ==null) {
            viewHolder = new ViewHolder();
            convertView =inflater.inflate(R.layout.row_order_details,parent,false);
            viewHolder.lblItem = (TextView) convertView.findViewById(R.id.row_item);
            viewHolder.lblQty = (TextView) convertView.findViewById(R.id.row_cases);
            viewHolder.lblAMt = (TextView) convertView.findViewById(R.id.row_piece);
            viewHolder.showStatus=(TextView)convertView.findViewById(R.id.row_free_status);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lblItem.setText(list.get(position).getORDDET_ITEMCODE()+ " - " +new ItemController(convertView.getContext()).getItemNameByCode(list.get(position)
                .getORDDET_ITEMCODE()));
        viewHolder.lblQty.setText(list.get(position).getORDDET_QTY());
        viewHolder.lblAMt.setText("0.0");


        return convertView;
    }
    private  static  class  ViewHolder{
        TextView lblItem;
        TextView lblQty;
        TextView lblAMt;
        TextView showStatus;

    }

}
