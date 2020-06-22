package com.example.routeplanner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;

public class CreateRouteTitleFragment extends Fragment {
    EditText mEditTextRouteName;

    public static CreateRouteTitleFragment newInstance() {
        CreateRouteTitleFragment fragment = new CreateRouteTitleFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_route_title, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEditTextRouteName = (EditText) getView().findViewById(R.id.editText_route_name);
        mEditTextRouteName.setText("Route "+(RouteOverviewActivity.getRouteListItems().size()+1));
    }
    public String getRouteName() {
        return mEditTextRouteName.getText().toString();
    }
}
