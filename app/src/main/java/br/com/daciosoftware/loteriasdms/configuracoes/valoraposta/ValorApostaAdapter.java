package br.com.daciosoftware.loteriasdms.configuracoes.valoraposta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.configuracoes.ItemConfiguracao;
import br.com.daciosoftware.loteriasdms.configuracoes.ItemConfiguracaoEditListener;

/**
 * Created by fdacio on 01/02/17.
 */
public class ValorApostaAdapter extends BaseAdapter {

    private Context context;
    private List<ItemConfiguracao> lista;
    private ItemConfiguracaoEditListener itemConfiguracaoEditListener;

    public ValorApostaAdapter(Context context, List<ItemConfiguracao> lista, ItemConfiguracaoEditListener itemConfiguracaoEditListener) {
        this.context = context;
        this.lista = lista;
        this.itemConfiguracaoEditListener = itemConfiguracaoEditListener;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public ItemConfiguracao getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_configuracao_adapter, parent, false);
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
                itemConfiguracaoEditListener.editClicked(position);
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
