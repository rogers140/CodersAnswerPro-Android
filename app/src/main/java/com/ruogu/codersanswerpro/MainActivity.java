package com.ruogu.codersanswerpro;

import android.app.Fragment;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.ruogu.codersanswerpro.Models.Problem;

import java.util.ArrayList;

import com.loopj.android.http.*;

import org.json.*;

/**
 * Created by rogers on 2/13/14.
 * Main entrance of the application.
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private ArrayList<Problem> problemList;
    private ArrayList<Integer> difficulty_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getString(R.string.app_name);

        // Set up starred file
        StarredFileHandler starredFileHandler = StarredFileHandler.getInstance(this);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        // Build up a map from int to Resource id.
        difficulty_level = new ArrayList<Integer>(5);
        difficulty_level.add(R.drawable.level_1);
        difficulty_level.add(R.drawable.level_2);
        difficulty_level.add(R.drawable.level_3);
        difficulty_level.add(R.drawable.level_4);
        difficulty_level.add(R.drawable.level_5);
        try {
            initProblemList();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),
                    "Can not connectted to server. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initProblemList() throws JSONException {
        problemList = new ArrayList<Problem>();
        AsyncHttpClient listClient = new AsyncHttpClient();
        listClient.get(getApplicationContext(), "https://dl.dropboxusercontent.com/u/65861208/code/list.json",
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray tempList = (JSONArray) response.get("list");
                            for (int i = 0; i < tempList.length(); ++i) {
                                JSONObject o = tempList.getJSONObject(i);
                                problemList.add(new Problem(
                                        o.getString("problemName"),
                                        difficulty_level.get(o.getInt("level") - 1),
                                        o.getString("tag")));
                            }
                        } catch (org.json.JSONException e) {
                        }
                        ProblemListFragment pf =
                                (ProblemListFragment) getFragmentManager().findFragmentByTag("problem_list");
                        pf.mArrayAdapter.setmProblemData(problemList);
                        pf.mArrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Refresh finished.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        onSectionAttached(position);
        switch (position) {
            case 0:
                ProblemListFragment plf = new ProblemListFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, plf, "problem_list").commit();
                break;
            case 1:
                PickOneFragment pof = new PickOneFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, pof, "pick_one").commit();
                break;
            case 2:
                StarredFragment sf = new StarredFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, sf, "starred").commit();
                break;
            case 3:
                AboutFragment af = new AboutFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, af, "about").commit();
                break;
            case 4:
                FeedbackFragment ff = new FeedbackFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, ff, "feedback").commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        invalidateOptionsMenu();//rebuild the actionbar
        switch (number) {
            case 0:
                //mTitle = getString(R.string.all);
                mTitle = getString(R.string.app_name);
                break;
            case 1:
                mTitle = getString(R.string.random);
                break;
            case 2:
                mTitle = getString(R.string.star);
                break;
            case 3:
                mTitle = getString(R.string.about);
                break;
            case 4:
                mTitle = getString(R.string.feedback);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mNavigationDrawerFragment != null && !mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        //handle the press of "Back" button.
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof ProblemItemPager) {
            ProblemListFragment plf = new ProblemListFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, plf).commit();
        } else if (fragment instanceof StarredItemPager) {
            StarredFragment sf = new StarredFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, sf).commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        //handle the orientation change
        super.onConfigurationChanged(newConfiguration);
    }

    public ArrayList<Problem> getProblemList() {
        if (this.problemList == null) {
            try {
                initProblemList();
            } catch (JSONException e) {

            }
        }
        return this.problemList;
    }

    public void refreshProblemList() {
        try {
            initProblemList();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),
                    "Can not connectted to server. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
