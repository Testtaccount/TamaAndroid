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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.image.ImageLoaderUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoriesFragment extends Fragment implements TamaAccountHelperListener {

    private static final String ARG_PARAM_1 = "param";
    private static final String ARG_PARAM_2 = "boolean";

    private String mParam;
    private boolean isSubCategories;
    private List<CategoriesItem> categoriesItems;
    private TamaExpressActivity mListener;

    public CategoriesFragment() {}

    public static CategoriesFragment newInstance(String param, boolean isSubCategories) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_1, param);
        args.putBoolean(ARG_PARAM_2, isSubCategories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM_1);
            isSubCategories = getArguments().getBoolean(ARG_PARAM_2);
            if(isSubCategories){
                categoriesItems = getListFromSubJson(mParam);
            }else {
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
                if(categoriesItems.get(position).isSubCategoriesEmpty()){
                    new TamaAccountHelper().getListOfProducts(accountHelperListener,mListener.getCurrentCountry(),categoriesItems.get(position).category_id);
                }else{
                    mListener.setCurrentFragment(CategoriesFragment.newInstance(categoriesItems.get(position).sub_category,true));
                }
            }
        });
        if(!categoriesItems.isEmpty()) {
            listView.setAdapter(new CategoriesListAdapter(categoriesItems));
        }
        return view;
    }

    private List<CategoriesItem> getListFromSubJson(String data){
        if (data == null)
            return null;
        List<CategoriesItem> items = new ArrayList<>();
        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(data);
            Iterator<String> keys= jsonObject.keys();
            while (keys.hasNext())
            {
                String keyValue = (String)keys.next();
                JSONObject jsonItem = jsonObject.getJSONObject(keyValue);
                CategoriesItem item = new CategoriesItem();
                item.category_id = jsonItem.getString("category_id");
                item.parent_id = jsonItem.getString("parent_id");
                item.category_name = jsonItem.getString("category_name");
                item.category_desc = jsonItem.getString("category_desc");
                item.category_image = jsonItem.getString("category_image");
                item.sub_category = jsonItem.optString("sub_category");
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    private List<CategoriesItem> getListFromJson(String data){
        if (data == null)
            return null;
        List<CategoriesItem> items = new ArrayList<>();
        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(data);
            JSONArray jsonDataList = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < jsonDataList.length(); ++i){
                CategoriesItem item = new CategoriesItem();
                JSONObject jsonItem = jsonDataList.getJSONObject(i);
                item.category_id = jsonItem.getString("category_id");
                item.parent_id = jsonItem.getString("parent_id");
                item.category_name = jsonItem.getString("category_name");
                item.category_desc = jsonItem.getString("category_desc");
                item.category_image = jsonItem.getString("category_image");
                item.sub_category = jsonItem.optString("sub_category");
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
                    if(loadedImage!=null) {
                        v.setImageBitmap(loadedImage);
                    }else{
                        v.setImageResource(R.drawable.world_icon);
                    }
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
        mListener.setCurrentFragment(ProductsFragment.newInstance(data));
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

        public CategoriesListAdapter( List<CategoriesItem> data) {
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
                vi = inflater.inflate(R.layout.item_categories_list, null);
            CategoriesItem categoriesItem = data.get(position);
            ImageView view = (ImageView) vi.findViewById(R.id.item_categories_image);
            loadImageByUri(categoriesItem.category_image,view);
            ((TextView) vi.findViewById(R.id.item_categories_name)).setText(categoriesItem.category_name);
            return vi;
        }
    }

    public class CategoriesItem{
        public String category_id;
        public String parent_id;
        public String category_name;
        public String category_desc;
        public String category_image;
        public String sub_category;

        public boolean isSubCategoriesEmpty(){
            return sub_category.length()<10;
        }
    }
}
