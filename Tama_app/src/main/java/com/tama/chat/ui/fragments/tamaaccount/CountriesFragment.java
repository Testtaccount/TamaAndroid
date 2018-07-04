package com.tama.chat.ui.fragments.tamaaccount;

import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.ProductsItemToSave;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.image.ImageLoaderUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CountriesFragment extends Fragment implements TamaAccountHelperListener {

    private static final String ARG_PARAM = "param";

    private String mParam;
    private List<CountriesItem> countriesItems;
    private TamaExpressActivity mListener;

    public CountriesFragment() {}

    public static CountriesFragment newInstance(String param) {
        CountriesFragment fragment = new CountriesFragment();
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
            countriesItems = getListFromJson(mParam);
        }
        mListener.setVisibleShoppingButton(VISIBLE);
        mListener.refreshProductCountInButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countries, container, false);
//        GridView gridView = (GridView) view.findViewById(R.id.grid_countries_view);
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) view.findViewById(R.id.grid_countries_view);

        final TamaAccountHelperListener accountHelperListener = this;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.setCurrentCountry(countriesItems.get(position).country_name);
                List<ProductsItemToSave> prodList = mListener.getProductsListToSave();

                Log.d("testt", String.valueOf(checkProductList(prodList)));
                if(prodList!=null){
//                    Log.d("testt", prodList.get(0).country);
                    Log.d("testt", mListener.getCurrentCountry());
                }

                if(checkProductList(prodList)) {
                    new TamaAccountHelper().getListOfCategories(accountHelperListener);
                }else{
                    createDialog(prodList);
                }
            }
        });
        if(!countriesItems.isEmpty()) {
            gridView.setAdapter(new CountriesListAdapter(countriesItems));
            gridView.setExpanded(true);
        }
        return view;
    }

    private boolean checkProductList(List<ProductsItemToSave> prodList) {

        return (prodList == null || prodList.isEmpty()) || prodList.get(0).country.equalsIgnoreCase(mListener.getCurrentCountry());

    }

    protected void createDialog(final List<ProductsItemToSave> prodList){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mListener);
        LayoutInflater inflater = mListener.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.topup_dialog_with_two_btn, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        final TamaAccountHelperListener accountHelperListener = this;


        TextView alertText = (TextView) dialogView.findViewById(R.id.dialog_text);
        alertText.setText(getString(R.string.alertTextFromCountry,prodList.get(0).country));


        Button btnYes = (Button)dialogView.findViewById(R.id.topup_button);
        btnYes.setText(R.string.close_btn);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                prodList.clear();
                mListener.setProductsListToSave(prodList);
                mListener.refreshProductCountInButton();
                new TamaAccountHelper().getListOfCategories(accountHelperListener);
            }
        });

        Button closeButton = (Button)dialogView.findViewById(R.id.continue_button);
        closeButton.setText(R.string.dlg_cancel);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }


    private List<CountriesItem> getListFromJson(String data){
        if (data == null)
            return null;
        List<CountriesItem> items = new ArrayList<>();
        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(data);
            JSONArray jsonDataList = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < jsonDataList.length(); ++i){
                CountriesItem item = new CountriesItem();
                JSONObject jsonItem = jsonDataList.getJSONObject(i);
                item.country_name = jsonItem.getString("country_name");
                item.country_img_path = jsonItem.getString("country_img_path");
                item.url = jsonItem.getString("url");
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
        ToastUtils.longToast(data);
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

    public class CountriesListAdapter extends BaseAdapter {

        List<CountriesItem> data;

        private LayoutInflater inflater = null;

        public CountriesListAdapter( List<CountriesItem> data) {
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
                vi = inflater.inflate(R.layout.item_countries_list, null);
            CountriesItem countriesItem = data.get(position);
            ImageView view = (ImageView) vi.findViewById(R.id.item_countries_image);
            loadImageByUri(countriesItem.country_img_path,view);
            String countryName = FirstCharToUpperCase(countriesItem.country_name);
            // countriesItem.country_name.substring(0,1).toUpperCase() + countriesItem.country_name.substring(1).toLowerCase();
            ((TextView) vi.findViewById(R.id.item_countries_name)).setText(countryName);
            return vi;
        }

        private String FirstCharToUpperCase(String str){
            str = str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
            for(int i=0;i<str.length();i++){
                if((str.charAt(i)=='-'||str.charAt(i)==' ')&& str.length()>i){
                    str =str.substring(0,i+1)+ str.substring(i+1,i+2).toUpperCase() + str.substring(i+2).toLowerCase();
                }
            }
            return str;
        }
    }

    public class CountriesItem{
        public String country_name;
        public String country_img_path;
        public String url;
    }
}
