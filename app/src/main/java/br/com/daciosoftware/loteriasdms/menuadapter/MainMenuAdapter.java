package br.com.daciosoftware.loteriasdms.menuadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by Dácio Braga on 17/07/2016.
 */
public class MainMenuAdapter extends BaseAdapter {
    
    private Context context;
    private String[] list;

    public MainMenuAdapter(Context context, String[] list){
        this.context = context;
        this.list = list;

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
        ImageView imageViewIconJogo;
        TextView textViewLabelJogo;
    }

    private int getIconJogo(int index){
        switch (index){
            case 0: return R.mipmap.ic_mega_sena;
            case 1: return R.mipmap.ic_lotafacil;
            case 2: return R.mipmap.ic_quina;
            default: return 0;
        }
    }

    private int getColorLabel(int index){
        switch (index){
            case 0: return this.context.getResources().getColor(R.color.colorMegasena);
            case 1: return this.context.getResources().getColor(R.color.colorLotofacil);
            case 2: return this.context.getResources().getColor(R.color.colorQuina);
            default: return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_loteriasdms_adapter, parent, false);
            holder = new ViewHolder();
            holder.imageViewIconJogo = (ImageView)view.findViewById(R.id.imageViewIconJogo);
            holder.textViewLabelJogo = (TextView) view.findViewById(R.id.textViewLabelJogo);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String labelJogo = getItem(position);

        holder.textViewLabelJogo.setText(labelJogo);
        holder.textViewLabelJogo.setTextColor(getColorLabel(position));
        holder.imageViewIconJogo.setImageResource(getIconJogo(position));


        return view;

    }
}
