package com.mityukovalexander.helloworld;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> mItemList = new ArrayList<>();
    private int mPriceColor;

    public ItemsAdapter(int mPriceColorl) {
        this.mPriceColor = mPriceColorl;
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = View.inflate(viewGroup.getContext(), R.layout.item_view, null);
        TextView priceView = itemView.findViewById(R.id.BudgetItemPrice);
        priceView.setTextColor(itemView.getContext().getResources().getColor(mPriceColor));
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder viewHolder, int i) {
        final Item item = mItemList.get(i);
        viewHolder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    void addItem(final Item item) {
        mItemList.add(item);
        notifyItemInserted(mItemList.size());
    }

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;
        private TextView mPriceView;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);


            mNameView = itemView.findViewById(R.id.BudgetItemText);
            mPriceView = itemView.findViewById(R.id.BudgetItemPrice);
        }

        void bindItem(final Item item) {
            mNameView.setText(item.getName());
            mPriceView.setText(
                    mPriceView.getContext().getResources().getString(R.string.currencyRuble, String.valueOf(item.getPrice())));
        }
    }
}
