package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;

/**
 * Created by Dácio Braga on 31/07/2016.
 */
public class ResultadoSeuJogoAdapter extends BaseAdapter {

    private Context context;
    private List<SorteioAcerto> list;
    private TypeSorteio typeSorteio;

    public ResultadoSeuJogoAdapter(Context context, List<SorteioAcerto> list, TypeSorteio typeSorteio) {
        this.context = context;
        this.list = list;
        this.typeSorteio = typeSorteio;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SorteioAcerto getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private abstract static class ViewHolder {
        protected TextView textViewNumero;
        protected TextView textViewData;
        protected TextView textViewQtdeAcertos;
        protected View view;

        ViewHolder(View view) {
            this.view = view;
            textViewNumero = (TextView) view.findViewById(R.id.textViewNumero);
            textViewData = (TextView) view.findViewById(R.id.textViewData);
            textViewQtdeAcertos = (TextView) view.findViewById(R.id.textViewQtdeAcertos);
        }

        private static void findTextViews(ViewGroup viewGroup, ArrayList<TextView> textViews) {
            for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof ViewGroup) {
                    findTextViews((ViewGroup) child, textViews);
                } else if (child instanceof TextView) {
                    textViews.add((TextView) child);
                }
            }
        }

        private ArrayList<TextView> getTextViews() {
            ArrayList<TextView> textViews = new ArrayList<>();
            ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.tableLayoutDezenas);
            findTextViews(viewGroup, textViews);
            return textViews;
        }

        public void setBackgroundMarcacao(SorteioAcerto sorteioacerto, TypeSorteio typeSorteio) {
            int resourceBackground = 0;
            switch (typeSorteio) {
                case MEGASENA:
                    resourceBackground = R.drawable.bg_dezenas_acertos_megasena;
                    break;
                case LOTOFACIL:
                    resourceBackground = R.drawable.bg_dezenas_acertos_lotofacil;
                    break;
                case QUINA:
                    resourceBackground = R.drawable.bg_dezenas_acertos_quina;
                    break;
            }

            for (TextView textView : getTextViews()) {
                textView.setBackgroundResource(0);
            }

            for (TextView textView : getTextViews()) {
                int dezena = Integer.valueOf(textView.getText().toString());
                int[] arrayDezenasAcertos = sorteioacerto.getDezenasAcertos();
                for (int nDezenasAcerto : arrayDezenasAcertos) {
                    if (dezena == nDezenasAcerto) {
                        textView.setBackgroundResource(resourceBackground);
                    }
                }

            }

        }

        public abstract void setValue(SorteioAcerto sorteioacerto);

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
        public void setValue(SorteioAcerto sorteioacerto) {
            textViewNumero.setText(String.valueOf(sorteioacerto.getSorteio().getNumero()));
            textViewData.setText(MyDateUtil.calendarToDateBr(sorteioacerto.getSorteio().getData()));
            textViewD1.setText(String.format("%02d",sorteioacerto.getSorteio().getD1()));
            textViewD2.setText(String.format("%02d",sorteioacerto.getSorteio().getD2()));
            textViewD3.setText(String.format("%02d",sorteioacerto.getSorteio().getD3()));
            textViewD4.setText(String.format("%02d",sorteioacerto.getSorteio().getD4()));
            textViewD5.setText(String.format("%02d",sorteioacerto.getSorteio().getD5()));
            textViewD6.setText(String.format("%02d",sorteioacerto.getSorteio().getD6()));
            textViewQtdeAcertos.setText(String.format("%d Acerto(s)",sorteioacerto.getQtdeAcertos()));

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
        public void setValue(SorteioAcerto sorteioacerto) {
            textViewNumero.setText(String.valueOf(sorteioacerto.getSorteio().getNumero()));
            textViewData.setText(MyDateUtil.calendarToDateBr(sorteioacerto.getSorteio().getData()));
            textViewD1.setText(String.format("%02d",sorteioacerto.getSorteio().getD1()));
            textViewD2.setText(String.format("%02d",sorteioacerto.getSorteio().getD2()));
            textViewD3.setText(String.format("%02d",sorteioacerto.getSorteio().getD3()));
            textViewD4.setText(String.format("%02d",sorteioacerto.getSorteio().getD4()));
            textViewD5.setText(String.format("%02d",sorteioacerto.getSorteio().getD5()));
            textViewQtdeAcertos.setText(String.format("%d Acerto(s)",sorteioacerto.getQtdeAcertos()));
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
        public void setValue(SorteioAcerto sorteioacerto) {
            textViewNumero.setText(String.valueOf(sorteioacerto.getSorteio().getNumero()));
            textViewData.setText(MyDateUtil.calendarToDateBr(sorteioacerto.getSorteio().getData()));
            textViewD1.setText(String.format("%02d",sorteioacerto.getSorteio().getD1()));
            textViewD2.setText(String.format("%02d",sorteioacerto.getSorteio().getD2()));
            textViewD3.setText(String.format("%02d",sorteioacerto.getSorteio().getD3()));
            textViewD4.setText(String.format("%02d",sorteioacerto.getSorteio().getD4()));
            textViewD5.setText(String.format("%02d",sorteioacerto.getSorteio().getD5()));
            textViewD6.setText(String.format("%02d",sorteioacerto.getSorteio().getD6()));
            textViewD7.setText(String.format("%02d",sorteioacerto.getSorteio().getD7()));
            textViewD8.setText(String.format("%02d",sorteioacerto.getSorteio().getD8()));
            textViewD9.setText(String.format("%02d",sorteioacerto.getSorteio().getD9()));
            textViewD10.setText(String.format("%02d",sorteioacerto.getSorteio().getD10()));
            textViewD11.setText(String.format("%02d",sorteioacerto.getSorteio().getD11()));
            textViewD12.setText(String.format("%02d",sorteioacerto.getSorteio().getD12()));
            textViewD13.setText(String.format("%02d",sorteioacerto.getSorteio().getD13()));
            textViewD14.setText(String.format("%02d",sorteioacerto.getSorteio().getD14()));
            textViewD15.setText(String.format("%02d",sorteioacerto.getSorteio().getD15()));
            textViewQtdeAcertos.setText(String.format("%d Acerto(s)",sorteioacerto.getQtdeAcertos()));
        }

    }

    public int getLayoutRow(TypeSorteio typeSorteio) {
        switch (typeSorteio) {
            case MEGASENA:
                return R.layout.row_resultado_seu_jogo_megasena_adapter;
            case LOTOFACIL:
                return R.layout.row_resultado_seu_jogo_lotofacil_adapter;
            case QUINA:
                return R.layout.row_resultado_seu_jogo_quina_adapter;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(getLayoutRow(typeSorteio), parent, false);
            holder = getViewHolder(typeSorteio, view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SorteioAcerto sorteioacerto = getItem(position);

        holder.setValue(sorteioacerto);

        holder.setBackgroundMarcacao(sorteioacerto, typeSorteio);

        return view;

    }

}
