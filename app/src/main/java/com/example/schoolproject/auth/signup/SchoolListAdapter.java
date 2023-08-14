package com.example.schoolproject.auth.signup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SchoolListAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<String> schoolList;
    private List<String> filteredList;

    public SchoolListAdapter(Context context) {
        this.context = context;
        this.schoolList = new ArrayList<>();
        this.filteredList = new ArrayList<>();
    }

    public void setSchoolList(List<String> schoolList) {
        this.schoolList = schoolList;
        this.filteredList = new ArrayList<>(schoolList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(filteredList.get(position));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterPattern = constraint.toString().toLowerCase().trim();
                List<String> filteredResults = new ArrayList<>();

                for (String school : schoolList) {
                    if (school.toLowerCase().startsWith(filterPattern)) {
                        filteredResults.add(school);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<String>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
