package com.bit.sfa.helpers;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.bit.sfa.expense.ExpenseMain;
import com.bit.sfa.model.CustomNameValuePair;
import com.bit.sfa.model.DayExpHed;
import com.bit.sfa.model.DayNPrdHed;
import com.bit.sfa.model.NewCustomer;
import com.bit.sfa.model.Order;
import com.bit.sfa.model.User;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;


/**
 * Created by Rashmi on 1/11/2018.
 * Handles network based functions.
 */
public class NetworkFunctions {

    private final String LOG_TAG = NetworkFunctions.class.getSimpleName();

    private final SharedPref pref;

    /**
     * The base URL to POST/GET the parameters to. The function names will be appended to this
     */
    private String baseURL;

    private User user;

    public NetworkFunctions(Context context) {
         pref = SharedPref.getInstance(context);
        String domain = pref.getBaseURL();
        Log.wtf("baseURL>>>>>>>>>",domain);
        baseURL = domain + "/SFAnew/android_service/";
        //baseURL = domain + "/android_service/";
        user = pref.getLoginUser();
    }

    public void setUser(User user) {
        this.user = user;
    }
public String validate(String macId) throws IOException {

    List<CustomNameValuePair> params = new ArrayList<>();
    params.add(new CustomNameValuePair("mac_id", macId));
    Log.d(LOG_TAG, "Validating : " + baseURL + "login" + params);

    return postToServer(baseURL + "login.php", params);
}
    /**
     * This function will POST repCode will return a the response JSON
     * from the server.
     *
     * @param repCode The string of the logged user's code
     * @return The response as a String
     * @throws IOException Throws if unable to reach the server
     */

    public String getCustomer(String repCode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();
        params.add(new CustomNameValuePair("repcode", repCode));

        Log.d(LOG_TAG, "Getting customer : " + baseURL + "customer" + params);

        return postToServer(baseURL + "customer.php", params);
    }

    public String getRoutes(String repCode) throws IOException {

        List<CustomNameValuePair> params = new ArrayList<>();
        params.add(new CustomNameValuePair("repcode", repCode));

        Log.d(LOG_TAG, "Getting Routes");

        return postToServer(baseURL + "route.php", params);
    }
//
    public String getReferences(String repCode) throws IOException {
        List<CustomNameValuePair> params = new ArrayList<>();
        params.add(new CustomNameValuePair("repcode", repCode));

        Log.d(LOG_TAG, "Getting References");

        return postToServer(baseURL + "reference.php", params);
    }

    public String getReferenceSettings() throws IOException {

        Log.d(LOG_TAG, "Getting Reference Settings");

        return getFromServer(baseURL + "refsetting.php", null);
    }

    public String getReasons() throws IOException {

        Log.d(LOG_TAG, "Getting Reasons");

        return getFromServer(baseURL + "reason.php", null);
    }

    public String getItems() throws IOException {

        Log.d(LOG_TAG, "Getting Items");

        return getFromServer(baseURL + "item.php", null);
    }

