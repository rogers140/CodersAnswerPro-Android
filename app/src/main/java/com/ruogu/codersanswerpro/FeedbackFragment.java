package com.ruogu.codersanswerpro;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by rogers on 3/1/14.
 * Show "Feedback" page.
 * Let user send problem report to developer via Email.
 */
public class FeedbackFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);//to control the menu.

        Uri uri = Uri.parse("mailto:rogers140@gmail.com");
        String[] email = {"rogers140@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SEND, uri);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Problem Report for " + getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, "Sent from my " + Build.MODEL);
        startActivity(Intent.createChooser(intent, "Choose app to send feedback"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.feedback, container, false);
        ListView listView = (ListView) baseView.findViewById(R.id.appreciate_list);
        String[] values = {getString(R.string.appreciate)};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.feedback_list_item, values);
        listView.setAdapter(arrayAdapter);
        listView.setEnabled(false);
        return baseView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();//clear the menu item left by previous fragment.
    }
}
