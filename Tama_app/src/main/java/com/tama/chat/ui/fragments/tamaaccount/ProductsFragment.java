package com.tama.chat.ui.fragments.tamaaccount;

import static com.tama.chat.tamaAccount.TamaAccountHelper.parse;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductsFragment extends Fragment implements TamaAccountHelperListener {

    private static final String ARG_PARAM = "param";

    private String mParam;
    private List<ProductsItem> productsItems;
    private TamaExpressActivity mActivity;

    public ProductsFragment() {
    }

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
                mActivity
                    .setCurrentFragment(SingleProductFragment.newInstance(productsItems.get(position)));
            }
        });
        if (!productsItems.isEmpty()) {
            gridView.setAdapter(new ProductsListAdapter(productsItems));
        }
        return view;
    }

    private List<ProductsItem> getListFromJson(String data) {
        if (data == null) {
            return null;
        }
        List<ProductsItem> items = new ArrayList<>();

        JSONObject object = null;
        Map<String, String> map = new HashMap<>();
        try {
            object = new JSONObject(data);
            JSONObject jsonObject = object.getJSONObject("data");
            parse(jsonObject, map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String message = map.get("message");
        String products = map.get("result");
        Map<String, String> cMap = new HashMap<>();
        JSONArray jsonarray = null;

        try {
            jsonarray = new JSONArray(products);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                String product_id = jsonobject.getString("product_id");
                String product_name = jsonobject.getString("product_name");
                String product_tags = jsonobject.getString("product_tags");
                String product_desc = jsonobject.getString("product_desc");
                String category_name = jsonobject.getString("category_name");
                String product_image = jsonobject.getString("product_image");
                String product_country = jsonobject.getString("product_country");
                String product_cost = jsonobject.getString("product_cost");
                String product_cost_ws = jsonobject.getString("product_cost_ws");
                String shipping_available = jsonobject.getString("shipping_available");
                String free_shipping = jsonobject.getString("free_shipping");
                String min_to_order = jsonobject.getString("min_to_order");
                String max_to_order = jsonobject.getString("max_to_order");
                String shipping_cost = jsonobject.getString("shipping_cost");
                String shipping_cost_ws = jsonobject.getString("shipping_cost_ws");
                String lang = jsonobject.getString("lang");

                ProductsItem item = new ProductsItem(
                    product_id,
                    product_name,
                    product_tags,
                    product_desc,
                    category_name,
                    product_image,
                    product_country,
                    product_cost,
                    product_cost_ws,
                    shipping_available,
                    free_shipping,
                    min_to_order,
                    max_to_order,
                    shipping_cost,
                    shipping_cost_ws,
                    lang
                );
                items.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void loadImageByUri(String logoUrl, final ImageView v) {
        ImageLoader.getInstance().loadImage(logoUrl,
            ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS,
            new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    v.setImageBitmap(loadedImage);
                }
            });
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mActivity != null) {
//            mActivity.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TamaExpressActivity) {
            mActivity = (TamaExpressActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void requestSuccess(String data) {
        mActivity.setCurrentFragment(CategoriesFragment.newInstance("url", data, false));
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

        public ProductsListAdapter(List<ProductsItem> data) {
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
            if (vi == null) {
                vi = inflater.inflate(R.layout.item_products_list, null);
            }
            ProductsItem product = data.get(position);
            ImageView view = (ImageView) vi.findViewById(R.id.item_products_image);
            loadImageByUri(product.product_image, view);
            ((TextView) vi.findViewById(R.id.item_products_name))
                .setText(product.product_name + " " + product.product_cost);
            return vi;
        }
    }
}
