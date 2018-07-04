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
import com.tama.chat.ui.activities.tamaaccount.TamaHistoryActivity;
import com.tama.chat.ui.fragments.tamaaccount.TamaSingleHistoryFragment;
import java.util.ArrayList;

/**
 * Created by Avo on 18-May-17.
 */

public class TamaHistoryAdapter extends BaseAdapter {

    TamaHistoryActivity activity;
    ArrayList<TamaHistoryElement> data;

    private static LayoutInflater inflater = null;

    public TamaHistoryAdapter(TamaHistoryActivity activity, ArrayList<TamaHistoryElement> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vi = inflater.inflate(R.layout.item_new_tama_historey_list, null);
        final TamaHistoryElement element = data.get(position);

        ((TextView) vi.findViewById(R.id.history_id)).setText(element.getHistoryId());
        ((TextView) vi.findViewById(R.id.history_header_status)).setText(element.getHistoryName());
        ((TextView) vi.findViewById(R.id.history_amount)).setText(element.getAmount());
        ((TextView) vi.findViewById(R.id.history_mobile_no)).setText(element.getPhoneNumber());
        ((TextView) vi.findViewById(R.id.history_status)).setText(element.getStatus());
        ((TextView) vi.findViewById(R.id.history_header_status)).setText(element.getHeaderStatus());
        ((TextView) vi.findViewById(R.id.history_timestamp)).setText(element.getTimeStamp());

        ((TextView) vi.findViewById(R.id.history_view_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setCurrentFragment(
                    TamaSingleHistoryFragment.newInstance(element.getUser_id(),element.getHistoryId()));
            }
        });

        loadImageByUri(element.getImageUrl(),(ImageView)vi.findViewById(R.id.history_image));
        return vi;
    }
}
