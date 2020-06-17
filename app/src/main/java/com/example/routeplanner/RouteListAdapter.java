package com.example.routeplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class RouteListAdapter extends ArrayAdapter<RouteListItem> {

    private Context mContext;
    private int mResource;

    public RouteListAdapter(Context context, int resource, ArrayList<RouteListItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String distance = getItem(position).getDistance();
        String completions = getItem(position).getCompletions();
        String avgSpeed = getItem(position).getAvgSpeed();

        RouteListItem item = new RouteListItem(name, distance, completions, avgSpeed);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent);

    }
}
