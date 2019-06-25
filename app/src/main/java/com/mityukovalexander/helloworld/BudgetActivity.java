package com.mityukovalexander.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class BudgetActivity extends AppCompatActivity {

    private ItemsAdapter mItemsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        mItemsAdapter = new ItemsAdapter();

        recyclerView.setAdapter(mItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mItemsAdapter.addItem(new Item("Krab", 480));
        mItemsAdapter.addItem(new Item("Pizza", 180));
        mItemsAdapter.addItem(new Item("Coffee prosto chtobi proverit rabotaet li perenos. a on rabotaet.", 30));

        Button addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            Item item = new Item(data.getStringExtra("name"), Integer.parseInt(data.getStringExtra("price")));
            mItemsAdapter.addItem(item);
        }
    }
}
