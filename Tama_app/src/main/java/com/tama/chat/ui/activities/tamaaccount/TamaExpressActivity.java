package com.tama.chat.ui.activities.tamaaccount;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tama.chat.tamaAccount.TamaAccountHelper.parse;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.CountriesItem;
import com.tama.chat.tamaAccount.ProductsItemToSave;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.ui.fragments.tamaaccount.CountriesFragment;
import com.tama.chat.ui.fragments.tamaaccount.ShoppingCartFragment;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.helpers.SharedHelper;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.google.gson.reflect.TypeToken;

public class TamaExpressActivity extends TamaAccountBaseActivity {

  private static final String TAG = "myLogs";
  private String currentCountry;
  private int screenWidth;
  //    private List<ProductsItemToSave> productsListToSave = new ArrayList<>();
  private List<CountriesItem> countriesItems;

  @Override
  protected int getContentResId() {
    return R.layout.activity_tama_express;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTamaToolbar(R.string.tama_express_one_line, R.string.tama_express_one_line);
    initFields();
  }

  private void initFields() {
    countriesItems = new ArrayList<>();
    String user_id = new SharedHelper(this).getTamaAccountId();
    if (isNetworkAvailable()) {
      new TamaAccountHelper().getListOfCountries(this);
    } else {
      Toast.makeText(this, getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
    }
    screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
  }

  public List<ProductsItemToSave> getProductsListToSave() {
    String strList = new SharedHelper(this).getProductsList();
    if (strList != null) {
      Gson gson = new Gson();
      Type type = new TypeToken<List<ProductsItemToSave>>() {
      }.getType();
      return gson.fromJson(strList, type);
    }
    return null;
  }

  public void setProductsListToSave(List<ProductsItemToSave> productList) {
    Gson gson = new Gson();
    String jsonStr = gson.toJson(productList);
    new SharedHelper(this).saveProductsList(jsonStr);
  }

  public void addProductToList(ProductsItemToSave product) {
    List<ProductsItemToSave> listProd = getProductsListToSave();
    if (listProd != null) {
      findAndAddProduct(listProd, product);
    } else {
      listProd = new ArrayList<>();
      listProd.add(product);
    }
    setProductsListToSave(listProd);
  }

  private void findAndAddProduct(List<ProductsItemToSave> listProd, ProductsItemToSave product) {
    for (ProductsItemToSave item : listProd) {
      if (item.product_id.equals(product.product_id)) {
        item.count = item.count + 1;
        return;
      }
    }
    listProd.add(product);
  }

  public String getProductsCount() {
    List<ProductsItemToSave> listProd = getProductsListToSave();
    if (listProd != null && !listProd.isEmpty()) {
      int count = 0;
      for (ProductsItemToSave item : listProd) {
        count += getDouble(String.valueOf(item.count));
      }
      return String.valueOf(count);
    }
    return null;
  }

  public void refreshProductCountInButton() {
    String count = getProductsCount();
    if (count != null) {
      setVisibleProductsCount(VISIBLE);
      setProductCountInButton(count);
    } else {
      setVisibleProductsCount(GONE);
    }
  }


  public void startCartAnimation() {
    Animation anim = AnimationUtils.loadAnimation(this, R.anim.shoping_cart_animation);
    anim.setStartOffset(screenWidth - 1000);
    shoppingButton.startAnimation(anim);
  }

  @Override
  public void onBackPressed() {
    if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
      finish();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onClickOnShoppingCart() {
    super.onClickOnShoppingCart();
    setCurrentFragment(ShoppingCartFragment.newInstance());
  }

  public void setCurrentCountry(String name) {
    currentCountry = name;
  }

  public String getCurrentCountry() {
    return currentCountry;
  }

  @Override
  public void requestSuccess(String data) {
    super.requestSuccess(data);

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
    String countrys = map.get("result");
    Map<String, String> cMap = new HashMap<>();
    JSONArray jsonarray = null;

    try {
      jsonarray = new JSONArray(countrys);
      for (int i = 0; i < jsonarray.length(); i++) {
        JSONObject jsonobject = jsonarray.getJSONObject(i);
        String country_name = jsonobject.getString("country_name");
        String country_img_path = jsonobject.getString("country_img_path");
        String url = jsonobject.getString("url");
        CountriesItem item = new CountriesItem(country_name, country_img_path, url);
        countriesItems.add(item);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }
    setCurrentFragment(CountriesFragment.newInstance(countriesItems));
  }

  @Override
  public void requestError(String data) {
    super.requestError(data);
    ToastUtils.longToast(data);
  }

  @Override
  public Context getAppContext() {
    return this;
  }

}
