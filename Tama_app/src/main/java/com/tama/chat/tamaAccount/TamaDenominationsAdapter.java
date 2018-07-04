package com.tama.chat.tamaAccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tama.chat.R;

public class TamaDenominationsAdapter extends BaseAdapter {

    Context context;
    Denominations data;

    private static LayoutInflater inflater = null;

    public TamaDenominationsAdapter(Context context, Denominations data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.productList.size();
    }

    @Override
    public Object getItem(int position) {
        return data.productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.item_denominations, null);

        String str_1 = data.productList.get(position) + " " + data.destination_currency;
        String str_2 = data.retilPriceList.get(position) + " " + "€";

        ((TextView) vi.findViewById(R.id.denomination_text_1)).setText(str_1);
        ((TextView) vi.findViewById(R.id.denomination_text_2)).setText(str_2);
        return vi;
    }
}
