package com.tama.chat.tamaAccount;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.R;
import com.tama.chat.utils.image.ImageLoaderUtils;
import java.util.List;

/**
 * Created by Avo on 18-May-17.
 */

public class OutGoingRequestAdapter extends BaseAdapter {

    private final String STATUS_ACCEPT = "Accepted";
    private final String STATUS_PROGRESS = "In Progress";
    private final String STATUS_DENIED = "Denied";
    private Context context;
    private List<IncomingRequestElement> data;

    private static LayoutInflater inflater = null;

    public OutGoingRequestAdapter(Context context, List<IncomingRequestElement> data) {
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
            vi = inflater.inflate(R.layout.item_outgoing_request, null);
        IncomingRequestElement element = data.get(position);

        ((TextView) vi.findViewById(R.id.outgoing_request_item_text)).setText(element.getMessage());
        loadImageByUri(element.getImageUrl(),(ImageView)vi.findViewById(R.id.outgoing_request_item_image));
        String request_status = element.getRequestStatus();
        TextView request_status_text = (TextView) vi.findViewById(R.id.outgoing_request_status);
        switch (request_status){
            case STATUS_ACCEPT:
                request_status_text.setBackgroundResource(R.drawable.background_green_oval);
                request_status_text.setText(R.string.success);
                break;
            case STATUS_PROGRESS:
                request_status_text.setBackgroundResource(R.drawable.background_orange_oval);
                request_status_text.setText(R.string.progress);
                break;
            default:
                request_status_text.setBackgroundResource(R.drawable.background_red_oval);
                request_status_text.setText(R.string.denied);
        }

        return vi;
    }

    private void loadImageByUri(String logoUrl,final ImageView v) {
   ImageLoader.getInstance().loadImage(logoUrl, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS,
       new SimpleImageLoadingListener(){
           @Override
           public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
               super.onLoadingComplete(imageUri, view, loadedImage);
               v.setImageBitmap(loadedImage);

           }
       });
    }
}
