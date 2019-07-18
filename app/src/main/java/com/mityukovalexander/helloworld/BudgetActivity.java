package com.mityukovalexander.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BudgetActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BudgetViewPagerAdapter mViewPagerAdapter;
    private FloatingActionButton mFloatingActionButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_budget);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPagerAdapter = new BudgetViewPagerAdapter(getSupportFragmentManager());

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText(R.string.outcome);
        mTabLayout.getTabAt(1).setText(R.string.income);
        mTabLayout.getTabAt(2).setText(R.string.balance);

        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabIndicatorColor));

        mFloatingActionButton = findViewById(R.id.addItemFAB);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                for (Fragment fragment : fragmentManager.getFragments()) {
                    if (fragment.getUserVisibleHint()) {
                        fragment.startActivityForResult(
                                new Intent((BudgetActivity.this), AddItemActivity.class),
                                101
                        );
                    }
                }
            }
        });

    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.selectForDeleteColor));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.selectForDeleteColor));
        mode.setTitle(getResources().getString(R.string.selected));
        mFloatingActionButton.hide();

    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mFloatingActionButton.show();

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 2) {
            mFloatingActionButton.hide();
            } else {
            mFloatingActionButton.show();
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    static class BudgetViewPagerAdapter extends FragmentPagerAdapter {
        public BudgetViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return BudgetFragment.newInstance(FragmentType.expense);
                case 1:
                    return BudgetFragment.newInstance(FragmentType.income);
                case 2:
                    return BalanceFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
