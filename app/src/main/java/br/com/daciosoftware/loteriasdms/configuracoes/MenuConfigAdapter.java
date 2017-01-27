package br.com.daciosoftware.loteriasdms.configuracoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by fdacio on 25/08/16.
 */
public class MenuConfigAdapter extends BaseAdapter {

    private Context context;
    private List<ItemConfiguracao> list;

    public MenuConfigAdapter(Context context, List<ItemConfiguracao> list) {
        this.context = context;
        this.list = list;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_configuracoes_menu_adapter, parent, false);
            holder = new ViewHolder();
            holder.textViewLabel = (TextView) view.findViewById(R.id.textViewLabel);
            holder.textViewSublabel = (TextView) view.findViewById(R.id.textViewSublabel);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemConfiguracao itemConfiguracao = getItem(position);

        holder.textViewLabel.setText(itemConfiguracao.getLabel());
        holder.textViewSublabel.setText(itemConfiguracao.getSublabel());

        return view;

    }

    private static class ViewHolder {
        TextView textViewLabel;
        TextView textViewSublabel;
    }

}
