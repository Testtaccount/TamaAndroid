package com.tama.chat.tamaAccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tama.chat.R;

import java.util.List;

/**
 * Created by Avo on 18-May-17.
 */

public class IncomingRequestAdapter extends BaseAdapter {

    private final String TAMA_TOPUP_REQUEST = "TAMA TOPUP REQUEST";
    private final String MY_TAMA_TOPUP_REQUEST = "MY TAMA TOPUP REQUEST";
    private Context context;
    private List<IncomingRequestElement> data;

    private static LayoutInflater inflater = null;

    public IncomingRequestAdapter(Context context, List<IncomingRequestElement> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.item_inqoming_request, null);
        IncomingRequestElement element = data.get(position);

        ((TextView) vi.findViewById(R.id.incoming_request_item_text)).setText(element.getMessage());
        switch (element.getRequestFor()){
            case TAMA_TOPUP_REQUEST:
                ((ImageView) vi.findViewById(R.id.incoming_request_item_image)).setImageResource(R.drawable.incoming_tamatopup_logo);
                break;
            case MY_TAMA_TOPUP_REQUEST:
                ((ImageView) vi.findViewById(R.id.incoming_request_item_image)).setImageResource(R.drawable.incoming_mytama_logo);
                break;
            default:
                ((ImageView) vi.findViewById(R.id.incoming_request_item_image)).setImageResource(R.drawable.incoming_paytama_logo);
        }

        return vi;
    }
}
