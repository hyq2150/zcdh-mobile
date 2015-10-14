package com.zcdh.mobile.app.activities.main;

import com.zcdh.mobile.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: xyh
 * Date: 2015/9/1
 * Time: 18:55
 */
public class SubMainPageFragment extends Fragment {

    public static Fragment newInstance() {
        return new SubMainPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_main_page,container,false);
    }
}
