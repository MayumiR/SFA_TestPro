package com.bit.sfa.presale;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.sfa.adapter.NewPreProductAdapter;
import com.bit.sfa.adapter.OrderDetailsAdapter;
import com.bit.sfa.controller.ItemController;
import com.bit.sfa.controller.OrderDetailController;
import com.bit.sfa.controller.OrderController;
import com.bit.sfa.controller.OrderTempController;
import com.bit.sfa.dialog.CustomProgressDialog;
import com.bit.sfa.dialog.PrintPreviewAlertBox;
import com.bit.sfa.helpers.NetworkFunctions;
import com.bit.sfa.helpers.SharedPref;
import com.bit.sfa.utils.UtilityContainer;
import com.bit.sfa.view.ActivityHome;
import com.bit.sfa.model.OrderDetail;
import com.bit.sfa.model.Order;
import com.bit.sfa.model.tempOrderDet;

import com.bit.sfa.R;
import com.bit.sfa.utils.NetworkUtil;
import com.bit.sfa.settings.ReferenceNum;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Rashmi
 */
public class DetailFragment extends Fragment{

    private static final String TAG = "DetailFragment";
    public View view;
    public SharedPref mSharedPref;
    private ListView lv_pre_order_detlv;
    private TextView totQty,TotAmt;
    private ImageButton pre_Product_btn;
    private FloatingActionButton save, undo, upload;
    //TODO :: should implement upload button in option menu
    private  String RefNo;
    public ActivityHome mainActivity;
    int seqno = 0;
    ArrayList<tempOrderDet> PreproductList = null, selectedItemList = null;
    ArrayList<OrderDetail> orderList;
    SweetAlertDialog pDialog;
    private  MyReceiver r;
    private Order tmpsoHed=null;
    private FloatingActionButton next;
    List<String> resultList;
    NetworkFunctions networkFunctions;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.sales_management_pre_sales_details_new, container, false);
        lv_pre_order_detlv = (ListView) view.findViewById(R.id.lvProducts_Pre);
        networkFunctions = new NetworkFunctions(getActivity());
        pre_Product_btn = (ImageButton) view.findViewById(R.id.btnPreSalesProduct);
        save = (FloatingActionButton) view.findViewById(R.id.btn_save);
        undo = (FloatingActionButton) view.findViewById(R.id.btn_undo);
        TotAmt = (TextView)view.findViewById(R.id.lblGrossVal);
        totQty = (TextView)view.findViewById(R.id.lblTotQty);
        seqno = 0;
        mSharedPref =    SharedPref.getInstance(getActivity());
        RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
        mainActivity = (ActivityHome) getActivity();
        tmpsoHed=new Order();
        resultList = new ArrayList<>();
        //------------------------------------------------------------------------------------------------------------------------

        showData();
        pre_Product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoardingPreProductFromDB().execute();
            }
        });
//------------------------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------------------------------------------
        lv_pre_order_detlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                OrderDetail tranSODet=orderList.get(position);
               // popEditDialogBox(tranSODet);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                        .content("Do you want to upload order details?")
                        .positiveText("Save & Upload")
                        .positiveColor(getResources().getColor(R.color.material_alert_positive_button))
                        .negativeText("Save Only")
                        .negativeColor(getResources().getColor(R.color.material_alert_negative_button))
//                        .neutralText("Save")
//                        .negativeColor(getResources().getColor(R.color.material_alert_positive_button))

                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                Upload();
                                new PrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview", RefNo);
                                dialog.dismiss();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                Save();
                                super.onNegative(dialog);
                            }

