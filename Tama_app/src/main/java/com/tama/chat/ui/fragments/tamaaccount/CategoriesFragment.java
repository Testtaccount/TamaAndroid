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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.CategoriesItem;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.image.ImageLoaderUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoriesFragment extends Fragment implements TamaAccountHelperListener {

    private static final String ARG_PARAM_0 = "param_url";
    private static final String ARG_PARAM_1 = "param";
    private static final String ARG_PARAM_2 = "boolean";

    private String mParam;
    private boolean isSubCategories;
    private List<CategoriesItem> categoriesItems;
    private TamaExpressActivity mActivity;
    private String  url;

    public CategoriesFragment() {
    }

    public static CategoriesFragment newInstance(String url,String param, boolean isSubCategories) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_0, url);
        args.putString(ARG_PARAM_1, param);
        args.putBoolean(ARG_PARAM_2, isSubCategories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_PARAM_0);
            mParam = getArguments().getString(ARG_PARAM_1);
            isSubCategories = getArguments().getBoolean(ARG_PARAM_2);
            if (isSubCategories) {
                categoriesItems = getListFromSubJson(mParam);
            } else {
                categoriesItems = getListFromJson(mParam);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ListView listView = (ListView) view.findViewById(R.id.categories_list_view);
        final TamaAccountHelperListener accountHelperListener = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (categoriesItems.get(position).isSubCategoriesEmpty()) {
                    new TamaAccountHelper().getListOfProducts(accountHelperListener, url,
                        categoriesItems.get(position).category_id);
                } else {
                    mActivity.setCurrentFragment(
                        CategoriesFragment.newInstance(url,categoriesItems.get(position).sub_category, true));
                }
            }
        });
        if (!categoriesItems.isEmpty()) {
            listView.setAdapter(new CategoriesListAdapter(categoriesItems));
        }
        return view;
    }

    private List<CategoriesItem> getListFromSubJson(String data) {

        if (data == null) {
            return null;
        }
        List<CategoriesItem> items = new ArrayList<>();

        Map<String, String> cMap = new HashMap<>();
        JSONArray jsonarray = null;

        try {
            jsonarray = new JSONArray(data);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                String category_id = jsonobject.getString("category_id");
                String parent_id = jsonobject.getString("parent_id");
                String category_name = jsonobject.getString("category_name");
                String category_desc = jsonobject.getString("category_desc");
                String category_image = jsonobject.getString("category_image");
                String trans_lang = jsonobject.getString("trans_lang");
                String sub_category = jsonobject.getString("sub_category");

                CategoriesItem item = new CategoriesItem(category_id, parent_id, category_name,
                    category_desc, category_image, trans_lang, sub_category);

//        CountriesItem item = new CountriesItem(country_name, country_img_path, url);
                items.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    private List<CategoriesItem> getListFromJson(String data) {
        if (data == null) {
            return null;
        }
        List<CategoriesItem> items = new ArrayList<>();

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
        String categorys = map.get("result");
        Map<String, String> cMap = new HashMap<>();
        JSONArray jsonarray = null;

        try {
            jsonarray = new JSONArray(categorys);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                String category_id = jsonobject.getString("category_id");
                String parent_id = jsonobject.getString("parent_id");
                String category_name = jsonobject.getString("category_name");
                String category_desc = jsonobject.getString("category_desc");
                String category_image = jsonobject.getString("category_image");
                String trans_lang = jsonobject.getString("trans_lang");
                String sub_category = jsonobject.getString("sub_category");

                CategoriesItem item = new CategoriesItem(category_id, parent_id, category_name,
                    category_desc, category_image, trans_lang, sub_category);

//        CountriesItem item = new CountriesItem(country_name, country_img_path, url);
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
                    if (loadedImage != null) {
                        v.setImageBitmap(loadedImage);
                    } else {
                        v.setImageResource(R.drawable.world_icon);
                    }
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
        mActivity.setCurrentFragment(ProductsFragment.newInstance(data));
    }

    @Override
    public void requestError(String data) {
        ToastUtils.longToast(data);
    }

    @Override
    public void alertDialogCancelListener() {

    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    public class CategoriesListAdapter extends BaseAdapter {

        List<CategoriesItem> data;

        private LayoutInflater inflater = null;

        public CategoriesListAdapter(List<CategoriesItem> data) {
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
                vi = inflater.inflate(R.layout.item_categories_list, null);
            }
            CategoriesItem categoriesItem = data.get(position);
            ImageView view = (ImageView) vi.findViewById(R.id.item_categories_image);
            loadImageByUri(categoriesItem.category_image, view);
            ((TextView) vi.findViewById(R.id.item_categories_name)).setText(categoriesItem.category_name);
            return vi;
        }
    }

}
