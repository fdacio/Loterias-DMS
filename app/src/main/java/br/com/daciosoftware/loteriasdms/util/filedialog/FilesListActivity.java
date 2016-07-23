package br.com.daciosoftware.loteriasdms.util.filedialog;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by Dácio Braga on 05/07/2016.
 */
public class FilesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<String> path = null;
    private TextView myPath;
    private ArrayList<HashMap<String, Object>> mList;
    private ListView listViewFiles;
    private Button btnOK;
    private EditText editTextFileName;
    private InputMethodManager inputManager;
    private FileDialog.FileDialogType fileDialogType;
    private String parentPath;
    private String currentPath = FileDialog.ROOT;
    private boolean canSelectDir = false;
    private File selectedFile;
    private HashMap<String, Integer> lastPositions = new HashMap<>();
    private String[] formatFilter;
    private String fileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED, getIntent());
        setContentView(R.layout.activity_file_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        myPath = (TextView) findViewById(R.id.path);
        listViewFiles = (ListView) findViewById(R.id.listViewFiles);
        listViewFiles.setOnItemClickListener(this);
        listViewFiles.setEmptyView(findViewById(R.id.textViewEmpty));
        editTextFileName = (EditText) findViewById(R.id.editTextFileName);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setEnabled(false);
        btnOK.setOnClickListener(new OnClikListenerButtonOK());
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListenerButtonCancel());


        fileDialogType = (FileDialog.FileDialogType) getIntent().getSerializableExtra(FileDialog.TYPE_DIALOG);
        if (fileDialogType == FileDialog.FileDialogType.OPEN_FILE) {
            toolbar.setTitle(getResources().getString(R.string.title_open_file));
        } else if (fileDialogType == FileDialog.FileDialogType.SAVE_FILE) {
            toolbar.setTitle(getResources().getString(R.string.title_save_file));
        } else if (fileDialogType == FileDialog.FileDialogType.SELECT_DIR) {
            toolbar.setTitle(getResources().getString(R.string.title_select_directory));
            LinearLayout linearLayoutFileName = (LinearLayout) findViewById(R.id.linearLayoutNameFile);
            linearLayoutFileName.setVisibility(View.INVISIBLE);
        }
        setSupportActionBar(toolbar);


        inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        canSelectDir = getIntent().getBooleanExtra(FileDialog.CAN_SELECT_DIR, false);
        fileName = getIntent().getStringExtra(FileDialog.FILE_NAME);
        formatFilter = getIntent().getStringArrayExtra(FileDialog.FORMAT_FILTER);


        if (fileName != null) {
            editTextFileName.setText(fileName);
        }

        String startPath = getIntent().getStringExtra(FileDialog.START_PATH);
        startPath = startPath != null ? startPath : FileDialog.ROOT;
        if (canSelectDir) {
            selectedFile = new File(startPath);
            btnOK.setEnabled(true);
        }
        getDir(startPath);

    }

    private void getDir(String dirPath) {

        boolean useAutoSelection = dirPath.length() < currentPath.length();

        Integer position = lastPositions.get(parentPath);

        getDirImpl(dirPath);

        if (position != null && useAutoSelection) {
            listViewFiles.setSelection(position);
        }
    }

    /**
     * Monta a estrutura de arquivos e diretorios filhos do diretorio fornecido.
     *
     * @param dirPath Diretorio pai.
     */
    private void getDirImpl(final String dirPath) {

        List<String> item = new ArrayList<>();
        currentPath = dirPath;
        path = new ArrayList<>();
        mList = new ArrayList<>();

        File f = new File(currentPath);
        File[] files = f.listFiles();
        if (files == null) {
            currentPath = FileDialog.ROOT;
            f = new File(currentPath);
            files = f.listFiles();
        }
        myPath.setText(currentPath);

        if (!currentPath.equals(FileDialog.ROOT)) {

            item.add(FileDialog.ROOT);
            addItem(FileDialog.ROOT, R.drawable.folder);
            path.add(FileDialog.ROOT);

            item.add("../");
            addItem("../", R.drawable.folder);
            path.add(f.getParent());
            parentPath = f.getParent();

        }

        TreeMap<String, String> dirsMap = new TreeMap<>();
        TreeMap<String, String> dirsPathMap = new TreeMap<>();
        TreeMap<String, String> filesMap = new TreeMap<>();
        TreeMap<String, String> filesPathMap = new TreeMap<>();
        for (File file : files) {
            if (file.isDirectory()) {
                String dirName = file.getName();
                dirsMap.put(dirName, dirName);
                dirsPathMap.put(dirName, file.getPath());
            } else if (!canSelectDir) {
                final String fileName = file.getName();
                final String fileNameLwr = fileName.toLowerCase();
                // se ha um filtro de formatos, utiliza-o
                if (formatFilter != null) {
                    boolean contains = false;
                    for (int i = 0; i < formatFilter.length - 1; i++) {
                        String mimeType = getMimeType(file);
                        if (mimeType != null) {
                            //Log.i(Constantes.CATEGORIA, "Mime Filter:" + formatFilter[i].toLowerCase() + " = Mine File:" + mimeType);
                            if (mimeType.toLowerCase().contains(formatFilter[i].toLowerCase())) {
                                contains = true;
                                break;
                            }
                        } else {
                            final String formatLwr = formatFilter[i].toLowerCase();
                            if (fileNameLwr.endsWith(formatLwr)) {
                                contains = true;
                                break;
                            }
                        }
                    }
                    if (contains) {
                        filesMap.put(fileName, fileName);
                        filesPathMap.put(fileName, file.getPath());
                    }
                    // senao, adiciona todos os arquivos
                } else {
                    filesMap.put(fileName, fileName);
                    filesPathMap.put(fileName, file.getPath());
                }
            }
        }
        item.addAll(dirsMap.tailMap("").values());
        item.addAll(filesMap.tailMap("").values());
        path.addAll(dirsPathMap.tailMap("").values());
        path.addAll(filesPathMap.tailMap("").values());

        SimpleAdapter fileList = new SimpleAdapter(this, mList,
                R.layout.row_file_dialog,
                new String[]{FileDialog.ITEM_KEY, FileDialog.ITEM_IMAGE}, new int[]{
                R.id.fdrowtext, R.id.imagefolder});

        for (String dir : dirsMap.tailMap("").values()) {
            addItem(dir, R.drawable.folder);
        }

        for (String file : filesMap.tailMap("").values()) {
            addItem(file, R.drawable.file);
        }

        fileList.notifyDataSetChanged();

        listViewFiles.setAdapter(fileList);

    }

    private String getMimeType(File file) {
        Uri uri = Uri.fromFile(file);
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void addItem(String fileName, int imageId) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(FileDialog.ITEM_KEY, fileName);
        item.put(FileDialog.ITEM_IMAGE, imageId);
        mList.add(item);
    }

    private void setSelectVisible(View v) {
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        btnOK.setEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            btnOK.setEnabled(false);
            if (!currentPath.equals(FileDialog.ROOT)) {
                getDir(parentPath);
            } else {
                return super.onKeyDown(keyCode, event);
            }

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        File file = new File(path.get(position));

        setSelectVisible(v);

        if (file.isDirectory()) {
            btnOK.setEnabled(false);
            if (file.canRead()) {
                lastPositions.put(currentPath, position);
                getDir(path.get(position));
                if (canSelectDir) {
                    selectedFile = file;
                    v.setSelected(true);
                    btnOK.setEnabled(true);
                }
            } else {
                String msg = "[" + file.getName() + "] " + getText(R.string.fd_cant_read_folder);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        } else {
            selectedFile = file;
            editTextFileName.setText(selectedFile.getName());
            v.setSelected(true);
            btnOK.setEnabled(true);
        }
    }

    private class OnClikListenerButtonOK implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String nameFile = editTextFileName.getText().toString();
            if (selectedFile != null) {
                if (fileDialogType == FileDialog.FileDialogType.SELECT_DIR) {
                    getIntent().putExtra(FileDialog.RESULT_PATH, selectedFile.getPath());
                } else if (fileDialogType == FileDialog.FileDialogType.SAVE_FILE) {
                    if (nameFile.equals("")) {
                        Toast.makeText(FilesListActivity.this, getResources().getString(R.string.msg_erro_name_file), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getIntent().putExtra(FileDialog.RESULT_PATH, selectedFile.getPath() + "/" + nameFile);
                } else if (fileDialogType == FileDialog.FileDialogType.OPEN_FILE) {
                    getIntent().putExtra(FileDialog.RESULT_PATH, selectedFile.getPath());
                }

                setResult(RESULT_OK, getIntent());
                finish();
            }
        }
    }

    private class OnClickListenerButtonCancel implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED, getIntent());
            finish();
        }
    }

}
