package com.tama.chat.ui.activities.tamaaccount;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.ui.fragments.tamaaccount.TamaHistoryFragment;
import com.tama.chat.utils.helpers.SharedHelper;

public class TamaHistoryActivity extends TamaAccountBaseActivity {

    private String HISTORY_ALL = "all";
    private String user_id;

    @Bind(R.id.error_message_text)
    TextView errorMessageText;

    @Override
    protected int getContentResId() {
        return R.layout.activity_tama_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTamaToolbar(R.string.mytama_history,R.string.history);
        initFields();
    }

    private void initFields() {
        user_id = new SharedHelper(this).getTamaAccountId();
        new TamaAccountHelper().getHistory(this,user_id,HISTORY_ALL);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void requestError(String data) {
        super.requestError(data);
        errorMessageText.setVisibility(View.VISIBLE);
        errorMessageText.setText(data);
//        setButtonEnable(true);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void requestSuccess(String data) {
        setCurrentFragment(TamaHistoryFragment.newInstance(data));
    }
}
