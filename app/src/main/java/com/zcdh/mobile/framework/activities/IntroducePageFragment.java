package com.zcdh.mobile.framework.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zcdh.mobile.R;

/**
 * Created by Administrator on 2015/8/18.
 * 用于显示每页的内容那个
 */
public class IntroducePageFragment extends Fragment {

    private View contentView;

    private ImageView image;

    private int resId; //要显示的图片的资源ID

    private View.OnClickListener clickListener;


    public IntroducePageFragment(int resId, View.OnClickListener clickListener){
        this.resId = resId;
        this.clickListener =  clickListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            View v = LayoutInflater.from(getActivity()).inflate(resId, null);
            if(v!=null){
                contentView=v;
            }
        }catch(android.content.res.Resources.NotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(contentView==null){
            contentView = inflater.inflate(R.layout.framework_introduce_fragment, null);
            image = (ImageView) contentView.findViewById(R.id.image);
            image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clickListener.onClick(v);
                }
            });
            image.setImageResource(resId);
        }
        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
