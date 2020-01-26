package com.bit.sfa.view;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
//import android.support.v7.app.AlertDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.afollestad.materialdialogs.MaterialDialog;
import com.bit.sfa.R;
import com.bit.sfa.dialog.CustomProgressDialog;
import com.bit.sfa.helpers.NetworkFunctions;
import com.bit.sfa.helpers.SharedPref;

import com.bit.sfa.utils.NetworkUtil;
import com.bit.sfa.helpers.DatabaseHelper;
import com.bit.sfa.model.User;
import com.bit.sfa.utils.GetMacAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ActivitySplash extends AppCompatActivity{

    private ImageView logo;
    private static int SPLASH_TIME_OUT = 4000;
    DatabaseHelper db;
    private SharedPref pref;
    private NetworkFunctions networkFunctions;
    private String TAG = "ActivitySplash";
    private TextView tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(ActivitySplash.this);

        View v = layoutInflater.inflate(R.layout.activity_splash, null);
        setContentView(v);

        db=new DatabaseHelper(getApplicationContext());
        SQLiteDatabase SFA;
        SFA = db.getWritableDatabase();
        pref = SharedPref.getInstance(ActivitySplash.this);
        db.onUpgrade(SFA, 1, 2);

        logo = (ImageView)findViewById(R.id.logo);
        tryAgain = (TextView) findViewById(R.id.tryAgain);
        networkFunctions = new NetworkFunctions(ActivitySplash.this);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_up);
        logo.startAnimation(animation1);

        final boolean connectionStatus = NetworkUtil.isNetworkAvailable(ActivitySplash.this);

        goToLogin();


//        GetMacAddress macAddress = new GetMacAddress();
//        if (android.os.Build.VERSION.SDK_INT < 23) {
//            pref.setMacAddress(macAddress.getMacAddress(getApplicationContext()).toString().replace(":", ""));
//        } else {
//            pref.setMacAddress(macAddress.getMacAddressNewApi(getApplicationContext()).toString().replace(":", ""));
//        }
//
//
//        if(pref.getLoginUser()==null) {
//
//            if (connectionStatus) {
//
//                new Handler().postDelayed(new Runnable() {
//
//                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                    @Override
//                    public void run() {
//
//                       // new Validate(pref.getMacAddress().trim()).execute();
//
//                    }
//                }, SPLASH_TIME_OUT);
//            tryAgain.setVisibility(View.INVISIBLE);
//            } else {
//
//                Snackbar snackbar = Snackbar.make(v, R.string.txt_msg, Snackbar.LENGTH_LONG);
//                View snackbarLayout = snackbar.getView();
//                snackbarLayout.setBackgroundColor(Color.RED);
//                TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
//                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signal_wifi_off_black_24dp, 0, 0, 0);
//                textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.body_size));
//                textView.setTextColor(Color.WHITE);
//                snackbar.show();
//                tryAgain.setVisibility(View.VISIBLE);
//               // reCallActivity();
//
//            }
//        }else{
//            if(pref.isLoggedIn()){
//                goToHome();
//            }else {
//                goToLogin();
//            }
//        }

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetworkAvailable(ActivitySplash.this)) {
                new Validate(pref.getMacAddress().trim()).execute();
                } else {

                    Snackbar snackbar = Snackbar.make(v, R.string.txt_msg, Snackbar.LENGTH_LONG);
                    View snackbarLayout = snackbar.getView();
                    snackbarLayout.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signal_wifi_off_black_24dp, 0, 0, 0);
                    textView.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.body_size));
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                    tryAgain.setVisibility(View.VISIBLE);

                }
            }
        });

}

//
    public void reCallActivity(){
        Intent mainActivity = new Intent(ActivitySplash.this, ActivitySplash.class);
        startActivity(mainActivity);
    }
//already checked macId
    public void goToLogin(){
        Intent mainActivity = new Intent(ActivitySplash.this, ActivityLogin.class);
        startActivity(mainActivity);
       // finish();
    }
    public void goToHome(){
        Intent mainActivity = new Intent(ActivitySplash.this, ActivityHome.class);
        startActivity(mainActivity);
        finish();
    }


    private class Validate extends AsyncTask<String, Integer, Boolean> {
        int totalRecords=0;
        CustomProgressDialog pdialog;
        private String macId;

        public Validate(String macId){
            this.macId = macId;
            this.pdialog = new CustomProgressDialog(ActivitySplash.this);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Validating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

           try {
                int recordCount = 0;
                int totalBytes  = 0;
                String validateResponse = null;
                JSONObject validateJSON;
                try {
                    validateResponse = networkFunctions.validate(macId);
                    Log.d("validateResponse",validateResponse);
                    validateJSON = new JSONObject(validateResponse);


                if (validateJSON != null && validateJSON.getBoolean("result")) {
                    pref = SharedPref.getInstance(ActivitySplash.this);
                    //dbHandler.clearTables();
                    // Login successful. Proceed to download other items
                    User user = User.parseUser(validateJSON);
                    //networkFunctions.setUser(user);
                    pref.storeLoginUser(user);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Authenticated...");
                        }
                    });

                    return true;
                }else{
                    Toast.makeText(ActivitySplash.this,"Invalid response",Toast.LENGTH_SHORT).show();
                    return false;
                }

                } catch (IOException e) {
                    Log.e("networkFunctions ->","IOException -> "+e.toString());
                    throw e;
                } catch (JSONException e) {
                    Log.e("networkFunctions ->","JSONException -> "+e.toString());
                    throw e;
                }


           } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            pDialog.setMessage("Prefetching data..." + progress[0] + "/" + totalRecords);
//
//        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pdialog.isShowing())
                pdialog.cancel();
           // pdialog.cancel();
            if(result){
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                pref.setValidateStatus(true);
                tryAgain.setVisibility(View.INVISIBLE);
                //set user details to shared prefferences
                //Intent mainActivity = new Intent(ActivitySplash.this, SettingsActivity.class);
                Intent loginActivity = new Intent(ActivitySplash.this, ActivityLogin.class);
                startActivity(loginActivity);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Invalid Mac Id", Toast.LENGTH_LONG).show();
                tryAgain.setVisibility(View.VISIBLE);


            }
        }
    }
}