    public String getPrices() throws IOException {

        Log.d(LOG_TAG, "Getting ItemPrices");

        return getFromServer(baseURL + "itemprices.php", null);
    }
//
//    public String syncAttendanceDetails(Attendance attendance) throws IOException {
//        List<CustomNameValuePair> params = new ArrayList<>();
//        params.add(new CustomNameValuePair("web_user_id", String.valueOf(user.getWebUserId())));
//        params.add(new CustomNameValuePair("user_position_id", String.valueOf(user.getLocationId())));
//        params.add(new CustomNameValuePair("log_status", String.valueOf(attendance.getLogStatus())));
//        params.add(new CustomNameValuePair("time", String.valueOf(attendance.getLogTime())));
//        params.add(new CustomNameValuePair("millage", String.valueOf(attendance.getMileage())));
//        params.add(new CustomNameValuePair("latitude", String.valueOf(attendance.getLatitude())));
//        params.add(new CustomNameValuePair("longitude", String.valueOf(attendance.getLongitude())));
//        params.add(new CustomNameValuePair("location_string", (attendance.getLoc() == null || attendance.getLoc().isEmpty() ? "" : attendance.getLoc())));
//        params.add(new CustomNameValuePair("location_accuracy", String.valueOf(attendance.getLocationAccuracy())));
//        params.add(new CustomNameValuePair("location_type", String.valueOf(attendance.getLocationType())));
//
//        return postToServer(baseURL + "send_attendance", params);
//    }
//
//    public String syncCreatedOutlet(Outlet outlet) throws IOException {
//        List<CustomNameValuePair> params = new ArrayList<>();
//        params.add(new CustomNameValuePair("position_id", String.valueOf(user.getLocationId())));
//        params.add(new CustomNameValuePair("jsonString", outlet.getCreatedOutletAsJSON(user.getWebUserId()).toString()));
//        params.add(new CustomNameValuePair("new_outlet", String.valueOf(outlet.getOutletId() <= 0)));
//        //params.add(new CustomNameValuePair("new_outlet", "\"" + String.valueOf(outlet.getOutletId() <= 0) + "\""));
//
//        Log.d(LOG_TAG, "POSTing created outlet");
//
//        return postToServer(baseURL + "save_new_outlet", params);
//    }
//
//    /**
//     * Returns the selected monthly report String
//     *
//     * @param time The required time in the format of yyyy-MM-dd
//     * @return The String of the response JSONObject
//     * @throws IOException Throws when unable to reach the server
//     */
//    public String getMonthlyReport(String time) throws IOException {
//
//        List<CustomNameValuePair> params = new ArrayList<>();
//        params.add(new CustomNameValuePair("position_id", String.valueOf(user.getLocationId())));
//        params.add(new CustomNameValuePair("date", time));
//        params.add(new CustomNameValuePair("user_id", String.valueOf(user.getWebUserId())));
//
//        Log.d(LOG_TAG, "Getting monthly report");
//
//        return postToServer(baseURL + "get_monthly_statement", params);
//    }
//
    public String syncOrder(Order order) throws IOException, JSONException {

        List<CustomNameValuePair> params = new ArrayList<>();

        String jsonString = order.getOrderAsJSON().toString();
        params.add(new CustomNameValuePair("jsonString", jsonString));
        params.add(new CustomNameValuePair("repcode", pref.getLoginUser().getCode()));

        Log.d(LOG_TAG, "Syncing order JSON : " + jsonString);

        return postToServer(baseURL + "insert_order.php", params);
    }
    public String syncOutlet(NewCustomer customer) throws IOException, JSONException {

        List<CustomNameValuePair> params = new ArrayList<>();

        String jsonString = customer.getOutletAsJSON().toString();
        params.add(new CustomNameValuePair("jsonString", jsonString));
        params.add(new CustomNameValuePair("repcode", pref.getLoginUser().getCode()));

        Log.d(LOG_TAG, "Syncing outlet JSON : " + jsonString);

        return postToServer(baseURL + "insert_outlet.php", params);
    }
    public String syncExpense(DayExpHed expense) throws IOException, JSONException {

        List<CustomNameValuePair> params = new ArrayList<>();

        String jsonString = expense.getExpenseAsJSON().toString();
        params.add(new CustomNameValuePair("jsonString", jsonString));
        params.add(new CustomNameValuePair("repcode", pref.getLoginUser().getCode()));

        Log.d(LOG_TAG, "Syncing expense JSON : " + jsonString);

        return postToServer(baseURL + "insert_expense.php", params);
    }
    public String syncNonPrd(DayNPrdHed nonprd) throws IOException, JSONException {

        List<CustomNameValuePair> params = new ArrayList<>();

        String jsonString = nonprd.getNonprdAsJSON().toString();
        params.add(new CustomNameValuePair("jsonString", jsonString));
        params.add(new CustomNameValuePair("repcode", pref.getLoginUser().getCode()));

        Log.d(LOG_TAG, "Syncing nonproductive JSON : " + jsonString);

        return postToServer(baseURL + "insert_nonproductive.php", params);
    }
    /**
     * This function POSTs params to server and gets the response.
     *
     * @param url    The URL to POST to
     * @param params The list of {@link CustomNameValuePair} of params to POST
     * @return The response from the server as a String
     * @throws IOException Throws if unable to connect to the server
     */
    private String postToServer(String url, List<CustomNameValuePair> params) throws IOException {

        String response = "";

        URL postURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) postURL.openConnection();
        con.setConnectTimeout(60 * 1000);
        con.setReadTimeout(30 * 1000);
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(generatePOSTParams(params));
        writer.flush();
        writer.close();
        os.close();

        con.connect();

