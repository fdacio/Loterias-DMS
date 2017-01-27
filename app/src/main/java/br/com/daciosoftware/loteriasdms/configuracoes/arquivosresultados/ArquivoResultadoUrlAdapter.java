package br.com.daciosoftware.loteriasdms.configuracoes.arquivosresultados;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.configuracoes.ItemConfiguracao;

/**
 * Created by fdacio on 28/08/16.
 */
public class ArquivoResultadoUrlAdapter extends BaseAdapter {

    private Context context;
    private List<ItemConfiguracao> list;
    private ArquivoResultadoUrlListener arquivoResultadoUrlListener;

    public ArquivoResultadoUrlAdapter(Context context, List<ItemConfiguracao> list,
                                      ArquivoResultadoUrlListener arquivoResultadoUrlListener) {
        this.context = context;
        this.list = list;
        this.arquivoResultadoUrlListener = arquivoResultadoUrlListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ItemConfiguracao getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            holder.textViewLabel = (TextView) view.findViewById(R.id.textViewLabel);
            holder.textViewSublabel = (TextView) view.findViewById(R.id.textViewSublabel);
            holder.textViewEdit = (TextView) view.findViewById(R.id.textViewEdit);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemConfiguracao itemConfiguracao = getItem(position);

        holder.textViewLabel.setText(itemConfiguracao.getLabel());
        holder.textViewSublabel.setText(itemConfiguracao.getSublabel());
        holder.textViewEdit.setText(context.getResources().getString(R.string.label_editar));
        holder.textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arquivoResultadoUrlListener.editClicked(position);
            }
        });

        return view;

    }

    private static class ViewHolder {
        TextView textViewLabel;
        TextView textViewSublabel;
        TextView textViewEdit;
    }
}
