package edu.ucsd.cse110.cse110lab4part5;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.util.Optional;


public class Utilities {
    public static void showAlert(Activity activity, String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder.setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok",(dialog, id) -> {
            dialog.cancel();
        })
            .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

    }

    public static void closeAppServerError(Context activity){
        Log.e("Unresponsive Server", "unresponsive server called by " + activity + ", closing app");
        try{
            showAlert((Activity) activity, "Global Server is unresponsive. Please close the app and try again later.");
        } catch (Exception e) {
            Log.e("Unresponsive Server", "Error in displaying alert for unresponsive server");
        }
        //System.exit(1);
    }


}
