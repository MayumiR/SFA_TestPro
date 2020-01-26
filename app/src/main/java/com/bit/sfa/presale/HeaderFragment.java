package com.bit.sfa.presale;


import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.sfa.controller.CustomerController;
import com.bit.sfa.controller.OrderController;
import com.bit.sfa.controller.RouteController;
import com.bit.sfa.helpers.SharedPref;
import com.bit.sfa.settings.GPSTracker;
import com.bit.sfa.utils.LocationProvider;
import com.bit.sfa.view.ActivityHome;
import com.bit.sfa.model.Order;
import com.bit.sfa.R;
import com.bit.sfa.settings.ReferenceNum;
//import com.bit.sfa.Settings.SharedPreferencesClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import static com.bit.sfa.Settings.SharedPreferencesClass.getLocalSharedPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeaderFragment extends Fragment {


    View view;
    private FloatingActionButton next;
    public static EditText ordno, date, mNo, deldate, remarks;
    public String LOG_TAG = "HeaderFragment";
    public TextView route, costcenter;
    private TextView cusName;
    private LocationProvider locationProvider;
    private Location finalLocation;
    MyReceiver r;
    //SharedPreferencesClass localSP;


    public HeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_promo_sale_header, container, false);

        next = (FloatingActionButton) view.findViewById(R.id.fab);

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy"); //change this
        String formattedDate = simpleDateFormat.format(d);
        ActivityHome home = new ActivityHome();
        ReferenceNum referenceNum = new ReferenceNum(getActivity());
     //   localSP = new SharedPreferencesClass();

        ordno = (EditText) view.findViewById(R.id.editTextOrdno);
        date = (EditText) view.findViewById(R.id.editTextDate);
        mNo        = (EditText) view.findViewById(R.id.editTextManualNo);
        deldate    = (EditText) view.findViewById(R.id.editTextdelDate);
        remarks    = (EditText) view.findViewById(R.id.editTextRemarks);
        costcenter = (TextView) view.findViewById(R.id.editTextcostCenter);
        route = (TextView) view.findViewById(R.id.editTextRoute);
        cusName = (TextView) view.findViewById(R.id.textViewCustomer);


     //   cusName.setText(home.SAcustomer);
       // route.setText(StaticData.Route);
        //rashmi
