package com.ruogu.codersanswerpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rogers on 3/5/14.
 */
public class StarredItemPager extends Fragment {
    private WebViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int mPosition;
    private ShareActionProvider mShareActionProvider;
    private boolean mStarred;
    private StarredFileHandler mStarredFileHandler;
    private Intent mShareIntent;
    private MenuItem mStarItem;

    public StarredItemPager() {

    }

    public int getCurrent() {
        return mPosition;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStarredFileHandler = StarredFileHandler.getInstance(getActivity());
        String problemName = getArguments().getString("problemName");
        for (int i = 0; i < mStarredFileHandler.getStarredList().size(); ++i) {
            if (mStarredFileHandler.getStarredList().get(i).equals(problemName)) {
                mPosition = i;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.starred_code_pager, container, false);
        mPager = (WebViewPager) view.findViewById(R.id.starred_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());//define slide animation
        mPager.setCurrentItem(getCurrent());

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //Log.i("call OnPageSelected","ok");
                String problemName = mStarredFileHandler.getStarredList().get(position);
                refreshOptionMenu(problemName);
            }
        });
        mPager.setOffscreenPageLimit(0);//缓存问题
        setHasOptionsMenu(true);//call onCreateOptionMenu
        return view;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            StarredItemFragment item = new StarredItemFragment();
            Bundle args = new Bundle();
            args.putString("problemName", mStarredFileHandler.getStarredList().get(position));
            item.setArguments(args);
            return item;
        }

        @Override
        public int getCount() {
            return mStarredFileHandler.getStarredList().size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.starred_list_item_action, menu);
        MenuItem item = menu.findItem(R.id.starred_list_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //define share message
        mShareIntent = new Intent(Intent.ACTION_SEND);
        mShareIntent.setType("text/*");
        mShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        int problemIndex = 0;
        if (mPager == null) {
            problemIndex = mPosition;
        } else {
            problemIndex = mPager.getCurrentItem();
        }
        String problemName = mStarredFileHandler.getStarredList().get(problemIndex);
        mShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_text1)
                + problemName + getString(R.string.share_text2) + getString(R.string.app_link));

        mShareActionProvider.setShareIntent(mShareIntent);
        if (mStarredFileHandler == null) {
            mStarredFileHandler = StarredFileHandler.getInstance(getActivity());
        }
        mStarred = mStarredFileHandler.isStarred(problemName);
        mStarItem = menu.findItem(R.id.starred_list_item_star);
        if (mStarred) {
            mStarItem.setIcon(R.drawable.ic_action_important);
        } else {
            mStarItem.setIcon(R.drawable.ic_action_not_important);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.starred_list_item_star) {
            if (mStarred) {
                item.setIcon(R.drawable.ic_action_not_important);
                mStarred = false;
                String problemToRemove = mStarredFileHandler.getStarredList().get(mPager.getCurrentItem());
                int size = mStarredFileHandler.getStarredList().size();
                int currentIndex = mPager.getCurrentItem();
                if (size == 1) {
                    mStarredFileHandler.deleteFromStarred(problemToRemove);
                    mPagerAdapter.notifyDataSetChanged();
                    //move back to star list
                    StarredFragment sf = new StarredFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, sf);
                    transaction.commit();
                } else if (currentIndex != size - 1) {
                    //not the last one, it will pop the next page to front
                    //so we need to refresh the options menu
                    String nextProblem = mStarredFileHandler.getStarredList().get(currentIndex + 1);
                    mStarredFileHandler.deleteFromStarred(problemToRemove);
                    mPagerAdapter.notifyDataSetChanged();
                    refreshOptionMenu(nextProblem);
                } else {
                    //last one, so it will find the previous page, don't need to refresh options menu
                    mStarredFileHandler.deleteFromStarred(problemToRemove);
                    mPagerAdapter.notifyDataSetChanged();
                }

            } else {
                item.setIcon(R.drawable.ic_action_important);
                mStarred = true;
                mStarredFileHandler.addToStarred(mStarredFileHandler.getStarredList().get(mPager.getCurrentItem()));
                mPagerAdapter.notifyDataSetChanged();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void refreshOptionMenu(String currentProblemName) {
        mShareIntent = new Intent(Intent.ACTION_SEND);
        mShareIntent.setType("text/*");
        mShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        mShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_text1)
                + currentProblemName + getString(R.string.share_text2) + getString(R.string.app_link));
        mShareActionProvider.setShareIntent(mShareIntent);
        mStarred = mStarredFileHandler.isStarred(currentProblemName);
        if (mStarred) {
            mStarItem.setIcon(R.drawable.ic_action_important);
        } else {
            mStarItem.setIcon(R.drawable.ic_action_not_important);
        }
    }
}