        int status = con.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                response = sb.toString();
                Log.d(LOG_TAG, "Server Response : \n" + response);
        }

        return response;
    }
//    private String postToServer(String url, List<CustomNameValuePair> params) throws IOException {
//
//        String response = "";
//
//        URL postURL = new URL(url);
//        HttpsURLConnection con = (HttpsURLConnection) postURL.openConnection();
//        // Create the SSL connection
//        SSLContext sc = null;
//        try {
//            sc = SSLContext.getInstance("TLS");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            sc.init(null, null, new java.security.SecureRandom());
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        con.setSSLSocketFactory(sc.getSocketFactory());
//
//        // Use this if you need SSL authentication
//       // String userpass = user + ":" + password;
//       // String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
//       // con.setRequestProperty("Authorization", basicAuth);
//        con.setConnectTimeout(60 * 1000);
//        con.setReadTimeout(30 * 1000);
//        con.setRequestMethod("POST");
//        con.setDoInput(true);
//        con.setDoOutput(true);
//
//        OutputStream os = con.getOutputStream();
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//        writer.write(generatePOSTParams(params));
//        writer.flush();
//        writer.close();
//        os.close();
//
//        con.connect();
//
//        int status = con.getResponseCode();
//        switch (status) {
//            case 200:
//            case 201:
//                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line).append("\n");
//                }
//                br.close();
//
//                response = sb.toString();
//                Log.d(LOG_TAG, "Server Response : \n" + response);
//        }
//
//        return response;
//    }
//
/**
     * This function GETs params to server and returns the response.
     *
     * @param url    The URL to GET from
     * @param params The List<CustomNameValuePair></> of params to encode as GET parameters
     * @return The response string from the server
     * @throws IOException Throws if unable to connect to the server
     */
    private String getFromServer(String url, List<CustomNameValuePair> params) throws IOException {

        String response = "";

        URL postURL = new URL(url + generateGETParams(params));
//        Log.d(LOG_TAG, postURL.toString());
        HttpURLConnection con = (HttpURLConnection) postURL.openConnection();
        con.setConnectTimeout(20 * 1000);
        con.setReadTimeout(30 * 1000);
        con.setRequestMethod("GET");
        con.setDoInput(true);
//        con.setDoOutput(true);

        con.connect();

        int status = con.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                response = sb.toString();
                Log.d(LOG_TAG, "Server Response : \n" + response);
        }

        return response;
    }

    /**
     * This function will return the params as a queried String to POST to the server
     *
     * @param params The parameters to be POSTed
     * @return The formatted String
     * @throws UnsupportedEncodingException
     */
    private String generatePOSTParams(List<CustomNameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (CustomNameValuePair pair : params) {
            if (pair != null) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }
        }

        Log.d(LOG_TAG, "Server REQUEST : " + result.toString());
        Log.d(LOG_TAG, "Upload size : " + result.toString().getBytes().length + " bytes");

        return result.toString();
    }

    /**
     * This function will return the params as a queried String to GET from the server
     *
     * @param params The parameters to encode as GET params
     * @return The formatted String
     */
    private String generateGETParams(List<CustomNameValuePair> params) {

        StringBuilder result = new StringBuilder().append("");
        boolean first = true;

        if (params != null) {
            for (CustomNameValuePair pair : params) {
                if (pair != null) {
                    if (first) {
                        first = false;
                        result.append("?");
                    } else
                        result.append("&");

                    result.append(pair.getName());
                    result.append("=");
                    result.append(pair.getValue());
                }
            }
        }

        Log.d(LOG_TAG, "Upload size : " + result.toString().getBytes().length + " bytes");

        return result.toString();
    }

/*
* URL url = new URL(urlStr);
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

    // Create the SSL connection
    SSLContext sc;
    sc = SSLContext.getInstance("TLS");
    sc.init(null, null, new java.security.SecureRandom());
    conn.setSSLSocketFactory(sc.getSocketFactory());

    // Use this if you need SSL authentication
    String userpass = user + ":" + password;
    String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
    conn.setRequestProperty("Authorization", basicAuth);

    // set Timeout and method
    conn.setReadTimeout(7000);
    conn.setConnectTimeout(7000);
    conn.setRequestMethod("POST");
    conn.setDoInput(true);

    // Add any data you wish to post here

    conn.connect();
    return conn.getInputStream();
* */


}
