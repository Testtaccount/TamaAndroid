package com.tama.chat.ui.fragments.tamaaccount;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.ProductsItem;
import com.tama.chat.tamaAccount.ProductsItemToSave;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.utils.image.ImageLoaderUtils;

public class SingleProductFragment extends Fragment implements TamaAccountHelperListener {

    private static final String ARG_PARAM = "param";

    private ProductsItem product;
    private TamaExpressActivity mListener;

    @Bind(R.id.item_products_image)
    ImageView itemProductsImage;

    @Bind(R.id.product_name_text)
    TextView productNameText;

    @Bind(R.id.product_description)
    TextView productDescription;

    @Bind(R.id.product_price)
    TextView productPrice;

    @Bind(R.id.add_to_cart)
    Button btnAddToCart;

    @Bind(R.id.back)
    Button btnBack;

    public SingleProductFragment() {}

    public static SingleProductFragment newInstance(ProductsItem product) {
        SingleProductFragment fragment = new SingleProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (ProductsItem) getArguments().getSerializable(ARG_PARAM);
//            productsItems = getListFromJson(mParam);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_product, container, false);
        ButterKnife.bind(this, view);
        initFields();
        return view;
    }

    private void initFields(){
        loadImageByUri(product.product_image, itemProductsImage);
        productNameText.setText(product.product_name );
        productPrice.setText(product.product_cost);
        productDescription.setText(product.product_desc);
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

    @OnClick(R.id.add_to_cart)
    public void OnClickAddToCart(){
        mListener.startCartAnimation();
        mListener.addProductToList(new ProductsItemToSave(product));
        mListener.refreshProductCountInButton();
        mListener.onBackPressed();
    }

    @OnClick(R.id.back)
    public void OnClickBack(){
        mListener.onBackPressed();
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
}