//        route.setText(""+home.SAroute);
//        date.setText(formattedDate);
//        ordno.setText(referenceNum.getCurrentRefNo(getResources().getString(R.string.NumVal)));


        deldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MDatePicker newFragment = new MDatePicker();
                newFragment.show(getFragmentManager(), "date picker");
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //validate tabs
                if (SalesManagementFragment.isheader == true) {
                    SalesManagementFragment.viewPager.setCurrentItem(2);
                }

            }
        });
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

    public static class MDatePicker extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int i, int i1, int i2) {

                        deldate.setText(view.getDayOfMonth() + "-" + (view.getMonth() + 1) + "-" + view.getYear());
                    }

                };
    }
    /*-*-*-*-*-*-rashmi 2018/08/07*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    /*-*-*-*-*-*Rashmi 2018-8-17-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void SaveSalesHeader() {
        GPSTracker tracker = new GPSTracker(getActivity());
        if (ordno.getText().length() > 0) {

            //mSharedPref.setGlobalVal("PrekeyCostPos", String.valueOf(spnCostCenter.getSelectedItemPosition()));

            ActivityHome activity = (ActivityHome) getActivity();
            RouteController routeDS = new  RouteController(getActivity());

            //LocationsDS locDS = new LocationsDS(activity);
            Order hed =new Order();
            String AppVersion = "";

            try{
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                AppVersion = pInfo.versionName;

            }catch (Exception e){
                e.printStackTrace();
            }

            if(activity.selectedOrdHed !=null)
                hed =activity.selectedOrdHed;//set already enter values objects

            hed.setORDHED_REFNO(ordno.getText().toString());
            hed.setORDHED_CUS_CODE(SharedPref.getInstance(getActivity()).getGlobalVal("PrekeyCusCode"));
            hed.setORDHED_ADD_DATE(date.getText().toString());
            hed.setORDHED_DELV_DATE(deldate.getText().toString());
            hed.setORDHED_ROUTE_CODE(new CustomerController(getActivity()).getSelectedCustomerByCode(SharedPref.getInstance(getActivity()).getGlobalVal("PrekeyCusCode")).getCusRoute());
            hed.setORDHED_MANU_REF(mNo.getText().toString());
            hed.setORDHED_REMARKS(remarks.getText().toString());
            hed.setORDHED_IS_ACTIVE("1");
            hed.setORDHED_TXN_DATE(""+date.getText().toString());
            hed.setORDHED_START_TIME(""+currentTime().split(" ")[1]);
            hed.setORDHED_REPCODE(""+SharedPref.getInstance(getActivity()).getLoginUser().getCode());
            hed.setORDHED_LONGITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Longitude"));
            hed.setORDHED_LATITUDE(SharedPref.getInstance(getActivity()).getGlobalVal("Latitude"));
            hed.setORDHED_LONGITUDE(""+tracker.getLongitude());
            hed.setORDHED_LATITUDE(""+tracker.getLatitude());

            activity.selectedOrdHed = hed;//new updated object (new data + already enter data)

     //       SharedPreferencesClass.setLocalSharedPreference(activity, "SO_Start_Time",currentTime());

            ArrayList<Order> ordHedList=new ArrayList<Order>();
            OrderController ordHedDS =new OrderController(getActivity());
            //head
            ordHedList.add(activity.selectedOrdHed);
            ordHedDS.createOrUpdateOrdHed(ordHedList);
        }
    }
    /*-*Rashmi 2018-08-17-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void mRefreshHeader() {

        if (SharedPref.getInstance(getActivity()).getGlobalVal("PrekeyCustomer").equals("Y")) {
            ActivityHome home = new ActivityHome();
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

                                SharedPref.getInstance(getActivity()).setGlobalVal("Longitude", String.valueOf(finalLocation.getLongitude()));
                                SharedPref.getInstance(getActivity()).setGlobalVal("Latitude", String.valueOf(finalLocation.getLatitude()));
                                System.currentTimeMillis();
                                SaveSalesHeader();

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
//            issueList = new FmisshedDS(getActivity()).getIssuesByDebCode(new SharedPref(getActivity()).getGlobalVal("PrekeyCusCode"));
//
//            List<String> issues = new ArrayList<String>();
//            /* Merge group code with group name to the list */
//            issues.add("-SELECT REFNO-");
//            for (Fmisshed iss : issueList) {
//                issues.add(iss.getRefNo());
//            }
//
//            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_spinner_item, issues);
//            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spnIssueRefNos.setAdapter(dataAdapter3);

            date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
          //  route.setText(SharedPref.getInstance(getActivity()).getLoginUser().getRoute()+" - "+new RouteController(getActivity()).getRouteNameByCode(SharedPref.getInstance(getActivity()).getLoginUser().getRoute()));
            deldate.setEnabled(true);
            remarks.setEnabled(true);
            mNo.setEnabled(true);
            cusName.setText(SharedPref.getInstance(getActivity()).getGlobalVal("PrekeyCusName"));
            ordno.setText(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));
           // String debCode= new SharedPref(getActivity()).getGlobalVal("PrekeyCusCode");

            if (home.selectedOrdHed != null) {
                if (home.selectedDebtor == null)
                   // home.selectedDebtor = new FmDebtorDS(getActivity()).getSelectedCustomerByCode(home.selectedOrdHed.getFORDHED_DEB_CODE());

                cusName.setText(home.selectedDebtor.getCusName());
                ordno.setText(home.selectedOrdHed.getORDHED_REFNO());
                deldate.setText(home.selectedOrdHed.getORDHED_DELV_DATE());
                mNo.setText(home.selectedOrdHed.getORDHED_MANU_REF());
                remarks.setText(home.selectedOrdHed.getORDHED_REMARKS());

            } else {

                ordno.setText(new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));
                deldate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                SaveSalesHeader();
            }

        } else {
            Toast.makeText(getActivity(), "Select a customer to continue...", Toast.LENGTH_SHORT).show();
            remarks.setEnabled(false);
            mNo.setEnabled(false);
            deldate.setEnabled(false);
        }

    }
    /*-*-*-*-Rashmi 2018-08-17*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    /*-*-*-*-*-*-Rashmi 2018-08-17*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onResume() {
        super.onResume();
        r = new HeaderFragment.MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_HEADER"));
    }


    /*-*-*-*-*-Rashmi 2018-08-17*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            HeaderFragment.this.mRefreshHeader();
        }
    }
    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
}
