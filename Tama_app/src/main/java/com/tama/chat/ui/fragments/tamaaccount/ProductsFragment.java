package com.tama.chat.ui.fragments.tamaaccount;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.ProductsItem;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.utils.image.ImageLoaderUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductsFragment extends Fragment implements TamaAccountHelperListener {

    private static final String ARG_PARAM = "param";

    private String mParam;
    private List<ProductsItem> productsItems;
    private TamaExpressActivity mListener;

    public ProductsFragment() {}

    public static ProductsFragment newInstance(String param) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
            productsItems = getListFromJson(mParam);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.grid_products_view);
        final TamaAccountHelperListener accountHelperListener = this;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                new TamaAccountHelper().getListOfCategories(accountHelperListener);
                mListener.setCurrentFragment(SingleProductFragment.newInstance(productsItems.get(position)));
            }
        });
        if(!productsItems.isEmpty()) {
            gridView.setAdapter(new ProductsListAdapter(productsItems));
        }
        return view;
    }



    private List<ProductsItem> getListFromJson(String data){
        if (data == null)
            return null;
        List<ProductsItem> items = new ArrayList<>();
        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(data);
            JSONArray jsonDataList = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < jsonDataList.length(); ++i){
                ProductsItem item = new ProductsItem();
                JSONObject jsonItem = jsonDataList.getJSONObject(i);
                item.translation = jsonItem.optString("translation");
                item.product_id = jsonItem.getString("product_id");
                item.product_name = jsonItem.getString("product_name");
                item.product_tags = jsonItem.getString("product_tags");
                item.product_desc = jsonItem.getString("product_desc");
                item.product_category = jsonItem.getString("product_category");
                item.product_image = jsonItem.getString("product_image");
                item.country = jsonItem.getString("country");
                item.product_cost = jsonItem.getString("product_cost");
                item.status = jsonItem.getString("status");
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void loadImageByUri(String logoUrl,final ImageView v) {
        ImageLoader.getInstance().loadImage(logoUrl,
            ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS,
            new SimpleImageLoadingListener(){
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    v.setImageBitmap(loadedImage);
                }
            });
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TamaExpressActivity) {
            mListener = (TamaExpressActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void requestSuccess(String data) {
        mListener.setCurrentFragment(CategoriesFragment.newInstance(data,false));
    }

    @Override
    public void requestError(String data) {

    }

    @Override
    public void alertDialogCancelListener() {

    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public class ProductsListAdapter extends BaseAdapter {

        Context context;
        List<ProductsItem> data;

        private LayoutInflater inflater = null;

        public ProductsListAdapter( List<ProductsItem> data) {
            this.data = data;
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.item_products_list, null);
            ProductsItem product = data.get(position);
            ImageView view = (ImageView) vi.findViewById(R.id.item_products_image);
            loadImageByUri(product.product_image,view);
            ((TextView) vi.findViewById(R.id.item_products_name))
                    .setText(product.product_name + " " + product.product_cost);
            return vi;
        }
    }
}
