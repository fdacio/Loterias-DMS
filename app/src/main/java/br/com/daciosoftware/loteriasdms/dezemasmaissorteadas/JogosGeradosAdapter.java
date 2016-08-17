package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by fdacio on 14/08/16.
 */
public class JogosGeradosAdapter extends BaseAdapter {

    private Context context;
    private List<JogoGerado> list;

    public JogosGeradosAdapter(Context context, List<JogoGerado> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public JogoGerado getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView textViewDezenas;
        TextView textViewOrdem;

        public ViewHolder() {
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.row_jogos_gerados_adapter, parent, false);
            holder = new ViewHolder();
            holder.textViewDezenas = (TextView) view.findViewById(R.id.textViewDezenas);
            holder.textViewOrdem = (TextView) view.findViewById(R.id.textViewOrdem);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JogoGerado jogoGerado = getItem(position);
        holder.textViewDezenas.setText(jogoGerado.toString());
        String label = context.getResources().getString(R.string.label_ordem);
        holder.textViewOrdem.setText(String.format(label, position + 1));
        return view;
    }
}
