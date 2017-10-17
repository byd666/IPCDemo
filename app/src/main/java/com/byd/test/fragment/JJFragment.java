package com.byd.test.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byd.test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JJFragment extends Fragment {

    public static JJFragment newInstance(String param1) {
        JJFragment fragment = new JJFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public JJFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_jj, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
        return view;
    }

}
