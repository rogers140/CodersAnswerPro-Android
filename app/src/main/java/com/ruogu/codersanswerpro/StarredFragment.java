package com.ruogu.codersanswerpro;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ruogu.codersanswerpro.Models.Problem;
import com.ruogu.codersanswerpro.Tools.ProblemListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rogers on 3/5/14.
 */
public class StarredFragment extends Fragment {
    private ArrayList<Problem> mProblemList;
    private List<String> starredList;
    private ArrayList<Problem> mStarredProblem;
    private ListView mListView;
    private ProblemListAdapter mArrayAdapter;
    private StarredFileHandler mStarredFileHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStarredFileHandler = StarredFileHandler.getInstance(getActivity());
        starredList = mStarredFileHandler.getStarredList();
        mProblemList = ((MainActivity) getActivity()).getProblemList();
        mStarredProblem = new ArrayList<Problem>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.starred_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.starred_list_view);
        mListView.setTextFilterEnabled(true);
        for (String name : starredList) { //should put starredlist out of problemList to keep the order
            for (Problem p : mProblemList) {
                if (name.equals(p.mProblemName)) {
                    mStarredProblem.add(p);
                    break;
                }
            }
        }

        mArrayAdapter = new ProblemListAdapter(getActivity().getApplicationContext(), R.layout.problem_list_item, mStarredProblem);
        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //fold virtual keyboard
                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                StarredItemPager item = new StarredItemPager();
                Bundle args = new Bundle();
                String problemName = mArrayAdapter.getItem(position).mProblemName;
                args.putString("problemName", problemName);
                item.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, item);
                transaction.commit();

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_search);
        //define search
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    mArrayAdapter.getFilter().filter("");
                } else {
                    mArrayAdapter.getFilter().filter(s);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