//                            @Override
//                            public void onNeutral(MaterialDialog dialog) {
//                                super.onNeutral(dialog);
//                            }
                        })
                        .build();
                materialDialog.setCancelable(false);
                materialDialog.setCanceledOnTouchOutside(false);
                materialDialog.show();

            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoEditingData(getActivity());
            }
        });

        return view;
    }

    //-------------------------rashmi 2018-08-20----------------------------------------------------------------------------------------------------------------

    public void showData() {
        try {
            lv_pre_order_detlv.setAdapter(null);
            orderList = new OrderDetailController(getActivity()).getAllOrderDetails(RefNo);
            lv_pre_order_detlv.setAdapter(new OrderDetailsAdapter(getActivity(), orderList));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------- show pre product dialog----------------------------------------------------------------------

    public void PreProductDialogBox() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.product_dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final ListView lvProducts = (ListView) promptView.findViewById(R.id.lv_product_list);
        final SearchView search = (SearchView) promptView.findViewById(R.id.et_search);

        lvProducts.clearTextFilter();
        PreproductList = new OrderTempController(getActivity()).getAllItems("");
        lvProducts.setAdapter(new NewPreProductAdapter(getActivity(), PreproductList));

        alertDialogBuilder.setCancelable(false).setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int qty = 0;
                double total = 0.0;
                selectedItemList = new OrderTempController(getActivity()).getSelectedItems();
                updateSOeDet(selectedItemList);
                if(selectedItemList != null && !selectedItemList.isEmpty()){
                    for(tempOrderDet det : selectedItemList){
                        qty += Integer.parseInt(det.getPREPRODUCT_QTY());
                        total += Double.parseDouble(det.getPREPRODUCT_PRICE()) * Integer.parseInt(det.getPREPRODUCT_QTY());
                    }
                    totQty.setText(" "+qty);
                    TotAmt.setText(" Rs."+total);
                }
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                PreproductList = new OrderTempController(getActivity()).getAllItems(query);
                lvProducts.setAdapter(new NewPreProductAdapter(getActivity(), PreproductList));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                PreproductList.clear();
                PreproductList = new OrderTempController(getActivity()).getAllItems(newText);
                lvProducts.setAdapter(new NewPreProductAdapter(getActivity(), PreproductList));

                return false;
            }
        });

    }
    //------------------------------------------------------------------------------------------------------------------------------------------------
    public void updateSOeDet(final ArrayList<tempOrderDet> list) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Fetch Data Please Wait.");
                pDialog.setCancelable(false);
                pDialog.show();

                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                int i = 0;
                new OrderDetailController(getActivity()).restData(RefNo);

                for (tempOrderDet product : list) {
                    i++;
                   // String prilCode, String itemCode, String Qty, String price, String itemname
                    mUpdatePrsSales(product.getPREPRODUCT_UOM(), product.getPREPRODUCT_ITEMCODE(), product.getPREPRODUCT_QTY(), product.getPREPRODUCT_PRICE(),product.getPREPRODUCT_ITEMNAME());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(pDialog.isShowing()){
                    pDialog.dismiss();
                }

                showData();
            }

        }.execute();
    }

/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private void undoEditingData(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure you want to Undo this entry?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                ActivityHome activity = (ActivityHome) getActivity();
                try {
                    new OrderController(getActivity()).undoOrdHedByID(RefNo);
                } catch (Exception e) {

                }
                try {
                    new OrderDetailController(getActivity()).OrdDetByRefno(RefNo);
                } catch (Exception e) {

                }
                activity.cusPosition = 0;
                activity.selectedDebtor = null;
                UtilityContainer.mLoadFragment(new SalesManagementFragment(), getActivity());
                //UtilityContainer.ClearNonSharedPref(getActivity());
                Toast.makeText(getActivity(), "Undo Success", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();

            }
        });

        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    //------------------------------------------------------update orderdetail tbl------------------------------------------------------------------------------------

    public void mUpdatePrsSales(String prilCode, String itemCode, String Qty, String price, String itemname) {
        OrderDetail SODet = new OrderDetail();
        ArrayList<OrderDetail> SOList = new ArrayList<OrderDetail>();
        SODet.setORDDET_AMT(""+(Double.parseDouble(Qty)*Double.parseDouble(price)));
        SODet.setORDDET_QTY(Qty);
        SODet.setORDDET_PRICE(price);
        SODet.setORDDET_ITEMCODE(itemCode);
        SODet.setORDDET_REFNO(RefNo);
        SODet.setORDDET_ITEMNAME(itemname);
        SODet.setORDDET_IS_ACTIVE("1");
        SOList.add(SODet);
        new OrderDetailController(getActivity()).createOrUpdateOrdDet(SOList);

    }

//    public void mUploadResult(String message) {
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setMessage(message);
//        alertDialogBuilder.setTitle("Upload Summary");
//
//        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int id) {
//
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertD = alertDialogBuilder.create();
//        alertD.show();
//        alertD.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//
//    }
    //---------------------------------LoardingPreProductFromDB----------------------------------------------------------------------------------------
