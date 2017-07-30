package com.finalproject.elhen15.musicbox.Utiles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by Elhen15 on 31/07/2017.
 */

public class Functions {

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
