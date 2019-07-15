package com.mityukovalexander.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.TYPE;
import static com.mityukovalexander.helloworld.MainActivity.AUTH_TOKEN;


public class BudgetFragment extends Fragment implements ItemAdapterListener, ActionMode.Callback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PRICE_COLOR = "price_color";

    private static final String TYPE = "type";
    private SwipeRefreshLayout mSwipeRefresh;
    private ItemsAdapter mItemsAdapter;
    private Api mApi;
    private ActionMode mActionMode;

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

        mSwipeRefresh = fragmentView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        mItemsAdapter = new ItemsAdapter(getArguments().getInt(PRICE_COLOR));
        mItemsAdapter.setListener(this);

        recyclerView.setAdapter(mItemsAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {

            final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
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
                mSwipeRefresh.setRefreshing(false);
                mItemsAdapter.clear();
                List<Item> itemsList = response.body();

                for (Item item : itemsList) {
                    mItemsAdapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                mSwipeRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onItemClick(final Item item, final int position) {
        if (mItemsAdapter.isSelected(position)) {
            mItemsAdapter.toogleItem(position);
            mItemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemLongClick(final Item item, final int position) {
        mItemsAdapter.toogleItem(position);
        mItemsAdapter.notifyDataSetChanged();
        if (mActionMode == null) {
            ((AppCompatActivity) getActivity()).startSupportActionMode(this);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mActionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.item_menu_remove, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.deleteMenuItem) {
            showDialog();
        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mItemsAdapter.clearSelections();
        mItemsAdapter.notifyDataSetChanged();
        mActionMode = null;
    }

    private void showDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.remove_conf)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItems();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }

    private void removeItems() {
        List<Integer> selectedIds = mItemsAdapter.getSelectedItemIds();
        for (int selectedId : selectedIds) {
            removeItem(selectedId);
        }
    }

    private void removeItem(int selectedId) {
            final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("auth_token", "");
            Call<Status> itemsRemoveCall = mApi.removeItem(selectedId, token);
            itemsRemoveCall.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    loadItems();
                    mItemsAdapter.clearSelections();
                }
                @Override
                public void onFailure(final Call<Status> call, final Throwable t) {

                }
            });
        }

    }