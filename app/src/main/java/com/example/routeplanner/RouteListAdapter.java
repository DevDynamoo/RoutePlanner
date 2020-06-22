package com.example.routeplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Stack;

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
        float distance = getItem(position).getDistance();
        boolean cyclic = getItem(position).isCyclic();

        //RouteListItem item = new RouteListItem(name, distance, completions, avgSpeed);
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (convertView == null) {
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.RouteMainTitle);
        TextView tvDistance = (TextView) convertView.findViewById(R.id.RouteDistance);
        TextView tvCyclic = (TextView) convertView.findViewById(R.id.RouteCompletions);

        tvTitle.setText(name);
        tvDistance.setText("Distance: " + distance);
        tvCyclic.setText("Is Cyclic? " + getItem(position).isCyclic());

        return convertView;
    }
}