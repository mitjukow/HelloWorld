package com.mityukovalexander.helloworld;

public enum FragmentType {
    expense(R.color.colorPrimaryDark),
    income(R.color.incomePriceColor);

    FragmentType(int priceColor) {
        this.priceColor = priceColor;
    }

    private int priceColor;

    public int getPriceColor() {
        return priceColor;
    }
}
