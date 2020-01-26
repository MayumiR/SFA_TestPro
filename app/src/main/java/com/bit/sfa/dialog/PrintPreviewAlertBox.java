package com.bit.sfa.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bit.sfa.R;
import com.bit.sfa.adapter.PrintVanSaleItemAdapter;
import com.bit.sfa.controller.CustomerController;
import com.bit.sfa.controller.OrderController;
import com.bit.sfa.controller.OrderDetailController;
import com.bit.sfa.helpers.SharedPref;
import com.bit.sfa.model.Customer;
import com.bit.sfa.model.Order;
import com.bit.sfa.model.OrderDetail;
import com.bit.sfa.model.User;
import com.bit.sfa.printutils.Block;
import com.bit.sfa.printutils.Board;
import com.bit.sfa.printutils.Table;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PrintPreviewAlertBox {

    public static final String SETTINGS = "SETTINGS";
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    public static final String SETTING = "SETTINGS";
    User salRep;
    Customer debtor;
    Order vansalprintpre;
    String Fdealadd3 = "";
    String Fdealmob = "";
    String printLineSeperator = "____________________________________________";
    String printLineSeperatorNew = "--------------------------------------------";
    String printSpaceName = "                    ";
    String printSpaceQty = "     ";
    String Heading_a = "";
    String Heading_bmh = "";
    String Heading_b = "";
    String Heading_c = "";
    String Heading_d = "";
    String Heading_e = "";
    String buttomRaw = "";
    String BILL;
    LinearLayout lnComp, lnCus, lnTerm;
    Dialog dialogProgress;
    ListView lvItemDetails,lvReturnDetails;
    String PRefno = "";
    String printMainInvDiscount, printMainInvDiscountVal, PrintNetTotalValuePrintVal, printCaseQuantity,
            printPicesQuantity, TotalInvoiceDiscount;
    int countCountInv;
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    String PRINTER_MAC_ID;
    Context context;
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            try {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    System.out.println("***" + device.getName() + " : " + device.getAddress());

                    if (device.getAddress().equalsIgnoreCase(PRINTER_MAC_ID)) {
                        mBTAdapter.cancelDiscovery();
                        dialogProgress.dismiss();
                        printBillToDevice(PRINTER_MAC_ID);
                    }
                }
            } catch (Exception e) {
                Log.e("Class  ", "fire 1 ", e);

            }
        }
    };

    public PrintPreviewAlertBox(Context context) {
        this.context = context;
    }

	/*-*-*-*-*-*-**-*-**-*-*-*-*-*-*-*-*-*-*-*-*-**-*-**-*-*-*--*/

    public int PrintDetailsDialogbox(final Context context, String title, String refno) {

        try
        {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View promptView = layoutInflater.inflate(R.layout.sales_management_vansales_print_view, null);

        final TextView Companyname = (TextView) promptView.findViewById(R.id.headcompanyname);
        final TextView Companyaddress1 = (TextView) promptView.findViewById(R.id.headaddress1);
        final TextView Companyaddress2 = (TextView) promptView.findViewById(R.id.headaddress2);
        final TextView CompanyTele = (TextView) promptView.findViewById(R.id.headteleno);
        final TextView Companyemail = (TextView) promptView.findViewById(R.id.heademail);

        final TextView SalesRepname = (TextView) promptView.findViewById(R.id.salesrepname);
        final TextView SalesRepPhone = (TextView) promptView.findViewById(R.id.salesrepphone);

        final TextView Debname = (TextView) promptView.findViewById(R.id.headcusname);
        final TextView SalOrdDate = (TextView) promptView.findViewById(R.id.printsalorddate);
        final TextView OrderNo = (TextView) promptView.findViewById(R.id.printrefno);
        final TextView Remarks = (TextView) promptView.findViewById(R.id.printremark);


        final TextView TotalNetValue = (TextView) promptView.findViewById(R.id.printnettotal);

        final TextView txtTotVal = (TextView) promptView.findViewById(R.id.printTotalVal);
        final TextView TotalPieceQty = (TextView) promptView.findViewById(R.id.printpiecesqty);
        final TextView txtRoute = (TextView) promptView.findViewById(R.id.printRoute);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title.toUpperCase());

        alertDialogBuilder.setView(promptView);


        PRefno = refno;

        Companyname.setText("Amashi Distributors (Pvt) Ltd");
        Companyaddress1.setText("No.19, Sadhu Mawatha, ");
        Companyaddress2.setText("Maharagama");
        CompanyTele.setText("Tele: " +"0114856985");
        //Companyweb.setText("");
        Companyemail.setText("amashidistributors@gmail.com");

        User salrep = SharedPref.getInstance(context).getLoginUser();
            CustomerController cc = new CustomerController(context);
        SalesRepname.setText(salrep.getCode() + "/ " + salrep.getName());
        SalesRepPhone.setText("Tele: " + salrep.getMobile());

      //  OrderController invhed = new OrderController(context).getDetailsforPrint(refno);

        ArrayList<OrderDetail> list = new OrderDetailController(context).getAllOrderDetailPrint(refno);
     //   ArrayList<FInvRDet> Rlist = new FInvRDetDS(context).getAllInvRDetForPrint(refno.trim());

     //   Debtor debtor = new DebtorDS(context).getSelectedCustomerByCode(invhed.getFINVHED_DEBCODE());

        Debname.setText(cc.getCNameByCode(refno));
//        Debaddress1.setText(debtor.getFDEBTOR_ADD1() + ", " + debtor.getFDEBTOR_ADD2());
//        Debaddress2.setText(debtor.getFDEBTOR_ADD3());
//        DebTele.setText(debtor.getFDEBTOR_TELE());

//        SalOrdDate.setText("Date: " + invhed.getFINVHED_TXNDATE() + " " + currentTime());
//        Remarks.setText("Remarks: " + invhed.getFINVHED_REMARKS());
        OrderNo.setText("Ref No: " + refno);
       // txtRoute.setText(invhed.getFINVHED_TOURCODE() + " / " + invhed.getFINVHED_ROUTECODE());

        int qty = 0;
        double dTotAmt = 0;

        for (OrderDetail det : list) {

                qty += Integer.parseInt(det.getORDDET_QTY());
//            else
//                fiQty += Integer.parseInt(det.getFINVDET_QTY());

           // dDisc += Double.parseDouble(det.getFINVDET_DISVALAMT());
            dTotAmt += Double.parseDouble(det.getORDDET_AMT());
        }

        lvItemDetails = (ListView) promptView.findViewById(R.id.vansaleList);
        lvItemDetails.setAdapter(new PrintVanSaleItemAdapter(context, list));

		/*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-Gross/Net values*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        TotalPieceQty.setText(String.valueOf(qty));
        TotalNetValue.setText(String.format("%,.2f", (dTotAmt)));
        txtTotVal.setText(String.format("%,.2f", dTotAmt));

        PRINTER_MAC_ID =  SharedPref.getInstance(context).getGlobalVal("printer_mac_address").toString();

        alertDialogBuilder.setCancelable(false).setPositiveButton("Print", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PrintCurrentview();
            }
        });

        alertDialogBuilder.setCancelable(false).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        //ListExpandHelper.getListViewSize(lvItemDetails);

        return 1;

        }
        catch (Exception ex)
        {
            return -1;
        }
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

	/*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*--*-*-*-*-*-*-*-*/

    public void printItems() {
        final int LINECHAR = 44;
        String printGapAdjustCom = "                      ";

        User salrep = SharedPref.getInstance(context).getLoginUser();
        int lengthDealACom = 10;
        int lengthDealABCom = (LINECHAR - lengthDealACom) / 2;
        String printGapAdjustACom = printGapAdjustCom.substring(0, Math.min(lengthDealABCom, printGapAdjustCom.length()));

        int lengthDealBCom = 10;
        int lengthDealBBCom = (LINECHAR - lengthDealBCom) / 2;
        String printGapAdjustBCom = printGapAdjustCom.substring(0, Math.min(lengthDealBBCom, printGapAdjustCom.length()));

        String addressCCom = "No 123, Sadhu mawatha, Maharagama";
        int lengthDealCCom = addressCCom.length();
        int lengthDealCBCom = (LINECHAR - lengthDealCCom) / 2;
        String printGapAdjustCCom = printGapAdjustCom.substring(0, Math.min(lengthDealCBCom, printGapAdjustCom.length()));

        String TelCom = "Tel: 0112563412 ";
        int lengthDealDCom = TelCom.length();
        int lengthDealDBCom = (LINECHAR - lengthDealDCom) / 2;
        String printGapAdjustDCom = printGapAdjustCom.substring(0, Math.min(lengthDealDBCom, printGapAdjustCom.length()));

        int lengthDealECom = 10;
        int lengthDealEBCom = (LINECHAR - lengthDealECom) / 2;
        String printGapAdjustECom = printGapAdjustCom.substring(0, Math.min(lengthDealEBCom, printGapAdjustCom.length()));

        int lengthDealFCom = 25;
        int lengthDealFBCom = (LINECHAR - lengthDealFCom) / 2;
        String printGapAdjustFCom = printGapAdjustCom.substring(0, Math.min(lengthDealFBCom, printGapAdjustCom.length()));

        String subTitleheadACom = printGapAdjustACom + "set Company name";
        String subTitleheadBCom = printGapAdjustBCom + "set company address";
        String subTitleheadCCom = printGapAdjustCCom + "set company address";
        String subTitleheadDCom = printGapAdjustDCom + "Tel: 0114523658";
        String subTitleheadECom = printGapAdjustECom + "set web addres";
        String subTitleheadFCom = printGapAdjustFCom + "set email";

        String subTitleheadGCom = printLineSeperatorNew;

        String title_Print_ACom = "\r\n" + subTitleheadACom;
        String title_Print_BCom = "\r\n" + subTitleheadBCom;
        String title_Print_CCom = "\r\n" + subTitleheadCCom;
        String title_Print_DCom = "\r\n" + subTitleheadDCom;
        String title_Print_ECom = "\r\n" + subTitleheadECom;
        String title_Print_FCom = "\r\n" + subTitleheadFCom;;
        String title_Print_GCom = "\r\n" + subTitleheadGCom;

        Heading_a = title_Print_ACom + title_Print_BCom + title_Print_CCom + title_Print_DCom + title_Print_ECom + title_Print_FCom + title_Print_GCom;

        String printGapAdjust = "                        ";

        String SalesRepNamestr = "Sales Rep: " + salrep.getCode() + "/ " + salrep.getName();// +
        // "/
        // "
        // +
        // salrep.getLOCCODE();

        int lengthDealE = SalesRepNamestr.length();
        int lengthDealEB = (LINECHAR - lengthDealE) / 2;
        String printGapAdjustE = printGapAdjust.substring(0, Math.min(lengthDealEB, printGapAdjust.length()));
        String subTitleheadF = printGapAdjustE + SalesRepNamestr;

        String SalesRepPhonestr = "Tele: " + salrep.getMobile();
        int lengthDealF = SalesRepPhonestr.length();
        int lengthDealFB = (LINECHAR - lengthDealF) / 2;
        String printGapAdjustF = printGapAdjust.substring(0, Math.min(lengthDealFB, printGapAdjust.length()));
        String subTitleheadG = printGapAdjustF + SalesRepPhonestr;

        String subTitleheadH = printLineSeperatorNew;

//        OrderController invHed = new InvHedDS(context).getDetailsforPrint(PRefno);
//        FInvRHed invRHed = new FInvRHedDS(context).getDetailsforPrint(PRefno);
//        Debtor debtor = new DebtorDS(context).getSelectedCustomerByCode(invHed.getFINVHED_DEBCODE());
//
//        int lengthDealI = debtor.getFDEBTOR_CODE().length() + "-".length() + debtor.getFDEBTOR_NAME().length();
//        int lengthDealIB = (LINECHAR - lengthDealI) / 2;
//        String printGapAdjustI = printGapAdjust.substring(0, Math.min(lengthDealIB, printGapAdjust.length()));
//
//        String customerAddressStr = debtor.getFDEBTOR_ADD1() + "," + debtor.getFDEBTOR_ADD2();
//        int lengthDealJ = customerAddressStr.length();
//        int lengthDealJB = (LINECHAR - lengthDealJ) / 2;
//        String printGapAdjustJ = printGapAdjust.substring(0, Math.min(lengthDealJB, printGapAdjust.length()));
//
//        int lengthDealK = debtor.getFDEBTOR_ADD3().length();
//        int lengthDealKB = (LINECHAR - lengthDealK) / 2;
//        String printGapAdjustK = printGapAdjust.substring(0, Math.min(lengthDealKB, printGapAdjust.length()));
//
//        int lengthDealL = debtor.getFDEBTOR_TELE().length();
//        int lengthDealLB = (LINECHAR - lengthDealL) / 2;
//        String printGapAdjustL = printGapAdjust.substring(0, Math.min(lengthDealLB, printGapAdjust.length()));
//
//        int cusVatNo = 0;
//        if(TextUtils.isEmpty(debtor.getFDEBTOR_CUS_VATNO()))
//        {
//
//        }
//        else
//        {
//            cusVatNo = "TIN No: ".length() + debtor.getFDEBTOR_CUS_VATNO().length();
//        }

        //int lengthCusTIN = (LINECHAR - cusVatNo) / 2;
        //String printGapCusTIn = printGapAdjust.substring(0, Math.min(lengthCusTIN, printGapAdjust.length()));

//        String subTitleheadI = printGapAdjustI + debtor.getFDEBTOR_CODE() + "-" + debtor.getFDEBTOR_NAME();
//        String subTitleheadJ = printGapAdjustJ + debtor.getFDEBTOR_ADD1() + "," + debtor.getFDEBTOR_ADD2();
//        String subTitleheadK = printGapAdjustK + debtor.getFDEBTOR_ADD3();
//        String subTitleheadL = printGapAdjustL + debtor.getFDEBTOR_TELE();
//        //String subTitleheadTIN = printGapCusTIn + "TIN No: " + debtor.getFDEBTOR_CUS_VATNO();
//
//        String subTitleheadO = printLineSeperatorNew;
//
//        String subTitleheadM = "VJO Date: " + invHed.getFINVHED_TXNDATE() + " " + currentTime();
//        int lengthDealM = subTitleheadM.length();
//        int lengthDealMB = (LINECHAR - lengthDealM) / 2;
//        String printGapAdjustM = printGapAdjust.substring(0, Math.min(lengthDealMB, printGapAdjust.length()));
//
//        String subTitleheadN = "VJO Number: " + PRefno;
//        int lengthDealN = subTitleheadN.length();
//        int lengthDealNB = (LINECHAR - lengthDealN) / 2;
//        String printGapAdjustN = printGapAdjust.substring(0, Math.min(lengthDealNB, printGapAdjust.length()));
//
//        // String TempsubTermCode = "Terms: " + invHed.getFINVHED_TERMCODE() +
//        // "/" + new
//        // TermDS(context).getTermDetails(invHed.getFINVHED_TERMCODE());
//        // int lenTerm = TempsubTermCode.length();
//        // String sp = String.format("%" + ((LINECHAR - lenTerm) / 2) + "s", "
//        // ");
//        // TempsubTermCode = sp + "Terms: " + invHed.getFINVHED_TERMCODE() + "/"
//        // + new TermDS(context).getTermDetails(invHed.getFINVHED_TERMCODE());
//
//        String subTitleheadR;
//
//        if (invHed.getFINVHED_REMARKS().equals(""))
//            subTitleheadR = "Remarks : None";
//        else
//            subTitleheadR = "Remarks : " + invHed.getFINVHED_REMARKS();

//        int lengthDealR = subTitleheadR.length();
//        int lengthDealRB = (LINECHAR - lengthDealR) / 2;
//        String printGapAdjustR = printGapAdjust.substring(0, Math.min(lengthDealRB, printGapAdjust.length()));
//
//        subTitleheadM = printGapAdjustM + subTitleheadM;
//        subTitleheadN = printGapAdjustN + subTitleheadN;
//        subTitleheadR = printGapAdjustR + subTitleheadR;

        String title_Print_F = "\r\n" + subTitleheadF;
        String title_Print_G = "\r\n" + subTitleheadG;
        String title_Print_H = "\r\n" + subTitleheadH;

//        String title_Print_I = "\r\n" + subTitleheadI;
//        String title_Print_J = "\r\n" + subTitleheadJ;
//        String title_Print_K = "\r\n" + subTitleheadK;
//        String title_Print_O = "\r\n" + subTitleheadO;
//
//        String title_Print_M = "\r\n" + subTitleheadM;
//        String title_Print_N = "\r\n" + subTitleheadN;
        String title_Print_R = "\r\n";// + TempsubTermCode + "\r\n" +
        // subTitleheadR;

       // ArrayList<InvDet> itemList = new InvDetDS(context).getAllItemsforPrint(PRefno);
       // ArrayList<FInvRDet> Rlist = new FInvRDetDS(context).getAllInvRDetForPrint(PRefno);

        BigDecimal compDisc = BigDecimal.ZERO;// new
        // BigDecimal(itemList.get(0).getFINVDET_COMDISPER().toString());
        BigDecimal cusDisc = BigDecimal.ZERO;// new
        // BigDecimal(itemList.get(0).getFINVDET_CUSDISPER().toString());
        BigDecimal termDisc = BigDecimal.ZERO;// new
        // BigDecimal(itemList.get(0).getFINVDET_TERM_DISPER().toString());

        Heading_c = "";
        countCountInv = 0;

//        if (subTitleheadK.toString().equalsIgnoreCase(" ")) {
//            Heading_bmh = "\r" + title_Print_F + title_Print_G + title_Print_H + title_Print_I + title_Print_J + title_Print_O + title_Print_M + title_Print_N + title_Print_R;
//        } else
           // Heading_bmh = "\r" + title_Print_F + title_Print_G + title_Print_H + title_Print_I + title_Print_J + title_Print_K + title_Print_O + title_Print_M + title_Print_N + title_Print_R;
            Heading_bmh = "\r" + title_Print_F + title_Print_G + title_Print_H +  title_Print_R;

        String title_cb = "\r\nITEM CODE          QTY     PRICE     AMOUNT ";
        String title_cc = "\r\nITEM NAME								   ";
        String title_cd = "\r\n             INVOICE DETAILS                ";

        Heading_b = "\r\n" + printLineSeperatorNew + title_cb + title_cc + title_cd+ "\r\n" + printLineSeperatorNew+"\n";

		/*-*-*-*-*-*-*-*-*-*-*-*-*-*Individual Item details*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        int totQty = 0 ;
      //  ArrayList<StkIss> list = new ArrayList<StkIss>();

        //Order Item total
//        for (InvDet det : itemList) {
//            totQty += Integer.parseInt(det.getFINVDET_QTY());
//        }

        int nos = 1;
        String SPACE0, SPACE1, SPACE2, SPACE3, SPACE4, SPACE5, SPACE6;
        SPACE6 = "                                            ";

        //for (StkIss iss : list) {
//        for (InvDet det : itemList) {
//
//            String sItemcode = det.getFINVDET_ITEM_CODE();
//            String sItemname = new ItemsDS(context).getItemNameByCode(sItemcode);
//            String sQty = det.getFINVDET_QTY();
//            // String sMRP = iss.getPRICE().substring(0, iss.getPRICE().length()
//            // - 3);
//
//            String sPrice = "", sTotal = "";
//
//            sTotal = det.getFINVDET_AMT();
//            sPrice = det.getFINVDET_SELL_PRICE();
//
//            String sDiscount;
//
//            //sPrice = "";// iss.getPRICE();
//            //sTotal = "";// iss.getAMT();
//            sDiscount = "";// iss.getBrand();
//            sDiscount = det.getFINVDET_DISVALAMT();
//
//
//            int itemCodeLength = sItemcode.length();
//
//            if(itemCodeLength > 15)
//            {
//                sItemcode = sItemcode.substring(0,15);
//            }
//
//            //SPACE0 = String.format("%"+ (44 - (sItemname.length())) +(String.valueOf(nos).length() + 2)+ "s", " ");
//            //SPACE1 = String.format("%" + (20 - (sItemcode.length() + (String.valueOf(nos).length() + 2))) + "s", " ");
//            SPACE1 = padString("",(20 - (sItemcode.length() + (String.valueOf(nos).length() + 2))));
//            //SPACE2 = String.format("%" + (9 - (sPrice.length())) + "s", " ");
//            SPACE2 = padString("",(9 - (sPrice.length())));
//            //SPACE3 = String.format("%" + (3 - (sQty.length())) + "s", " ");
//            SPACE3 = padString("",(3 - (sQty.length())));
//            //SPACE4 = String.format("%" + (12 - (sTotal.length())) + "s", " ");
//            SPACE4 = padString("",(12 - (sTotal.length())));
//            //SPACE5 = String.format("%" + (String.valueOf(nos).length() + 2) + "s", " ");
//            SPACE5 = padString("",(String.valueOf(nos).length() + 2));
//
//
//            String doubleLineItemName1 = "",doubleLineItemName2 = "";
//            int itemNameLength = sItemname.length();
//            if(itemNameLength > 40)
//            {
//                doubleLineItemName1 += sItemname.substring(0,40);
//                doubleLineItemName2 += sItemname.substring(41,sItemname.length());
//
//                Heading_c += nos + ". " + sItemcode +	SPACE1
//
//                       // + SPACE2
//                        + sQty
//
//                        + SPACE3
//                        +SPACE2
//                        + sPrice +SPACE4+ sTotal
//                        +
//                        "\r\n" +SPACE5+doubleLineItemName1.trim()+
//                        "\r\n" +SPACE5+doubleLineItemName2.trim()+
//
//                        "\r\n"+SPACE6+"\r\n";
//            }
//            else
//            {
//                doubleLineItemName1 += sItemname.substring(0,itemNameLength);
//
//                Heading_c += nos + ". " + sItemcode +	SPACE1
//
//                        //+ SPACE2
//                        + sQty
//
//                        + SPACE3
//                        +SPACE2
//                        + sPrice +SPACE4+ sTotal
//                        +
//                        "\r\n" +SPACE5+doubleLineItemName1.trim()+
//
//
//                        "\r\n"+SPACE6+"\r\n";
//            }
//
//            nos++;
//        }

		/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


		/*-*-*-*-*-*-*-*-*-*-*-*-*-*Return Header*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
        Heading_d = "";
        String title_da = "\r\nITEM CODE          QTY     PRICE     AMOUNT ";
        String title_db = "\r\nITEM NAME								   ";
        String title_dc = "\r\n             RETURN DETAILS                 ";

        Heading_d = "\r\n" + printLineSeperatorNew + title_da + title_db + title_dc+ "\r\n" + printLineSeperatorNew+"\r\n";

        /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


        String space = "";
        String sNetTot = "", sGross = "", sRetGross = "0.00";

        // if (invHed.getFINVHED_INV_TYPE().equals("NON")) {
//        sNetTot = String.format(Locale.US, "%,.2f", Double.parseDouble(invHed.getFINVHED_TOTALAMT()));
//
//        sGross = String.format(Locale.US, "%,.2f",
//        Double.parseDouble(invHed.getFINVHED_TOTALAMT()) +
//        Double.parseDouble(invHed.getFINVHED_TOTALDIS()));


        int totReturnQty = 0;
        Double returnTot = 0.00;

//        if(invRHed.getFINVRHED_REFNO() != null) {
//
//            sRetGross = String.format(Locale.US, "%,.2f",
//                    Double.parseDouble(invRHed.getFINVRHED_TOTAL_AMT()));
//
//
//
//        /*-*-*-*-*-*-*-*-*-*-*-*-*-*Individual Return Item details*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//            Heading_e = "";
//            //Return Item Total
//            for (FInvRDet retrnDet1 : Rlist) {
//                totReturnQty += Integer.parseInt(retrnDet1.getFINVRDET_QTY());
//                returnTot += Double.parseDouble(retrnDet1.getFINVRDET_AMT());
//            }
//
//            int retNos = 1;
//
//            for (FInvRDet retrnDet : Rlist) {
//
//                String sRetItemcode = retrnDet.getFINVRDET_ITEMCODE();
//                String sRetItemname = new ItemsDS(context).getItemNameByCode(sRetItemcode);
//                String sRetQty = retrnDet.getFINVRDET_QTY();
//                // String sMRP = iss.getPRICE().substring(0, iss.getPRICE().length()
//                // - 3);
//
//                String sRetPrice = "", sRetTotal = "";
//
//                sRetTotal = retrnDet.getFINVRDET_AMT();
//                sRetPrice = String.format(Locale.US, "%,.2f",
//                        Double.parseDouble(retrnDet.getFINVRDET_SELL_PRICE()));
//
//                int itemCodeLength = sRetItemcode.length();
//
//                if(itemCodeLength > 15)
//                {
//                    sRetItemcode = sRetItemcode.substring(0,15);
//                }
//
//                //SPACE0 = String.format("%" + (44 - (sRetItemname.length())) + (String.valueOf(retNos).length() + 2) + "s", " ");
//                //SPACE1 = String.format("%" + (20 - (sRetItemcode.length() + (String.valueOf(retNos).length() + 2))) + "s", " ");
//                SPACE1 = padString("",(20 - (sRetItemcode.length() + (String.valueOf(retNos).length() + 2))));
//                //SPACE2 = String.format("%" + (9 - (sRetPrice.length())) + "s", " ");
//                SPACE2 = padString("",(9 - (sRetPrice.length())));
//                //SPACE3 = String.format("%" + (3 - (sRetQty.length())) + "s", " ");
//                SPACE3 = padString("",(3 - (sRetQty.length())));
//                //SPACE4 = String.format("%" + (12 - (sRetTotal.length())) + "s", " ");
//                SPACE4 = padString("",(12 - (sRetTotal.length())));
//                //SPACE5 = String.format("%" + (String.valueOf(retNos).length() + 2) + "s", " ");
//                SPACE5 = padString("",(String.valueOf(retNos).length() + 2));
//
//                String doubleLineItemName1 = "", doubleLineItemName2 = "";
//                int itemNameLength = sRetItemname.length();
//                if (itemNameLength > 40) {
//                    doubleLineItemName1 += sRetItemname.substring(0, 40);
//                    doubleLineItemName2 += sRetItemname.substring(41, sRetItemname.length());
//
//                    Heading_e += retNos + ". " + sRetItemcode + SPACE1
//
//                            // + SPACE2
//                            + sRetQty
//
//                            + SPACE3
//                            + SPACE2
//                            + sRetPrice + SPACE4 + sRetTotal
//                            +
//                            "\r\n" + SPACE5 + doubleLineItemName1.trim() +
//                            "\r\n" + SPACE5 + doubleLineItemName2.trim() +
//
//                            "\r\n"+SPACE6+"\r\n";
//                } else {
//                    doubleLineItemName1 += sRetItemname.substring(0, itemNameLength);
//
//                    Heading_e += retNos + ". " + sRetItemcode + SPACE1
//
//                            //+ SPACE2
//                            + sRetQty
//
//                            + SPACE3
//                            + SPACE2
//                            + sRetPrice + SPACE4 + sRetTotal
//                            +
//                            "\r\n" + SPACE5 + doubleLineItemName1.trim() +
//
//
//                            "\r\n"+SPACE6+"\r\n";
//                }
//
//                retNos++;
//            }
//
//		/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//
//        }



		/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-Discounts*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

       // BigDecimal TotalAmt = new BigDecimal(Double.parseDouble(invHed.getFINVHED_TOTALAMT()) + Double.parseDouble(invHed.getFINVHED_TOTALDIS()));

        String sComDisc, sCusdisc = "0", sTermDisc = "0";
        String fullDisc_String = "";

//        if (compDisc.doubleValue() > 0) {
//            // sComDisc = String.format(Locale.US, "%,.2f", (TotalAmt.divide(new
//            // BigDecimal("100"))).multiply(compDisc));
//            TotalAmt = TotalAmt.divide(new BigDecimal("100")).multiply(new BigDecimal("100").subtract(compDisc));
//            sGross = String.format(Locale.US, "%,.2f", TotalAmt);
//
//        }
//
//        if (cusDisc.doubleValue() > 0) {
//            sCusdisc = String.format(Locale.US, "%,.2f", (TotalAmt.divide(new BigDecimal("100"))).multiply(cusDisc));
//            TotalAmt = TotalAmt.divide(new BigDecimal("100")).multiply(new BigDecimal("100").subtract(cusDisc));
//            space = String.format("%" + (LINECHAR - ("   Customer Discount @ ".length() + cusDisc.toString().length() + "%".length() + sCusdisc.length())) + "s", " ");
//            fullDisc_String += "   Customer Discount @ " + cusDisc.toString() + "%" + space + sCusdisc + "\r\n";
//        }
//
//        if (termDisc.doubleValue() > 0) {
//            sTermDisc = String.format(Locale.US, "%,.2f", (TotalAmt.divide(new BigDecimal("100"))).multiply(termDisc));
//            TotalAmt = TotalAmt.divide(new BigDecimal("100")).multiply(new BigDecimal("100").subtract(termDisc));
//            space = String.format("%" + (LINECHAR - ("   Term Discount @ ".length() + termDisc.toString().length() + "%".length() + sTermDisc.length())) + "s", " ");
//            fullDisc_String += "   Term Discount @ " + termDisc.toString() + "%" + space + sTermDisc + "\r\n";
//        }
//
//        String sDisc = String.format(Locale.US, "%,.2f", Double.parseDouble(sTermDisc.replace(",", "")) + Double.parseDouble(sCusdisc.replace(",", "")));
//
//		/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*Gross Net values-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        String printSpaceSumName = "                    ";
        String summaryTitle_a = "Total Quantity" + printSpaceSumName;
        summaryTitle_a = summaryTitle_a.substring(0, Math.min(20, summaryTitle_a.length()));

        //Total Order Item Qty
        space = String.format("%" + (LINECHAR - ("Total Quantity".length() + String.valueOf(totQty).length())) + "s", " ");
        String buttomTitlea = "\r\n\n\n" + "Total Quantity" + space + String.valueOf(totQty);

        //Total Return Item Qty
        space = String.format("%" + (LINECHAR - ("Total Return Quantity".length() + String.valueOf(totReturnQty).length())) + "s", " ");
        String buttomTitleb = "\r\n"+"Total Return Quantity" + space + String.valueOf(totReturnQty);

		/* print gross amount */
        space = String.format("%" + (LINECHAR - ("Total Value".length() + sGross.length())) + "s", " ");
        String summaryTitle_c_Val = "Total Value" + space + sGross;

        space = String.format("%" + (LINECHAR - ("Total Return Value".length() + sRetGross.length())) + "s", " ");
        String summaryTitle_RetVal = "Total Return Value" + space + sRetGross;

		/* print net total */
        space = String.format("%" + (LINECHAR - ("Net Total".length() + sNetTot.length())) + "s", " ");
        String summaryTitle_e_Val = "Net Total" + space + sNetTot;

		/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        String summaryBottom_cpoyline1 = "by Datamation Systems / www.datamation.lk";
        int lengthsummarybottm = summaryBottom_cpoyline1.length();
        int lengthsummarybottmline1 = (LINECHAR - lengthsummarybottm) / 2;
        String printGapbottmline1 = printGapAdjust.substring(0, Math.min(lengthsummarybottmline1, printGapAdjust.length()));

        // String summaryBottom_cpoyline3 = "www.datamation.lk";
        // int lengthsummarybotline3 = summaryBottom_cpoyline3.length();
        // int lengthsummarybottmline3 = (LINECHAR - lengthsummarybotline3) / 2;
        // String printGapbottmline3 = printGapAdjust.substring(0,
        // Math.min(lengthsummarybottmline3, printGapAdjust.length()));

        // String summaryBottom_cpoyline2 = " +94 11 2 501202 / + 94 (0) 777
        // 899899 ";
        // int lengthsummarybotline2 = summaryBottom_cpoyline2.length();
        // int lengthsummarybottmline2 = (LINECHAR - lengthsummarybotline2) / 2;
        // String printGapbottmline2 = printGapAdjust.substring(0,
        // Math.min(lengthsummarybottmline2, printGapAdjust.length()));

        String buttomTitlec = "\r\n" + summaryTitle_c_Val;
        String buttomTitled = "\r\n" + summaryTitle_RetVal;
        String buttomTitlee = "\r\n" + summaryTitle_e_Val;



        String buttomTitlef = "\r\n\n\n" + "------------------        ------------------" + "\r\n" + "     Customer               Sales Executive";

        String buttomTitlefa = "\r\n\n\n" + "Please place the rubber stamp.";
        String buttomTitlecopyw = "\r\n" + printGapbottmline1 + summaryBottom_cpoyline1;
        // String buttomTitlecopywbottom = "\r\n" + printGapbottmline2 +
        // summaryBottom_cpoyline2;
        // String buttomTitlecopywbottom3 = "\r\n" + printGapbottmline3 +
        // summaryBottom_cpoyline3;
        buttomRaw = printLineSeperatorNew + buttomTitlea + buttomTitleb + buttomTitlec + buttomTitled + "\r\n" + printLineSeperatorNew + buttomTitlee + "\r\n" + printLineSeperatorNew + "\r\n" + buttomTitlef + buttomTitlefa + "\r\n" + printLineSeperatorNew + buttomTitlecopyw + "\r\n" + printLineSeperatorNew + "\n";
        callPrintDevice();
    }

	/*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*--*-*-*-*-*-*-*-*/

    public void PrintCurrentview() {
        // checkPrinter();
        // if (PRINTER_MAC_ID.equals("404")) {
        // Log.v("", "No MAC Address Found.Enter Printer MAC Address.");
        // android.widget.Toast.makeText(context, "No MAC Address Found.Enter
        // Printer MAC Address.", android.widget.Toast.LENGTH_LONG).show();
        // } else {
        printItems();
        // }
    }

	/*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*--*-*-*-*-*-*-*-*/

    private void checkPrinter() {

        if (PRINTER_MAC_ID.trim().length() == 0) {
            PRINTER_MAC_ID = "404";
        } else {
            PRINTER_MAC_ID = PRINTER_MAC_ID;
        }
    }

	/*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*--*-*-*-*-*-*-*-*/

    private void callPrintDevice() {
        BILL = " ";
        PRINTER_MAC_ID =  SharedPref.getInstance(context).getGlobalVal("printer_mac_address").toString();
        String company = ""
                + "PLATINUM COMPUTERS(PVT) LTD\n"
                + "NO 20/B, Main Street, Kandy, Sri Lanka.\n"
                + "Land: 812254630 Mob: 712205220 Fax: 812254639\n"
                + " \n"
                + "CUSTOMER INVOICE\n"
                + " \n";
        List<String> t1Headers = Arrays.asList("INFO", "CUSTOMER");
        List<List<String>> t1Rows = Arrays.asList(
                Arrays.asList("DATE: 2015-9-8", "ModernTec Distributors"),
                Arrays.asList("TIME: 10:53:AM", "MOB: +94719530398"),
                Arrays.asList("BILL NO: 12", "ADDRES: No 25, Main Street, Kandy."),
                Arrays.asList("INVOICE NO: 458-80-108", "")
        );
        String t2Desc = "SELLING DETAILS";
        List<String> t2Headers = Arrays.asList("ITEM", "PRICE($)", "QTY", "VALUE");
        List<List<String>> t2Rows = Arrays.asList(
                Arrays.asList("Optical mouse", "120.00", "20", "2400.00"),
                Arrays.asList("Gaming keyboard", "550.00", "30", "16500.00"),
                Arrays.asList("320GB SATA HDD", "220.00", "32", "7040.00"),
                Arrays.asList("500GB SATA HDD", "274.00", "13", "3562.00"),
                Arrays.asList("1TB SATA HDD", "437.00", "11", "4807.00"),
                Arrays.asList("RE-DVD ROM", "144.00", "29", "4176.00"),
                Arrays.asList("DDR3 4GB RAM", "143.00", "13", "1859.00"),
                Arrays.asList("Blu-ray DVD", "94.00", "28", "2632.00"),
                Arrays.asList("WR-DVD", "122.00", "34", "4148.00"),
                Arrays.asList("Adapter", "543.00", "28", "15204.00")
        );
        List<Integer> t2ColWidths = Arrays.asList(17, 9, 5, 12);
        String t3Desc = "RETURNING DETAILS";
        List<String> t3Headers = Arrays.asList("ITEM", "PRICE($)", "QTY", "VALUE");
        List<List<String>> t3Rows = Arrays.asList(
                Arrays.asList("320GB SATA HDD", "220.00", "4", "880.00"),
                Arrays.asList("WR-DVD", "122.00", "7", "854.00"),
                Arrays.asList("1TB SATA HDD", "437.00", "7", "3059.00"),
                Arrays.asList("RE-DVD ROM", "144.00", "4", "576.00"),
                Arrays.asList("Gaming keyboard", "550.00", "6", "3300.00"),
                Arrays.asList("DDR3 4GB RAM", "143.00", "7", "1001.00")
        );
        String summary = ""
                + "GROSS\n"
                + "DISCOUNT(5%)\n"
                + "RETURN\n"
                + "PAYABLE\n"
                + "CASH\n"
                + "CHEQUE\n"
                + "CREDIT(BALANCE)\n";
        String summaryVal = ""
                + "59,928.00\n"
                + "2,996.40\n"
                + "9,670.00\n"
                + "47,261.60\n"
                + "20,000.00\n"
                + "15,000.00\n"
                + "12,261.60\n";
        String sign1 = ""
                + "---------------------\n"
                + "CASH COLLECTOR\n";
        String sign2 = ""
                + "---------------------\n"
                + "GOODS RECEIVED BY\n";
        String advertise = "soulution by clough.com";

        //bookmark
        Board b = new Board(48);
        b.setInitialBlock(new Block(b, 46, 7, company).allowGrid(false).setBlockAlign(Block.BLOCK_CENTRE).setDataAlign(Block.DATA_CENTER));
        b.appendTableTo(0, Board.APPEND_BELOW, new Table(b, 48, t1Headers, t1Rows));
        b.getBlock(3).setBelowBlock(new Block(b, 46, 1, t2Desc).setDataAlign(Block.DATA_CENTER));
        b.appendTableTo(5, Board.APPEND_BELOW, new Table(b, 48, t2Headers, t2Rows, t2ColWidths));
        b.getBlock(10).setBelowBlock(new Block(b, 46, 1, t3Desc).setDataAlign(Block.DATA_CENTER));
        b.appendTableTo(14, Board.APPEND_BELOW, new Table(b, 48, t3Headers, t3Rows, t2ColWidths));
        Block summaryBlock = new Block(b, 35, 9, summary).allowGrid(false).setDataAlign(Block.DATA_MIDDLE_RIGHT);
        b.getBlock(19).setBelowBlock(summaryBlock);
        Block summaryValBlock = new Block(b, 12, 9, summaryVal).allowGrid(false).setDataAlign(Block.DATA_MIDDLE_RIGHT);
        summaryBlock.setRightBlock(summaryValBlock);
        Block sign1Block = new Block(b, 24, 7, sign1).setDataAlign(Block.DATA_BOTTOM_MIDDLE).allowGrid(false);
        summaryBlock.setBelowBlock(sign1Block);
        sign1Block.setRightBlock(new Block(b, 24, 7, sign2).setDataAlign(Block.DATA_BOTTOM_MIDDLE).allowGrid(false));
        sign1Block.setBelowBlock(new Block(b, 48, 3, advertise).setDataAlign(Block.DATA_CENTER).allowGrid(false));
        //b.showBlockIndex(true);
        System.out.println(b.invalidate().build().getPreview());

        BILL = b.invalidate().build().getPreview();
       // BILL = Heading_a + Heading_bmh + Heading_b + Heading_c + Heading_d + Heading_e + buttomRaw;
        Log.v("", "BILL :" + BILL);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            if (mBTAdapter.isDiscovering())
                mBTAdapter.cancelDiscovery();
            else
                mBTAdapter.startDiscovery();
        } catch (Exception e) {
            Log.e("Class ", "fire 4", e);
        }
        System.out.println("BT Searching status :" + mBTAdapter.isDiscovering());

        if (mBTAdapter == null) {
            android.widget.Toast.makeText(context, "Device has no bluetooth		 capability...", android.widget.Toast.LENGTH_SHORT).show();
        } else {
            if (!mBTAdapter.isEnabled()) {
                Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            }
            printBillToDevice(PRINTER_MAC_ID);
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        }
    }

	/*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*--*-*-*-*-*-*-*-*/

    public void printBillToDevice(final String address) {

        mBTAdapter.cancelDiscovery();
        try {
            BluetoothDevice mdevice = mBTAdapter.getRemoteDevice(address);
            Method m = mdevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            mBTSocket = (BluetoothSocket) m.invoke(mdevice, 1);
            mBTSocket.connect();
            OutputStream os = mBTSocket.getOutputStream();
            os.flush();
            os.write(BILL.getBytes());
            System.out.println(BILL);

            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();
        } catch (Exception e) {
            Log.e("Printer error",e.toString());
            android.widget.Toast.makeText(context, "Printer Device Disable Or Invalid MAC.Please Enable the Printer or MAC Address.", android.widget.Toast.LENGTH_LONG).show();
            e.printStackTrace();
            this.PrintDetailsDialogbox(context, "", PRefno);
        }
    }

	/*-*-*-*--*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*--*-*-*-*-*-*-*-*/

    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }


    public static String padString(String str, int leng) {
        for (int i = str.length(); i < leng; i++)
            str += " ";
        return str;
    }
}
