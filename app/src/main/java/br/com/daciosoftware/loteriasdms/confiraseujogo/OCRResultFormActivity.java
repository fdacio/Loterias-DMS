package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.util.Constantes;

public class OCRResultFormActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrresult_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editTextOCR = (EditText) findViewById(R.id.editTextOCR);
        ImageView imageViewOCR = (ImageView) findViewById(R.id.imageViewOCR);

        String textoOCR = getIntent().getStringExtra(Constantes.TEXTO_OCR);
        Bitmap bmp = (Bitmap) getIntent().getExtras().getParcelable(Constantes.IMAGE_OCR);
        imageViewOCR.setImageBitmap(bmp);

        editTextOCR.setText(textoOCR);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}