package br.com.daciosoftware.loteriasdms.notificacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by fdacio on 26/01/17.
 */
public class AtualizaSorteioListAdapter extends BaseAdapter {

    private Context context;
    private List<Sorteio> lista;

    public AtualizaSorteioListAdapter(Context context, List<Sorteio> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Sorteio getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getLayoutRow(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return R.layout.row_atualiza_sorteio_megasena_adapter;
            case LOTOFACIL:
                return R.layout.row_atualiza_sorteio_lotofacil_adapter;
            case QUINA:
                return R.layout.row_atualiza_sorteio_quina_adapter;
            default:
                return 0;
        }
    }

    public ViewHolder getViewHolder(TypeSorteio typeSorteio, View view) {
        switch (typeSorteio) {
            case MEGASENA:
                return new ViewHolderMegasena(view);
            case LOTOFACIL:
                return new ViewHolderLotofacil(view);
            case QUINA:
                return new ViewHolderQuina(view);
            default:
                return null;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Sorteio sorteio = sortDezenasCrescente(getItem(position));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(getLayoutRow(sorteio.getTypeSorteio()), parent, false);
        ViewHolder holder = getViewHolder(sorteio.getTypeSorteio(), view);
        view.setTag(holder);
        holder = (ViewHolder) view.getTag();
        holder.setValue(sorteio);

        return view;
    }

    private Sorteio sortDezenasCrescente(Sorteio sorteio) {
        int[] arrayDezenas = sorteio.getDezenas();
        Arrays.sort(arrayDezenas);
        sorteio.setDezenas(arrayDezenas);
        return sorteio;
    }

    private abstract static class ViewHolder {
        protected TextView textViewNumero;
        protected TextView textViewData;
        protected TextView textViewLocal;
        protected View view;

        ViewHolder(View view) {
            this.view = view;
            textViewNumero = (TextView) view.findViewById(R.id.textViewNumero);
            textViewData = (TextView) view.findViewById(R.id.textViewData);
            textViewLocal = (TextView) view.findViewById(R.id.textViewLocal);
        }

        public abstract void setValue(Sorteio sorteio);

    }

    private static class ViewHolderMegasena extends ViewHolder {
        TextView textViewD1,
                textViewD2,
                textViewD3,
                textViewD4,
                textViewD5,
                textViewD6;

        public ViewHolderMegasena(View view) {
            super(view);
            textViewD1 = (TextView) view.findViewById(R.id.textViewD1);
            textViewD2 = (TextView) view.findViewById(R.id.textViewD2);
            textViewD3 = (TextView) view.findViewById(R.id.textViewD3);
            textViewD4 = (TextView) view.findViewById(R.id.textViewD4);
            textViewD5 = (TextView) view.findViewById(R.id.textViewD5);
            textViewD6 = (TextView) view.findViewById(R.id.textViewD6);

        }

        @Override
        public void setValue(Sorteio sorteio) {

            textViewNumero.setText(String.valueOf(sorteio.getNumero()));
            textViewData.setText(MyDateUtil.calendarToDateBr(sorteio.getData()));
            textViewD1.setText(String.format("%02d", sorteio.getDezenas()[0]));
            textViewD2.setText(String.format("%02d", sorteio.getDezenas()[1]));
            textViewD3.setText(String.format("%02d", sorteio.getDezenas()[2]));
            textViewD4.setText(String.format("%02d", sorteio.getDezenas()[3]));
            textViewD5.setText(String.format("%02d", sorteio.getDezenas()[4]));
            textViewD6.setText(String.format("%02d", sorteio.getDezenas()[5]));
            textViewLocal.setText(sorteio.getLocal());

        }

    }

    private static class ViewHolderQuina extends ViewHolder {
        TextView textViewD1,
                textViewD2,
                textViewD3,
                textViewD4,
                textViewD5;

        public ViewHolderQuina(View view) {
            super(view);
            textViewD1 = (TextView) view.findViewById(R.id.textViewD1);
            textViewD2 = (TextView) view.findViewById(R.id.textViewD2);
            textViewD3 = (TextView) view.findViewById(R.id.textViewD3);
            textViewD4 = (TextView) view.findViewById(R.id.textViewD4);
            textViewD5 = (TextView) view.findViewById(R.id.textViewD5);
        }

        @Override
        public void setValue(Sorteio sorteio) {
            textViewNumero.setText(String.valueOf(sorteio.getNumero()));
            textViewData.setText(MyDateUtil.calendarToDateBr(sorteio.getData()));
            textViewD1.setText(String.format("%02d", sorteio.getDezenas()[0]));
            textViewD2.setText(String.format("%02d", sorteio.getDezenas()[1]));
            textViewD3.setText(String.format("%02d", sorteio.getDezenas()[2]));
            textViewD4.setText(String.format("%02d", sorteio.getDezenas()[3]));
            textViewD5.setText(String.format("%02d", sorteio.getDezenas()[4]));
            textViewLocal.setText(sorteio.getLocal());
        }

    }

    private static class ViewHolderLotofacil extends ViewHolder {
        private TextView textViewD1, textViewD2, textViewD3, textViewD4, textViewD5,
                textViewD6, textViewD7, textViewD8, textViewD9, textViewD10,
                textViewD11, textViewD12, textViewD13, textViewD14, textViewD15;


        public ViewHolderLotofacil(View view) {
            super(view);

            textViewD1 = (TextView) view.findViewById(R.id.textViewD1);
            textViewD2 = (TextView) view.findViewById(R.id.textViewD2);
            textViewD3 = (TextView) view.findViewById(R.id.textViewD3);
            textViewD4 = (TextView) view.findViewById(R.id.textViewD4);
            textViewD5 = (TextView) view.findViewById(R.id.textViewD5);
            textViewD6 = (TextView) view.findViewById(R.id.textViewD6);
            textViewD7 = (TextView) view.findViewById(R.id.textViewD7);
            textViewD8 = (TextView) view.findViewById(R.id.textViewD8);
            textViewD9 = (TextView) view.findViewById(R.id.textViewD9);
            textViewD10 = (TextView) view.findViewById(R.id.textViewD10);
            textViewD11 = (TextView) view.findViewById(R.id.textViewD11);
            textViewD12 = (TextView) view.findViewById(R.id.textViewD12);
            textViewD13 = (TextView) view.findViewById(R.id.textViewD13);
            textViewD14 = (TextView) view.findViewById(R.id.textViewD14);
            textViewD15 = (TextView) view.findViewById(R.id.textViewD15);
        }

        @Override
        public void setValue(Sorteio sorteio) {
            textViewNumero.setText(String.valueOf(sorteio.getNumero()));
            textViewData.setText(MyDateUtil.calendarToDateBr(sorteio.getData()));
            textViewD1.setText(String.format("%02d", sorteio.getDezenas()[0]));
            textViewD2.setText(String.format("%02d", sorteio.getDezenas()[1]));
            textViewD3.setText(String.format("%02d", sorteio.getDezenas()[2]));
            textViewD4.setText(String.format("%02d", sorteio.getDezenas()[3]));
            textViewD5.setText(String.format("%02d", sorteio.getDezenas()[4]));
            textViewD6.setText(String.format("%02d", sorteio.getDezenas()[5]));
            textViewD7.setText(String.format("%02d", sorteio.getDezenas()[6]));
            textViewD8.setText(String.format("%02d", sorteio.getDezenas()[7]));
            textViewD9.setText(String.format("%02d", sorteio.getDezenas()[8]));
            textViewD10.setText(String.format("%02d", sorteio.getDezenas()[9]));
            textViewD11.setText(String.format("%02d", sorteio.getDezenas()[10]));
            textViewD12.setText(String.format("%02d", sorteio.getDezenas()[11]));
            textViewD13.setText(String.format("%02d", sorteio.getDezenas()[12]));
            textViewD14.setText(String.format("%02d", sorteio.getDezenas()[13]));
            textViewD15.setText(String.format("%02d", sorteio.getDezenas()[14]));
            textViewLocal.setText(sorteio.getLocal());
        }

    }

}
