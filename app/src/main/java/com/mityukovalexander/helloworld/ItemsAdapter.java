package com.mityukovalexander.helloworld;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> mItemList = new ArrayList<>();
    private int mPriceColor;
    private ItemAdapterListener mListener;

    public static SparseBooleanArray mSelectedItems = new SparseBooleanArray();

    public ItemsAdapter(int mPriceColorl) {
        this.mPriceColor = mPriceColorl;
    }

    public void setListener(ItemAdapterListener listener) {
        mListener = listener;

    }

    public boolean isSelected(final int position) {
        return mSelectedItems.get(position);
    }

    public void toogleItem(final int position) {
        mSelectedItems.put(position, !mSelectedItems.get(position));
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
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder viewHolder, int position) {
        final Item item = mItemList.get(position);
        viewHolder.bindItem(item, mSelectedItems.get(position));
        viewHolder.setListener(item, mListener, position);
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

    public void clearSelections() {
        mSelectedItems.clear();
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> selectedIds = new ArrayList<>();
        for (int i = 0; i < mItemList.size(); i++) {
            if (mSelectedItems.get(i)) {
                selectedIds.add(mItemList.get(i).getId());
            }
        }
        return selectedIds;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameView;
        private TextView mPriceView;
        private View mItemView;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            mNameView = itemView.findViewById(R.id.BudgetItemText);
            mPriceView = itemView.findViewById(R.id.BudgetItemPrice);
        }

        void bindItem(final Item item, final boolean selected) {
            mItemView.setSelected(selected);
            mNameView.setText(item.getName());
            mPriceView.setText(
                    mPriceView.getContext().getResources().getString(R.string.currencyRuble, String.valueOf(item.getPrice())));
        }

        public void setListener(final Item item, final ItemAdapterListener listener, final int position) {
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, position);

                }
            });
            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    listener.onItemLongClick(item, position);
                    return false;
                }
            });
        }
    }
}
