package com.zcdh.mobile.framework.activities;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.utils.BitmapUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/8/18.
 * 用于显示每页的内容那个
 */
public class IntroducePageFragment extends Fragment {

    private static final String IMAGE_ID="ImageId";
    private View contentView;

    private ImageView image;

    private int resId; //要显示的图片的资源ID

    private View.OnClickListener mListener;


//    public IntroducePageFragment(int resId, View.OnClickListener clickListener){
//        this.resId = resId;
//        this.clickListener =  clickListener;
//    }

    public static IntroducePageFragment newInstance(int args) {
        IntroducePageFragment fragment=new IntroducePageFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(IMAGE_ID,args);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resId=getArguments().getInt(IMAGE_ID);
//        try{
//            View v = LayoutInflater.from(getActivity()).inflate(resId, null);
//            if(v!=null){
//                contentView=v;
//            }
//        }catch(android.content.res.Resources.NotFoundException e){
//            e.printStackTrace();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if(contentView==null){
            contentView = inflater.inflate(R.layout.framework_introduce_fragment, container,false);
//        }
        image = (ImageView) contentView;
        image.setOnClickListener(mListener);
//        image.setImageResource(resId);
        image.setImageBitmap(BitmapUtils.readBitMap(ZcdhApplication.getInstance(),resId));
        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener= (View.OnClickListener) activity;
        } catch (Exception e) {
            throw new ClassCastException("The parent activity must implements ButtonClickListener!");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //对不使用的bitmap进行回收，避免内存溢出
        if (image.getDrawable() != null) {

            Bitmap oldBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

            image.setImageDrawable(null);

            if (oldBitmap != null) {
                oldBitmap.recycle();
                oldBitmap = null;
            }
        }
        System.gc();
    }
}
