package br.com.daciosoftware.loteriasdms.configuracoes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by fdacio on 25/08/16.
 */
public class MenuConfigAdapter extends BaseAdapter {

    private Context context;
    private String[] list;

    public MenuConfigAdapter(Context context, String[] list) {
        this.context = context;
        this.list = list;

    }

    private int getIcon(int index) {
        switch (index) {
            case 0:
                return R.drawable.ic_coin_grey_36dp;
            case 1:
                return R.drawable.ic_web_grey_36dp;
            case 2:
                return R.drawable.ic_cloud_check_grey_36dp;
            default:
                return 0;
        }
    }

    private static class ViewHolder {
        ImageView imageViewIcon;
        TextView textViewLabel;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int position) {
        return list[position];
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
            holder.imageViewIcon = (ImageView) view.findViewById(R.id.imageViewIcon);
            holder.textViewLabel = (TextView) view.findViewById(R.id.textViewLabel);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String labelJogo = getItem(position);

        holder.imageViewIcon.setImageResource(getIcon(position));
        holder.textViewLabel.setText(labelJogo);

        return view;

    }

}