public class LoardingPreProductFromDB extends AsyncTask<Object, Object, ArrayList<tempOrderDet>> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetch Data Please Wait.");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected ArrayList<tempOrderDet> doInBackground(Object... objects) {

        if (new OrderTempController(getActivity()).tableHasRecords()) {
            PreproductList = new OrderTempController(getActivity()).getAllItems("");
        } else {
            PreproductList =new ItemController(getActivity()).getAllItemForPreSales("",RefNo);

            new OrderTempController(getActivity()).insertOrUpdateProducts(PreproductList);
            //---------re Order Temp product  list added for  fProducts_pre table-----------------dhanushika-------------------------------
            if(tmpsoHed!=null) {
                ArrayList<OrderDetail> detArrayList = tmpsoHed.getSoDetArrayList();
                if (detArrayList != null) {
                    for (int i = 0; i < detArrayList.size(); i++) {
                        String tmpItemName = detArrayList.get(i).getORDDET_ITEMCODE();
                        String tmpQty = detArrayList.get(i).getORDDET_QTY();
                        //Update Qty in  fProducts_pre table
                        int count = new OrderTempController(getActivity()).updateProductQtyFor(tmpItemName, tmpQty);
                        if (count > 0) {
                            // Log.d("InsertOrUpdate", "success");
                        } else {
                            // Log.d("InsertOrUpdate", "Failed");
                        }

                    }
                }
            }
            //----------------------------------------------------------------------------

        }
        return PreproductList;
    }


    @Override
    protected void onPostExecute(ArrayList<tempOrderDet> preProducts) {
        super.onPostExecute(preProducts);

        if(pDialog.isShowing()){
            pDialog.dismiss();
        }

        PreProductDialogBox();

    }
}
//------------------------------------------------------------------------------------------------------------------------

    public void Save() {

        if (new OrderDetailController(getActivity()).getItemCountForSave(RefNo) > 0 ) {
            new OrderController(getActivity()).updateTotal(new OrderDetailController(getActivity()).getGrossValue(RefNo),RefNo);
            new SaveAsyncTask().execute();

        }else{
            Toast.makeText(getActivity(), "No Records to save !", android.widget.Toast.LENGTH_LONG).show();
        }
    }
//------------------------------------------------------------------------------------------------------------------------


    public void Upload() {
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
        if (new OrderDetailController(getActivity()).getItemCountForUpload(RefNo) > 0 ) {
            new OrderController(getActivity()).updateTotal(new OrderDetailController(getActivity()).getGrossValue(RefNo),RefNo);
            new TaskRunner().execute();

        }else{
            Toast.makeText(getActivity(), "No Records to upload !", android.widget.Toast.LENGTH_LONG).show();
        }
    } else
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();

}
    /*-*-*-*-*-*-*-*-*-*-save and upload*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    class TaskRunner extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
            Log.v(">>SaveHed>>","OrdHedDS.InactiveStatusUpdate(RefNo)");
            new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);

            Log.v(">>SaveDet>>","OrdDetDS.InactiveStatusUpdate(RefNo)");
            new OrderTempController(getActivity()).mClearTables();

            final ActivityHome activity = (ActivityHome) getActivity();

            activity.selectedDebtor = null;
            activity.selectedDebtor = null;
            activity.selectedOrdHed = null;
            activity.SAroute = null;
            activity.SAcustomer = null;

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ActivityHome home = new ActivityHome();
            try {

                    OrderController hedDS = new OrderController(getActivity());

                    ArrayList<Order> ordHedList = hedDS.getAllUnSyncOrdHed();
//                    /* If records available for upload then */
                    if (ordHedList.size() <= 0)
                        Toast.makeText(getActivity(), "No Records to upload !", Toast.LENGTH_LONG).show();
                    else{
                        for(Order order : ordHedList){
                            new SyncOrder(order).execute();
                        }
                        Log.v(">>8>>","UploadPreSales execute finish");
                        new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.NumVal));
