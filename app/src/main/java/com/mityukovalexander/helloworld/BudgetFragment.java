package com.mityukovalexander.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.TYPE;


public class BudgetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PRICE_COLOR = "price_color";

    private static final String TYPE = "type";
    private ItemsAdapter mItemsAdapter;
    private Api mApi;

    public BudgetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance(FragmentType fragmentType) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(PRICE_COLOR, fragmentType.getPriceColor());
        args.putString(TYPE, fragmentType.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = ((MoneyApp) getActivity().getApplication()).getApi();

    }


    @Override
    public void onStart() {
        super.onStart();
        loadItems();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);

        mItemsAdapter = new ItemsAdapter(getArguments().getInt(PRICE_COLOR));

        recyclerView.setAdapter(mItemsAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button addItemButton = fragmentView.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getContext(), AddItemActivity.class),
                        101
                );
            }
        });
        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {

            final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("auth_token", "");
            final int price = Integer.parseInt(data.getStringExtra("price"));
            final String name = data.getStringExtra("name");
            Call<Status> call = mApi.addItems(new AddItemsRequest(price, name, getArguments().getString(TYPE)), token);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    loadItems();
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {

                }
            });

        }

    }
    private void loadItems() {
        final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("auth_token", "");
        Call<List<Item>> itemsResponseCall = mApi.getItems(getArguments().getString(TYPE), token);
        itemsResponseCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                mItemsAdapter.clear();
                List<Item> itemsList = response.body();

                for (Item item : itemsList) {
                    mItemsAdapter.addItem(item);
                }
            }


            @Override
            public void onFailure(Call<List<Item>> call, Throwable t){
                }
            });
    }

    }