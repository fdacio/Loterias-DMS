package br.com.daciosoftware.loteriasdms.sorteio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 20/07/2016.
 */
public class SorteioListAdapter extends BaseAdapter {

    private Context context;
    private List<Sorteio> list;
    private TypeSorteio typeSorteio;

    public SorteioListAdapter(Context context, List<Sorteio> list, TypeSorteio typeSorteio) {
        this.context = context;
        this.list = list;
        this.typeSorteio = typeSorteio;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Sorteio getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getLayoutRow(TypeSorteio typeSorteio){
        switch (typeSorteio){
            case MEGASENA: return R.layout.row_sorteio_megasena_adapter;
            case LOTOFACIL: return R.layout.row_sorteio_lotofacil_adapter;
            case QUINA: return R.layout.row_sorteio_quina_adapter;
            default: return 0;
        }
    }

    public void getInstanceTextViewMegasena(View view, ViewHolder holder) {
        holder.textViewD1 = (TextView) view.findViewById(R.id.textViewD1);
        holder.textViewD2 = (TextView) view.findViewById(R.id.textViewD2);
        holder.textViewD3 = (TextView) view.findViewById(R.id.textViewD3);
        holder.textViewD4 = (TextView) view.findViewById(R.id.textViewD4);
        holder.textViewD5 = (TextView) view.findViewById(R.id.textViewD5);
        holder.textViewD6 = (TextView) view.findViewById(R.id.textViewD6);
    }

    public void setValueTextViewMegasena(ViewHolder holder, Sorteio sorteio) {
        holder.textViewD1.setText(String.valueOf(sorteio.getDezenas()[0]));
        holder.textViewD2.setText(String.valueOf(sorteio.getDezenas()[1]));
        holder.textViewD3.setText(String.valueOf(sorteio.getDezenas()[2]));
        holder.textViewD4.setText(String.valueOf(sorteio.getDezenas()[3]));
        holder.textViewD5.setText(String.valueOf(sorteio.getDezenas()[4]));
        holder.textViewD6.setText(String.valueOf(sorteio.getDezenas()[5]));
    }

    public void getInstanceTextViewQuina(View view, ViewHolder holder) {
        holder.textViewD1 = (TextView) view.findViewById(R.id.textViewD1);
        holder.textViewD2 = (TextView) view.findViewById(R.id.textViewD2);
        holder.textViewD3 = (TextView) view.findViewById(R.id.textViewD3);
        holder.textViewD4 = (TextView) view.findViewById(R.id.textViewD4);
        holder.textViewD5 = (TextView) view.findViewById(R.id.textViewD5);
    }

    public void setValueTextViewQuina(ViewHolder holder, Sorteio sorteio) {
        holder.textViewD1.setText(String.valueOf(sorteio.getDezenas()[0]));
        holder.textViewD2.setText(String.valueOf(sorteio.getDezenas()[1]));
        holder.textViewD3.setText(String.valueOf(sorteio.getDezenas()[2]));
        holder.textViewD4.setText(String.valueOf(sorteio.getDezenas()[3]));
        holder.textViewD5.setText(String.valueOf(sorteio.getDezenas()[4]));
    }

    public void getInstanceTextViewLotofacil(View view, ViewHolder holder) {
        holder.textViewD1 = (TextView) view.findViewById(R.id.textViewD1);
        holder.textViewD2 = (TextView) view.findViewById(R.id.textViewD2);
        holder.textViewD3 = (TextView) view.findViewById(R.id.textViewD3);
        holder.textViewD4 = (TextView) view.findViewById(R.id.textViewD4);
        holder.textViewD5 = (TextView) view.findViewById(R.id.textViewD5);
        holder.textViewD6 = (TextView) view.findViewById(R.id.textViewD6);
        holder.textViewD7 = (TextView) view.findViewById(R.id.textViewD7);
        holder.textViewD8 = (TextView) view.findViewById(R.id.textViewD7);
        holder.textViewD9 = (TextView) view.findViewById(R.id.textViewD9);
        holder.textViewD10 = (TextView) view.findViewById(R.id.textViewD10);
        holder.textViewD11 = (TextView) view.findViewById(R.id.textViewD11);
        holder.textViewD12 = (TextView) view.findViewById(R.id.textViewD12);
        holder.textViewD13 = (TextView) view.findViewById(R.id.textViewD13);
        holder.textViewD14 = (TextView) view.findViewById(R.id.textViewD14);
        holder.textViewD15 = (TextView) view.findViewById(R.id.textViewD15);

    }

    public void setValueTextViewLotofacil(ViewHolder holder, Sorteio sorteio) {
        holder.textViewD1.setText(String.valueOf(sorteio.getDezenas()[0]));
        holder.textViewD2.setText(String.valueOf(sorteio.getDezenas()[1]));
        holder.textViewD3.setText(String.valueOf(sorteio.getDezenas()[2]));
        holder.textViewD4.setText(String.valueOf(sorteio.getDezenas()[3]));
        holder.textViewD5.setText(String.valueOf(sorteio.getDezenas()[4]));
        holder.textViewD6.setText(String.valueOf(sorteio.getDezenas()[5]));
        holder.textViewD7.setText(String.valueOf(sorteio.getDezenas()[6]));
        holder.textViewD8.setText(String.valueOf(sorteio.getDezenas()[7]));
        holder.textViewD9.setText(String.valueOf(sorteio.getDezenas()[8]));
        holder.textViewD10.setText(String.valueOf(sorteio.getDezenas()[9]));
        holder.textViewD11.setText(String.valueOf(sorteio.getDezenas()[10]));
        holder.textViewD12.setText(String.valueOf(sorteio.getDezenas()[11]));
        holder.textViewD13.setText(String.valueOf(sorteio.getDezenas()[12]));
        holder.textViewD14.setText(String.valueOf(sorteio.getDezenas()[13]));
        holder.textViewD15.setText(String.valueOf(sorteio.getDezenas()[14]));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(getLayoutRow(typeSorteio), parent, false);
            holder = new ViewHolder();
            holder.textViewNumero = (TextView) view.findViewById(R.id.textViewNumero);
            holder.textViewData = (TextView) view.findViewById(R.id.textViewData);
            holder.textViewLocal = (TextView) view.findViewById(R.id.textViewLocal);
            if (typeSorteio == TypeSorteio.MEGASENA) {
                getInstanceTextViewMegasena(view, holder);
            } else if (typeSorteio == TypeSorteio.QUINA) {
                getInstanceTextViewQuina(view, holder);
            } else if (typeSorteio == TypeSorteio.LOTOFACIL) {
                getInstanceTextViewLotofacil(view, holder);
            }

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Sorteio sorteio = getItem(position);

        holder.textViewNumero.setText(String.valueOf(sorteio.getNumero()));
        holder.textViewData.setText(MyDateUtil.calendarToDateBr(sorteio.getData()));
        holder.textViewLocal.setText(sorteio.getLocal());

        if (typeSorteio == TypeSorteio.MEGASENA) {
            setValueTextViewMegasena(holder, sorteio);
        } else if (typeSorteio == TypeSorteio.QUINA) {
            setValueTextViewQuina(holder, sorteio);
        } else if (typeSorteio == TypeSorteio.LOTOFACIL) {
            setValueTextViewLotofacil(holder, sorteio);
        }
        return view;

    }

    private static class ViewHolder {
        TextView textViewNumero;
        TextView textViewData;
        TextView textViewLocal;
        TextView textViewD1, textViewD2, textViewD3, textViewD4, textViewD5,
                textViewD6, textViewD7, textViewD8, textViewD9, textViewD10,
                textViewD11, textViewD12, textViewD13, textViewD14, textViewD15;
    }
}
