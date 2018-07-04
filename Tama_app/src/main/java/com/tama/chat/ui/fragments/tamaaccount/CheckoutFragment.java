package com.tama.chat.ui.fragments.tamaaccount;

import static com.tama.chat.app.PhoneNumber.FIRST;
import static com.tama.chat.app.PhoneNumber.SECOND;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tama.chat.R;
import com.tama.chat.app.PhoneNumber;
import com.tama.chat.countryCode.Country;
import com.tama.chat.countryCode.CountryAdapter;
import com.tama.chat.countryCode.CustomPhoneNumberFormattingTextWatcher;
import com.tama.chat.countryCode.OnPhoneChangedListener;
import com.tama.chat.countryCode.PhoneUtils;
import com.tama.chat.tamaAccount.ProductsItemToSave;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.ui.fragments.chats.ContactsListFragment;
import com.tama.chat.utils.helpers.SharedHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CheckoutFragment extends Fragment implements TamaAccountHelperListener {

    protected static final TreeSet<String> CANADA_CODES = new TreeSet<String>();
    protected static final TreeSet<String> US_CODES = new TreeSet<String>();
    protected static final TreeSet<String> DO_CODES = new TreeSet<String>();
    protected static final TreeSet<String> PR_CODES = new TreeSet<String>();

    private static final String ARG_PARAM_1 = "param_1";
    private static final String ARG_PARAM_2 = "param_2";
    private static final String ARG_PARAM_4 = "param_4";
    private static final String ARG_PARAM_5 = "param_5";
    private static final String ARG_PARAM_3 = "param_3";
    private static final String TAG = "myLogs";

    //    private ProductsItem product;
    private TamaExpressActivity mListener;
    private SharedHelper sharedHelper;
    private boolean isEuropa;
    private boolean showConfirm;
    private boolean editFirstNumber = false;
    private boolean editSecondNumber = false;
    private String firstPhoneNumber, secondPhoneNumber;
    private CustomPhoneNumberFormattingTextWatcher textWatcherFirst;//,textWatcherSecond;
    private String productCountry;

    @Bind(R.id.confirm_btn)
    Button btnConfirm;

    @Bind(R.id.request_order_btn)
    Button btnRequestOrder;

    @Bind(R.id.checkbox)
    CheckBox checkBox;

    static {
        //USA
        US_CODES.add("201");
        US_CODES.add("202");
        US_CODES.add("203");
        US_CODES.add("205");
        US_CODES.add("206");
        US_CODES.add("207");
        US_CODES.add("208");
        US_CODES.add("209");
        US_CODES.add("210");
        US_CODES.add("212");
        US_CODES.add("213");
        US_CODES.add("214");
        US_CODES.add("215");
        US_CODES.add("216");
        US_CODES.add("217");
        US_CODES.add("218");
        US_CODES.add("219");
        US_CODES.add("224");
        US_CODES.add("225");
        US_CODES.add("228");
        US_CODES.add("229");
        US_CODES.add("231");
        US_CODES.add("234");
        US_CODES.add("239");
        US_CODES.add("240");
        US_CODES.add("248");
        US_CODES.add("251");
        US_CODES.add("252");
        US_CODES.add("253");
        US_CODES.add("254");
        US_CODES.add("256");
        US_CODES.add("260");
        US_CODES.add("262");
        US_CODES.add("267");
        US_CODES.add("269");
        US_CODES.add("270");
        US_CODES.add("276");
        US_CODES.add("281");
        US_CODES.add("301");
        US_CODES.add("302");
        US_CODES.add("303");
        US_CODES.add("304");
        US_CODES.add("305");
        US_CODES.add("307");
        US_CODES.add("308");
        US_CODES.add("309");
        US_CODES.add("310");
        US_CODES.add("312");
        US_CODES.add("313");
        US_CODES.add("314");
        US_CODES.add("315");
        US_CODES.add("316");
        US_CODES.add("317");
        US_CODES.add("318");
        US_CODES.add("319");
        US_CODES.add("320");
        US_CODES.add("321");
        US_CODES.add("323");
        US_CODES.add("325");
        US_CODES.add("330");
        US_CODES.add("334");
        US_CODES.add("336");
        US_CODES.add("337");
        US_CODES.add("339");
        US_CODES.add("347");
        US_CODES.add("351");
        US_CODES.add("352");
        US_CODES.add("360");
        US_CODES.add("361");
        US_CODES.add("386");
        US_CODES.add("401");
        US_CODES.add("402");
        US_CODES.add("404");
        US_CODES.add("405");
        US_CODES.add("406");
        US_CODES.add("407");
        US_CODES.add("408");
        US_CODES.add("409");
        US_CODES.add("410");
        US_CODES.add("412");
        US_CODES.add("413");
        US_CODES.add("414");
        US_CODES.add("415");
        US_CODES.add("417");
        US_CODES.add("419");
        US_CODES.add("423");
        US_CODES.add("425");
        US_CODES.add("430");
        US_CODES.add("432");
        US_CODES.add("434");
        US_CODES.add("435");
        US_CODES.add("440");
        US_CODES.add("443");
        US_CODES.add("469");
        US_CODES.add("478");
        US_CODES.add("479");
        US_CODES.add("480");
        US_CODES.add("484");
        US_CODES.add("501");
        US_CODES.add("502");
        US_CODES.add("503");
        US_CODES.add("504");
        US_CODES.add("505");
        US_CODES.add("507");
        US_CODES.add("508");
        US_CODES.add("509");
        US_CODES.add("510");
        US_CODES.add("512");
        US_CODES.add("513");
        US_CODES.add("515");
        US_CODES.add("516");
        US_CODES.add("517");
        US_CODES.add("518");
        US_CODES.add("520");
        US_CODES.add("530");
        US_CODES.add("540");
        US_CODES.add("541");
        US_CODES.add("551");
        US_CODES.add("559");
        US_CODES.add("561");
        US_CODES.add("562");
        US_CODES.add("563");
        US_CODES.add("567");
        US_CODES.add("570");
        US_CODES.add("571");
        US_CODES.add("573");
        US_CODES.add("574");
        US_CODES.add("575");
        US_CODES.add("580");
        US_CODES.add("585");
        US_CODES.add("586");
        US_CODES.add("601");
        US_CODES.add("602");
        US_CODES.add("603");
        US_CODES.add("605");
        US_CODES.add("606");
        US_CODES.add("607");
        US_CODES.add("608");
        US_CODES.add("609");
        US_CODES.add("610");
        US_CODES.add("612");
        US_CODES.add("614");
        US_CODES.add("615");
        US_CODES.add("616");
        US_CODES.add("617");
        US_CODES.add("618");
        US_CODES.add("619");
        US_CODES.add("620");
        US_CODES.add("623");
        US_CODES.add("626");
        US_CODES.add("630");
        US_CODES.add("631");
        US_CODES.add("636");
        US_CODES.add("641");
        US_CODES.add("646");
        US_CODES.add("650");
        US_CODES.add("651");
        US_CODES.add("660");
        US_CODES.add("661");
        US_CODES.add("662");
        US_CODES.add("678");
        US_CODES.add("682");
        US_CODES.add("701");
        US_CODES.add("702");
        US_CODES.add("703");
        US_CODES.add("704");
        US_CODES.add("706");
        US_CODES.add("707");
        US_CODES.add("708");
        US_CODES.add("712");
        US_CODES.add("713");
        US_CODES.add("714");
        US_CODES.add("715");
        US_CODES.add("716");
        US_CODES.add("717");
        US_CODES.add("718");
        US_CODES.add("719");
        US_CODES.add("720");
        US_CODES.add("724");
        US_CODES.add("727");
        US_CODES.add("731");
        US_CODES.add("732");
        US_CODES.add("734");
        US_CODES.add("740");
        US_CODES.add("754");
        US_CODES.add("757");
        US_CODES.add("760");
        US_CODES.add("763");
        US_CODES.add("765");
        US_CODES.add("770");
        US_CODES.add("772");
        US_CODES.add("773");
        US_CODES.add("774");
        US_CODES.add("775");
        US_CODES.add("781");
        US_CODES.add("785");
        US_CODES.add("786");
        US_CODES.add("801");
        US_CODES.add("802");
        US_CODES.add("803");
        US_CODES.add("804");
        US_CODES.add("805");
        US_CODES.add("806");
        US_CODES.add("808");
        US_CODES.add("810");
        US_CODES.add("812");
        US_CODES.add("813");
        US_CODES.add("814");
        US_CODES.add("815");
        US_CODES.add("816");
        US_CODES.add("817");
        US_CODES.add("818");
        US_CODES.add("828");
        US_CODES.add("830");
        US_CODES.add("831");
        US_CODES.add("832");
        US_CODES.add("843");
        US_CODES.add("845");
        US_CODES.add("847");
        US_CODES.add("848");
        US_CODES.add("850");
        US_CODES.add("856");
        US_CODES.add("857");
        US_CODES.add("858");
        US_CODES.add("859");
        US_CODES.add("860");
        US_CODES.add("862");
        US_CODES.add("863");
        US_CODES.add("864");
        US_CODES.add("865");
        US_CODES.add("866");
        US_CODES.add("870");
        US_CODES.add("901");
        US_CODES.add("903");
        US_CODES.add("904");
        US_CODES.add("906");
        US_CODES.add("907");
        US_CODES.add("908");
        US_CODES.add("909");
        US_CODES.add("910");
        US_CODES.add("912");
        US_CODES.add("913");
        US_CODES.add("914");
        US_CODES.add("915");
        US_CODES.add("916");
        US_CODES.add("917");
        US_CODES.add("918");
        US_CODES.add("919");
        US_CODES.add("920");
        US_CODES.add("925");
        US_CODES.add("928");
        US_CODES.add("931");
        US_CODES.add("936");
        US_CODES.add("937");
        US_CODES.add("940");
        US_CODES.add("941");
        US_CODES.add("947");
        US_CODES.add("949");
        US_CODES.add("951");
        US_CODES.add("952");
        US_CODES.add("954");
        US_CODES.add("956");
        US_CODES.add("970");
        US_CODES.add("971");
        US_CODES.add("972");
        US_CODES.add("973");
        US_CODES.add("978");
        US_CODES.add("979");
        US_CODES.add("980");
        US_CODES.add("985");
        US_CODES.add("989");

        //Dominican Republic
        DO_CODES.add("809");
        DO_CODES.add("829");
        DO_CODES.add("849");

        //Puerto Rico
        PR_CODES.add("787");
        PR_CODES.add("939");

        //Canada
        CANADA_CODES.add("204");
        CANADA_CODES.add("226");
        CANADA_CODES.add("236");
        CANADA_CODES.add("249");
        CANADA_CODES.add("250");
        CANADA_CODES.add("289");
        CANADA_CODES.add("306");
        CANADA_CODES.add("343");
        CANADA_CODES.add("365");
        CANADA_CODES.add("387");
        CANADA_CODES.add("403");
        CANADA_CODES.add("416");
        CANADA_CODES.add("418");
        CANADA_CODES.add("431");
        CANADA_CODES.add("437");
        CANADA_CODES.add("438");
        CANADA_CODES.add("450");
        CANADA_CODES.add("506");
        CANADA_CODES.add("514");
        CANADA_CODES.add("519");
        CANADA_CODES.add("548");
        CANADA_CODES.add("579");
        CANADA_CODES.add("581");
        CANADA_CODES.add("587");
        CANADA_CODES.add("604");
        CANADA_CODES.add("613");
        CANADA_CODES.add("639");
        CANADA_CODES.add("647");
        CANADA_CODES.add("672");
        CANADA_CODES.add("705");
        CANADA_CODES.add("709");
        CANADA_CODES.add("742");
        CANADA_CODES.add("778");
        CANADA_CODES.add("780");
        CANADA_CODES.add("782");
        CANADA_CODES.add("807");
        CANADA_CODES.add("819");
        CANADA_CODES.add("825");
        CANADA_CODES.add("867");
        CANADA_CODES.add("873");
        CANADA_CODES.add("902");
        CANADA_CODES.add("905");
    }

    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();

    protected PhoneNumberUtil mPhoneNumberUtilFirst = PhoneNumberUtil.getInstance();
    protected PhoneNumberUtil mPhoneNumberUtilSecond = PhoneNumberUtil.getInstance();
    private String products;
    private String balance;
    private double grandTotal;

    protected String mLastEnteredPhoneFirst = null;
    protected String mLastEnteredPhoneSecond = null;

    protected CountryAdapter mAdapterFirst, mAdapterSecond;

    @Bind(R.id.country_code_spinner_first)
    protected Spinner countryCodeSpinnerFirst;

    @Bind(R.id.country_code_spinner_second)
    protected Spinner countryCodeSpinnerSecond;

    @Bind(R.id.enter_phone_number_text_first)
    protected EditText enterPhoneNumberTextFirst;

    @Bind(R.id.enter_phone_number_text_second)
    protected EditText enterPhoneNumberTextSecond;

    @Bind(R.id.phone_number_text_second)
    protected TextView phoneNumberTextSecond;

    @Bind(R.id.sender_name)
    EditText senderName;

    @Bind(R.id.beneficiary_name)
    EditText beneficiaryName;

    @Bind(R.id.open_contacts_list_first)
    Button openContactsListFirst;

    @Bind(R.id.open_contacts_list_second)
    Button openContactsListSecond;

    @OnClick(R.id.open_contacts_list_first)
    public void clickOpenContactsListFirst(){
        editFirstNumber = true;
        editSecondNumber = true;
        mListener.setCurrentFragment(ContactsListFragment.newInstance(true,FIRST));
    }

    @OnClick(R.id.open_contacts_list_second)
    public void clickOpenContactsListSecond(){
        editFirstNumber = true;
        editSecondNumber = true;
        mListener.setCurrentFragment(ContactsListFragment.newInstance(true,SECOND));
    }

    public CheckoutFragment() {}

    public static CheckoutFragment newInstance(String dataStr, String productCountry,String balance,double grandTotal, boolean showConfirm) {
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_1, dataStr);
        args.putString(ARG_PARAM_2, productCountry);
        args.putString(ARG_PARAM_4, balance);
        args.putDouble(ARG_PARAM_5, grandTotal);
        args.putBoolean(ARG_PARAM_3, showConfirm);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedHelper = new SharedHelper(getActivity());
        mListener.setFirstPhoneNumber(sharedHelper.getUserFullPhoneNumber(),sharedHelper.getUserFullName());
        editFirstNumber= true;
        editSecondNumber = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            products = bundle.getString(ARG_PARAM_1);
            productCountry = bundle.getString(ARG_PARAM_2);
            balance = bundle.getString(ARG_PARAM_4);
            grandTotal = bundle.getDouble(ARG_PARAM_5);
            showConfirm = bundle.getBoolean(ARG_PARAM_3);
//            productCountry = productCountry.substring(0,1).toUpperCase() + productCountry.substring(1).toLowerCase();
        }


        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        ButterKnife.bind(this, view);

        initUI();
        initCodes();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mLastEnteredPhoneFirst!=null&&!mLastEnteredPhoneFirst.isEmpty()) {
            mLastEnteredPhoneFirst = null;
            if(senderName!=null&&!senderName.getText().toString().isEmpty()){
                mListener.setFirstPhoneNumber(enterPhoneNumberTextFirst.getText().toString(),
                        senderName.getText().toString());
            }else {
                mListener.setFirstPhoneNumber(enterPhoneNumberTextFirst.getText().toString());
            }
        }
        if(mLastEnteredPhoneSecond!=null&&!mLastEnteredPhoneSecond.isEmpty()) {
            mLastEnteredPhoneSecond = null;
            if(beneficiaryName!=null&&!beneficiaryName.getText().toString().isEmpty()){
                mListener.setSecondPhoneNumber(phoneNumberTextSecond.getText().toString()+
                                enterPhoneNumberTextSecond.getText().toString(), beneficiaryName.getText().toString());
            }else {
                mListener.setSecondPhoneNumber(phoneNumberTextSecond.getText().toString() +
                        enterPhoneNumberTextSecond.getText().toString());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        firstPhoneNumber = mListener.getFirstNumber();
        secondPhoneNumber = mListener.getSecondNumber();
    }

    @OnClick(R.id.request_order_btn)
    public void OnClickRequestOrder(){
        sendConfirmOrOrderRequest("retailer");
    }

    @OnClick(R.id.confirm_btn)
    public void OnClickConfirmBtn(){
//        double d1 = Double.valueOf(balance);
//        if(d1<grandTotal){
//            createDialog();
//        }else{
            sendConfirmOrOrderRequest("balance");
//        }
    }

    private void sendConfirmOrOrderRequest(String requestType){
        btnConfirm.setEnabled(false);
        String user_id = sharedHelper.getTamaAccountId();
        String sender_name = senderName.getText().toString();
        String sender_mobile = getFullPhoneNumberFirst();// enterPhoneNumberTextFirst.getText().toString();
        String receiver_name = beneficiaryName.getText().toString();
        String receiver_mobile = getFullPhoneNumberSecond();
        new TamaAccountHelper().setConfirm(this,user_id,products,sender_name,sender_mobile,receiver_name,receiver_mobile,requestType);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void requestSuccess(String data) {
        btnConfirm.setEnabled(true);
        createDialog(data,true);
        List<ProductsItemToSave> list = mListener.getProductsListToSave();
        list.clear();
        mListener.setProductsListToSave(list);
        mListener.refreshProductCountInButton();
        products = "";
    }

    @Override
    public void requestError(String data) {
        btnConfirm.setEnabled(true);
        createDialog(data,false);
    }

    @Override
    public void alertDialogCancelListener() {

    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    protected void createDialog(String message, final boolean isSuccess){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mListener);
        LayoutInflater inflater = mListener.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.topup_dialog_with_two_btn, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        TextView text = (TextView)dialogView.findViewById(R.id.dialog_text);
        text.setText(message);

        Button topupButton = (Button)dialogView.findViewById(R.id.topup_button);
        topupButton.setVisibility(View.GONE);

        Button closeButton = (Button)dialogView.findViewById(R.id.continue_button);
        closeButton.setText(getString(R.string.close));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                if(isSuccess){
                    Intent intent = new Intent(getActivity(),TamaExpressActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        alertDialog.show();
    }

    protected String getPhoneNumberFirst(){
        Country c = (Country) countryCodeSpinnerFirst.getSelectedItem();
        String phoneNumber = enterPhoneNumberTextFirst.getText().toString();
        phoneNumber = phoneNumber.replace(c.getCountryCodeStr(),"");
        return phoneNumber.replace(" ","");
    }

    protected String getCountryCodeFirst(){
        Country c = (Country) countryCodeSpinnerFirst.getSelectedItem();
        return c.getCountryCodeStr().replace("+","");
    }

    protected String getPhoneNumberSecond(){
//        Country c = (Country) countryCodeSpinnerSecond.getSelectedItem();
        String phoneNumber = enterPhoneNumberTextSecond.getText().toString();
//        phoneNumber = phoneNumber.replace(c.getCountryCodeStr(),"");
        return phoneNumber.replace(" ","");
    }

    protected String getCountryCodeSecond(){
        Country c = (Country) countryCodeSpinnerSecond.getSelectedItem();
        return c.getCountryCodeStr().replace("+","");
    }

    protected String getFullPhoneNumberFirst(){
        String phoneNumber = enterPhoneNumberTextFirst.getText().toString();
        phoneNumber = phoneNumber.replace(" ","");
        return phoneNumber.replace("+","");
    }

    protected String getFullPhoneNumberSecond(){
        String phoneNumber = phoneNumberTextSecond.getText().toString()+enterPhoneNumberTextSecond.getText().toString();
        phoneNumber = phoneNumber.replace(" ","");
        return phoneNumber.replace("+","");
    }

    protected void initUI() {
        countryCodeSpinnerFirst.setOnItemSelectedListener(getOnItemSelectedListener(countryCodeSpinnerFirst,FIRST));
        countryCodeSpinnerSecond.setOnItemSelectedListener(getOnItemSelectedListener(countryCodeSpinnerSecond,SECOND));

        mAdapterFirst = new CountryAdapter(getActivity());
        mAdapterSecond = new CountryAdapter(getActivity());

        countryCodeSpinnerFirst.setAdapter(mAdapterFirst);
        countryCodeSpinnerSecond.setAdapter(mAdapterSecond);
        countryCodeSpinnerSecond.setEnabled(false);

        textWatcherFirst = new CustomPhoneNumberFormattingTextWatcher(getOnPhoneChangedListener(countryCodeSpinnerFirst, mPhoneNumberUtilFirst,FIRST));

        enterPhoneNumberTextFirst.addTextChangedListener(textWatcherFirst);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (dstart > 0 && !Character.isDigit(c)) {
                        return "";
                    }
                }
                return null;
            }
        };

        enterPhoneNumberTextFirst.setFilters(new InputFilter[]{filter});
        enterPhoneNumberTextFirst.setImeOptions(EditorInfo.IME_ACTION_SEND);
        enterPhoneNumberTextFirst.setImeActionLabel(getString(R.string.label_send), EditorInfo.IME_ACTION_SEND);

        isEuropa = sharedHelper.getTamaIsEurope();
        if(isEuropa&&showConfirm){
            btnConfirm.setVisibility(View.VISIBLE);
        }
    }

    protected void initCodes() {
        new AsyncPhoneInitTask(getActivity(),countryCodeSpinnerFirst,mPhoneNumberUtilFirst,mAdapterFirst,FIRST).execute();
        new AsyncPhoneInitTask(getActivity(),countryCodeSpinnerSecond,mPhoneNumberUtilSecond,mAdapterSecond,SECOND).execute();
    }

    static boolean a = true;



    protected class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<Country>> {

        private int mSpinnerPosition = -1;
        private Context mContext;
        private Spinner spinner;
        private PhoneNumberUtil mPhoneNumberUtil;
        private CountryAdapter mAdapter;
        private PhoneNumber phoneNumber;


        public AsyncPhoneInitTask(Context context,Spinner spinner, PhoneNumberUtil mPhoneNumberUtil,CountryAdapter mAdapter, PhoneNumber phoneNumber) {
            mContext = context;
            this.spinner = spinner;
            this.mPhoneNumberUtil = mPhoneNumberUtil;
            this.mAdapter = mAdapter;
            this.phoneNumber = phoneNumber;
        }

        @Override
        protected ArrayList<Country> doInBackground(Void... params) {
            ArrayList<Country> data = new ArrayList<Country>(233);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(mContext.getApplicationContext().getAssets().open("countries.dat"), "UTF-8"));

                // do reading, usually loop until end of file reading
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    //process line
                    Country c = new Country(mContext, line, i);
                    data.add(c);
                    ArrayList<Country> list = mCountriesMap.get(c.getCountryCode());
                    if (list == null) {
                        list = new ArrayList<Country>();
                        mCountriesMap.put(c.getCountryCode(), list);
                    }
                    list.add(c);
                    i++;
                }
            } catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
            if(phoneNumber==FIRST) {
                String countryRegion = PhoneUtils.getCountryRegionFromPhone(mContext);
                int code = mPhoneNumberUtil.getCountryCodeForRegion(countryRegion);
                ArrayList<Country> list = mCountriesMap.get(code);
                if (list != null) {
                    for (Country c : list) {
                        if (c.getPriority() == 0) {
                            mSpinnerPosition = c.getNum();
                            break;
                        }
                    }
                }
            }else{
                for (Country c : data) {
                    if (c.getName().equals(productCountry)) {
                        mSpinnerPosition = c.getNum();
                        break;
                    }
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> data) {
            mAdapter.addAll(data);
            if (mSpinnerPosition > 0) {
                spinner.setSelection(mSpinnerPosition);
            }
        }
    }

    protected void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    protected AdapterView.OnItemSelectedListener getOnItemSelectedListener(final Spinner spinner, final PhoneNumber phoneNumber){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country c = (Country) spinner.getItemAtPosition(position);
                switch (phoneNumber){
                    case FIRST:
                        if (!editFirstNumber && mLastEnteredPhoneFirst != null && mLastEnteredPhoneFirst.startsWith(c.getCountryCodeStr())) {
                            return;
                        }
                        else if(editFirstNumber && firstPhoneNumber!=null&&!firstPhoneNumber.isEmpty()){
                            mLastEnteredPhoneFirst =firstPhoneNumber;
                            enterPhoneNumberTextFirst.setText(mLastEnteredPhoneFirst);
                            if(mListener.getFirstNumberName()!=null) {
                                senderName.setText(mListener.getFirstNumberName());
                            }
                            textWatcherFirst.setTextToTextEdit(mLastEnteredPhoneFirst);
                            textWatcherFirst.setEditabale();
                            editFirstNumber = false;
                            return;
                        }
                        enterPhoneNumberTextFirst.getText().clear();
                        enterPhoneNumberTextFirst.getText().insert(enterPhoneNumberTextFirst.getText().length() > 0 ? 1 : 0, String.valueOf(c.getCountryCode()));
                        enterPhoneNumberTextFirst.setSelection(enterPhoneNumberTextFirst.length());
                        mLastEnteredPhoneFirst = null;
                        break;
                    case SECOND:
                        if (!editSecondNumber && mLastEnteredPhoneSecond != null && mLastEnteredPhoneSecond.startsWith(c.getCountryCodeStr())) {
                            return;
                        }
                        String tempCountryCode = c.getCountryCodeStr();

                        phoneNumberTextSecond.setText(tempCountryCode);

                        if(editSecondNumber && secondPhoneNumber!=null&&!secondPhoneNumber.isEmpty()){
                            if(secondPhoneNumber.startsWith(tempCountryCode)){
                                secondPhoneNumber = secondPhoneNumber.replace(tempCountryCode,"");
                                mLastEnteredPhoneSecond =secondPhoneNumber;
                                if(mListener.getSecondsNumberName()!=null) {
                                    beneficiaryName.setText(mListener.getSecondsNumberName());
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage(getString(R.string.contact_alert_message) + productCountry);
                                builder.show();
                                mLastEnteredPhoneSecond ="";
                            }
                            enterPhoneNumberTextSecond.setText(mLastEnteredPhoneSecond);
                            editSecondNumber = false;
                            return;
                        }
                        mLastEnteredPhoneSecond = null;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    protected OnPhoneChangedListener getOnPhoneChangedListener(final Spinner spinner,final PhoneNumberUtil mPhoneNumberUtil,final PhoneNumber phoneNumber){
        return new OnPhoneChangedListener() {
            @Override
            public void onPhoneChanged(String phone) {
                try {
                    switch (phoneNumber){
                        case FIRST:
                            mLastEnteredPhoneFirst = phone;
                            break;
                        case SECOND:
                            mLastEnteredPhoneSecond = phone;
                            break;
                    }
                    Phonenumber.PhoneNumber p = mPhoneNumberUtil.parse(phone, null);
                    ArrayList<Country> list = mCountriesMap.get(p.getCountryCode());
                    Country country = null;
                    if (list != null) {
                        if (p.getCountryCode() == 1) {
                            String num = String.valueOf(p.getNationalNumber());
                            if (num.length() >= 3) {
                                String code = num.substring(0, 3);
                                if (CANADA_CODES.contains(code)) {
                                    for (Country c : list) {
                                        // Canada has priority 1, US has priority 0
                                        if (c.getPriority() == 1) {
                                            country = c;
                                            break;
                                        }
                                    }
                                }
                                else if (DO_CODES.contains(code)) {
                                    for (Country c : list) {
                                        // Dominican Republic has priority 2
                                        if (c.getPriority() == 2) {
                                            country = c;
                                            break;
                                        }
                                    }
                                }
                                else if (PR_CODES.contains(code)) {
                                    for (Country c : list) {
                                        // Puerto Rico has priority 3
                                        if (c.getPriority() == 3) {
                                            country = c;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (country == null) {
                            for (Country c : list) {
                                if (c.getPriority() == 0) {
                                    country = c;
                                    break;
                                }
                            }
                        }
                    }
                    if (country != null) {
                        final int position = country.getNum();
                        spinner.post(new Runnable() {
                            @Override
                            public void run() {
                                spinner.setSelection(position);
                            }
                        });
                    }
                } catch (NumberParseException ignore) {
                }
            }
        };
    }
}
