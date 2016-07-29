package br.com.daciosoftware.loteriasdms.menuadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.StyleTypeSorteio;
import br.com.daciosoftware.loteriasdms.TypeSorteio;

/**
 * Created by Dácio Braga on 17/07/2016.
 */
public class MenuSecundarioAdapter extends BaseAdapter {

    private Context context;
    private String[] list;
    private TypeSorteio typeSorteio;

    public MenuSecundarioAdapter(Context context, String[] list, TypeSorteio typeSorteio){
        this.context = context;
        this.list = list;
        this.typeSorteio = typeSorteio;
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

    private static class ViewHolder{
        TextView textViewLabelMenu;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_menu_secundario_adapter, parent, false);
            holder = new ViewHolder();
            holder.textViewLabelMenu = (TextView) view.findViewById(R.id.textViewLabelMenu);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String labelJogo = getItem(position);
        holder.textViewLabelMenu.setText(labelJogo);

        /*
        Configurar estilo de cores e imagens para o menu
         */
        StyleTypeSorteio styleTypeSorteio = new StyleTypeSorteio(context,view);
        styleTypeSorteio.setStyleMenu(typeSorteio,position);

        return view;

    }
}
