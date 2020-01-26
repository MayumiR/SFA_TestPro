package com.bit.sfa.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.sfa.R;
import com.bit.sfa.controller.CustomerController;
import com.bit.sfa.controller.ItemController;
import com.bit.sfa.controller.ItemPriceController;
import com.bit.sfa.controller.ReasonController;
import com.bit.sfa.controller.ReferenceDetailDownloader;
import com.bit.sfa.controller.ReferenceSettingController;
import com.bit.sfa.controller.RouteController;
import com.bit.sfa.dialog.CustomProgressDialog;
import com.bit.sfa.helpers.NetworkFunctions;
import com.bit.sfa.helpers.SharedPref;
import com.bit.sfa.model.Item;
import com.bit.sfa.model.ItemPri;
import com.bit.sfa.model.Reason;
import com.bit.sfa.model.ReferenceDetail;
import com.bit.sfa.model.Customer;
import com.bit.sfa.model.RefSetting;
import com.bit.sfa.model.Route;
import com.bit.sfa.model.User;
import com.bit.sfa.utils.AESUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    EditText username, password;
    TextView txtver;
    SharedPref pref;
    User loggedUser;
    NetworkFunctions networkFunctions;

    int tap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        networkFunctions = new NetworkFunctions(this);
        pref = SharedPref.getInstance(this);
        username = (EditText) findViewById(R.id.editText1);
        password = (EditText) findViewById(R.id.editText2);
        Button login = (Button) findViewById(R.id.btnlogin);
        txtver = (TextView) findViewById(R.id.textVer);
        txtver.setText("Version " + getVersionCode());
        loggedUser = pref.getLoginUser();

        login.setOnClickListener(this);



    }

    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "0";

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin: {
  //              if(pref.isLoggedIn()){
                    Intent intent = new Intent(ActivityLogin
                            .this, ActivityHome
                            .class);
                    startActivity(intent);
                    finish();
 //               }

//               else if(!(username.getText().toString().equals("")) && !(password.getText().toString().equals(""))) {
//                    String decrepted = getMD5HashVal(password.getText().toString());
//                    Log.d("TEST", "decrypted:" + decrepted);
////                    String decrypted = "";
////                    try {
////                        decrypted = AESUtils.decrypt(password.getText().toString().trim());
////                        Log.d("TEST", "decrypted:" + decrypted);
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
//                    String logged = loggedUser.getPassword();
//                   if(!(username.getText().toString().equals(loggedUser.getUserName())) || !(decrepted.equals(loggedUser.getPassword()))){
//                       Toast.makeText(this,"Invalid Username or Password",Toast.LENGTH_LONG).show();
//
//                    }else{
//                       Toast.makeText(this,"Username and Password are correct",Toast.LENGTH_LONG).show();
//
//                       new Authenticate(username.getText().toString(), password.getText().toString(), loggedUser.getCode()).execute();
//                    }
//                }else
//                    Toast.makeText(this,"Please fill the fields",Toast.LENGTH_LONG).show();
//
//
           }
           break;

            default:
                break;
        }
    }


    //--
