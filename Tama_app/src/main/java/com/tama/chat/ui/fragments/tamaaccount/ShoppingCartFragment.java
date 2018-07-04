package com.tama.chat.ui.fragments.tamaaccount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.ProductsItemToSave;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.ui.activities.tamaaccount.TopupMyAccountActivity;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.helpers.SharedHelper;
import com.tama.chat.utils.image.ImageLoaderUtils;
import java.util.List;

public class ShoppingCartFragment extends Fragment implements TamaAccountHelperListener {

    private static final String TAG = "myLogs";
    private List<ProductsItemToSave> productItems;
    private TamaExpressActivity mListener;
    private double grandTotal = 0;
//    private EditText editTextOnFocus;
    private String balance;
    private ProductToSaveListAdapter productListAdapter;

    @Bind(R.id.grand_total_text)
    TextView grandTotalView;

    @Bind(R.id.shopping_cart_list_view)
    ListView productListView;

    @Bind(R.id.continue_shopping_btn)
    Button continueShoppingBtn;

    @Bind(R.id.checkout_btn)
    Button checkoutBtn;

    public ShoppingCartFragment() {}

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        return fragment;
    }

    @OnClick(R.id.continue_shopping_btn)
    public void OnClickContinueShopping(){
        mListener.onBackPressed();
    }

    @OnClick(R.id.checkout_btn)
    public void onClickCheckOutBtn(){
        if(productItems==null||productItems.isEmpty())
            return;
        setEnableBtn(false);
        String user_id = new SharedHelper(mListener).getTamaAccountId();
        new TamaAccountHelper().getBalance(this.getContext(), this ,user_id);
    }

    private void setEnableBtn(boolean bool){
        checkoutBtn.setEnabled(bool);
        continueShoppingBtn.setEnabled(bool);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productItems = mListener.getProductsListToSave();
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.shoppingButton.setEnabled(false);
        productItems = mListener.getProductsListToSave();
        refreshProductListView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mListener.shoppingButton.setEnabled(true);
        mListener.setProductsListToSave(productItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        ButterKnife.bind(this, view);
        productListView.setClickable(false);
        if(productItems!=null&&!productItems.isEmpty()) {
            productListAdapter = new ProductToSaveListAdapter(productItems);
            productListView.setAdapter(productListAdapter);
        }
        setGrandTotal();
        return view;
    }

    private void setGrandTotal(){
        grandTotal = 0;
        if(productItems!=null&&!productItems.isEmpty()) {
            for(ProductsItemToSave item:productItems){
                grandTotal += (mListener.getDouble(item.count)*mListener.getDouble(item.product_cost));
            }
        }
        grandTotalView.setText(String.format("%.2f", grandTotal)+getString(R.string.euro));
    }

    private void refreshProductListView(){
        grandTotal = 0;
//        productItems = mListener.getProductsListToSave();
        if(productItems!=null&&!productItems.isEmpty()) {
            productListView.setAdapter(new ProductToSaveListAdapter(productItems));
        }else{
            productListView.setAdapter(null);
        }
        setGrandTotal();
        mListener.refreshProductCountInButton();
    }

    private void loadImageByUri(String logoUrl, final ImageView v) {
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

    protected void createDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mListener);
        LayoutInflater inflater = mListener.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.topup_dialog_with_two_btn, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();


        Button topupButton = (Button)dialogView.findViewById(R.id.topup_button);
        topupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                if(!new SharedHelper(getActivity()).getTamaIsEurope()){
                    createMessageDialog(getString(R.string.message_available_european_users),"","");
                    return;
                }

                alertDialogCancelListener();
            }
        });

