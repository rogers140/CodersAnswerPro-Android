package com.ruogu.codersanswerpro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by rogers on 2/13/14.
 * Show "About" page.
 */
public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//to control the menu.
        View baseView = inflater.inflate(R.layout.about, container, false);
        ListView listView = (ListView) baseView.findViewById(R.id.about_list);
        String[] values = {getString(R.string.version), getString(R.string.problem_from),
                getString(R.string.problem_source_1), getString(R.string.answer_source)};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.about_list_item, values);
        listView.setAdapter(arrayAdapter);
        //listView.setEnabled(false);
        return baseView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();//clear the menu item left by previous fragment.
    }
}
