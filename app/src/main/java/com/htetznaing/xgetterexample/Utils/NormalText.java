package com.htetznaing.xgetterexample.Utils;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class NormalText {
    public static void set(AlertDialog dialog){
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setAllCaps(false);
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setAllCaps(false);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setAllCaps(false);
    }
}
