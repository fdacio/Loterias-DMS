package br.com.daciosoftware.loteriasdms.dezemasmaissorteadas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by Dácio Braga on 05/08/2016.
 */
public class DezenasMaisSorteadasAdapter extends BaseAdapter {
    private Context context;
    private List<MaisSorteada> list;
    private DezenasMaisSorteadasClickable dezenasMaisSorteadasClickable;

    public DezenasMaisSorteadasAdapter(Context context, List<MaisSorteada> list, DezenasMaisSorteadasClickable dezenasMaisSorteadasClickable) {
        this.context = context;
        this.list = list;
        this.dezenasMaisSorteadasClickable = dezenasMaisSorteadasClickable;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MaisSorteada getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private static class ViewHolder {
        TextView textViewDezena;
        TextView textViewQtdeVezes;
        CheckBox checkBoxSelecionada;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_dezenas_mais_sorteadas_adapter, parent, false);
            holder = new ViewHolder();
            holder.textViewDezena = (TextView) view.findViewById(R.id.textViewDezena);
            holder.textViewQtdeVezes = (TextView) view.findViewById(R.id.textViewQtdeVezes);
            holder.checkBoxSelecionada = (CheckBox) view.findViewById(R.id.checkBoxSelecionada);
            view.setTag(holder);

            holder.checkBoxSelecionada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    MaisSorteada maisSorteada = (MaisSorteada) checkBox.getTag();
                    maisSorteada.setSelecionada(checkBox.isChecked());
                }
            });


        } else {
            holder = (ViewHolder) view.getTag();
        }

        MaisSorteada maisSorteada = getItem(position);

        holder.textViewDezena.setText(String.valueOf(maisSorteada.getDezena()));
        int qtdeVezes = maisSorteada.getQtdeVezes();
        String textoVezes;
        if(qtdeVezes != 1){
            textoVezes = " Vezes";
        }else{
            textoVezes = " Vez";
        }

        holder.textViewQtdeVezes.setText(String.valueOf(qtdeVezes)+textoVezes);
        holder.checkBoxSelecionada.setChecked(maisSorteada.isSelecionada());
        holder.checkBoxSelecionada.setTag(maisSorteada);

        return view;

    }
}