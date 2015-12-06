package com.ruogu.codersanswerpro.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruogu.codersanswerpro.Models.Problem;
import com.ruogu.codersanswerpro.R;

import java.util.ArrayList;

/**
 * Created by rogers on 3/16/14.
 */
public class ProblemListAdapter extends ArrayAdapter<Problem> implements Filterable {
    private ArrayList<Problem> mProblems;

    private ArrayList<Problem> mProblemDataSet; //store the original problem list
    private int mResource;
    private LayoutInflater mInflater;

    public ProblemListAdapter(Context context, int layout, ArrayList<Problem> problems) {
        super(context, layout, problems);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = layout;
        mProblemDataSet = problems;
        mProblems = problems;
    }

    public void setmProblemData(ArrayList<Problem> mProblemDataSet) {
        this.mProblemDataSet = mProblemDataSet;
        this.mProblems = mProblemDataSet;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.problem_icon);
        icon.setImageResource(mProblems.get(position).mIconSource);
        TextView textView = (TextView) convertView.findViewById(R.id.problem_list_item_textView);
        textView.setText(mProblems.get(position).mProblemName);
        TextView tagView = (TextView) convertView.findViewById(R.id.problem_list_item_tags);
        tagView.setText(mProblems.get(position).mTags);
        return convertView;
    }

    @Override
    public int getCount() { //need to overwrite this function, or getView will through out of bounds exception
        return mProblems.size();
    }

    @Override
    public Problem getItem(int position) { //have to use my own function
        return mProblems.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Problem> tempList = new ArrayList<Problem>();
                int length = mProblemDataSet.size();
                int index = 0;
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.values = mProblemDataSet;
                    filterResults.count = mProblemDataSet.size();
                    return filterResults;
                }
                String searchText = charSequence.toString();
                while (index < length) {
                    String problemName = mProblemDataSet.get(index).mProblemName;
                    String tag = mProblemDataSet.get(index).mTags;
                    if ((problemName.toLowerCase()).contains(searchText.toLowerCase()) || (tag.toLowerCase()).contains(searchText.toLowerCase())) {
                        tempList.add(mProblemDataSet.get(index));
                    }
                    ++index;
                }
                filterResults.values = tempList;
                filterResults.count = tempList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mProblems = (ArrayList<Problem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