//        Button closeButton = (Button)dialogView.findViewById(R.id.close_button);
        Button closeButton = (Button)dialogView.findViewById(R.id.continue_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
//                alertDialog.dismiss();
                String ids = getStringIds();
                mListener.setCurrentFragment(CheckoutFragment.newInstance(ids,getProductCountry(),balance,grandTotal,false));
            }
        });
        alertDialog.show();
    }

    protected void createMessageDialog(String textMessage, String title, String subTitle){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.topup_dialog_view, null);
        dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        TextView textTitle = (TextView) dialogView.findViewById(R.id.topup_dialog_title_text);
        TextView textBig = (TextView) dialogView.findViewById(R.id.topup_dialog_big_text);
        TextView textView = (TextView) dialogView.findViewById(R.id.topup_dialog_subtitle_text);
        Button topupDialogButton = (Button)dialogView.findViewById(R.id.topup_dialog_button);

        textBig.setText(textMessage);
        textBig.setTextSize(16);
        textBig.setTextColor(Color.BLACK);
        topupDialogButton.setText(getString(R.string.ok));
        topupDialogButton.setBackground( ContextCompat.getDrawable(getActivity(),R.drawable.selector_button_red_oval));

        topupDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    private String getProductCountry(){
        return productItems.get(0).country;
    }

    @Override
    public void requestSuccess(String data) {
        balance = data;
        if(mListener.getDouble(data)<grandTotal){
            createDialog();
        }else{
            String ids = getStringIds();
            mListener.setCurrentFragment(CheckoutFragment.newInstance(ids,getProductCountry(),balance,grandTotal,true));
        }
        setEnableBtn(true);
    }

    private String getStringIds() {
        String ids = "";
        for(ProductsItemToSave item:productItems){
            if(mListener.getDouble(item.count)==1){
                ids +=item.product_id+",";
            }else if(mListener.getDouble(item.count)>1){
                ids +=item.product_id+"-"+item.count+",";
            }
        }
        if (ids.length() > 0) {
            ids = ids.substring(0, ids.length()-1);
        }
        return ids;
    }

    @Override
    public void requestError(String data) {
        setEnableBtn(true);
        ToastUtils.longToast(data);
    }

    @Override
    public void alertDialogCancelListener() {
        Intent intent = new Intent(mListener,TopupMyAccountActivity.class);
        intent.putExtra(CURRENT_BALANCE, balance);
        startActivity(intent);
        setEnableBtn(true);
    }

    @Override
    public Context getAppContext() {
        return getContext();
    }


    public class ProductToSaveListAdapter extends BaseAdapter {

        List<ProductsItemToSave> data;

        private LayoutInflater inflater = null;

        public ProductToSaveListAdapter( List<ProductsItemToSave> data) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null) {
//                LayoutInflater v;
//                v = LayoutInflater.from(getContext());
                vi = inflater.inflate(R.layout.item_shopping_cart_list_view, null);
                final ProductsItemToSave productItem = data.get(position);
                ImageView view = (ImageView) vi.findViewById(R.id.image_view);
                loadImageByUri(productItem.product_image,view);
                ((TextView) vi.findViewById(R.id.name_text_view)).setText(productItem.product_name);
                ((TextView) vi.findViewById(R.id.unite_price_text_view)).setText(productItem.product_cost);
                final EditText countText = (EditText) vi.findViewById(R.id.quantity_text_view);
                final TextView totalText = (TextView) vi.findViewById(R.id.total_text_view);
                double d = Double.valueOf(productItem.count);
                countText.setText(String.valueOf((int)d));

                countText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        productItem.count = (countText.getText().toString().isEmpty()?"0":countText.getText().toString());
                        double total = mListener.getDouble(productItem.product_cost)*Double.valueOf(productItem.count.isEmpty()?"0":productItem.count);
                        totalText.setText(String.format("%.2f", total)+ getString(R.string.euro));
                        setGrandTotal();
                        mListener.setProductsListToSave(productItems);
                        mListener.refreshProductCountInButton();
                    }
                });

                double total = mListener.getDouble(productItem.product_cost)*Double.valueOf(productItem.count.isEmpty()?"0":productItem.count);
                totalText.setText(String.format("%.2f", total)+ getString(R.string.euro));

                ImageView removeBtn = (ImageView) vi.findViewById(R.id.remove_view);
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"remove Button");
                        data.remove(position);
                        mListener.setProductsListToSave(data);
                        refreshProductListView();
                    }
                });
            }
            return vi;
        }
    }
}
