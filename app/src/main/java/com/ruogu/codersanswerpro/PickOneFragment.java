package com.ruogu.codersanswerpro;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.support.v7.widget.ShareActionProvider;

import com.ruogu.codersanswerpro.Models.Problem;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rogers on 2/13/14.
 * Show "Pick One" page.
 * Choose random problem for users to read.
 */
public class PickOneFragment extends Fragment {
    private WebView mWebView;
    private ArrayList<Problem> mProblemList;
    private String mProblemName;
    private ShareActionProvider mShareActionProvider;
    private boolean mStarred;
    private StarredFileHandler mStarredFileHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStarredFileHandler = StarredFileHandler.getInstance(getActivity());
        mProblemList = ((MainActivity) getActivity()).getProblemList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.pick_one, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mWebView = (WebView) view.findViewById(R.id.pick_one_webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //to see if it has savedInstance to restore.
        if (savedInstanceState != null) {
            mProblemName = savedInstanceState.getString("random_name");
        } else {
            Random rdm = new Random(System.currentTimeMillis());
            int index = Math.abs(rdm.nextInt() % mProblemList.size());
            mProblemName = mProblemList.get(index).mProblemName;
        }

        mStarred = mStarredFileHandler.isStarred(mProblemName);

//        String content = "";
//        try {
//            InputStream is = getActivity().getApplicationContext().getResources().getAssets()
//                    .open("code/"+ mProblemName +".html");
//            ByteArrayOutputStream bs = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int i = 0;
//            while ((i = is.read(buffer, 0, buffer.length)) > 0) {
//                bs.write(buffer, 0, i);
//            }
//            content = new String(bs.toString());
//        } catch (IOException e) {}
        mWebView.loadUrl("https://dl.dropboxusercontent.com/u/65861208/code/" +
                mProblemName + ".html");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("random_name", mProblemName);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.pick_one_action, menu);

        MenuItem shareItem = menu.findItem(R.id.pick_one_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //define share message.
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_text1)
                + mProblemName + getString(R.string.share_text2) + getString(R.string.app_link));
        mShareActionProvider.setShareIntent(shareIntent);

        MenuItem starItem = menu.findItem(R.id.pick_one_item_star);
        if (mStarred) {
            starItem.setIcon(R.drawable.ic_action_important);
        } else {
            starItem.setIcon(R.drawable.ic_action_not_important);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pick_one_item_star) {
            if (mStarred) {
                item.setIcon(R.drawable.ic_action_not_important);
                mStarred = false;
                mStarredFileHandler.deleteFromStarred(mProblemName);
            } else {
                item.setIcon(R.drawable.ic_action_important);
                mStarred = true;
                mStarredFileHandler.addToStarred(mProblemName);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
