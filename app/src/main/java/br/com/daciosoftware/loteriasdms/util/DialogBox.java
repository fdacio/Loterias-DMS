package br.com.daciosoftware.loteriasdms.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import br.com.daciosoftware.loteriasdms.R;


/**
 * Created by Dácio Braga on 22/06/2016.
 */
public class DialogBox {

    private AlertDialog.Builder dialogBox;

    public enum DialogBoxType {INFORMATION, QUESTION}

    private static final String TEXTO_BUTTON_OK = "OK";
    private static final String TEXTO_BUTTON_YES = "SIM";
    private static final String TEXTO_BUTTON_NO = "NÃO";
    private static final boolean CANCELABLE = false;

    private DialogInterface.OnClickListener onClickListenerOK;
    private DialogInterface.OnClickListener onClickListenerYES;
    private DialogInterface.OnClickListener onClickListenerNO;
    private Context context;
    private boolean finishActivity = false;


    public DialogBox(Context context, DialogBoxType dialogBoxType, String title, String message) {
        this.context = context;
        dialogBox = new AlertDialog.Builder(context);
        dialogBox.setTitle(title);
        dialogBox.setMessage(message);
        dialogBox.setCancelable(CANCELABLE);

        switch (dialogBoxType) {
            case INFORMATION:
                dialogBox.setIcon(R.drawable.ic_info_outline_black_24dp);
                setOnClickListenerOK();
                dialogBox.setNeutralButton(TEXTO_BUTTON_OK, onClickListenerOK);
                break;
            case QUESTION:
                dialogBox.setIcon(R.drawable.ic_help_outline_black_24dp);
                break;
        }
    }

    public DialogBox(Context context, DialogBoxType dialogBoxType, String title, String message, DialogInterface.OnClickListener onClickListenerYES, DialogInterface.OnClickListener onClickListenerNO ) {
        this(context, dialogBoxType, title, message);
        dialogBox.setPositiveButton(TEXTO_BUTTON_YES, onClickListenerYES);
        dialogBox.setNegativeButton(TEXTO_BUTTON_NO, onClickListenerNO);

    }

    public DialogBox(Context context, DialogBoxType dialogBoxType, String title, String message, boolean finishActivity ) {
        this(context, dialogBoxType, title, message);
        this.finishActivity = finishActivity;
    }

    private void setOnClickListenerOK() {
        this.onClickListenerOK = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(finishActivity){
                    ((Activity)context).finish();
                }
            }
        };
    }

    public void setOnClickListenerYES(DialogInterface.OnClickListener onClickListenerYES) {
        this.onClickListenerYES = onClickListenerYES;
    }

    public void setOnClickListenerNO(DialogInterface.OnClickListener onClickListenerNO) {
        this.onClickListenerNO = onClickListenerNO;
    }

    public void show() {
        dialogBox.create();
        dialogBox.show();
    }
}
