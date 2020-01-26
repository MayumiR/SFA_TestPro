package com.bit.sfa.presale;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bit.sfa.adapter.OrderAdapter;
import com.bit.sfa.controller.OrderController;
import com.bit.sfa.controller.OrderDetailController;
import com.bit.sfa.dialog.PrintPreviewAlertBox;
import com.bit.sfa.helpers.IResponseListener;
import com.bit.sfa.R;
import com.bit.sfa.model.Order;
import com.bit.sfa.model.OrderDetail;
import com.bit.sfa.utils.UtilityContainer;

import java.util.ArrayList;


public class OrderMainFragment extends Fragment {

    private Toolbar detail_toolbar;
    private FloatingActionButton fab;
    private FragmentActivity myContext;
    private ListView salesOrderLst;
    private ArrayList<Order> ordHedList;
    View view;
    IResponseListener listener;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.frag_sales_order, container, false);

        //set title
        getActivity().setTitle("SFA");

        //initializations
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        salesOrderLst = (ListView) view.findViewById(R.id.sales_lv);
        registerForContextMenu(salesOrderLst);

        fatchData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilityContainer.mLoadFragment(new SalesManagementFragment(), getActivity());
//                Fragment promosale = new PromoSaleManagement();
//                getFragmentManager().beginTransaction().replace(
//                        R.id.fragmentContainer, promosale)
//                        .commit();
            }
        });

        //DISABLED BACK NAVIGATION
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("", "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getActivity(), "Back Navigationid is disable", Toast.LENGTH_SHORT).show();
                    return true;
                } else if ((keyCode == KeyEvent.KEYCODE_HOME)) {

                    getActivity().finish();

                    return true;

                } else {
                    return false;
                }
            }
        });


        this.salesOrderLst.setEmptyView(view.findViewById(android.R.id.empty));


        return view;
    }
    /*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.sales_lv) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    /*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Order ordHed = ordHedList.get(info.position);
        ArrayList<OrderDetail> itemList = new OrderDetailController(getActivity()).getAllOrderDetailPrint(ordHed.getORDHED_REFNO());
        ordHed.setSoDetArrayList(itemList);
        switch (item.getItemId()) {

            case R.id.print:
                setBluetooth(true);
                new PrintPreviewAlertBox(getActivity()).PrintDetailsDialogbox(getActivity(), "Print preview", ordHed.getORDHED_REFNO());
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*/
    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
        try {
            listener = (IResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }


    public void fatchData() {

        salesOrderLst.clearTextFilter();
        ordHedList = new OrderController(getActivity()).getAllOrders();
        if(ordHedList.size()==0){
            this.salesOrderLst.setEmptyView(view.findViewById(android.R.id.empty));
        }else{
            salesOrderLst.setAdapter(new OrderAdapter(getActivity(), ordHedList));
        }

    }
    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        return true;
    }
}
