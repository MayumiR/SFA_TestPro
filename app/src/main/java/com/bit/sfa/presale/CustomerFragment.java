package com.bit.sfa.presale;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.sfa.adapter.CustomerAdapter;
import com.bit.sfa.controller.CustomerController;
import com.bit.sfa.controller.RouteController;
import com.bit.sfa.helpers.IResponseListener;
import com.bit.sfa.helpers.SharedPref;
import com.bit.sfa.libs.progress.ProgressWheel;
import com.bit.sfa.model.Customer;
import com.bit.sfa.utils.LocationProvider;
import com.bit.sfa.utils.NetworkUtil;
import com.bit.sfa.view.ActivityHome;
import com.bit.sfa.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerFragment extends Fragment {

    ListView lvCustomers;
    View view;
    ArrayList<Customer> customerList;
    public String LOG_TAG = "FragmentCustomer";
    SharedPref mSharedPref;
    IResponseListener listener;
    Button all,route;
    private LocationProvider locationProvider;
    private Location finalLocation;
   // private ProgressWheel progressWheel;
    public CustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_frag_promo_sale_customer, container, false);
        //initializations
        lvCustomers = (ListView) view.findViewById(R.id.cus_lv);
       // progressWheel = (ProgressWheel) view.findViewById(R.id.mark_attendance_progress_location);
        all = (Button) view.findViewById(R.id.all_cust);
        route = (Button) view.findViewById(R.id.route_wise_cust);
        mSharedPref = SharedPref.getInstance(getActivity());
        //data controls
        RouteController ds = new RouteController(getActivity());
        final CustomerController cusContrl = new CustomerController(getActivity());

        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH) + 1;
        String month = null;
        if (cmonth < 10) {
            month = "0" + cmonth;
        } else {
            month = "" + cmonth;
        }
        DecimalFormat df_month = new DecimalFormat("00");

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy"); //change this
        final String formattedDate = simpleDateFormat.format(d);


        //get route customers

        lvCustomers.clearTextFilter();
        customerList = null;
        customerList = cusContrl.getAllCustomers();
        lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));


        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"All Customers",Toast.LENGTH_LONG).show();
                lvCustomers.clearTextFilter();
                customerList = new CustomerController(getActivity()).getAllCustomers();
                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));
            }
        });

//        route.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Route Customers",Toast.LENGTH_LONG).show();
//                lvCustomers.clearTextFilter();
//               / customerList = new CustomerController(getActivity()).getRouteCustomers(formattedDate,current_route);
//                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));
//            }
//        });


        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int x = android.provider.Settings.Global.getInt(getActivity().getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
                if (x > 0){
                    //customerList = debtorDS.getRouteCustomers(current_route);
                    Customer customer = customerList.get(i);
                    ActivityHome home = new ActivityHome();
                    String selectedDebtor = customer.getCusName();
                    System.out.println("selectedDebtor" + ActivityHome.SAcustomer);
                    home.selectedDebtor = customer;
                    ActivityHome.SAcustomer = selectedDebtor;
                    ActivityHome.SAroute = customer.getCusRoute();
                    SalesManagementFragment.iscustomer = true;
                    //locationProvider.startLocating();
                   SharedPref.getInstance(getActivity()).setGlobalVal("PrekeyCusName" ,customer.getCusName());
                    SharedPref.getInstance(getActivity()).setGlobalVal("PrekeyCusCode", customer.getCusCode());
                    navigateToHeader(i);
                }else{
                    android.widget.Toast.makeText(getActivity(), "Please tick the 'Automatic Date and Time' option to continue..", android.widget.Toast.LENGTH_LONG).show();
                    /* Show Date/time settings dialog */
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            }
        });


        /*******************************************getting GPS ******************************/
        locationProvider = new LocationProvider((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE),
                new LocationProvider.ICustomLocationListener() {

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.d(LOG_TAG, "Provider enabled");
                        locationProvider.startLocating();
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.d(LOG_TAG, "Provider disabled");
                        locationProvider.stopLocating();
                    }

                    @Override
                    public void onUnableToGetLocation() {
                        Toast.makeText(getActivity(), "Unable to get location", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onGotLocation(Location location, int locationType) {
                        if (location != null) {
                            finalLocation = location;

SharedPref.getInstance(getActivity()).setGlobalVal("startLongitude", String.valueOf(finalLocation.getLongitude()));
SharedPref.getInstance(getActivity()).setGlobalVal("startLatitude", String.valueOf(finalLocation.getLatitude()));
                             System.currentTimeMillis();



                        }
                    }

                    @Override
                    public void onProgress(int type) {
                        if (type == LocationProvider.LOCATION_TYPE_GPS) {
                            Toast.makeText(getActivity(),"Getting location (GPS)",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(),"Getting location (Network)",Toast.LENGTH_LONG).show();

                        }
                    }
                });
        try {
            locationProvider.setOnGPSTimeoutListener(new LocationProvider.OnGPSTimeoutListener() {
                @Override
                public void onGPSTimeOut() {

                    MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                            .content("Please move to a more clear location to get GPS")
                            .positiveText("Try Again")
                            .positiveColor(getResources().getColor(R.color.material_alert_neutral_button))
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    locationProvider.startLocating();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                }

                                @Override
                                public void onNeutral(MaterialDialog dialog) {
                                    super.onNeutral(dialog);
                                }
                            })
                            .build();
                    materialDialog.setCancelable(false);
                    materialDialog.setCanceledOnTouchOutside(false);
                    materialDialog.show();
                }
            }, 0);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }

        return view;
    }
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void navigateToHeader(int position) {
        ActivityHome home = new ActivityHome();
        Customer debtor = customerList.get(position);
        String selectedDebtor = debtor.getCusName();
        System.out.println("selectedDebtor" + ActivityHome.SAcustomer);
        home.selectedDebtor = debtor;
        mSharedPref.setGlobalVal("PrekeyCustomer", "Y");
//        SharedPref.getInstance(getActivity()).setGlobalVal("PrekeyCusCode", debtor.getDebCodeM());
//        SharedPref.getInstance(getActivity()).setGlobalVal("PrekeyCusName", debtor.getDebNameM());
//        SharedPref.getInstance(getActivity()).setGlobalVal("preKeyRoute" ,debtor.getRouteCode());
        listener.moveNextFragment_Pre();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (IResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.all_cust:
//                Toast.makeText(getActivity(),"All Customers",Toast.LENGTH_LONG).show();
//                lvCustomers.clearTextFilter();
//                customerList = new FmDebtorDS(getActivity()).getRouteCustomers("");
//                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));
//
//            case R.id.route_wise_cust:
//                Toast.makeText(getActivity(),"Route Customers",Toast.LENGTH_LONG).show();
//                lvCustomers.clearTextFilter();
//                customerList = new FmDebtorDS(getActivity()).getAllCustomers();
//                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));
//                //prodcutDetailsDialogbox();
//
//                break;
//            default:
//        }
//    }
}
