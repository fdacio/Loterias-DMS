package br.com.daciosoftware.loteriasdms.configuracoes.urls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by fdacio on 28/08/16.
 */
public class UrlsAdapter extends BaseAdapter {

    private Context context;
    private List<Urls> list;
    private UrlsEditListener urlsEditListener;

    public UrlsAdapter(Context context, List<Urls> list, UrlsEditListener urlsEditListener) {
        this.context = context;
        this.list = list;
        this.urlsEditListener = urlsEditListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Urls getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageButton imageButtonEdit;
        TextView textViewHeader;
        TextView textViewUrlResultado;
        TextView textViewUrlArquivo;
    }

    private int getColorRgb(int index) {
        switch (index) {
            case 0:
                return Color.rgb(34, 149, 81);
            case 1:
                return Color.rgb(124, 11, 137);
            case 2:
                return Color.rgb(41, 11, 137);
            default:
                return Color.rgb(63, 81, 181);
        }
    }

    private ColorStateList getColorLabel(int index) {
        return ColorStateList.valueOf(getColorRgb(index));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_config_urls_adapter, parent, false);
            holder = new ViewHolder();
            holder.textViewHeader = (TextView) view.findViewById(R.id.textViewHeaderJogo);
            holder.textViewUrlResultado = (TextView) view.findViewById(R.id.textViewUrlResultado);
            holder.textViewUrlArquivo = (TextView) view.findViewById(R.id.textViewUrlArquivo);
            holder.imageButtonEdit = (ImageButton) view.findViewById(R.id.imageButtonEdit);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Urls url = getItem(position);

        holder.textViewHeader.setText(url.getHeader());
        holder.textViewHeader.setTextColor(getColorLabel(position));
        holder.textViewUrlResultado.setText(url.getUrlResultado());
        holder.textViewUrlArquivo.setText(url.getUrlArquivo());
        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlsEditListener.editClicked(position);
            }
        });

        return view;

    }
}