//
                   }


            }catch(Exception e){
                Log.v("Exception in sync order",e.toString());
            }

           //  mSharedPref.clear_shared_pref();
             home.selectedDebtor = null;
             home.selectedOrdHed = null;

        }
    }
    /*-*-*-*-*-*-*-*-*-*-save*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    class SaveAsyncTask extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            new OrderController(getActivity()).InactiveStatusUpdate(RefNo);
            Log.v(">>SaveHed>>","OrdHedDS.InactiveStatusUpdate(RefNo)");
            new OrderDetailController(getActivity()).InactiveStatusUpdate(RefNo);

            Log.v(">>SaveDet>>","OrdDetDS.InactiveStatusUpdate(RefNo)");
            new OrderTempController(getActivity()).mClearTables();

            final ActivityHome activity = (ActivityHome) getActivity();

            activity.selectedDebtor = null;
            activity.selectedDebtor = null;
            activity.selectedOrdHed = null;
            activity.SAroute = null;
            activity.SAcustomer = null;

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ActivityHome home = new ActivityHome();

            //OrderController hedDS = new OrderController(getActivity());

//            ArrayList<Order> ordHedList = hedDS.getAllActiveOrdHed();
////                    /* If records available for upload then */
//            if (ordHedList.size() <= 0){
               // new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.NumVal));
                Toast.makeText(getActivity(), "Successfully saved !", Toast.LENGTH_LONG).show();
           //     }
//                    else{
//                        Log.v(">>Done>>","SavePreSales execute finish");
//                        new ReferenceNum(getActivity()).NumValueUpdate(getResources().getString(R.string.NumVal));
//                        Toast.makeText(getActivity(), "Successfully saved !", Toast.LENGTH_LONG).show();
//
//                    }

            home.selectedDebtor = null;
            home.selectedOrdHed = null;
            UtilityContainer.mLoadFragment(new SalesManagementFragment(), getActivity());

        }
    }
    //---------------------------------LoardingPreProductFromDB----------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_DETAILS"));
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }
    /*-*-*-*-*-Rashmi 2018-08-20*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            DetailFragment.this.mToggleTextbox();
        }
    }
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    private class SyncOrder extends AsyncTask<Void, Void, Boolean> {

        private CustomProgressDialog pDialog;
        private List<String> errors = new ArrayList<>();
        CustomProgressDialog pdialog;
        Order order;

        public SyncOrder(Order order){
            this.order = order;

            this.pdialog = new CustomProgressDialog(getActivity());
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new CustomProgressDialog(getActivity());
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.show();
            pDialog.setMessage("Synchronizing Order...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String syncResponse = null;
            try {
                syncResponse = networkFunctions.syncOrder(order);


                JSONObject responseJSON = new JSONObject(syncResponse);

                boolean isSynced = responseJSON.getBoolean("result");
                if (!isSynced) {
                    errors.add("Server refused the order");

                }
                return isSynced;

            } catch (IOException e) {
                e.printStackTrace();
                errors.add("Cannot reach the server");

                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                if (syncResponse != null && syncResponse.contains("{\"result\":true}")) {

                    return true;
                } else {
                    errors.add("Invalid response received from the server");
                    return false;
                }
                //errors.add("Invalid response received from the server");
               // return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (pDialog.isShowing()) pDialog.dismiss();




            if (aBoolean) {
                // Success
                new OrderController(getActivity()).updateIsSynced(true,RefNo);
                Toast.makeText(getActivity(), "Order synced with the server successfully", Toast.LENGTH_SHORT).show();

                UtilityContainer.mLoadFragment(new SalesManagementFragment(), getActivity());

            } else {
                // Failure
                StringBuilder builder = new StringBuilder();
                if (errors.size() == 1) {
                    builder.append(errors.get(0));
                } else {
                    builder.append("Following errors occurred");
                    for (String error : errors) {
                        builder.append("\n -").append(error);
                    }
                }

                builder.append("\nOrder saved in local database");


            }

        }
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void mToggleTextbox() {

        if (mSharedPref.getGlobalVal("PrekeyCustomer").equals("Y")) {
            pre_Product_btn.setEnabled(true);
            // from PreSalesAdapter----- for re oder creation
            Bundle mBundle = getArguments();
            if (mBundle != null) {
                tmpsoHed = (Order) mBundle.getSerializable("order");
            }
            showData();
            selectedItemList = new OrderTempController(getActivity()).getSelectedItems();
            if(selectedItemList !=null &&  !selectedItemList.isEmpty()){
                updateSOeDet(selectedItemList);
            }

        } else {
            Toast.makeText(getActivity(), "Select a customer to continue...", Toast.LENGTH_SHORT).show();
            pre_Product_btn.setEnabled(false);
        }
    }
}
