
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
    public static final int FILE_DIALOG = 100;
    public static final String RESULT_PATH = "RESULT_PATH";
    public static final String TYPE_DIALOG = "TYPE_DIALOG";
    public static final String FILE_NAME = "FILE_NAME";
    public static final String START_PATH = "START_PATH";
    public static final String CAN_SELECT_DIR = "CAN_SELECT_DIR";
    public static final String FORMAT_FILTER = "FORMAT_FILTER";
    public static final String ITEM_KEY = "key";
    public static final String ITEM_IMAGE = "image";
    public static final String ROOT = "/";


    private Context context;
    private FileDialogType fileDialogType;
    private String fileName;


    private String startPath;
    private String[] formaterFilter;

    public FileDialog(Context context, FileDialogType fileDialogType){
        this.context = context;
        this.fileDialogType = fileDialogType;
    }

    public void show(){
        Intent intent = new Intent(this.context, FilesListActivity.class);
        Activity activity =  (Activity)(this.context);

        if(this.fileDialogType == FileDialogType.OPEN_FILE){
            intent.putExtra(CAN_SELECT_DIR, false);
         }else if(this.fileDialogType == FileDialogType.SAVE_FILE){
            intent.putExtra(CAN_SELECT_DIR, true);
        }else if(this.fileDialogType == FileDialogType.SELECT_DIR){
            intent.putExtra(CAN_SELECT_DIR, true);
        }

        intent.putExtra(TYPE_DIALOG,this.fileDialogType);

        if(this.startPath != null) {
            intent.putExtra(START_PATH, this.startPath);
        }else{
            intent.putExtra(START_PATH, Environment.getExternalStorageDirectory().getPath());
        }
        if(this.fileName != null){
            intent.putExtra(FILE_NAME, this.fileName);
        }
        if(this.formaterFilter != null){
            intent.putExtra(FORMAT_FILTER,this.formaterFilter);
        }

        activity.startActivityForResult(intent,FILE_DIALOG);
    }


    /**
     *
     * @param fileName Nome padrao
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFormaterFilter(String[] formaterFilter){
        this.formaterFilter = formaterFilter;
    }

    public void setStartPath(String startPath) {
        this.startPath = startPath;
    }

}