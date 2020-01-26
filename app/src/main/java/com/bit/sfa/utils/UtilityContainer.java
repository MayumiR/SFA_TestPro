package com.bit.sfa.utils;


import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bit.sfa.R;


/**
 * common functions
 */

public class UtilityContainer {


    //---------------------------------------------------------------------------------------------------------------------------------------------------

    public static void  showSnackBarError(View v, String message, Context context) {
        Snackbar snack = Snackbar.make(v, "" + message, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        view.setBackgroundColor(Color.parseColor("#CB4335"));
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        snack.show();

    }

  /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public static void mLoadFragment(Fragment fragment, Context context) {

        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(R.anim.enter, R.anim.exit_to_right);
        ft.replace(R.id.fragmentContainer, fragment, fragment.getClass().getSimpleName());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }
    protected static void sacaSnackbar (Context context, View view, String s)
    {

    }


    //-----------------------------------------------------------------------------------------------------------------------------------------------------


}
