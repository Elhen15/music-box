package com.finalproject.elhen15.musicbox.Utiles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.finalproject.elhen15.musicbox.Model.Model;

/**
 * Created by Elhen15 on 31/07/2017.
 */

public class Functions {

    public static void helloUser(View v,String userName){


        if (Model.user.getIsAdmin())
        {
            alertMessage(v,"Welcome admin","Hello "+userName.toString()+" welcome to MusicBox");
        }
        else
        {
            alertMessage(v,"Welcome user","Hello "+userName.toString()+" welcome to MusicBox");
        }
    }

    public static void alertMessage(View v, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }
}
