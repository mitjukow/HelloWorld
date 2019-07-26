package com.mityukovalexander.helloworld;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mityukovalexander.helloworld.MainActivity.AUTH_TOKEN;

public class BalanceFragment extends Fragment {

    private DiagramView mDiagramView;
    private Api mApi;
    private TextView mTotalMoney;
    private TextView mExpenseMoney;
    private TextView mIncomeMoney;

    public static BalanceFragment newInstance() {
        BalanceFragment fragment = new BalanceFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_balance, container, false);
        mDiagramView = fragmentView.findViewById(R.id.diagramViewBalance);
        mTotalMoney = fragmentView.findViewById(R.id.totalMoney);
        mExpenseMoney = fragmentView.findViewById(R.id.expenseMoney);
        mIncomeMoney = fragmentView.findViewById(R.id.incomeMoney);
        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = ((MoneyApp) getActivity().getApplication()).getApi();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadBalance();
        
    }

    private void loadBalance() {
        final String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(AUTH_TOKEN, "");
        Call<BalanceResponse> balanceResponseCall = mApi.getBalance(token);
        balanceResponseCall.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                mTotalMoney.setText(getString(R.string.currencyRuble, String.valueOf(response.body().getTotalIncome() - response.body().getTotalExpense())));
                mExpenseMoney.setText(getString(R.string.currencyRuble, String.valueOf(response.body().getTotalExpense())));
                mIncomeMoney.setText(getString(R.string.currencyRuble, String.valueOf(response.body().getTotalIncome())));

                mDiagramView.update(response.body().getTotalIncome(), response.body().getTotalExpense());
            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {
            }
        });
    }
}
