package com.tama.chat.tamaAccount;

import static com.tama.chat.method.Methods.loadImageByUri;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.HistoryAdapter.HistoryViewHolder;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryResult;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

  private List<HistoryResult> mHistoryResults;
  private OnHistoryItemClickListener itemClickListener;

  public HistoryAdapter(List<HistoryResult> historyResults,
      OnHistoryItemClickListener itemClickListener) {
    this.mHistoryResults = historyResults;
    this.itemClickListener = itemClickListener;

  }

  public void set(List<HistoryResult> historyResults) {

//    if (historyResults == null || historyResults.size() == 0) {
//      return;
//    }
    if (historyResults != null && historyResults.size() > 0) {
      this.mHistoryResults.clear();
      this.mHistoryResults.addAll(historyResults);

    }
    notifyDataSetChanged();
  }

  public boolean isEmpty() {
    return mHistoryResults.size() == 0;
  }

  @Override
  public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Get the RecyclerView item layout
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_new_tama_historey_list, parent, false);
    return new HistoryViewHolder(view, itemClickListener);
  }

  @Override
  public void onBindViewHolder(HistoryViewHolder holder, int position) {

    holder.bindData(mHistoryResults.get(position));
  }


  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public int getItemCount() {
    if (mHistoryResults.isEmpty()) {
      return 0;
    }
    return mHistoryResults.size();
  }

  public void clear() {
    if (mHistoryResults != null) {
      mHistoryResults.clear();
    }
    notifyDataSetChanged();
  }


  /**
   * HistoryViewHolder class for the recycler view item
   */
  public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private LinearLayout rootLl;
    private TextView history_idTv;
    private TextView history_header_statusTv;
    private TextView history_amountTv;
    private TextView history_mobile_noTv;
    private TextView history_statusTv;
    private TextView history_timestampTv;
    private TextView btnTv;
    private ImageView mImageView;


    public HistoryResult mResult;

    private OnHistoryItemClickListener itemClickListener;


    public HistoryViewHolder(View itemView, OnHistoryItemClickListener itemClickListener) {
      super(itemView);
      this.itemClickListener = itemClickListener;
      findViews(itemView);
    }

    void findViews(View view) {
      rootLl = (LinearLayout) view.findViewById(R.id.item_history_card_root);
      rootLl.setOnClickListener(this);
      history_idTv = (TextView) view.findViewById(R.id.history_id);
      history_header_statusTv = (TextView) view.findViewById(R.id.history_header_status);
      history_amountTv = (TextView) view.findViewById(R.id.history_amount);
      history_mobile_noTv = (TextView) view.findViewById(R.id.history_mobile_no);
      history_statusTv = (TextView) view.findViewById(R.id.history_status);
      history_timestampTv = (TextView) view.findViewById(R.id.history_timestamp);
      mImageView = (ImageView) view.findViewById(R.id.history_image);
      btnTv = (TextView) view.findViewById(R.id.history_view_btn);

    }

    public void bindData(HistoryResult element) {

      this.mResult = element;

      history_idTv.setText(String.valueOf(element.getHistoryId()));
      history_header_statusTv.setText(element.getHistoryName());
      history_amountTv.setText(element.getAmount());
      if (element.getMobileNo() != null) {
        history_mobile_noTv.setText(String.valueOf(element.getMobileNo()));
      }else {
        history_mobile_noTv.setText("");
      }
      history_statusTv.setText(element.getStatus());
      history_timestampTv.setText(element.getTimestamp());
      loadImageByUri(element.getImage(),mImageView);

      btnTv.setOnClickListener(this);
    }

    private void notifyHistoryItemClicked() {
      if (itemClickListener != null) {
        itemClickListener.onOilHistoryItemClick(mResult);
      }
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.item_history_card_root:
//          notifyHistoryItemClicked();
          break;
        case R.id.history_view_btn:
          notifyHistoryItemClicked();
          break;
      }
    }

  }

  public interface OnHistoryItemClickListener {

    void onOilHistoryItemClick(HistoryResult result);

  }

}
