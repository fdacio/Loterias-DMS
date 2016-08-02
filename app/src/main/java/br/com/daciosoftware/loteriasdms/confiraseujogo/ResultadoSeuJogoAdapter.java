package br.com.daciosoftware.loteriasdms.confiraseujogo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.TypeSorteio;
import br.com.daciosoftware.loteriasdms.util.Constantes;
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
        //this.list.add(null);//para a última linha Ver Mais
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

        public abstract void setValue(SorteioAcerto sorteioacerto);
        public abstract void setBackgroundMarcacao(SorteioAcerto sorteioacerto);
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
            textViewD1.setText(String.valueOf(sorteioacerto.getSorteio().getD1()));
            textViewD2.setText(String.valueOf(sorteioacerto.getSorteio().getD2()));
            textViewD3.setText(String.valueOf(sorteioacerto.getSorteio().getD3()));
            textViewD4.setText(String.valueOf(sorteioacerto.getSorteio().getD4()));
            textViewD5.setText(String.valueOf(sorteioacerto.getSorteio().getD5()));
            textViewD6.setText(String.valueOf(sorteioacerto.getSorteio().getD6()));
            textViewQtdeAcertos.setText(String.valueOf(sorteioacerto.getQtdeAcertos() + " Acerto(s)"));

        }

        @Override
        public void setBackgroundMarcacao(SorteioAcerto sorteioacerto) {

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
            textViewD1.setText(String.valueOf(sorteioacerto.getSorteio().getD1()));
            textViewD2.setText(String.valueOf(sorteioacerto.getSorteio().getD2()));
            textViewD3.setText(String.valueOf(sorteioacerto.getSorteio().getD3()));
            textViewD4.setText(String.valueOf(sorteioacerto.getSorteio().getD4()));
            textViewD5.setText(String.valueOf(sorteioacerto.getSorteio().getD5()));
            textViewQtdeAcertos.setText(String.valueOf(sorteioacerto.getQtdeAcertos() + " Acerto(s)"));
        }

        @Override
        public void setBackgroundMarcacao(SorteioAcerto sorteioacerto) {

        }

    }

    private class ViewHolderLotofacil extends ViewHolder {
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
            textViewD1.setText(String.valueOf(sorteioacerto.getSorteio().getD1()));
            textViewD2.setText(String.valueOf(sorteioacerto.getSorteio().getD2()));
            textViewD3.setText(String.valueOf(sorteioacerto.getSorteio().getD3()));
            textViewD4.setText(String.valueOf(sorteioacerto.getSorteio().getD4()));
            textViewD5.setText(String.valueOf(sorteioacerto.getSorteio().getD5()));
            textViewD6.setText(String.valueOf(sorteioacerto.getSorteio().getD6()));
            textViewD7.setText(String.valueOf(sorteioacerto.getSorteio().getD7()));
            textViewD8.setText(String.valueOf(sorteioacerto.getSorteio().getD8()));
            textViewD9.setText(String.valueOf(sorteioacerto.getSorteio().getD9()));
            textViewD10.setText(String.valueOf(sorteioacerto.getSorteio().getD10()));
            textViewD11.setText(String.valueOf(sorteioacerto.getSorteio().getD11()));
            textViewD12.setText(String.valueOf(sorteioacerto.getSorteio().getD12()));
            textViewD13.setText(String.valueOf(sorteioacerto.getSorteio().getD13()));
            textViewD14.setText(String.valueOf(sorteioacerto.getSorteio().getD14()));
            textViewD15.setText(String.valueOf(sorteioacerto.getSorteio().getD15()));
            textViewQtdeAcertos.setText(String.valueOf(sorteioacerto.getQtdeAcertos() + " Acerto(s)"));
        }

        @Override
        public void setBackgroundMarcacao(SorteioAcerto sorteioacerto) {
            ArrayList<TextView> textViews = getTextViews(view);
            for (TextView textView : textViews) {
                int dezena = Integer.valueOf(textView.getText().toString());
                if(Arrays.binarySearch(sorteioacerto.getDezenasAcertos(),dezena) != -1) {
                    textView.setBackgroundResource(R.drawable.bg_dezenas_acertos_megasena);
                }
            }

        }
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
    private ArrayList<TextView> getTextViews(View view) {
        ArrayList<TextView> textViews = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        findTextViews(viewGroup, textViews);
        return textViews;
    }
    private void setBackgroundCirculo(TypeSorteio typeSorteio, View view, SorteioAcerto sorteioAcerto) {
        int resourceBackgroud;
        switch (typeSorteio) {
            case MEGASENA:
                resourceBackgroud = R.drawable.bg_dezenas_acertos_megasena;
                break;
            case LOTOFACIL:
                resourceBackgroud = R.drawable.bg_dezenas_acertos_lotofacil;
                break;
            case QUINA:
                resourceBackgroud = R.drawable.bg_dezenas_acertos_quina;
                break;
            default:
                resourceBackgroud = R.drawable.bg_dezenas_acertos;
                break;
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
        /*
        if (position == list.size() - 1) {
            return inflater.inflate(R.layout.row_resultado_seu_jogo_ver_mais_adapter, parent, false);
        }
        */

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

        holder.setBackgroundMarcacao(sorteioacerto);

        return view;

    }

}
