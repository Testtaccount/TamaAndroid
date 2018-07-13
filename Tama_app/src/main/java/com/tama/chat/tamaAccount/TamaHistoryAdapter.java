package com.tama.chat.tamaAccount;

import static com.tama.chat.method.Methods.loadImageByUri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avo on 18-May-17.
 */

public class TamaHistoryAdapter extends BaseAdapter {

//    TamaHistoryActivity activity;
    List<HistoryResult> data=new ArrayList<>();

    private static LayoutInflater inflater = null;

    public TamaHistoryAdapter( List<HistoryResult> data) {
//        this.activity = activity;
        this.data = data;
//        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_new_tama_historey_list, null);
        final HistoryResult element = data.get(position);

        ((TextView) vi.findViewById(R.id.history_id)).setText(String.valueOf(element.getHistoryId()));
        ((TextView) vi.findViewById(R.id.history_header_status)).setText(element.getHistoryName());
        ((TextView) vi.findViewById(R.id.history_amount)).setText(element.getAmount());
        ((TextView) vi.findViewById(R.id.history_mobile_no)).setText(String.valueOf(element.getMobileNo()));
        ((TextView) vi.findViewById(R.id.history_status)).setText(element.getStatus());
        ((TextView) vi.findViewById(R.id.history_header_status)).setText(element.getHeaderStatus());
        ((TextView) vi.findViewById(R.id.history_timestamp)).setText(element.getTimestamp());

        ((TextView) vi.findViewById(R.id.history_view_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.setCurrentFragment(  TamaSingleHistoryFragment.newInstance(String.valueOf(element.getUserId()),element.getHistoryId()));
            }
        });

        loadImageByUri(element.getImage(),(ImageView)vi.findViewById(R.id.history_image));
        return vi;
    }
}