//    private void LoginValidation() {
//        SalRepDS ds = new SalRepDS(getApplicationContext());
//        ArrayList<SalRep> list = ds.getSaleRepDetails();
//        for (SalRep salRep : list) {
//
//            if (salRep.getREPCODE().equals(username.getText().toString().toUpperCase()) && salRep.getNAME().equals(password.getText().toString().toUpperCase())) {
//
//                StartApp();
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Invalid username or password.", Toast.LENGTH_LONG).show();
//
//            }
//        }
//    }

    private class Authenticate extends AsyncTask<String, Integer, Boolean> {
        int totalRecords=0;
        CustomProgressDialog pdialog;
        private String uname,pwd,repcode;

        public Authenticate(String uname, String pwd, String repCode){
            this.uname = uname;
            this.pwd = pwd;
            this.repcode = repCode;
            this.pdialog = new CustomProgressDialog(ActivityLogin.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog  = new CustomProgressDialog(ActivityLogin.this);
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            pDialog.setMessage("Validating...");
//            pDialog.show();
//            pdialog = new SpotsDialog(getApplicationContext());
//            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {


            int totalBytes = 0;

            try {
//                if ((username.getText().toString().equals(uname)) && (password.getText().toString().equals(pwd))
//                        ) {

/*****************Customers**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Authenticated\nDownloading Customers...");
                        }
                    });

                    String outlets = "";
                    try {
                        outlets = networkFunctions.getCustomer(repcode);
                       // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (customer details)...");
                        }
                    });

                    // Processing outlets
                    try {
                        JSONObject customersJSON = new JSONObject(outlets);
                        JSONArray customersJSONArray =customersJSON.getJSONArray("outlets");
                        ArrayList<Customer> customerList = new ArrayList<Customer>();
                        CustomerController customerController = new CustomerController(ActivityLogin.this);
                        for (int i = 0; i < customersJSONArray.length(); i++) {
                            customerList.add(Customer.parseOutlet(customersJSONArray.getJSONObject(i)));
                        }
                        customerController.createOrUpdateDebtor(customerList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
/*****************end Customers**********************************************************************/
/*****************routes*****************************************************************************/

                    //routes
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Customer downloaded\nDownloading Routes...");
                        }
                    });

                    String routes = "";
                    try {
                        routes = networkFunctions.getRoutes(repcode);
                        // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (route details)...");
                        }
                    });

                    // Processing outlets
                    try {
                        JSONObject routesJSON = new JSONObject(routes);
                        JSONArray routesJSONArray =routesJSON.getJSONArray("routes");
                        ArrayList<Route> routeList = new ArrayList<Route>();
                        RouteController routeController = new RouteController(ActivityLogin.this);
                        for (int i = 0; i < routesJSONArray.length(); i++) {
                            routeList.add(Route.parseRoute(routesJSONArray.getJSONObject(i)));
                        }
                        routeController.createOrUpdateRoute(routeList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
/*****************end routes**********************************************************************/
/*****************references**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Routes downloaded\nDownloading References...");
                        }
                    });

                    String references = "";
                    try {
                        references = networkFunctions.getReferences(repcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (reference details)...");
                        }
                    });

                    // Processing references
                    try {
                        JSONObject refJSON = new JSONObject(references);
                        JSONArray refJSONArray =refJSON.getJSONArray("references");
                        ArrayList<ReferenceDetail> refList = new ArrayList<ReferenceDetail>();
                        ReferenceDetailDownloader refController = new ReferenceDetailDownloader(ActivityLogin.this);
                        for (int i = 0; i < refJSONArray.length(); i++) {
                            refList.add(ReferenceDetail.parseRef(refJSONArray.getJSONObject(i)));
                        }
                        refController.createOrUpdateFCompanyBranch(refList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
/*****************ennd references**********************************************************************/
/*****************reference settings**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Reference detail downloaded\nDownloading reference settings...");
                        }
                    });

                    String settings = "";
                    try {
                        settings = networkFunctions.getReferenceSettings();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (setting details)...");
                        }
                    });

                    // Processing outlets
                    try {
                        JSONObject settingJSON = new JSONObject(settings);
                        JSONArray settingJSONArray =settingJSON.getJSONArray("refSettings");
                        ArrayList<RefSetting> settingList = new ArrayList<RefSetting>();
                        ReferenceSettingController settingController = new ReferenceSettingController(ActivityLogin.this);
                        for (int i = 0; i < settingJSONArray.length(); i++) {
                            settingList.add(RefSetting.parseSetting(settingJSONArray.getJSONObject(i)));
                        }
                        settingController.createOrUpdateReferenceSetting(settingList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end reference settings**********************************************************************/
                    /*****************reasons**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Reference Setting details downloaded\nDownloading reasons...");
                        }
                    });

                    String reasons = "";
                    try {
                        reasons = networkFunctions.getReasons();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (reasons)...");
                        }
                    });

                    // Processing reasons
                    try {
                        JSONObject reasonJSON = new JSONObject(reasons);
                        JSONArray reasonJSONArray =reasonJSON.getJSONArray("reasons");
                        ArrayList<Reason> reasonList = new ArrayList<Reason>();
                        ReasonController reasonController = new ReasonController(ActivityLogin.this);
                        for (int i = 0; i < reasonJSONArray.length(); i++) {
                            reasonList.add(Reason.parseReason(reasonJSONArray.getJSONObject(i)));
                        }
                        reasonController.createOrUpdateReason(reasonList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end reasons**********************************************************************/
                    /*****************reasons**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Reasons downloaded\nDownloading items...");
                        }
                    });

                    String items = "";
                    try {
                        items = networkFunctions.getItems();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (items)...");
                        }
                    });

                    // Processing items
                    try {
                        JSONObject itemJSON = new JSONObject(items);
                        JSONArray itemJSONArray =itemJSON.getJSONArray("items");
                        ArrayList<Item> itemList = new ArrayList<Item>();
                        ItemController itemController = new ItemController(ActivityLogin.this);
                        for (int i = 0; i < itemJSONArray.length(); i++) {
                            itemList.add(Item.parseItem(itemJSONArray.getJSONObject(i)));
                        }
                        itemController.InsertItems(itemList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end items**********************************************************************/
                    /*****************prices**********************************************************************/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Items downloaded\nDownloading prices...");
                        }
                    });

                    String prices = "";
                    try {
                        prices = networkFunctions.getPrices();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (prices)...");
                        }
                    });

                    // Processing reasons
                    try {
                        JSONObject priceJSON = new JSONObject(prices);
                        JSONArray priceJSONArray =priceJSON.getJSONArray("itemprices");
                        ArrayList<ItemPri> priceList = new ArrayList<ItemPri>();
                        ItemPriceController priceController = new ItemPriceController(ActivityLogin.this);
                        for (int i = 0; i < priceJSONArray.length(); i++) {
                            priceList.add(ItemPri.parsePrices(priceJSONArray.getJSONObject(i)));
                        }
                        priceController.createOrUpdateItemPri(priceList);
                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end prices**********************************************************************/

                    return true;
//        } else {
//            //errors.add("Please enter correct username and password");
//            return false;
//        }
    } catch (IOException e) {
        e.printStackTrace();
       // errors.add("Unable to reach the server.");

//                ErrorUtil.logException(LoginActivity.this, "LoginActivity -> Authenticate -> doInBackground # Login",
//                        e, null, BugReport.SEVERITY_LOW);

        return false;
    } catch (JSONException e) {
        e.printStackTrace();
       // errors.add("Received an invalid response from the server.");

//                ErrorUtil.logException(LoginActivity.this, "LoginActivity -> Authenticate -> doInBackground # Login",
//                        e, loginResponse, BugReport.SEVERITY_HIGH);

        return false;
    } catch (NumberFormatException e) {
        e.printStackTrace();
        return false;
    }
}

    @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
                pref.setLoginStatus(true);
                Intent intent = new Intent(ActivityLogin
                        .this, ActivityHome
                        .class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ActivityLogin.this,"Invalid Response from server",Toast.LENGTH_LONG);
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }
    }

    public static String getMD5HashVal(String strToBeEncrypted) {
        String encryptedString = null;
        byte[] bytesToBeEncrypted;

        try {
            // convert string to bytes using a encoding scheme
            bytesToBeEncrypted = strToBeEncrypted.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theDigest = md.digest(bytesToBeEncrypted);
            // convert each byte to a hexadecimal digit
            Formatter formatter = new Formatter();
            for (byte b : theDigest) {
                formatter.format("%02x", b);
            }
            encryptedString = formatter.toString().toLowerCase();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

}
