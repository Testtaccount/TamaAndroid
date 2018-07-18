package com.tama.chat.ui.adapters.search;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.entry.findARetailerPojos.RetailerResult;
import com.tama.chat.ui.adapters.search.FindARetailersAdapter.RetailersHolder;
import java.util.ArrayList;
import java.util.List;

public class FindARetailersAdapter extends RecyclerView.Adapter<RetailersHolder> implements
    Filterable {

  private ArrayList<RetailerResult> mRetailerResults;
  private ArrayList<RetailerResult> filterList;
  private OnItemClickListener itemClickListener;
  private CustomFilter filter;


  public FindARetailersAdapter(ArrayList<RetailerResult> mRetailerResults,
      OnItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
    this.mRetailerResults = mRetailerResults;
    this.filterList = mRetailerResults;
  }

  public boolean isEmpty() {
    return mRetailerResults.size() == 0;
  }

  @Override
  public RetailersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Get the RecyclerView item layout
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_search_retailer, parent, false);
    return new RetailersHolder(view, itemClickListener);
  }

  @Override
  public void onBindViewHolder(RetailersHolder holder, int position) {

    Log.d("testt", "position" + position);
    holder.bindData(mRetailerResults.get(position));

  }


  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
  }

  @Override
  public int getItemCount() {
    if (mRetailerResults.isEmpty()) {
      return 0;
    }
    return mRetailerResults.size();
  }


  //RETURN FILTER OBJ
  @Override
  public Filter getFilter() {
    if (filter == null) {
      filter = new CustomFilter(filterList, this);
    }

    return filter;
  }

  public void setList(List<RetailerResult> list) {
    if (list != null ) {
      this.mRetailerResults.clear();
      this.mRetailerResults.addAll(list);
    }
    notifyDataSetChanged();
  }

  /**
   * RetailersHolder class for the recycler view item
   */

  public class CustomFilter extends Filter {

    FindARetailersAdapter adapter;
    ArrayList<RetailerResult> filterList;

    public CustomFilter(ArrayList<RetailerResult> filterList, FindARetailersAdapter adapter) {
      this.adapter = adapter;
      this.filterList = filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      FilterResults results = new FilterResults();

      //CHECK CONSTRAINT VALIDITY
      if (constraint != null && constraint.length() > 0) {
        //CHANGE TO UPPER
        constraint = constraint.toString().toUpperCase();
        //STORE OUR FILTERED PLAYERS
        ArrayList<RetailerResult> filterSpinnerItems = new ArrayList<>();

        for (int i = 0; i < filterList.size(); i++) {
          //CHECK
          if (filterList.get(i).getShopName().toUpperCase().contains(constraint)) {
            //ADD PLAYER TO FILTERED PLAYERS
            filterSpinnerItems.add(filterList.get(i));
          }
        }

        results.count = filterSpinnerItems.size();
        results.values = filterSpinnerItems;
      } else {
        results.count = filterList.size();
        results.values = filterList;

      }

      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

      adapter.mRetailerResults = (ArrayList<RetailerResult>) results.values;

      //REFRESH
      adapter.notifyDataSetChanged();
    }
  }

  public class RetailersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView rootCv;
    private TextView shopNameTv;
    private TextView shopAddressTv;

    public RetailerResult mRetailerResult;

    private OnItemClickListener itemClickListener;


    public RetailersHolder(View itemView, OnItemClickListener itemClickListener) {
      super(itemView);
      this.itemClickListener = itemClickListener;
      findViews(itemView);
    }

    void findViews(View view) {
      rootCv = (CardView) view.findViewById(R.id.item_search_retailer_root);
      rootCv.setOnClickListener(this);
      shopNameTv = (TextView) view.findViewById(R.id.tv_shop_name_label);
      shopAddressTv = (TextView) view.findViewById(R.id.tv_shop_address_label);
    }

    public void bindData(RetailerResult retailerResult) {

      this.mRetailerResult = retailerResult;
      shopNameTv.setText(retailerResult.getShopName());
      shopAddressTv.setText(retailerResult.getShopAddress());

    }

    private void notifyItemClicked() {
      if (itemClickListener != null) {
        itemClickListener.onItemClick(mRetailerResult);
      }
    }


    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.item_search_retailer_root:
          notifyItemClicked();
          break;
      }
    }

  }

  public interface OnItemClickListener {

    void onItemClick(RetailerResult retailerResult);

  }

}
