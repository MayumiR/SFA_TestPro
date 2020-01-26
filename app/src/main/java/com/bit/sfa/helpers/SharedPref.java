package com.bit.sfa.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.bit.sfa.model.User;

import java.util.List;

/**
 * Functions to access shared preferences.
 */
public class SharedPref {

    private static final String LOG_TAG = SharedPref.class.getSimpleName();

//    private Context context;
    private SharedPreferences sharedPref;

    private static SharedPref pref;

    public SharedPref() {
    }

    public static SharedPref getInstance(Context context) {
        if(pref == null) {
            pref = new SharedPref(context);
        }

        return pref;
    }

    private SharedPref(Context context) {
//        this.context = context;
        sharedPref = context.getSharedPreferences("app_data", Context.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("login_status", status).apply();
    }
    public void setValidateStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("validate_status", status).apply();
    }

    public boolean isLoggedIn() {
        return sharedPref.getBoolean("login_status", false);
    }
    public boolean isValidate() {
        return sharedPref.getBoolean("validate_status", false);
    }
    public void storeLoginUser(User user) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_id", user.getCode());
        editor.putString("user_name", user.getName());
        editor.putString("user_username", user.getUserName());
        editor.putString("user_password", user.getPassword());
        editor.putString("user_target", user.getTarget());
        editor.putString("user_status", user.getStatus());
        editor.putString("user_mobile", user.getMobile());
        editor.putString("user_address", user.getAddress());
        editor.putString("user_prefix", user.getPrefix());

        editor.apply();
    }

    public User getLoginUser() {

        User user = new User();
        user.setCode(sharedPref.getString("user_id", ""));
        user.setName(sharedPref.getString("user_name", ""));
        user.setUserName(sharedPref.getString("user_username", ""));
        user.setPassword(sharedPref.getString("user_password", ""));
        user.setTarget(sharedPref.getString("user_target", ""));
        user.setStatus(sharedPref.getString("user_status", ""));
        user.setMobile(sharedPref.getString("user_mobile", ""));
        user.setAddress(sharedPref.getString("user_address", ""));
        user.setPrefix(sharedPref.getString("user_prefix", ""));


        if (user.getCode().equals("")) {
            return null;
        } else {
            return user;
        }
    }

    public void setGlobalVal(String mKey, String mValue) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mKey, mValue);
        editor.commit();
    }

    public String getGlobalVal(String mKey) {
        return sharedPref.getString(mKey, "***");
    }


    public long generateOrderId() {
        long time = System.currentTimeMillis();
        Log.wtf("ID", String.valueOf(sharedPref.getInt("user_location_id", 0)) + String.valueOf(time));
        long order_id = Long.parseLong(String.valueOf(sharedPref.getInt("user_location_id", 0)) + String.valueOf(time));
        return (order_id < 0 ? -order_id : order_id);
    }





//    public int startDay() {
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean("day_status", true);
//
//        int session = sharedPref.getInt("local_session", 0) + 1;
//        editor.putInt("local_session", session);
//
//        long timeOut = TimeUtils.getDayEndTime(System.currentTimeMillis());
//
//        Log.d(LOG_TAG, "Setting timeout time at " + TimeUtils.formatDateTime(timeOut));
//
//        editor.putLong("login_timeout", timeOut);
//
//        editor.apply();
//
//        return session;
//    }
public  void  setMacAddress(String MacAddress){
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString("MAC_Address", MacAddress);
    editor.apply();
}

    public  String getMacAddress(){
        return  sharedPref.getString("MAC_Address","");
    }

    public long getLoginTimeout() {
        return sharedPref.getLong("login_timeout", 0);
    }

    public void endDay() {
        Log.d(LOG_TAG, "Ending Day");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("day_status", false);
     //   storePreviousRoute(getSelectedRoute());
        editor.putInt("selected_route_id", 0);
        editor.putString("selected_route_name", null);
        editor.putFloat("selected_route_fixed_target", 0);
        editor.putFloat("selected_route_selected_target", 0);
        editor.apply();
    }

    public int getLocalSessionId() {
        return sharedPref.getInt("local_session", 0);
    }

//    public void setDayStatus(boolean isDayStarted) {
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putBoolean("day_status", isDayStarted);
//        editor.apply();
//    }

    public boolean isDayStarted() {
        return sharedPref.getBoolean("day_status", false);
    }

    public void setTransferToDealerList(boolean flag) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("transfer_to_dlist",flag);
        editor.apply();
    }

    public boolean getTransferToDealerList(boolean inverse) {

        boolean result = sharedPref.getBoolean("transfer_to_dlist", false);

        if(inverse){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("transfer_to_dlist", false);
            editor.apply();
        }

        return result;
    }

    public boolean validForLogin(int outletId) {
        String key = "outlet_changed_".concat(String.valueOf(outletId));
        int updatedCount = sharedPref.getInt(key, 0);

        return updatedCount <= 2;
    }


    //</editor-fold>

    public void setPointingLocationIndex(int index) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("pointing_location", index);
        editor.apply();
    }

    public int getPointingLocationIndex() {
        return sharedPref.getInt("pointing_location", 0);
    }

    public void setBaseURL(String baseURL) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("baseURL", baseURL);
        editor.apply();
    }

    public String getBaseURL() {
        //return sharedPref.getString("baseURL", "https://19920502.000webhostapp.com");
        //return sharedPref.getString("baseURL", "http://192.168.43.62");
        return sharedPref.getString("baseURL", "http://192.168.8.114");
        //return sharedPref.getString("baseURL", "http://192.168.8.152");

    }

    public void setCurrentMillage(double millage){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("millage", (float) millage);
        editor.apply();
    }

    public double getPrevoiusMillage(){
        return sharedPref.getFloat("millage", 0);
    }



    public void setVersionName(String versionName) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("app_version_name", versionName).commit();
    }

    public String getVersionName(){
        return sharedPref.getString("app_version_name", "0.0.0");
    }
}
