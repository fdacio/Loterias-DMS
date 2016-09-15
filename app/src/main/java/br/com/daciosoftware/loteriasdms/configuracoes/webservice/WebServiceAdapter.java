package br.com.daciosoftware.loteriasdms.configuracoes.webservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;

/**
 * Created by fdacio on 01/09/16.
 */
public class WebServiceAdapter extends BaseAdapter {

    private Context context;
    private List<WebService> list;
    private WebServiceEditListener webServiceEditListener;

    public WebServiceAdapter(Context context, List<WebService> list, WebServiceEditListener webServiceEditListener) {
        this.context = context;
        this.list = list;
        this.webServiceEditListener = webServiceEditListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public WebService getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageButton imageButtonEdit;
        TextView textViewUrlWebService;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_config_webservice_adapter, parent, false);
            holder = new ViewHolder();
            holder.textViewUrlWebService = (TextView) view.findViewById(R.id.textViewUrlWebService);
            holder.imageButtonEdit = (ImageButton) view.findViewById(R.id.imageButtonEdit);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        WebService webService = getItem(position);

        holder.textViewUrlWebService.setText(webService.getUrlWebService());
        holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceEditListener.editClicked(position);
            }
        });

        return view;

    }
}
