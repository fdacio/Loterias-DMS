
package br.com.daciosoftware.loteriasdms.util.filedialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.Serializable;

/**
 * Esta classe é utilizada para dá o start na Activity FilesListActivity
 * com os parâmetros adequados para cada tipo de dialog.
 */
public class FileDialog {

    public enum FileDialogType implements Serializable{
        OPEN_FILE, SAVE_FILE, SELECT_DIR
    }
    public static final int FILE_DIALOG = 1;
    public static final String RESULT_PATH = "RESULT_PATH";

    private Context context;
    private FileDialogType fileDialogType;
    private String fileName;
    private String[] formaterFilter;

    public FileDialog(Context context, FileDialogType fileDialogType){
        this.context = context;
        this.fileDialogType = fileDialogType;
    }

    public void show(){
        Intent intent = new Intent(this.context, FilesListActivity.class);
        Activity activity =  (Activity)(this.context);


        if(this.fileDialogType == FileDialogType.OPEN_FILE){
            intent.putExtra("CAN_SELECT_DIR", false);
         }else if(this.fileDialogType == FileDialogType.SAVE_FILE){
            intent.putExtra("CAN_SELECT_DIR", true);
             if(this.fileName != null){
                intent.putExtra("FILE_NAME", this.fileName);
            }
        }else if(this.fileDialogType == FileDialogType.SELECT_DIR){
            intent.putExtra("CAN_SELECT_DIR", true);
        }

        intent.putExtra("TYPE_DIALOG",this.fileDialogType);
        intent.putExtra("START_PATH",Environment.getExternalStorageDirectory().getPath());
        if(this.formaterFilter != null){
            intent.putExtra("FORMAT_FILTER",this.formaterFilter);
        }

        activity.startActivityForResult(intent,FILE_DIALOG);
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFormaterFilter(String[] formaterFilter){
        this.formaterFilter = formaterFilter;
    }
}