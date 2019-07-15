package com.mityukovalexander.helloworld;

public interface ItemAdapterListener {
    void onItemClick(Item item, int position);

    void onItemLongClick(Item item, int position);
}
